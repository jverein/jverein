/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Queries.BuchungQuery;
import de.jost_net.JVerein.keys.ArtBuchungsart;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungAuswertungPDF {

  private double summe = 0;

  private double summeeinnahmen = 0;

  private double summeausgaben = 0;

  private double summeumbuchungen = 0;

  public BuchungAuswertungPDF(ArrayList<Buchungsart> buchungsarten, final File file,
      BuchungQuery query, boolean einzel) throws ApplicationException {
    try {
      FileOutputStream fos = new FileOutputStream(file);
      String title = null;
      if (einzel) {
        title = "Buchungsliste";
      } else {
        title = "Summenliste";
      }
      Reporter reporter = new Reporter(fos, title, query.getSubtitle(), buchungsarten.size());

      if (!einzel) {
        createTableHeaderSumme(reporter);
      }

      for (Buchungsart bua : buchungsarten) {
        if (einzel) {
          query.setOrderDatumID();
        }
        List<Buchung> liste = getBuchungenEinerBuchungsart(query.get(), bua);
        createTableContent(reporter, bua, liste, einzel);
      }
      if (buchungsarten.size() > 1) {
        if (einzel) {
          createTableHeaderEinzel(reporter);
          reporter.addColumn("", Element.ALIGN_RIGHT);
          reporter.addColumn("", Element.ALIGN_LEFT);
          reporter.addColumn("", Element.ALIGN_LEFT);
          reporter.addColumn("", Element.ALIGN_LEFT);
          reporter.addColumn("Gesamtsumme", Element.ALIGN_LEFT);
          reporter.addColumn(summe);
          reporter.closeTable();
        } else {
          reporter.addColumn("Summe Einnahmen", Element.ALIGN_LEFT);
          reporter.addColumn(summeeinnahmen);
          reporter.addColumn("Summe Ausgaben", Element.ALIGN_LEFT);
          reporter.addColumn(summeausgaben);
          reporter.addColumn("Summe Umbuchungen", Element.ALIGN_LEFT);
          reporter.addColumn(summeumbuchungen);
          reporter.addColumn("Saldo", Element.ALIGN_LEFT);
          reporter.addColumn(summeeinnahmen + summeausgaben + summeumbuchungen);
        }

      }
      GUI.getStatusBar().setSuccessText("Auswertung fertig.");

      reporter.close();
      fos.close();
      FileViewer.show(file);
    } catch (DocumentException e) {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    } catch (FileNotFoundException e) {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    } catch (IOException e) {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    }
  }

  private void createTableHeaderEinzel(Reporter reporter) throws DocumentException {
    reporter.addHeaderColumn("Nummer", Element.ALIGN_CENTER, 22, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Datum", Element.ALIGN_CENTER, 28, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Auszug", Element.ALIGN_CENTER, 20, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Name", Element.ALIGN_CENTER, 100, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Zahlungsgrund", Element.ALIGN_CENTER, 100, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 40, BaseColor.LIGHT_GRAY);
    reporter.createHeader();
  }

  private void createTableHeaderSumme(Reporter reporter) throws DocumentException {
    reporter.addHeaderColumn("Buchungsart", Element.ALIGN_CENTER, 200, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 60, BaseColor.LIGHT_GRAY);
    reporter.createHeader();
  }

  private void createTableContent(Reporter reporter, Buchungsart bua, List<Buchung> buchungen,
      boolean einzel) throws RemoteException, DocumentException {
    if (Einstellungen.getEinstellung().getUnterdrueckungOhneBuchung() && buchungen.size() == 0) {
      return;
    }
    if (einzel) {
      Paragraph pBuchungsart = new Paragraph(bua.getBezeichnung(), Reporter.getFreeSansBold(10));
      reporter.add(pBuchungsart);
    }
    double buchungsartSumme = 0;
    if (einzel) {
      createTableHeaderEinzel(reporter);
    }

    for (Buchung b : buchungen) {
      if (einzel) {
        if (!Einstellungen.getEinstellung().getVerwendeBelegnummer()) {
          reporter.addColumn(b.getID(), Element.ALIGN_RIGHT);
        } else {
          reporter.addColumn(b.getBelegnummer().toString(), Element.ALIGN_RIGHT);
        }
        reporter.addColumn(new JVDateFormatTTMMJJJJ().format(b.getDatum()), Element.ALIGN_CENTER);
        if (b.getAuszugsnummer() != null) {
          reporter.addColumn(
              b.getAuszugsnummer() + "/" + (b.getBlattnummer() != null ? b.getBlattnummer() : "-"),
              Element.ALIGN_LEFT);
        } else {
          reporter.addColumn("", Element.ALIGN_LEFT);
        }
        reporter.addColumn(b.getName(), Element.ALIGN_LEFT);
        reporter.addColumn(b.getZweck(), Element.ALIGN_LEFT);
        reporter.addColumn(b.getBetrag());
      }
      buchungsartSumme += b.getBetrag();
      if (bua.getArt() == ArtBuchungsart.EINNAHME) {
        summeeinnahmen += b.getBetrag();
      }
      if (bua.getArt() == ArtBuchungsart.AUSGABE) {
        summeausgaben += b.getBetrag();
      }
      if (bua.getArt() == ArtBuchungsart.UMBUCHUNG) {
        summeumbuchungen += b.getBetrag();
      }
    }
    if (einzel) {
      reporter.addColumn("", Element.ALIGN_RIGHT);
      reporter.addColumn("", Element.ALIGN_CENTER);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      if (buchungen.size() == 0) {
        reporter.addColumn("keine Buchung", Element.ALIGN_LEFT);
        reporter.addColumn("", Element.ALIGN_LEFT);
      } else {
        reporter.addColumn(String.format("Summe %s", bua.getBezeichnung()), Element.ALIGN_LEFT);
        summe += buchungsartSumme;
        reporter.addColumn(buchungsartSumme);
      }
    } else {
      reporter.addColumn(bua.getBezeichnung(), Element.ALIGN_LEFT);
      reporter.addColumn(buchungsartSumme);
    }
    if (einzel) {
      reporter.closeTable();
    }
  }

  private List<Buchung> getBuchungenEinerBuchungsart(List<Buchung> buchungen, Buchungsart bua)
      throws RemoteException {
    List<Buchung> liste = new ArrayList<>();
    for (Buchung b : buchungen) {
      if (bua.getArt() != -1
          && (b.getBuchungsart() == null || b.getBuchungsart().getNummer() != bua.getNummer())) {
        continue;
      }

      if (bua.getArt() == -1 && b.getBuchungsart() != null) {
        continue;
      }
      liste.add(b);
    }
    return liste;
  }
}
