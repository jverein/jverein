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

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.view.IAuswertung;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.Tools.EigenschaftenTool;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedAuswertungPDF implements IAuswertung
{

  private MitgliedControl control;

  private Adresstyp adresstyp;

  private String subtitle = "";

  public MitgliedAuswertungPDF(MitgliedControl control)
  {
    this.control = control;
  }

  @Override
  public void beforeGo() throws RemoteException
  {
    this.adresstyp = (Adresstyp) control.getAdresstyp().getValue();
    if (adresstyp == null)
    {
      DBIterator it = Einstellungen.getDBService().createList(Adresstyp.class);
      it.addFilter("jvereinid=1");
      adresstyp = (Adresstyp) it.next();
    }
    subtitle = "";
    if (adresstyp.getJVereinid() == 1)
    {
      if (control.getMitgliedStatus().getValue().equals(

      "Abgemeldet"))
      {
        subtitle += "Abgemeldet" + "";
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
        subtitle += "Eintritt von " + new JVDateFormatTTMMJJJJ().format(d)
            + "  ";
      }
      if (control.getEintrittbis().getValue() != null)
      {
        Date d = (Date) control.getEintrittbis().getValue();
        subtitle += "Eintritt bis " + new JVDateFormatTTMMJJJJ().format(d)
            + "  ";
      }
      if (control.getAustrittvon().getValue() != null)
      {
        Date d = (Date) control.getAustrittvon().getValue();
        subtitle += "Austritt von " + new JVDateFormatTTMMJJJJ().format(d)
            + "  ";
      }
      if (control.getAustrittbis().getValue() != null)
      {
        Date d = (Date) control.getAustrittbis().getValue();
        subtitle += "Austritt bis " + new JVDateFormatTTMMJJJJ().format(d)
            + "  ";
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
      if (control.getMitgliedStatus().getValue().equals("Angemeldet")
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
        subtitle += "nur Beitragsgruppe " + bg.getBezeichnung() + "  ";
      }
    }
    String ueberschrift = (String) control.getAuswertungUeberschrift()
        .getValue();
    if (ueberschrift.length() > 0)
    {
      subtitle = ueberschrift;
    }

  }

  @Override
  public void go(ArrayList<Mitglied> list, final File file)
      throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);

      Reporter report = new Reporter(fos, adresstyp.getBezeichnungPlural(),
          subtitle, list.size(), 50, 10, 20, 25);

      report.addHeaderColumn("Name", Element.ALIGN_CENTER, 100,
          BaseColor.LIGHT_GRAY);
      report.addHeaderColumn("Anschrift\nKommunikation", Element.ALIGN_CENTER,
          130, BaseColor.LIGHT_GRAY);
      report.addHeaderColumn("Geburts- datum", Element.ALIGN_CENTER, 30,
          BaseColor.LIGHT_GRAY);
      if (adresstyp.getJVereinid() == 1)
      {
        report.addHeaderColumn("Eintritt / \nAustritt / \nKündigung"
            + (Einstellungen.getEinstellung().getSterbedatum() ? ("/\n"
                + "Sterbedatum") : ""), Element.ALIGN_CENTER, 30,
            BaseColor.LIGHT_GRAY);
      }
      report
          .addHeaderColumn(

              "Beitragsgruppe /\nEigenschaften"
                  + (Einstellungen.getEinstellung().getExterneMitgliedsnummer() ? "\nMitgliedsnummer"
                      : ""), Element.ALIGN_CENTER, 60, BaseColor.LIGHT_GRAY);
      report.createHeader(100, Element.ALIGN_CENTER);

      for (int i = 0; i < list.size(); i++)
      {
        Mitglied m = list.get(i);
        report.addColumn(Adressaufbereitung.getNameVorname(m),
            Element.ALIGN_LEFT);
        String anschriftkommunikation = Adressaufbereitung.getAnschrift(m);
        if (m.getTelefonprivat() != null && m.getTelefonprivat().length() > 0)
        {
          anschriftkommunikation += "\n" + "Tel. priv: " + m.getTelefonprivat();
        }
        if (m.getTelefondienstlich() != null
            && m.getTelefondienstlich().length() > 0)
        {
          anschriftkommunikation += "\n" + "Tel. dienstl: "
              + m.getTelefondienstlich();
        }
        if (m.getHandy() != null && m.getHandy().length() > 0)
        {
          anschriftkommunikation += "\n" + "Handy: " + m.getHandy();
        }
        if (m.getEmail() != null && m.getEmail().length() > 0)
        {
          anschriftkommunikation += "\n" + "EMail: " + m.getEmail();
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
        if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
        {
          zelle += "\n"
              + (m.getExterneMitgliedsnummer() != null ? m
                  .getExterneMitgliedsnummer() : "");
        }
        if (adresstyp.getJVereinid() == 1)
        {
          report.addColumn(zelle, Element.ALIGN_LEFT);
        }
        StringBuilder beitragsgruppebemerkung = new StringBuilder();
        if (m.getBeitragsgruppe() != null)
        {
          beitragsgruppebemerkung
              .append(m.getBeitragsgruppe().getBezeichnung());
        }
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

        zelle = "";
        if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
        {
          zelle += (m.getExterneMitgliedsnummer() != null ? m
              .getExterneMitgliedsnummer() : "");
        }

        report.addColumn(
            beitragsgruppebemerkung.toString() + eigenschaften.toString()
                + zelle, Element.ALIGN_LEFT);
      }
      report.closeTable();

      report.add(new Paragraph(MessageFormat.format("Anzahl {0}: {1}",
          adresstyp.getBezeichnungPlural(), list.size() + ""), FontFactory
          .getFont(FontFactory.HELVETICA, 8)));

      report.close();
      GUI.getStatusBar().setSuccessText(
          MessageFormat.format("Auswertung fertig. {0} Sätze.", list.size()));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    }
  }

  @Override
  public String getDateiname()
  {
    return "auswertung";
  }

  @Override
  public String getDateiendung()
  {
    return "PDF";
  }

  @Override
  public String toString()
  {
    return "Mitgliederliste PDF";
  }

  @Override
  public boolean openFile()
  {
    return true;
  }
}
