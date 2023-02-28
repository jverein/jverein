/**********************************************************************
 * JVerein - Mitgliederverwaltung und einfache Buchhaltung für Vereine
 * Copyright (c) by Heiner Jostkleigrewe
 * Copyright (c) 2014 by Thomas Hooge
 * Main Project: heiner@jverein.dem  http://www.jverein.de/
 * Module Author: thomas@hoogi.de, http://www.hoogi.de/
 *
 * This file is part of JVerein.
 *
 * JVerein is free software: you can redistribute it and/or modify 
 * it under the terms of the  GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JVerein is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/*
 * Importieren von Objekten zu Mitgliedern
 * 
 * TODO
 * - Die  Liste  mit den Objekttypen muß zuvor vorhanden sein!
 * - Optionale Felder
 *     gruppe
 *     extnr
 */
public class FormularfelderImportCSV implements Importer
{

  /**
   * Alle vorhandenen Formularfelder für ein bestimmtes Formular löschen
   */
  private void deleteExistingData(Formular f)
  {
    try
    {
      DBIterator<Formularfeld> list = Einstellungen.getDBService()
          .createList(Formularfeld.class);
      list.addFilter("formular = ?", f.getID());
      while (list.hasNext())
      {
        Formularfeld ff =  list.next();
        ff.delete();
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    catch (ApplicationException ae)
    {
      Logger.error("Fehler", ae);
    }
  }

  @Override
  public void doImport(Object context, IOFormat format, File file,
      String encoding, ProgressMonitor monitor)
  {

    // Datenbank bereinigen
    Formular f = (Formular) context;
    try
    {
      Logger
          .info("Importiere Formularfelder für Formular " + f.getBezeichnung());
    }
    catch (RemoteException re)
    {
      // pass
    }
    deleteExistingData(f);

    ResultSet results;
    try
    {

      Properties props = new java.util.Properties();
      props.put("separator", ";");
      props.put("suppressHeaders", "false");
      props.put("charset", encoding);
      String path = file.getParent();
      String fil = file.getName();
      int pos = fil.lastIndexOf('.');
      props.put("fileExtension", fil.substring(pos));

      Class.forName("org.relique.jdbc.csv.CsvDriver");
      Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + path,
          props);
      Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_READ_ONLY);
      results = stmt.executeQuery("SELECT * FROM " + fil.substring(0, pos));

      int anz = 0;
      while (results.next())
      {
        anz++;
        monitor.setStatus(anz);

        try
        {
          Formularfeld ff = (Formularfeld) Einstellungen.getDBService()
              .createObject(Formularfeld.class, null);
          ff.setFormular(f);
          try
          {
            ff.setName(results.getString("name"));
          }
          catch (SQLException e)
          {
            throw new ApplicationException("Spalte 'name' fehlt!");
          }

          try
          {
            ff.setX(results.getDouble("x"));
          }
          catch (SQLException e)
          {
            throw new ApplicationException("Spalte 'x' fehlt!");
          }

          try
          {
            ff.setY(results.getDouble("y"));
          }
          catch (SQLException e)
          {
            throw new ApplicationException("Spalte 'y' fehlt!");
          }

          try
          {
            ff.setSeite(results.getInt("seite"));
          }
          catch (SQLException e)
          {
            ff.setFontsize(1);
          }

          try
          {
            ff.setFont(results.getString("font"));
          }
          catch (SQLException e)
          {
            throw new ApplicationException("Spalte 'font' fehlt!");
          }

          try
          {
            ff.setFontsize(results.getInt("fontsize"));
          }
          catch (SQLException e)
          {
            ff.setFontsize(10);
          }

          try
          {
            ff.setFontstyle(results.getInt("fontstyle"));
          }
          catch (SQLException e)
          {
            ff.setFontstyle(0);
          }

          ff.store();
        }
        catch (Exception e)
        {
          throw new ApplicationException(e.getMessage());
        }
      }
      results.close();
      stmt.close();
      conn.close();
    }
    catch (Exception e)
    {
      monitor.log(" nicht importiert: " + e.getMessage());
      Logger.error("Fehler", e);
    }
    finally
    {
      //
    }
  }

  @Override
  public String getName()
  {
    return "CSV-Formularfeldimport";
  }

  public boolean hasFileDialog()
  {
    return true;
  }

  @Override
  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != Formularfeld.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {

      @Override
      public String getName()
      {
        return FormularfelderImportCSV.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      @Override
      public String[] getFileExtensions()
      {
        return new String[] { "*.csv" };
      }
    };
    return new IOFormat[] { f };
  }
}
