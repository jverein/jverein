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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class JahressaldoPDF
{

  public JahressaldoPDF(ArrayList<SaldoZeile> zeile, final File file,
      ProgressMonitor monitor, Geschaeftsjahr gj) throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = gj.toString();
      Reporter reporter = new Reporter(fos, "Jahressaldo", subtitle,
          zeile.size());

      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Konto-\nnummer"),
          Element.ALIGN_CENTER, 50, BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Kontobezeichnung"),
          Element.ALIGN_CENTER, 90, BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Anfangs-\nbestand"),
          Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Einnahmen"),
          Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Ausgaben"),
          Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Um-\nbuchungen"),
          Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Endbestand"),
          Element.ALIGN_CENTER, 45, BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Bemerkung"),
          Element.ALIGN_CENTER, 100, BaseColor.LIGHT_GRAY);
      reporter.createHeader();

      for (SaldoZeile sz : zeile)
      {
        reporter.addColumn((String) sz.getAttribute("kontonummer"),
            Element.ALIGN_LEFT);
        reporter.addColumn((String) sz.getAttribute("kontobezeichnung"),
            Element.ALIGN_LEFT);
        reporter.addColumn((Double) sz.getAttribute("anfangsbestand"));
        reporter.addColumn((Double) sz.getAttribute("einnahmen"));
        reporter.addColumn((Double) sz.getAttribute("ausgaben"));
        reporter.addColumn((Double) sz.getAttribute("umbuchungen"));
        reporter.addColumn((Double) sz.getAttribute("endbestand"));
        reporter.addColumn((String) sz.getAttribute("bemerkung"),
            Element.ALIGN_LEFT);
      }
      reporter.closeTable();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Auswertung fertig."));

      reporter.close();
      fos.close();
      GUI.getDisplay().asyncExec(new Runnable()
      {
        @Override
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
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler beim Erzeugen des Reports"), e);
    }
  }

}
