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
 * Revision 1.15  2010-10-30 11:31:23  jost
 * Neu: Sterbetag
 *
 * Revision 1.14  2010-10-15 09:58:28  jost
 * Code aufgeräumt
 *
 * Revision 1.13  2010-10-03 09:48:20  jost
 * Überflüssigen Import entfernt.
 *
 * Revision 1.12  2010-09-09 19:55:07  jost
 * Ausgabe der Eigenschaften
 *
 * Revision 1.11  2009/07/18 13:43:25  jost
 * NPE verhindert.
 *
 * Revision 1.10  2009/01/25 16:08:41  jost
 * Vermerke entfernt.
 *
 * Revision 1.9  2008/12/30 21:57:50  jost
 * ÃœberflÃ¼ssige Methode auskommentiert.
 *
 * Revision 1.8  2008/12/29 08:41:04  jost
 * Korrekte Verarbeitung bei fehlendem Geburts- und/oder Eintrittsdatum
 *
 * Revision 1.7  2008/10/01 14:17:48  jost
 * Warnungen entfernt
 *
 * Revision 1.6  2008/07/10 09:21:38  jost
 * Umstellung auf den Reporter
 * Kommunikationsdaten aufgenommen
 *
 * Revision 1.5  2008/01/27 09:43:42  jost
 * Vereinheitlichung der Mitgliedersuche durch die Klasse MitgliedQuery
 *
 * Revision 1.4  2007/02/23 20:28:04  jost
 * Mail- und Webadresse im Header korrigiert.
 *
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
import java.util.ArrayList;
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliedAuswertungPDF
{

  public MitgliedAuswertungPDF(ArrayList<Mitglied> list, final File file,
      ProgressMonitor monitor, String subtitle) throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      Reporter report = new Reporter(fos, monitor, "Mitglieder", subtitle,
          list.size(), 50, 10, 20, 15);

      report.addHeaderColumn("Name", Element.ALIGN_CENTER, 100,
          Color.LIGHT_GRAY);
      report.addHeaderColumn("Anschrift\nKommunikation", Element.ALIGN_CENTER,
          130, Color.LIGHT_GRAY);
      report.addHeaderColumn("Geburts- datum", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      report.addHeaderColumn(
          "Eintritt / \nAustritt / \nKündigung /\nSterbedatum",
          Element.ALIGN_CENTER, 30, Color.LIGHT_GRAY);
      report.addHeaderColumn("Beitragsgruppe /\nEigenschaften",
          Element.ALIGN_CENTER, 60, Color.LIGHT_GRAY);
      report.createHeader(100, Element.ALIGN_CENTER);

      int faelle = 0;

      for (int i = 0; i < list.size(); i++)
      {
        faelle++;
        Mitglied m = list.get(i);
        report.addColumn(m.getNameVorname(), Element.ALIGN_LEFT);
        String anschriftkommunikation = m.getAnschrift();
        if (m.getTelefonprivat() != null && m.getTelefonprivat().length() > 0)
        {
          anschriftkommunikation += "\nTel. priv: " + m.getTelefonprivat();
        }
        if (m.getTelefondienstlich() != null
            && m.getTelefondienstlich().length() > 0)
        {
          anschriftkommunikation += "\nTel. dienstl: "
              + m.getTelefondienstlich();
        }
        if (m.getHandy() != null && m.getHandy().length() > 0)
        {
          anschriftkommunikation += "\nHandy: " + m.getHandy();
        }
        if (m.getEmail() != null && m.getEmail().length() > 0)
        {
          anschriftkommunikation += "\nEMail: " + m.getEmail();
        }
        report.addColumn(anschriftkommunikation, Element.ALIGN_LEFT);
        report.addColumn(m.getGeburtsdatum(), Element.ALIGN_LEFT);

        Date d = m.getEintritt();
        if (d.equals(Einstellungen.NODATE))
        {
          d = null;
        }
        String zelle = "";
        if (d != null)
        {
          zelle = new JVDateFormatTTMMJJJJ().format(d);
        }

        if (m.getAustritt() != null)
        {
          zelle += "\n" + new JVDateFormatTTMMJJJJ().format(m.getAustritt());
        }
        if (m.getKuendigung() != null)
        {
          zelle += "\n" + new JVDateFormatTTMMJJJJ().format(m.getKuendigung());
        }
        if (m.getSterbetag() != null)
        {
          zelle += "\n" + new JVDateFormatTTMMJJJJ().format(m.getSterbetag());
        }
        report.addColumn(zelle, Element.ALIGN_LEFT);
        StringBuilder beitragsgruppebemerkung = new StringBuilder(m
            .getBeitragsgruppe().getBezeichnung());
        DBIterator it = Einstellungen.getDBService().createList(
            Eigenschaften.class);
        it.addFilter("mitglied = ?", new Object[] { m.getID() });
        if (it.size() > 0)
        {
          beitragsgruppebemerkung.append("\n");
        }
        while (it.hasNext())
        {
          Eigenschaften ei = (Eigenschaften) it.next();
          beitragsgruppebemerkung.append("\n");
          beitragsgruppebemerkung.append(ei.getEigenschaft().getBezeichnung());
        }
        report
            .addColumn(beitragsgruppebemerkung.toString(), Element.ALIGN_LEFT);
        report.setNextRecord();
      }
      report.closeTable();

      report.add(new Paragraph("Anzahl Mitglieder: " + list.size(), FontFactory
          .getFont(FontFactory.HELVETICA, 8)));

      report.close();
      monitor.setStatusText("Auswertung fertig. " + list.size() + " Sätze.");
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
   * Gibt einen Leerstring aus, falls der Text null ist.
   * 
   * @param text
   *          der Text.
   * @return der Text oder Leerstring - niemals null.
   */
  // private String notNull(String text)
  // {
  // return text == null ? "" : text;
  // }
}
