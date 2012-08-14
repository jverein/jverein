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
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.view.IAuswertung;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.Tools.EigenschaftenTool;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
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
    if (control
        .getMitgliedStatus()
        .getValue()
        .equals(
            JVereinPlugin.getI18n()
                .tr(JVereinPlugin.getI18n().tr("Abgemeldet"))))
    {
      subtitle += JVereinPlugin.getI18n().tr("Abgemeldet") + "";
    }
    if (control.getGeburtsdatumvon().getValue() != null)
    {
      Date d = (Date) control.getGeburtsdatumvon().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Geburtsdatum von {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getGeburtsdatumbis().getValue() != null)
    {
      Date d = (Date) control.getGeburtsdatumbis().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Geburtsdatum bis {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getEintrittvon().getValue() != null)
    {
      Date d = (Date) control.getEintrittvon().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Eintritt von {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getEintrittbis().getValue() != null)
    {
      Date d = (Date) control.getEintrittbis().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Eintritt bis {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getAustrittvon().getValue() != null)
    {
      Date d = (Date) control.getAustrittvon().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Austritt von  {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getAustrittbis().getValue() != null)
    {
      Date d = (Date) control.getAustrittbis().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Austritt bis {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getSterbedatumvon().getValue() != null)
    {
      Date d = (Date) control.getSterbedatumvon().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Sterbetag von {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getSterbedatumbis().getValue() != null)
    {
      Date d = (Date) control.getSterbedatumbis().getValue();
      subtitle += JVereinPlugin.getI18n().tr("Sterbedatum bis {0}",
          new JVDateFormatTTMMJJJJ().format(d))
          + "  ";
    }
    if (control.getMitgliedStatus().getValue()
        .equals(JVereinPlugin.getI18n().tr("Angemeldet"))
        && control.getAustrittvon().getValue() == null
        && control.getAustrittbis().getValue() == null
        && control.getSterbedatumvon().getValue() == null
        && control.getSterbedatumbis().getValue() == null)
    {
      subtitle += JVereinPlugin.getI18n().tr(
          "nur Angemeldete, keine Ausgetretenen (nur lfd. Jahr)")
          + "  ";
    }
    if (control.getBeitragsgruppeAusw().getValue() != null)
    {
      Beitragsgruppe bg = (Beitragsgruppe) control.getBeitragsgruppeAusw()
          .getValue();
      subtitle += JVereinPlugin.getI18n().tr("nur Beitragsgruppe {0}",
          bg.getBezeichnung())
          + "  ";
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

      Reporter report = new Reporter(fos, JVereinPlugin.getI18n().tr(
          "Mitglieder"), subtitle, list.size(), 50, 10, 20, 15);

      report.addHeaderColumn(JVereinPlugin.getI18n().tr("Name"),
          Element.ALIGN_CENTER, 100, Color.LIGHT_GRAY);
      report.addHeaderColumn(
          JVereinPlugin.getI18n().tr("Anschrift\nKommunikation"),
          Element.ALIGN_CENTER, 130, Color.LIGHT_GRAY);
      report.addHeaderColumn(JVereinPlugin.getI18n().tr("Geburts- datum"),
          Element.ALIGN_CENTER, 30, Color.LIGHT_GRAY);
      report
          .addHeaderColumn(
              JVereinPlugin.getI18n().tr("Eintritt / \nAustritt / \nKündigung")
                  + (Einstellungen.getEinstellung().getSterbedatum() ? ("/\n" + JVereinPlugin
                      .getI18n().tr(JVereinPlugin.getI18n().tr("Sterbedatum")))
                      : ""), Element.ALIGN_CENTER, 30, Color.LIGHT_GRAY);
      report.addHeaderColumn(
          JVereinPlugin.getI18n().tr("Beitragsgruppe /\nEigenschaften"),
          Element.ALIGN_CENTER, 60, Color.LIGHT_GRAY);
      report.createHeader(100, Element.ALIGN_CENTER);

      for (int i = 0; i < list.size(); i++)
      {
        Mitglied m = list.get(i);
        report.addColumn(m.getNameVorname(), Element.ALIGN_LEFT);
        String anschriftkommunikation = m.getAnschrift();
        if (m.getTelefonprivat() != null && m.getTelefonprivat().length() > 0)
        {
          anschriftkommunikation += "\n"
              + JVereinPlugin.getI18n().tr("Tel. priv: {0}",
                  m.getTelefonprivat());
        }
        if (m.getTelefondienstlich() != null
            && m.getTelefondienstlich().length() > 0)
        {
          anschriftkommunikation += "\n"
              + JVereinPlugin.getI18n().tr("Tel. dienstl: {0}",
                  m.getTelefondienstlich());
        }
        if (m.getHandy() != null && m.getHandy().length() > 0)
        {
          anschriftkommunikation += "\n"
              + JVereinPlugin.getI18n().tr("Handy: {0}", m.getHandy());
        }
        if (m.getEmail() != null && m.getEmail().length() > 0)
        {
          anschriftkommunikation += "\n"
              + JVereinPlugin.getI18n().tr("EMail: {0}", m.getEmail());
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

        StringBuilder eigenschaften = new StringBuilder();
        ArrayList<String> eig = new EigenschaftenTool().getEigenschaften(m
            .getID());
        for (int i2 = 0; i2 < eig.size(); i2 = i2 + 2)
        {
          if (i2 == 0)
          {
            beitragsgruppebemerkung.append("\n");
          }
          eigenschaften.append(eig.get(i2));
          eigenschaften.append(": ");
          eigenschaften.append(eig.get(i2 + 1));
          eigenschaften.append("\n");
        }

        report.addColumn(
            beitragsgruppebemerkung.toString() + eigenschaften.toString(),
            Element.ALIGN_LEFT);
      }
      report.closeTable();

      report.add(new Paragraph(JVereinPlugin.getI18n().tr(
          "Anzahl Mitglieder: {0}", list.size() + ""), FontFactory.getFont(
          FontFactory.HELVETICA, 8)));

      report.close();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Auswertung fertig. {0} Sätze.",
              list.size() + ""));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Logger.error("error while creating report", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr("Fehler"), e);
    }
  }

  public String getDateiname()
  {
    return JVereinPlugin.getI18n().tr("auswertung");
  }

  public String getDateiendung()
  {
    return "PDF";
  }

  public String toString()
  {
    return JVereinPlugin.getI18n().tr("Mitgliederliste PDF");
  }

  public boolean openFile()
  {
    return true;
  }
}
