/**********************************************************************
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.Queries.BuchungQuery;
import de.jost_net.JVerein.keys.ArtBuchungsart;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsjournalPDF
{

  public BuchungsjournalPDF(BuchungQuery query, final File file)
      throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      Reporter reporter = new Reporter(fos, "Buchungsjournal",
          query.getSubtitle(), 1);

      double einnahmen = 0;
      double ausgaben = 0;
      double umbuchungen = 0;
      double nichtzugeordnet = 0;

      createTableHeader(reporter);

      for (Buchung b : query.get())
      {
        reporter.addColumn(b.getID(), Element.ALIGN_RIGHT);
        reporter.addColumn(new JVDateFormatTTMMJJJJ().format(b.getDatum()),
            Element.ALIGN_LEFT);
        reporter.addColumn(b.getKonto().getNummer(), Element.ALIGN_RIGHT);
        if (b.getAuszugsnummer() != null)
        {
          reporter.addColumn(b.getAuszugsnummer() + "/"
              + (b.getBlattnummer() != null ? b.getBlattnummer() : "-"),
              Element.ALIGN_LEFT);
        }
        else
        {
          reporter.addColumn("", Element.ALIGN_LEFT);
        }
        reporter.addColumn(b.getName(), Element.ALIGN_LEFT);
        reporter.addColumn(b.getZweck(), Element.ALIGN_LEFT);
        reporter.addColumn(b.getBuchungsart() != null ? b.getBuchungsart()
            .getBezeichnung() : "", Element.ALIGN_LEFT);
        reporter.addColumn(b.getBetrag());
        if (b.getBuchungsart() != null)
        {
          int buchungsartart = b.getBuchungsart().getArt();
          switch (buchungsartart)
          {
            case ArtBuchungsart.EINNAHME:
            {
              einnahmen += b.getBetrag();
              break;
            }
            case ArtBuchungsart.AUSGABE:
            {
              ausgaben += b.getBetrag();
              break;
            }
            case ArtBuchungsart.UMBUCHUNG:
            {
              umbuchungen += b.getBetrag();
              break;
            }
          }
        }
        else
        {
          nichtzugeordnet += b.getBetrag();
        }
      }

      for (int i = 0; i < 5; i++)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.addColumn("Summe Einnahmen", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn(einnahmen);

      for (int i = 0; i < 5; i++)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.addColumn("Summe Ausgaben", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn(ausgaben);

      for (int i = 0; i < 5; i++)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.addColumn("Summe Umbuchungen", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn(umbuchungen);

      if (nichtzugeordnet != 0)
      {
        for (int i = 0; i < 5; i++)
        {
          reporter.addColumn("", Element.ALIGN_LEFT);
        }
        reporter.addColumn("Summe nicht zugeordnet", Element.ALIGN_LEFT);
        reporter.addColumn("", Element.ALIGN_LEFT);
        reporter.addColumn(nichtzugeordnet);
      }

      reporter.closeTable();
      GUI.getStatusBar().setSuccessText("Auswertung fertig.");

      reporter.close();
      fos.close();
      FileViewer.show(file);
    }
    catch (Exception e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    }
  }

  private void createTableHeader(Reporter reporter) throws DocumentException
  {
    reporter.addHeaderColumn("Nr", Element.ALIGN_RIGHT, 20,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Datum", Element.ALIGN_CENTER, 45,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Konto", Element.ALIGN_CENTER, 40,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Auszug", Element.ALIGN_CENTER, 20,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Name", Element.ALIGN_CENTER, 100,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Zahlungsgrund", Element.ALIGN_CENTER, 100,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Buchungsart", Element.ALIGN_CENTER, 60,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 50,
        BaseColor.LIGHT_GRAY);
    reporter.createHeader();
  }
}
