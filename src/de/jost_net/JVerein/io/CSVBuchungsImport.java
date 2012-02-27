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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.BuchungVar;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class CSVBuchungsImport implements Importer
{
  public void doImport(Object context, IOFormat format, File file,
      ProgressMonitor monitor) throws RemoteException, ApplicationException
  {
    ResultSet results;
    try
    {
      int anz = 0;

      Properties props = new java.util.Properties();
      props.put("separator", ";"); // separator is a bar
      props.put("suppressHeaders", "false"); // first line contains data
      props.put("charset", "ISO-8859-1");
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
          bu.setAuszugsnummer(results.getInt(BuchungVar.AUSZUGSNUMMER.getName()));
          bu.setArt(results.getString(BuchungVar.ART.getName()));
          bu.setBetrag(results.getDouble(BuchungVar.BETRAG.getName()));
          bu.setBlattnummer(results.getInt(BuchungVar.BLATTNUMMER.getName()));
          // bu.setBuchungsart()
          Date d = de.jost_net.JVerein.util.Datum.toDate(results
              .getString(BuchungVar.DATUM.getName()));
          bu.setDatum(d);
          bu.setKommentar(results.getString(BuchungVar.KOMMENTAR.getName()));
          // bu.setKonto(konto);
          bu.setName(results.getString(BuchungVar.NAME.getName()));
          bu.setZweck(results.getString(BuchungVar.ZWECK1.getName()));
          bu.setZweck2(results.getString(BuchungVar.ZWECK2.getName()));
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
      monitor.log(" nicht importiert: " + e.getMessage());
      Logger.error("Fehler", e);
    }
    finally
    {

    }
  }

  public String getName()
  {
    return "CSV-Buchungsimport";
  }

  public boolean hasFileDialog()
  {
    return true;
  }

  public IOFormat[] getIOFormats(Class objectType)
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