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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import de.willuhn.logging.Logger;

/**
 * This class handles the connection to a csv db file.
 * 
 * 
 * @author Christian Lutz
 * 
 */
public class CSVConnection
{

	private Connection conn;

	private Statement stmt;

	private ResultSet results;

	private File csvFile;

	private String tableName;

	private final char seperator = ';';

	/**
	 * Close all relevant connection etc if they are open
	 * 
	 * @throws SQLException
	 */
	public void closeCsvDB() throws SQLException
	{
		if (results != null)
		{
			try
			{
				results.close();
				results = null;
			}
			catch (SQLException e)
			{
				throw new SQLException("Konnte Result nicht ordentlich schliessen.");
			}
		}

		if (stmt != null)
		{
			try
			{
				stmt.close();
				stmt = null;
			}
			catch (SQLException e)
			{
				throw new SQLException("Konnte Statement nicht ordentlich schliessen.");
			}
		}

		if (conn != null)
		{
			try
			{
				conn.close();
				conn = null;
			}
			catch (SQLException e)
			{
				throw new SQLException(
						"Konnte Verbindung zur DB nicht ordentlich schliessen.");
			}
		}
	}

	/**
	 * Get the Header from the defined csv file
	 * 
	 * @return list of existing headers, they aren't sorted in any way
	 * @throws SQLException
	 */
	public List<String> getColumnHeaders() throws SQLException
	{
		List<String> importColumnList = new LinkedList<>();

		try
		{
			if (results == null || results.isClosed())
				this.getData();

			ResultSetMetaData meta = results.getMetaData();

			for (int i = 1; i <= meta.getColumnCount(); i++)
			{
				importColumnList.add(meta.getColumnName(i));
			}

		}
		catch (SQLException e)
		{
			throw new SQLException("Fehler beim lesen der Import Datei");
		}
		return importColumnList;
	}

	/**
	 * Get a ResultSet from all data in the csv File. If you called this method or
	 * another data method you will always get the same result object.
	 * 
	 * @return a ResultSet with the complete dataset
	 * @throws SQLException
	 */
	public ResultSet getData() throws SQLException
	{
		if (stmt == null)
			throw new NullPointerException("Statement wasn't created before");

		if (results == null || results.isClosed())
		{
			try
			{
				results = stmt.executeQuery("SELECT * FROM " + tableName);
			}
			catch (SQLException e)
			{
				throw new SQLException("Konnte Daten nicht aus der Datei lesen");
			}
		}
		return results;
	}

	/**
	 * Get only the Filename of the defined csv file. The string is empty if the
	 * file isn't defined.
	 */
	public String getFileName()
	{
		if (tableName == null)
			return "";

		return tableName;
	}

	/**
	 * To get the number of records in the file
	 * 
	 * @throws SQLException
	 */
	public int getNumberOfRows() throws SQLException
	{
		int result = 0;

		try
		{
			if (results == null || results.isClosed())
				this.getData();

			/*
			 * Bad implementation, but as long as no COUNT support is available in
			 * csvJDBC this is the only way
			 */
			results.beforeFirst();
			results.last();
			result = results.getRow();
		}
		catch (SQLException e)
		{
			Logger.error("Fehler", e);
			throw new SQLException(
					"Konnte Anzahl Daten nicht ermitteln - Häufiger Grund eine Leerstelle vor/nach einen Semikolon, siehe Stacktrace wegen der Zeile");
		}
		return result;
	}

	/**
	 * Before you can request anything from the file you have to open it. After
	 * you finished your operations you HAVE TO close it.
	 * 
	 * @throws SQLException
	 */
	public void openCsvDB() throws SQLException
	{
		Properties props = new java.util.Properties();
		props.put("separator", (Character.valueOf(seperator)).toString()); // separator
		// is a bar
		props.put("suppressHeaders", "false"); // first line contains data
		props.put("charset", "ISO-8859-1");
		int pos = csvFile.getName().lastIndexOf('.');
		props.put("fileExtension", csvFile.getName().substring(pos));

		/* load the driver into memory */
		try
		{
			Class.forName("org.relique.jdbc.csv.CsvDriver");
		}
		catch (ClassNotFoundException e)
		{
			Logger.error("Fehler", e);
			throw new SQLException("Wasn't able to load CSV DB Driver");
		}

		conn = DriverManager
				.getConnection("jdbc:relique:csv:" + csvFile.getParent(), props);

		/*
		 * The arguments are necessary to get a traveresable result set, so first()
		 * etc can be used
		 */
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 0);

	}

	/**
	 * Define csv File, this file will be opened if you request so. If the defined
	 * file doesn't exist or if the integrity isn't ok, This method will return
	 * false and you can't open the DB file.
	 * 
	 * @param csvFile
	 */
	public boolean setCSVFile(final File csvFile)
	{
		if (csvFile == null)
			throw new NullPointerException("csvFile may not be Null");

		/* if a connections is already established close this one for a new file */
		if (this.conn != null)
		{
			try
			{
				this.closeCsvDB();
			}
			catch (SQLException e)
			{
				/*
				 * this will just be printed to the stack trace because you can't do
				 * anything about it
				 */
				Logger.error("Fehler", e);
			}
		}

		this.csvFile = new File(csvFile.getPath());
		this.tableName = this.csvFile.getName().substring(0,
				csvFile.getName().lastIndexOf('.'));

		if (!this.csvFile.isFile() || !this.csvFile.exists())
			return false;

		return true;
	}

	public File getCSVFile()
	{
		return csvFile;
	}
}
