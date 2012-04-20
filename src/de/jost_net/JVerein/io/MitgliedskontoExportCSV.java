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

import com.lowagie.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.MitgliedskontoMap;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoExportCSV extends MitgliedskontoExport
{
  private ArrayList<Mitgliedskonto> mkonten = new ArrayList<Mitgliedskonto>();

  public String getName()
  {
    return "Mitgliedskonten CSV-Export";
  }

  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != Mitgliedskonto.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {
      public String getName()
      {
        return MitgliedskontoExportCSV.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      public String[] getFileExtensions()
      {
        return new String[] { "*.csv" };
      }
    };
    return new IOFormat[] { f };
  }

  public String getDateiname()
  {
    return "mitgliedskonten";
  }

  protected void open() throws DocumentException, FileNotFoundException
  {
    //
  }

  protected void startMitglied(Mitglied m)
  {
    //
  }

  protected void endeMitglied()
  {
    //
  }

  protected void add(Mitgliedskonto mk) throws RemoteException
  {
    mkonten.add(mk);
  }

  protected void close()
  {
    try
    {
      ICsvMapWriter writer = new CsvMapWriter(new FileWriter(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

      Mitglied m = null;
      Mitgliedskonto mk = null;
      if (mkonten.size() > 0)
      {
        mk = mkonten.get(0);
        m = mk.getMitglied();
      }
      else
      {
        mk = (Mitgliedskonto) Einstellungen.getDBService().createObject(
            Mitgliedskonto.class, null);
        m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, null);
      }

      Map<String, Object> map = mk.getMitglied().getMap(null);
      map = new MitgliedskontoMap().getMap(mk, map);
      String[] header = createHeader(map);
      Logger.debug("Header");
      for (String s : header)
      {
        Logger.debug(s);
      }
      CellProcessor[] processors = createCellProcessors(map);

      writer.writeHeader(header);

      for (Mitgliedskonto mkto : mkonten)
      {
        Map<String, Object> mp = mkto.getMitglied().getMap(null);
        map = new MitgliedskontoMap().getMap(mkto, mp);
        writer.write(map, header, processors);
      }
      writer.close();
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
    catch (FileNotFoundException e)
    {
      Logger.error("error while creating report", e);
      // throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }
    catch (IOException e)
    {
      Logger.error("error while creating report", e);
      // throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }

  }

  private String[] createHeader(Map<String, Object> map)
  {
    return map.keySet().toArray(new String[0]);
  }

  private CellProcessor[] createCellProcessors(Map<String, Object> map)
  {
    CellProcessor[] ret = new CellProcessor[map.size()];
    for (int i = 0; i < map.size(); i++)
    {
      ret[i] = new ConvertNullTo("");
    }
    return ret;
  }

}
