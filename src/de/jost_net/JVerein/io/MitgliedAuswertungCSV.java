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
import java.io.FileReader;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.IAuswertung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedAuswertungCSV implements IAuswertung
{

  private String vorlagedateiname;

  private String name;

  private String[] headerUser;

  private String[] headerKeys;

  public MitgliedAuswertungCSV()
  {
    this.vorlagedateiname = "";
    this.name = "Mitgliederliste CSV";
    this.headerKeys = null;
    this.headerUser = null;
  }

  public MitgliedAuswertungCSV(String filename)
  {
    this(); // call default constructor

    if (filename.length() > 0)
    {
      this.vorlagedateiname = filename;
      this.name = "Vorlage CSV: "
          + filename.substring(filename.lastIndexOf(File.separator) + 1,
              filename.lastIndexOf("."));
    }
  }

  @Override
  public void beforeGo() throws RemoteException
  {
    // read and check vorlagedateicsv
    headerKeys = null;
    headerUser = null;

    if (!vorlagedateiname.isEmpty())
    {
      Logger.info("reading " + vorlagedateiname);

      // read the file content: first 2 lines
      // 1st line: headerUser
      // 2nd line: headerKeys
      try
      {
        File file = new File(vorlagedateiname);
        ICsvListReader reader = new CsvListReader(new FileReader(file),
            CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        headerUser = reader.read().toArray(new String[0]);
        headerKeys = reader.read().toArray(new String[0]);

      }
      catch (Exception e)
      {
        Logger.error("error reading " + vorlagedateiname, e);
        throw new RemoteException("Fehler beim Einlesen der Vorlagedatei "
            + vorlagedateiname, e);
      }

      // check the file content
      if (headerUser.length == 0)
      {
        Logger.error("no elements in first line: " + vorlagedateiname);
        throw new RemoteException("keine Elemente in erster Zeile in Datei "
            + vorlagedateiname);
      }
      if (headerUser.length != headerKeys.length)
      {
        Logger.error("different number of elements in 1st and 2nd line: "
            + vorlagedateiname);
        throw new RemoteException(
            "unterschiedliche Anzahl Elemente in 1. und 2. Zeile: "
                + vorlagedateiname);
      }
    }
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

      if (headerKeys == null || headerUser == null)
      {
        headerKeys = createHeader(m);
        headerUser = headerKeys;
      }
      // TEST
      // headerKeys = new String[] { "mitglied_name", "mitglied_vorname",
      // "mitglied_eintritt" };
      // headerUser = new String[] { "Name", "Vorname", "Eintrittsdatum" };

      Logger.debug("Header");
      for (String s : headerKeys)
      {
        Logger.debug(s);
      }

      Map<String, Object> map = m.getMap(null);
      // check headerKeys against map
      for (String key : headerKeys)
      {
        if (!map.containsKey(key))
        {
          throw new ApplicationException("invalid key: " + key);
        }
      }

      CellProcessor[] processors = CellProcessors.createCellProcessors(map,
          headerKeys);

      writer.writeHeader(headerUser);

      for (Mitglied mit : list)
      {
        writer.write(mit.getMap(null), headerKeys, processors);
      }
      writer.close();
      FileViewer.show(file);
    }
    catch (Exception e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports ("
          + e.getMessage() + ")", e);
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
    return name;
  }

  @Override
  public boolean openFile()
  {
    return true;
  }

}
