/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.view.IAuswertung;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedAuswertungPDF implements IAuswertung
{
  private MitgliedControl control;

  private String subtitle = "";

  public MitgliedAuswertungPDF(MitgliedControl control)
  {
    this.control = control;
  }

  public void beforeGo() throws RemoteException
  {
    if (control.getMitgliedStatus().getValue()
        .equals(JVereinPlugin.getI18n().tr("Abgemeldet")))
    {
      subtitle += "Abgemeldet ";
    }
    if (control.getGeburtsdatumvon().getValue() != null)
    {
      Date d = (Date) control.getGeburtsdatumvon().getValue();
      subtitle += "Geburtsdatum von " + new JVDateFormatTTMMJJJJ().format(d)
          + "  ";
    }
    if (control.getGeburtsdatumbis().getValue() != null)
    {
      Date d = (Date) control.getGeburtsdatumbis().getValue();
      subtitle += "Geburtsdatum bis " + new JVDateFormatTTMMJJJJ().format(d)
          + "  ";
    }
    if (control.getEintrittvon().getValue() != null)
    {
      Date d = (Date) control.getEintrittvon().getValue();
      subtitle += "Eintritt von " + new JVDateFormatTTMMJJJJ().format(d) + "  ";
    }
    if (control.getEintrittbis().getValue() != null)
    {
      Date d = (Date) control.getEintrittbis().getValue();
      subtitle += "Eintritt bis " + new JVDateFormatTTMMJJJJ().format(d) + "  ";
    }
    if (control.getAustrittvon().getValue() != null)
    {
      Date d = (Date) control.getAustrittvon().getValue();
      subtitle += "Austritt von " + new JVDateFormatTTMMJJJJ().format(d) + "  ";
    }
    if (control.getAustrittbis().getValue() != null)
    {
      Date d = (Date) control.getAustrittbis().getValue();
      subtitle += "Austritt bis " + new JVDateFormatTTMMJJJJ().format(d) + "  ";
    }
    if (control.getSterbedatumvon().getValue() != null)
    {
      Date d = (Date) control.getSterbedatumvon().getValue();
      subtitle += "Sterbetag von " + new JVDateFormatTTMMJJJJ().format(d)
          + "  ";
    }
    if (control.getSterbedatumbis().getValue() != null)
    {
      Date d = (Date) control.getSterbedatumbis().getValue();
      subtitle += "Sterbedatum bis " + new JVDateFormatTTMMJJJJ().format(d)
          + "  ";
    }
    if (control.getMitgliedStatus().getValue()
        .equals(JVereinPlugin.getI18n().tr("Angemeldet"))
        && control.getAustrittvon().getValue() == null
        && control.getAustrittbis().getValue() == null
        && control.getSterbedatumvon().getValue() == null
        && control.getSterbedatumbis().getValue() == null)
    {
      subtitle += "nur Angemeldete, keine Ausgetretenen (nur lfd. Jahr)  ";
    }
    if (control.getBeitragsgruppeAusw().getValue() != null)
    {
      Beitragsgruppe bg = (Beitragsgruppe) control.getBeitragsgruppeAusw()
          .getValue();
      subtitle += "nur Beitragsgruppe " + bg.getBezeichnung();
    }

    String ueberschrift = (String) control.getAuswertungUeberschrift()
        .getValue();
    if (ueberschrift.length() > 0)
    {
      subtitle = ueberschrift;
    }

  }

  public void go(ArrayList<Mitglied> list, final File file)
      throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);

      Reporter report = new Reporter(fos, "Mitglieder", subtitle, list.size(),
          50, 10, 20, 15);

      report.addHeaderColumn("Name", Element.ALIGN_CENTER, 100,
          Color.LIGHT_GRAY);
      report.addHeaderColumn("Anschrift\nKommunikation", Element.ALIGN_CENTER,
          130, Color.LIGHT_GRAY);
      report.addHeaderColumn("Geburts- datum", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      report.addHeaderColumn("Eintritt / \nAustritt / \nKündigung"
          + (Einstellungen.getEinstellung().getSterbedatum() ? "/\nSterbedatum"
              : ""), Element.ALIGN_CENTER, 30, Color.LIGHT_GRAY);
      report.addHeaderColumn("Beitragsgruppe /\nEigenschaften",
          Element.ALIGN_CENTER, 60, Color.LIGHT_GRAY);
      report.createHeader(100, Element.ALIGN_CENTER);

      for (int i = 0; i < list.size(); i++)
      {
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
      }
      report.closeTable();

      report.add(new Paragraph("Anzahl Mitglieder: " + list.size(), FontFactory
          .getFont(FontFactory.HELVETICA, 8)));

      report.close();
      GUI.getStatusBar().setSuccessText(
          "Auswertung fertig. " + list.size() + " Sätze.");
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

  public String getDateiname()
  {
    return "auswertung";
  }

  public String getDateiendung()
  {
    return "PDF";
  }

  public String toString()
  {
    return "Mitgliederliste PDF";
  }

  public boolean openFile()
  {
    return true;
  }
}
