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
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.Variable.BuchungVar;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class CSVBuchungsImport implements Importer
{
  public void doImport(Object context, IOFormat format, File file,
      String encoding, ProgressMonitor monitor) throws RemoteException,
      ApplicationException
  {
    ResultSet results;
    try
    {
      int anz = 0;

      Properties props = new java.util.Properties();
      props.put("separator", ";"); // separator is a bar
      props.put("suppressHeaders", "false"); // first line contains data
      props.put("charset", encoding);
      String path = file.getParent();
      String fil = file.getName();
      int pos = fil.lastIndexOf('.');
      props.put("fileExtension", fil.substring(pos));

      // load the driver into memory
      Class.forName("org.relique.jdbc.csv.CsvDriver");

      // create a connection. The first command line parameter is assumed to
      // be the directory in which the .csv files are held
      Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + path,
          props);

      // create a Statement object to execute the query with
      Statement stmt = conn.createStatement();

      results = stmt.executeQuery("SELECT * FROM " + fil.substring(0, pos));

      while (results.next())
      {
        anz++;
        monitor.setStatus(anz);

        try
        {
          Buchung bu = (Buchung) Einstellungen.getDBService().createObject(
              Buchung.class, null);
          try
          {
            bu.setAuszugsnummer(results.getInt(BuchungVar.AUSZUGSNUMMER
                .getName()));
          }
          catch (SQLException e)
          {
            // Optionales Feld
          }
          try
          {
            bu.setArt(results.getString(BuchungVar.ART.getName()));
          }
          catch (SQLException e)
          {
            // Optionales Feld.
          }
          try
          {
            bu.setBetrag(results.getDouble(BuchungVar.BETRAG.getName()));
          }
          catch (SQLException e)
          {
            throw new ApplicationException(JVereinPlugin.getI18n().tr(
                "Spalte {0} fehlt!", BuchungVar.BETRAG.getName()));
          }
          try
          {
            bu.setBlattnummer(results.getInt(BuchungVar.BLATTNUMMER.getName()));
          }
          catch (SQLException e)
          {
            // Optionales Feld
          }
          try
          {
            Date d = de.jost_net.JVerein.util.Datum.toDate(results
                .getString(BuchungVar.DATUM.getName()));
            bu.setDatum(d);
          }
          catch (SQLException e)
          {
            throw new ApplicationException(JVereinPlugin.getI18n().tr(
                "Spalte {0} fehlt!", BuchungVar.DATUM.getName()));
          }
          try
          {
            bu.setKommentar(results.getString(BuchungVar.KOMMENTAR.getName()));
          }
          catch (SQLException e)
          {
            // Optionales Feld
          }
          try
          {
            Long knr = results.getLong(BuchungVar.KONTONUMMER.getName());
            DBIterator kit = Einstellungen.getDBService().createList(
                Konto.class);
            kit.addFilter("nummer = ?", knr);
            if (kit.size() == 0)
            {
              throw new ApplicationException(JVereinPlugin.getI18n().tr(
                  "Konto {0} existiert nicht in JVerein!", knr + ""));
            }
            Konto k1 = (Konto) kit.next();
            bu.setKonto(k1);
          }
          catch (SQLException e)
          {
            throw new ApplicationException(JVereinPlugin.getI18n().tr(
                "Spalte {0} fehlt!", BuchungVar.KONTONUMMER.getName()));
          }
          try
          {
            Integer bart = results.getInt(BuchungVar.BUCHUNGSARTNUMMER
                .getName());
            DBIterator bit = Einstellungen.getDBService().createList(
                Buchungsart.class);
            bit.addFilter("nummer = ?", bart);
            Buchungsart b1 = (Buchungsart) bit.next();
            if (bit.size() == 0)
            {
              throw new ApplicationException(JVereinPlugin.getI18n().tr(
                  "Buchungsart {0} existiert nicht in JVerein!", bart + ""));
            }
            bu.setBuchungsart(Integer.parseInt(b1.getID()));
          }
          catch (SQLException e)
          {
            // Optionales Feld
          }
          try
          {
            bu.setName(results.getString(BuchungVar.NAME.getName()));
          }
          catch (SQLException e)
          {
            throw new ApplicationException(JVereinPlugin.getI18n().tr(
                "Spalte {0} fehlt!", BuchungVar.NAME.getName()));
          }
          try
          {
            bu.setZweck(results.getString(BuchungVar.ZWECK1.getName()));
          }
          catch (SQLException e)
          {
            throw new ApplicationException(JVereinPlugin.getI18n().tr(
                "Spalte {0} fehlt!", BuchungVar.ZWECK1.getName()));
          }
          bu.store();
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
      monitor.log(JVereinPlugin.getI18n().tr(" nicht importiert: ")
          + e.getMessage());
      Logger.error(JVereinPlugin.getI18n().tr("Fehler"), e);
    }
    finally
    {

    }
  }

  public String getName()
  {
    return JVereinPlugin.getI18n().tr("CSV-Buchungsimport");
  }

  public boolean hasFileDialog()
  {
    return true;
  }

  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != Buchung.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {
      public String getName()
      {
        return CSVBuchungsImport.this.getName();
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
}