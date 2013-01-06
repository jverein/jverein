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
package de.jost_net.JVerein.gui.view;

import java.io.File;
import java.net.URL;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.io.BLZDatei;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.util.IbanBicCalc;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class SEPAKonvertierungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("SEPA-Konvertierung"));

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("starten"), new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        BackgroundTask t = new BackgroundTask()
        {
          @Override
          public void run(ProgressMonitor monitor) throws ApplicationException
          {
            try
            {
              starte(monitor);
              monitor.setPercentComplete(100);
              monitor.setStatus(ProgressMonitor.STATUS_DONE);
              GUI.getCurrentView().reload();
            }
            catch (Exception e)
            {
              monitor.setStatus(ProgressMonitor.STATUS_ERROR);
            }
          }

          @Override
          public void interrupt()
          {
            //
          }

          @Override
          public boolean isInterrupted()
          {
            return false;
          }
        };
        Application.getController().start(t);

      }
    }, null, true, "document-save.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return JVereinPlugin
        .getI18n()
        .tr("<form><p><span color=\"header\" font=\"header\">SEPA-Konvertierung</span></p></form>");
  }

  private void starte(ProgressMonitor monitor) throws ApplicationException
  {
    URL u = BLZDatei.class.getClassLoader().getResource("blz.zip");
    try
    {
      File f = new File(u.toURI());
      BLZDatei blz = new BLZDatei(f);

      DBIterator it = Einstellungen.getDBService()
          .createList(Einstellung.class);
      while (it.hasNext())
      {
        Einstellung einstellung = (Einstellung) it.next();
        if (einstellung != null)
        {
          einstellung.setBic(blz.get(einstellung.getBlz()).getBic());
          einstellung.setIban(IbanBicCalc.createIban(einstellung.getKonto(),
              einstellung.getBlz(), Einstellungen.getEinstellung()
                  .getDefaultLand()));
          einstellung.store();
          Einstellungen.setEinstellung(einstellung);
          monitor.log("Einstellung: BIC und IBAN gesetzt");
        }
      }
    }
    catch (Exception e)
    {
      throw new ApplicationException(e);
    }
  }
}
