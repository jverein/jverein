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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class DefaultZusatzbetraegeImport implements IZusatzbetraegeImport
{
  private String path;

  private String file;

  public void doImport(Object context, ProgressMonitor monitor)
  {
    ResultSet results;
    try
    {
      int anz = 0;

      Properties props = new java.util.Properties();
      props.put("separator", ";"); // separator is a bar
      props.put("suppressHeaders", "false"); // first line contains data
      props.put("charset", "ISO-8859-1");
      int pos = file.lastIndexOf('.');
      props.put("fileExtension", file.substring(pos));

      // load the driver into memory
      Class.forName("org.relique.jdbc.csv.CsvDriver");

      // create a connection. The first command line parameter is assumed to
      // be the directory in which the .csv files are held
      Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + path,
          props);

      // create a Statement object to execute the query with
      Statement stmt = conn.createStatement();

      results = stmt.executeQuery("SELECT * FROM " + file.substring(0, pos));
      boolean b_mitgliedsnummer = false;
      boolean b_nachname = false;
      boolean b_vorname = false;
      ResultSetMetaData meta = results.getMetaData();
      for (int i = 1; i < meta.getColumnCount(); i++)
      {
        if (meta.getColumnName(i).equals("Mitglieds_Nr"))
        {
          b_mitgliedsnummer = true;
        }
        if (meta.getColumnName(i).equals("Nachname"))
        {
          b_nachname = true;
        }
        if (meta.getColumnName(i).equals("Vorname"))
        {
          b_vorname = true;
        }
      }
      if (b_mitgliedsnummer && (b_nachname || b_vorname))
      {
        monitor.setStatus(ProgressMonitor.STATUS_ERROR);
        monitor
            .setStatusText("Entweder Mitglieds_Nr oder Nachname/Vorname zur Zuordnung des Mitglieds angeben. Abbruch!");
        return;
      }

      while (results.next())
      {
        anz++;
        monitor.setStatus(anz);

        DBIterator list = Einstellungen.getDBService().createList(
            Mitglied.class);
        if (b_mitgliedsnummer)
        {
          list.addFilter("id = ? ",
              new Object[] { results.getString("Mitglieds_Nr") });
        }
        if (b_nachname)
        {
          list.addFilter("name = ? ",
              new Object[] { results.getString("Nachname") });
        }
        if (b_vorname)
        {
          list.addFilter("vorname = ? ",
              new Object[] { results.getString("Vorname") });
        }
        if (list.size() == 0)
        {
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          monitor.setStatusText("Für die Importzeile " + anz
              + " kein Mitglied gefunden. Abbruch!");
          return;
        }
        if (list.size() > 1)
        {
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          monitor.setStatusText("Für die Importzeile " + anz
              + " mehr als ein Mitglied gefunden. Abbruch!");
          return;
        }

        Mitglied m = (Mitglied) list.next();
        try
        {
          Zusatzbetrag zus = (Zusatzbetrag) Einstellungen.getDBService()
              .createObject(Zusatzbetrag.class, null);
          zus.setMitglied(new Integer(m.getID()));
          zus.setBetrag(results.getDouble("Betrag"));
          zus.setBuchungstext(results.getString("Buchungstext"));
          try
          {
            zus.setBuchungstext2(results.getString("Buchungstext2"));
          }
          catch (SQLException e)
          {
            Logger.error("Fehler", e);
          }
          Date d = de.jost_net.JVerein.util.Datum.toDate(results
              .getString("Fälligkeit"));
          zus.setFaelligkeit(d);
          zus.setStartdatum(d);
          zus.setIntervall(results.getInt("Intervall"));

          zus.store();
        }
        catch (Exception e)
        {
          throw new ApplicationException(e.getMessage() + ", "
              + m.getNameVorname());
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

    }
  }

  public String getName()
  {
    return "Default";
  }

  public boolean hasFileDialog()
  {
    return true;
  }

  public void set(String path, String file)
  {
    this.path = path;
    this.file = file;
  }

  public String getPath()
  {
    return path;
  }

  public String getFile()
  {
    return file;
  }

}
