/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import de.willuhn.util.ProgressMonitor;

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
   * Currently, this integrity checks only if in each line, the same number of
   * columns are available if not it won't pass
   * 
   * @return
   */
  public boolean checkCSVIntegrity(final ProgressMonitor monitor)
  {

    if (monitor == null)
      throw new NullPointerException("monitor may not be null");

    boolean valid = true;

    try
    {
      FileInputStream fis = new FileInputStream(csvFile);
      BufferedInputStream bis = new BufferedInputStream(fis);

      byte[] rCache = new byte[1024];
      int numberOfChars = 0;
      int numColumns = 0;
      int lastPosition = 0;
      boolean headerComplete = false;

      /* read header and identify amount of columns */
      while (!headerComplete)
      {

        numberOfChars = bis.read(rCache, 0, 1024);
        if (numberOfChars == -1)
          break;

        for (lastPosition = 0; lastPosition < numberOfChars; lastPosition++)
        {
          if (rCache[lastPosition] == seperator)
            numColumns++;
          else if (rCache[lastPosition] == '\n')
          {
            numColumns++;
            headerComplete = true;
            break;
          }
          else
          {
            /*
             * for each other character nothing has to be done, this part will
             * be removed by the compiler so just leave for information
             */
          }
        }
      }

      /* not ending header */
      if (!headerComplete)
      {
        monitor.setStatusText("Keine Daten, oder keine Kopfzeile");
        valid = false;
      }

      /* the position needs to increased because of the break in the for loop */
      lastPosition++;

      int columnsPerLine = 0;
      int lineNo = 1;
      /* count columns in each line */
      do
      {

        for (; lastPosition < numberOfChars; lastPosition++)
        {
          if (rCache[lastPosition] == seperator)
            columnsPerLine++;
          else if (rCache[lastPosition] == ' ')
          {
            if (lastPosition - 1 >= 0 && rCache[lastPosition - 1] == seperator)
            {
              monitor
                  .setStatusText("Leerzeichen nach einem Semikolon in Zeile:"
                      + lineNo + " und Spalte:" + columnsPerLine);
              valid = false;
            }
            if (lastPosition + 1 < numberOfChars
                && rCache[lastPosition + 1] == seperator)
            {
              monitor.setStatusText("Leerzeichen vor einem Semikolon in Zeile:"
                  + lineNo + " und Spalte:" + columnsPerLine);
              valid = false;
            }
          }
          else if (rCache[lastPosition] == '\n')
          {
            columnsPerLine++;
            lineNo++;
            if (columnsPerLine != numColumns)
            {
              monitor
                  .setStatusText("Anzahl der Spalten in Zeile:"
                      + lineNo
                      + " passt nicht mit der Anzahl Spalten in der Kopfzeile ueberein.");
              valid = false;
            }
            columnsPerLine = 0;
          }
          else
          {
            /*
             * for each other character nothing has to be done, this part will
             * be removed by the compiler so just leave it for information
             */
          }
        }

        lastPosition = 0;
        numberOfChars = bis.read(rCache, 0, 1024);

      }
      while (numberOfChars != -1);

      bis.close();
      fis.close();

    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
      valid = false;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      valid = false;
    }

    return valid;
  }

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

    List<String> importColumnList = new LinkedList<String>();

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
   * 
   * @return
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
   * @param table
   * @return
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
      e.printStackTrace();
      throw new SQLException(
          "Konnte Anzahl Daten nicht ermitteln - "
              + "Haeufiger Grund eine Leerstelle vor/nach einen Semikolon, siehe Stacktrace wegen der Zeile");
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
    props.put("separator", (new Character(seperator)).toString()); // separator
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
      e.printStackTrace();
      throw new SQLException("Wasn't able to load CSV DB Driver");
    }

    conn = DriverManager.getConnection(
        "jdbc:relique:csv:" + csvFile.getParent(), props);

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
   * @return
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
        e.printStackTrace();
      }
    }

    this.csvFile = new File(csvFile.getPath());
    this.tableName = this.csvFile.getName().substring(0,
        csvFile.getName().lastIndexOf('.'));

    if (!this.csvFile.isFile() || !this.csvFile.exists())
      return false;

    return true;
  }
}