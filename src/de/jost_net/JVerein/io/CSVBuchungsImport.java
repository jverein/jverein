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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.BuchungMessage;
import de.jost_net.JVerein.Variable.BuchungVar;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class CSVBuchungsImport implements Importer
{

	@Override
	public void doImport(Object context, IOFormat format, File file,
			String encoding, ProgressMonitor monitor)
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
					Buchung bu = (Buchung) Einstellungen.getDBService()
							.createObject(Buchung.class, null);
					try
					{
						int auszugsnummer = results
								.getInt(BuchungVar.AUSZUGSNUMMER.getName());
						if (auszugsnummer > 0)
						{
							bu.setAuszugsnummer(auszugsnummer);
						}
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
						throw new ApplicationException(
								String.format("Spalte %s fehlt!", BuchungVar.BETRAG.getName()));
					}
					try
					{
						int blattnummer = results.getInt(BuchungVar.BLATTNUMMER.getName());
						if (blattnummer > 0)
						{
							bu.setBlattnummer(blattnummer);
						}
					}
					catch (SQLException e)
					{
						// Optionales Feld
					}
					try
					{
						Date d = de.jost_net.JVerein.util.Datum
								.toDate(results.getString(BuchungVar.DATUM.getName()));
						bu.setDatum(d);
					}
					catch (SQLException e)
					{
						throw new ApplicationException(
								String.format("Spalte %s fehlt!", BuchungVar.DATUM.getName()));
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
						String knr = results.getString(BuchungVar.KONTONUMMER.getName());
						DBIterator<Konto> kit = Einstellungen.getDBService()
								.createList(Konto.class);
						kit.addFilter("nummer = ?", knr);
						if (kit.size() == 0)
						{
							throw new ApplicationException(
									String.format("Konto %s existiert nicht in JVerein!", knr));
						}
						Konto k1 = kit.next();
						bu.setKonto(k1);
					}
					catch (SQLException e)
					{
						throw new ApplicationException(String.format("Spalte %s fehlt!",
								BuchungVar.KONTONUMMER.getName()));
					}
					try
					{
						Integer bart = results
								.getInt(BuchungVar.BUCHUNGSARTNUMMER.getName());
						DBIterator<Buchungsart> bit = Einstellungen.getDBService()
								.createList(Buchungsart.class);
						bit.addFilter("nummer = ?", bart);
						if (bit.size() != 1)
						{
							throw new ApplicationException(String
									.format("Buchungsart %d existiert nicht in JVerein!", bart));
						}
						Buchungsart b1 = bit.next();
						bu.setBuchungsart(new Long(b1.getID()));
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
						throw new ApplicationException(
								String.format("Spalte %s fehlt!", BuchungVar.NAME.getName()));
					}
					try
					{
						bu.setZweck(results.getString(BuchungVar.ZWECK1.getName()));
					}
					catch (SQLException e)
					{
						throw new ApplicationException(
								String.format("Spalte %s fehlt!", BuchungVar.ZWECK1.getName()));
					}
					bu.store();
					Application.getMessagingFactory().sendMessage(new BuchungMessage(bu));
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
		return "CSV-Buchungsimport";
	}

	public boolean hasFileDialog()
	{
		return true;
	}

	@Override
	public IOFormat[] getIOFormats(Class<?> objectType)
	{
		if (objectType != Buchung.class)
		{
			return null;
		}
		IOFormat f = new IOFormat()
		{

			@Override
			public String getName()
			{
				return CSVBuchungsImport.this.getName();
			}

			/**
			 * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
			 */
			@Override
			public String[] getFileExtensions()
			{
				return new String[]
				{
						"*.csv"
				};
			}
		};
		return new IOFormat[]
		{
				f
		};
	}
}
