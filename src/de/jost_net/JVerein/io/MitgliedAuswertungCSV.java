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
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.IAuswertung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedAuswertungCSV implements IAuswertung
{

  public MitgliedAuswertungCSV()
  {
  }

  @Override
  public void beforeGo()
  {
    // Nothing to do
  }

  @Override
  public void go(ArrayList<Mitglied> list, final File file)
      throws ApplicationException
  {
    try
    {
      ICsvMapWriter writer = new CsvMapWriter(new FileWriter(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

      Mitglied m = null;
      if (list.size() > 0)
      {
        m = list.get(0);
      }
      else
      {
        m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, null);
      }

      String[] header = createHeader(m);
      Logger.debug("Header");
      for (String s : header)
      {
        Logger.debug(s);
      }
      Map<String, Object> map = m.getMap(null);
      CellProcessor[] processors = CellProcessors.createCellProcessors(map);

      writer.writeHeader(header);

      for (Mitglied mit : list)
      {
        writer.write(mit.getMap(null), header, processors);
      }
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
    catch (Exception e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }
  }

  private String[] createHeader(Mitglied m)
  {
    try
    {
      return m.getMap(null).keySet().toArray(new String[0]);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    return null;
  }

  @Override
  public String getDateiname()
  {
    return "mitglied";
  }

  @Override
  public String getDateiendung()
  {
    return "CSV";
  }

  @Override
  public String toString()
  {
    return "Mitgliederliste CSV";
  }

  @Override
  public boolean openFile()
  {
    return true;
  }

}
