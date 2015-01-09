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
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ProjektSaldoPDF
{

  public ProjektSaldoPDF(ArrayList<ProjektSaldoZeile> zeile,
      final File file, Date datumvon, Date datumbis)
      throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = new JVDateFormatTTMMJJJJ().format(datumvon) + " - "
          + new JVDateFormatTTMMJJJJ().format(datumbis);
      Reporter reporter = new Reporter(fos, "Projekte-Saldo", subtitle,
          zeile.size());
      makeHeader(reporter);

      for (ProjektSaldoZeile pz : zeile)
      {
        switch (pz.getStatus())
        {
          case ProjektSaldoZeile.HEADER:
          {
            reporter.addColumn(
                (String) pz.getAttribute("projektbezeichnung"),
                Element.ALIGN_LEFT, new BaseColor(220, 220, 220), 4);
            break;
          }
          case ProjektSaldoZeile.DETAIL:
          {
            reporter.addColumn(
                (String) pz.getAttribute("buchungsartbezeichnung"),
                Element.ALIGN_LEFT);
            reporter.addColumn((Double) pz.getAttribute("einnahmen"));
            reporter.addColumn((Double) pz.getAttribute("ausgaben"));
            reporter.addColumn((Double) pz.getAttribute("umbuchungen"));
            break;
          }
          case ProjektSaldoZeile.SALDOFOOTER:
          {
            reporter.addColumn(
                (String) pz.getAttribute("projektbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) pz.getAttribute("einnahmen"));
            reporter.addColumn((Double) pz.getAttribute("ausgaben"));
            reporter.addColumn((Double) pz.getAttribute("umbuchungen"));
            break;
          }
          case ProjektSaldoZeile.SALDOGEWINNVERLUST:
          {
            reporter.addColumn(
                (String) pz.getAttribute("projektbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) pz.getAttribute("einnahmen"));
            reporter.addColumn("", Element.ALIGN_LEFT, 2);
            break;
          }
          case ProjektSaldoZeile.GESAMTSALDOFOOTER:
          {
            reporter.addColumn(
                      (String) pz.getAttribute("projektbezeichnung"),
                      Element.ALIGN_LEFT);
            reporter.addColumn((Double) pz.getAttribute("einnahmen"));
            reporter.addColumn((Double) pz.getAttribute("ausgaben"));
            reporter.addColumn((Double) pz.getAttribute("umbuchungen"));
            break;
          }
          case ProjektSaldoZeile.GESAMTSALDOGEWINNVERLUST:
          {
            reporter.addColumn(
                      (String) pz.getAttribute("projektbezeichnung"),
                      Element.ALIGN_LEFT);
            reporter.addColumn((Double) pz.getAttribute("einnahmen"));
            reporter.addColumn("", Element.ALIGN_LEFT, 2);
            break;
          }
          case ProjektSaldoZeile.NICHTZUGEORDNETEBUCHUNGEN:
          {
            reporter.addColumn(
            		  (String) pz.getAttribute("projektbezeichnung"), 
            		  Element.ALIGN_RIGHT, 4);
            break;
          }
        }
      }
      GUI.getStatusBar().setSuccessText("Auswertung fertig.");
      reporter.closeTable();
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

  private void makeHeader(Reporter reporter) throws DocumentException
  {
    reporter.addHeaderColumn("Buchungsart", Element.ALIGN_CENTER, 90,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Einnahmen", Element.ALIGN_CENTER, 45,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Ausgaben", Element.ALIGN_CENTER, 45,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Umbuchungen", Element.ALIGN_CENTER, 45,
        BaseColor.LIGHT_GRAY);
    reporter.createHeader();
  }
}