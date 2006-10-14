/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 * Revision 1.1  2006/10/14 06:03:00  jost
 * Erweiterung um Buchungsauswertung
 *
 * Revision 1.1  2006/09/20 15:39:24  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungAuswertungPDF
{
  private double summe = 0;

  public BuchungAuswertungPDF(DBIterator list, final File file,
      ProgressMonitor monitor, Date dVon, Date dBis)
      throws ApplicationException, RemoteException
  {
    Document rpt = null;

    try
    {
      // ////////////////////////////////////////////////////////////////////////
      // Header erzeugen
      rpt = new Document(PageSize.A4, 80, 60, 60, 60);

      FileOutputStream fos = new FileOutputStream(file);
      PdfWriter.getInstance(rpt, fos);
      rpt.setMargins(50, 10, 50, 30); // links, rechts, oben, unten

      AbstractPlugin plugin = Application.getPluginLoader().getPlugin(
          JVereinPlugin.class);
      rpt.addAuthor("JVerein - Version " + plugin.getManifest().getVersion());

      rpt.addTitle("hier kommt noch ein Subtitle rein");
      // ////////////////////////////////////////////////////////////////////////

      // ////////////////////////////////////////////////////////////////////////
      // Footer erzeugen
      Chunk fuss = new Chunk("Ausgegeben am "
          + Einstellungen.TIMESTAMPFORMAT.format(new Date())
          + "              Seite:  ", FontFactory.getFont(
          FontFactory.HELVETICA, 8, Font.BOLD));
      HeaderFooter hf = new HeaderFooter(new Phrase(fuss), true);
      hf.setAlignment(Element.ALIGN_CENTER);
      rpt.setFooter(hf);

      rpt.open();

      Paragraph pTitle = new Paragraph("Buchungen", FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 13));
      pTitle.setAlignment(Element.ALIGN_CENTER);
      rpt.add(pTitle);
      Paragraph psubTitle = new Paragraph("subtitle", FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 10));
      psubTitle.setAlignment(Element.ALIGN_CENTER);
      rpt.add(psubTitle);
      // ////////////////////////////////////////////////////////////////////////

      PdfPTable table = null;
      while (list.hasNext())
      {
        table = getTableWithHeader();
        createTableContent(rpt, list, table, dVon, dBis);
      }
      createTableContent(rpt, null, table, dVon, dBis);

      System.out.println("Gesamtsumme: " + summe);
      monitor.setStatusText("Auswertung fertig. " + list.size() + " Sätze.");

      if (rpt != null)
      {
        rpt.close();
      }
      fos.close();
      GUI.getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          try
          {
            new Program().handleAction(file);
          }
          catch (ApplicationException ae)
          {
            Application.getMessagingFactory().sendMessage(
                new StatusBarMessage(ae.getLocalizedMessage(),
                    StatusBarMessage.TYPE_ERROR));
          }
        }
      });
    }
    catch (DocumentException e)
    {
      e.printStackTrace();
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }
    catch (FileNotFoundException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }
    catch (IOException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }

  }

  /**
   * Erzeugt eine Zelle der Tabelle.
   * 
   * @param text
   *          der anzuzeigende Text.
   * @param align
   *          die Ausrichtung.
   * @param backgroundcolor
   *          die Hintergundfarbe.
   * @return die erzeugte Zelle.
   */
  private PdfPCell getDetailCell(String text, int align, Color backgroundcolor)
  {
    PdfPCell cell = new PdfPCell(new Phrase(notNull(text), FontFactory.getFont(
        FontFactory.HELVETICA, 8)));
    cell.setHorizontalAlignment(align);
    cell.setBackgroundColor(backgroundcolor);
    return cell;
  }

  /**
   * Erzeugt eine Zelle der Tabelle.
   * 
   * @param text
   *          der anzuzeigende Text.
   * @param align
   *          die Ausrichtung.
   * @return die erzeugte Zelle.
   */
  private PdfPCell getDetailCell(String text, int align)
  {
    return getDetailCell(text, align, Color.WHITE);
  }

  /**
   * Gibt einen Leerstring aus, falls der Text null ist.
   * 
   * @param text
   *          der Text.
   * @return der Text oder Leerstring - niemals null.
   */
  private String notNull(String text)
  {
    return text == null ? "" : text;
  }

  private PdfPTable getTableWithHeader() throws DocumentException
  {
    PdfPTable table = new PdfPTable(5);
    float[] widths = { 40, 100, 100, 100, 60 };
    table.setWidths(widths);
    table.setWidthPercentage(100);
    table.setSpacingBefore(10);
    table.setSpacingAfter(0);

    table
        .addCell(getDetailCell("Datum", Element.ALIGN_CENTER, Color.LIGHT_GRAY));
    table
        .addCell(getDetailCell("Name", Element.ALIGN_CENTER, Color.LIGHT_GRAY));
    table.addCell(getDetailCell("Zahlungsgrund", Element.ALIGN_CENTER,
        Color.LIGHT_GRAY));
    table.addCell(getDetailCell("Zahlungsgrund2", Element.ALIGN_CENTER,
        Color.LIGHT_GRAY));
    table.addCell(getDetailCell("Betrag", Element.ALIGN_CENTER,
        Color.LIGHT_GRAY));
    table.setHeaderRows(1);
    return table;
  }

  private void createTableContent(Document rpt, DBIterator list,
      PdfPTable table, Date dVon, Date dBis) throws RemoteException,
      DocumentException
  {
    Buchungsart ba = null;
    Paragraph pBuchungsart = null;
    if (list != null)
    {
      ba = (Buchungsart) list.next();
      pBuchungsart = new Paragraph(ba.getBezeichnung(), FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 10));
    }
    else
    {
      pBuchungsart = new Paragraph("ohne Zuordnung", FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 10));
    }

    rpt.add(pBuchungsart);
    DBIterator listb = Einstellungen.getDBService().createList(Buchung.class);
    listb.addFilter("datum >= ?", new Object[] { new java.sql.Date(dVon
        .getTime()) });
    listb.addFilter("datum <= ?", new Object[] { new java.sql.Date(dBis
        .getTime()) });
    if (list != null)
    {
      listb.addFilter("buchungsart = ?",
          new Object[] { new Integer(ba.getID()) });
    }
    else
    {
      listb.addFilter("buchungsart is null");
    }
    listb.setOrder("ORDER BY datum");
    double buchungsartSumme = 0;
    while (listb.hasNext())
    {
      Buchung b = (Buchung) listb.next();
      table.addCell(getDetailCell(
          Einstellungen.DATEFORMAT.format(b.getDatum()), Element.ALIGN_LEFT));
      table.addCell(getDetailCell(b.getName(), Element.ALIGN_LEFT));
      table.addCell(getDetailCell(b.getZweck(), Element.ALIGN_LEFT));
      table.addCell(getDetailCell(b.getZweck2(), Element.ALIGN_LEFT));
      table.addCell(getDetailCell(Einstellungen.DECIMALFORMAT.format(b
          .getBetrag()), Element.ALIGN_RIGHT));
      buchungsartSumme += b.getBetrag();
    }
    table.addCell(getDetailCell("", Element.ALIGN_LEFT));
    if (list != null)
    {
      table.addCell(getDetailCell("Summe " + ba.getBezeichnung(),
          Element.ALIGN_LEFT));
    }
    else
    {
      table.addCell(getDetailCell("Summe ohne Zuordnung", Element.ALIGN_LEFT));
    }
    summe += buchungsartSumme;
    System.out.println(summe);
    table.addCell(getDetailCell("", Element.ALIGN_LEFT));
    table.addCell(getDetailCell("", Element.ALIGN_LEFT));
    table.addCell(getDetailCell(Einstellungen.DECIMALFORMAT
        .format(buchungsartSumme), Element.ALIGN_RIGHT));
    rpt.add(table);
  }

}
