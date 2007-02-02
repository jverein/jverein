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
 * Revision 1.1  2006/10/29 07:50:08  jost
 * Neu: Mitgliederstatistik
 *
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.graphics.Point;

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
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliederStatistik
{
  public MitgliederStatistik(final File file) throws ApplicationException,
      RemoteException
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

      Paragraph pTitle = new Paragraph("Mitgliederstatistik", FontFactory
          .getFont(FontFactory.HELVETICA_BOLD, 13));
      pTitle.setAlignment(Element.ALIGN_CENTER);
      rpt.add(pTitle);

      Paragraph pAltersgruppen = new Paragraph("\nAltersgruppen", FontFactory
          .getFont(FontFactory.HELVETICA, 11));
      rpt.add(pAltersgruppen);

      PdfPTable table = new PdfPTable(4);
      float[] widths = { 100, 30, 30, 30 };
      table.setWidths(widths);
      table.setHorizontalAlignment(Element.ALIGN_LEFT);
      table.setWidthPercentage(50);
      table.setSpacingBefore(10);
      table.setSpacingAfter(0);

      table.addCell(getDetailCell("Altersgruppe", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("Insgesamt", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("männlich", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("weiblich", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.setHeaderRows(1);

      DBIterator stammd = Einstellungen.getDBService().createList(
          Stammdaten.class);
      Stammdaten stamm = (Stammdaten) stammd.next();

      AltersgruppenParser ap = new AltersgruppenParser(stamm.getAltersgruppen());
      while (ap.hasNext())
      {
        Point p = ap.getNext();
        addAltersgruppe(table, p.x, p.y);
      }
      addAltersgruppe(table, 0, 100);
      rpt.add(table);

      Paragraph pBeitragsgruppen = new Paragraph("\nBeitragsgruppen",
          FontFactory.getFont(FontFactory.HELVETICA, 11));
      rpt.add(pBeitragsgruppen);

      table = new PdfPTable(4);
      table.setWidths(widths);
      table.setHorizontalAlignment(Element.ALIGN_LEFT);
      table.setWidthPercentage(50);
      table.setSpacingBefore(10);
      table.setSpacingAfter(0);

      table.addCell(getDetailCell("Beitragsgruppe", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("Insgesamt", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("männlich", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.addCell(getDetailCell("weiblich", Element.ALIGN_CENTER,
          Color.LIGHT_GRAY));
      table.setHeaderRows(1);

      DBIterator beitragsgruppen = Einstellungen.getDBService().createList(
          Beitragsgruppe.class);
      while (beitragsgruppen.hasNext())
      {
        Beitragsgruppe bg = (Beitragsgruppe) beitragsgruppen.next();
        addBeitragsgruppe(table, bg);
      }
      addBeitragsgruppe(table, null);
      rpt.add(table);

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

  private void addAltersgruppe(PdfPTable table, int von, int bis)
      throws RemoteException
  {
    if (von == 0 && bis == 100)
    {
      table.addCell(getDetailCell("Insgesamt", Element.ALIGN_LEFT));
    }
    else
    {
      table.addCell(getDetailCell("Altersgruppe " + von + "-" + bis,
          Element.ALIGN_LEFT));
    }
    table.addCell(getDetailCell(getAltersgruppe(von, bis, null) + "",
        Element.ALIGN_RIGHT));
    table.addCell(getDetailCell(getAltersgruppe(von, bis, "m") + "",
        Element.ALIGN_RIGHT));
    table.addCell(getDetailCell(getAltersgruppe(von, bis, "w") + "",
        Element.ALIGN_RIGHT));

  }

  private void addBeitragsgruppe(PdfPTable table, Beitragsgruppe bg)
      throws RemoteException
  {
    if (bg == null)
    {
      table.addCell(getDetailCell("Insgesamt", Element.ALIGN_LEFT));
    }
    else
    {
      table.addCell(getDetailCell(bg.getBezeichnung(), Element.ALIGN_LEFT));
    }
    table.addCell(getDetailCell(getBeitragsgruppe(bg, null) + "",
        Element.ALIGN_RIGHT));
    table.addCell(getDetailCell(getBeitragsgruppe(bg, "m") + "",
        Element.ALIGN_RIGHT));
    table.addCell(getDetailCell(getBeitragsgruppe(bg, "w") + "",
        Element.ALIGN_RIGHT));

  }

  /**
   * Anzahl der Mitglieder in einer Altersgruppe ermitteln
   * 
   * @param von
   *          Alter in Jahren
   * @param bis
   *          Alter in Jahren
   * @param geschlecht
   *          m, w oder null
   * @return Anzahl der Mitglieder
   */
  private int getAltersgruppe(int von, int bis, String geschlecht)
      throws RemoteException
  {
    Calendar calVon = Calendar.getInstance();
    calVon.add(Calendar.YEAR, bis * -1);
    calVon.set(Calendar.MONTH, Calendar.JANUARY);
    calVon.set(Calendar.DAY_OF_MONTH, 1);
    calVon.set(Calendar.HOUR, 0);
    calVon.set(Calendar.MINUTE, 0);
    calVon.set(Calendar.SECOND, 0);
    calVon.set(Calendar.MILLISECOND, 0);
    java.sql.Date vd = new java.sql.Date(calVon.getTimeInMillis());

    Calendar calBis = Calendar.getInstance();
    calBis.add(Calendar.YEAR, von * -1);
    calBis.set(Calendar.MONTH, Calendar.DECEMBER);
    calBis.set(Calendar.DAY_OF_MONTH, 31);
    java.sql.Date bd = new java.sql.Date(calBis.getTimeInMillis());

    DBIterator list;
    list = Einstellungen.getDBService().createList(Mitglied.class);
    list.addFilter("geburtsdatum >= ?", new Object[] { vd });
    list.addFilter("geburtsdatum <= ?", new Object[] { bd });
    list.addFilter("austritt is null");

    if (geschlecht != null)
    {
      list.addFilter("geschlecht = ?", new Object[] { geschlecht });
    }

    return list.size();
  }

  private int getBeitragsgruppe(Beitragsgruppe bg, String geschlecht)
      throws RemoteException
  {
    DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
    list.addFilter("austritt is null");
    if (bg != null)
    {
      list.addFilter("beitragsgruppe = ?", new Object[] { new Integer(bg
          .getID()) });
    }
    if (geschlecht != null)
    {
      list.addFilter("geschlecht = ?", new Object[] { geschlecht });
    }

    return list.size();
  }

}
