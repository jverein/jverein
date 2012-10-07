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
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsklassesaldoPDF
{

  public BuchungsklassesaldoPDF(ArrayList<BuchungsklasseSaldoZeile> zeile,
      final File file, Date datumvon, Date datumbis)
      throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = new JVDateFormatTTMMJJJJ().format(datumvon) + " - "
          + new JVDateFormatTTMMJJJJ().format(datumbis);
      Reporter reporter = new Reporter(fos, JVereinPlugin.getI18n().tr(
          "Buchungsklassen-Saldo"), subtitle, zeile.size());
      makeHeader(reporter);

      for (BuchungsklasseSaldoZeile bkz : zeile)
      {
        switch (bkz.getStatus())
        {
          case BuchungsklasseSaldoZeile.HEADER:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_LEFT, new BaseColor(220, 220, 220), 4);
            break;
          }
          case BuchungsklasseSaldoZeile.DETAIL:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsartbezeichnung"),
                Element.ALIGN_LEFT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.SALDOFOOTER:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.GESAMTSALDOFOOTER:
          {
            reporter.addColumn(JVereinPlugin.getI18n().tr("Gesamt"),
                Element.ALIGN_LEFT, 4);
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.GESAMTGEWINNVERLUST:
          case BuchungsklasseSaldoZeile.SALDOGEWINNVERLUST:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn("", Element.ALIGN_LEFT, 2);
            break;
          }
          case BuchungsklasseSaldoZeile.NICHTZUGEORDNETEBUCHUNGEN:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_LEFT);
            reporter.addColumn((Integer) bkz.getAttribute("anzahlbuchungen"));
            reporter.addColumn("", Element.ALIGN_LEFT, 2);
            break;
          }
        }
      }
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Auswertung fertig."));
      reporter.closeTable();
      reporter.close();
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
    catch (Exception e)
    {
      e.printStackTrace();
      Logger.error("error while creating report", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr("Fehler"), e);
    }
  }

  private void makeHeader(Reporter reporter) throws DocumentException
  {
    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Buchungsart"),
        Element.ALIGN_CENTER, 90, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Einnahmen"),
        Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Ausgaben"),
        Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Umbuchungen"),
        Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
    reporter.createHeader();
  }
}
