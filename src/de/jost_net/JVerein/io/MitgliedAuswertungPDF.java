/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.3  2007/02/02 19:40:15  jost
 * RÃ¤nder korrigiert.
 *
 * Revision 1.2  2006/10/14 16:12:33  jost
 * Pagesize und Ränder gesetzt.
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
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliedAuswertungPDF
{
  public MitgliedAuswertungPDF(DBIterator list, final File file,
      ProgressMonitor monitor, String subtitle) throws ApplicationException,
      RemoteException
  {
    Document rpt = null;

    try
    {
      // ////////////////////////////////////////////////////////////////////////
      // Header erzeugen
      rpt = new Document(PageSize.A4);

      FileOutputStream fos = new FileOutputStream(file);
      PdfWriter.getInstance(rpt, fos);
      rpt.setMargins(50, 10, 20, 15); // links, rechts, oben, unten

      AbstractPlugin plugin = Application.getPluginLoader().getPlugin(
          JVereinPlugin.class);
      rpt.addAuthor("JVerein - Version " + plugin.getManifest().getVersion());

      rpt.addTitle(subtitle);
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

      Paragraph pTitle = new Paragraph("Mitglieder", FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 13));
      pTitle.setAlignment(Element.ALIGN_CENTER);
      rpt.add(pTitle);
      Paragraph psubTitle = new Paragraph(subtitle, FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 10));
      psubTitle.setAlignment(Element.ALIGN_CENTER);
      rpt.add(psubTitle);
      // ////////////////////////////////////////////////////////////////////////

      // ////////////////////////////////////////////////////////////////////////
      // Iteration ueber Umsaetze
      PdfPTable table = new PdfPTable(5);
      float[] widths = { 100, 130, 30, 30, 60 };
      table.setWidths(widths);
      table.setWidthPercentage(100);
      table.setSpacingBefore(5);
      table.setSpacingAfter(0);

      table.addCell(getDetailCell("Name", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("Anschrift", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("Geburts- datum", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("Eintritt / \nAustritt / \nKündigung",
          Element.ALIGN_CENTER, Color.LIGHT_GRAY));
      table.addCell(getDetailCell("Beitragsgruppe", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.setHeaderRows(1);

      int faelle = 0;

      while (list.hasNext())
      {
        faelle++;
        monitor.setStatus(faelle);
        Mitglied m = (Mitglied) list.next();
        table.addCell(getDetailCell(m.getNameVorname(), Element.ALIGN_LEFT));
        table.addCell(getDetailCell(m.getAnschrift(), Element.ALIGN_LEFT));
        table.addCell(getDetailCell(notNull(Einstellungen.DATEFORMAT.format(m
            .getGeburtsdatum())), Element.ALIGN_LEFT));
        String zelle = notNull(Einstellungen.DATEFORMAT.format(m.getEintritt()));
        if (m.getAustritt() != null)
        {
          zelle += "\n" + Einstellungen.DATEFORMAT.format(m.getAustritt());
        }
        if (m.getKuendigung() != null)
        {
          zelle += "\n" + Einstellungen.DATEFORMAT.format(m.getKuendigung());
        }
        table.addCell(getDetailCell(zelle, Element.ALIGN_LEFT));
        table.addCell(getDetailCell(m.getBeitragsgruppe().getBezeichnung(),
            Element.ALIGN_LEFT));
      }
      monitor.setStatusText("Auswertung fertig. " + list.size() + " Sätze.");
      rpt.add(table);

      Paragraph summary = new Paragraph("Anzahl Mitglieder: " + list.size(),
          FontFactory.getFont(FontFactory.HELVETICA, 8));
      rpt.add(summary);

      if (rpt != null)
        rpt.close();
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

}
