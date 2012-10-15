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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungAuswertungCSV
{

  public BuchungAuswertungCSV(ArrayList<Buchung> list, final File file,
      ProgressMonitor monitor) throws ApplicationException
  {

    try
    {
      ICsvMapWriter writer = new CsvMapWriter(new FileWriter(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

      String[] header = createHeader();
      CellProcessor[] processors = createCellProcessors();

      writer.writeHeader(header);

      for (Buchung b : list)
      {
        writer.write(b.getMap(null), header, processors);
      }
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Auswertung fertig. {0} Sätze.",
              list.size() + ""));
      writer.close();
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
    catch (FileNotFoundException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr("Fehler"), e);
    }
    catch (IOException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr("Fehler"), e);
    }

  }

  private String[] createHeader()
  {
    try
    {
      Buchung b = (Buchung) Einstellungen.getDBService().createObject(
          Buchung.class, null);
      return b.getMap(null).keySet().toArray(new String[0]);
    }
    catch (RemoteException e)
    {
      Logger.error(JVereinPlugin.getI18n().tr("Fehler"), e);
    }
    return null;
  }

  private CellProcessor[] createCellProcessors()
  {
    try
    {
      Buchung b = (Buchung) Einstellungen.getDBService().createObject(
          Buchung.class, null);
      Map<String, Object> map = b.getMap(null);
      CellProcessor[] ret = new CellProcessor[map.size()];
      for (int i = 0; i < map.size(); i++)
      {
        ret[i] = new ConvertNullTo("");
      }
      return ret;
    }
    catch (RemoteException e)
    {
      Logger.error(JVereinPlugin.getI18n().tr("Fehler"), e);
    }
    return null;
  }

}
