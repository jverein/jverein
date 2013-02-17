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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.util.ProgressMonitor;

/**
 * This class provides some helper methods to deal with csv files
 * 
 * 
 * @author Christian
 * 
 */
public class CSVFileHelper
{

  private static final char SEPARATOR = ';';

  /**
   * Currently, this integrity checks only if in each line, the same number of
   * columns are available if not it won't pass
   */
  public boolean checkCSVIntegrity(final ProgressMonitor monitor,
      final File csvFile)
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
          if (rCache[lastPosition] == SEPARATOR)
          {
            numColumns++;
          }
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
        monitor
            .setStatusText(JVereinPlugin
                .getI18n()
                .tr("Keine Daten oder keine Kopfzeile oder Encoding falsch. Siehe http://http://www.jverein.de/administration_import.php"));
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
          if (rCache[lastPosition] == SEPARATOR)
            columnsPerLine++;
          else if (rCache[lastPosition] == ' ')
          {
            if (lastPosition - 1 >= 0 && rCache[lastPosition - 1] == SEPARATOR)
            {
              monitor
                  .setStatusText(JVereinPlugin
                      .getI18n()
                      .tr("Leerzeichen nach einem Semikolon in Zeile: {0} und Spalte: {1}",
                          lineNo + "", columnsPerLine + ""));
              valid = false;
            }
            if (lastPosition + 1 < numberOfChars
                && rCache[lastPosition + 1] == SEPARATOR)
            {
              monitor
                  .setStatusText(JVereinPlugin
                      .getI18n()
                      .tr("Leerzeichen vor einem Semikolon in Zeile: {0} und Spalte: {1}",
                          lineNo + "", columnsPerLine + ""));
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
                  .setStatusText(JVereinPlugin
                      .getI18n()
                      .tr("Anzahl der Spalten in Zeile: {0} passt nicht mit der Anzahl Spalten in der Kopfzeile ueberein.",
                          lineNo + ""));
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
   * read Header from a given csvFile ( just the first line ) and return each
   * column back as List in the same order like in the file. If a column header
   * is empty it will be named as -unnamed-.
   * 
   * 
   * @param csvFile
   *          supplied csv file
   * @return List of columns
   * @throws IOException
   */
  public List<String> readHeader(final File csvFile) throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(csvFile));
    List<String> headerCol = parseHeader(br.readLine());
    br.close();

    return headerCol;
  }

  /**
   * 
   * @param headerString
   */
  private List<String> parseHeader(final String headerString)
  {

    List<String> header = new LinkedList<String>();
    for (String column : headerString.split(SEPARATOR + ""))
    {
      if (column.trim().equalsIgnoreCase(""))
      {
        header.add("unnamed");
      }
      else
      {
        header.add(column.trim());
      }
    }

    return header;
  }

  /**
   * if some entries in the list are equal, than a additional number will be
   * added like #1 and so on.
   * 
   * @param columns
   */
  public List<String> replaceDuplicates(final List<String> columns)
  {

    List<String> returnList = new LinkedList<String>(columns);

    for (int i = 0; i < returnList.size(); i++)
    {
      /* find all duplicate columns and collect the indicies .. */
      Set<Integer> equalColIndexes = new HashSet<Integer>();
      equalColIndexes.add(i);
      for (int j = i + 1; j < returnList.size(); j++)
      {
        if (returnList.get(i).equals(returnList.get(j)))
        {
          equalColIndexes.add(j);
        }
      }

      /* .. now enumerate all duplicate columns, so they are unique */
      if (equalColIndexes.size() > 1)
      {
        Iterator<Integer> iter = equalColIndexes.iterator();
        for (int addedIndex = 1; iter.hasNext(); addedIndex++)
        {
          int curColIndex = iter.next();
          String newColName = returnList.get(curColIndex) + "#" + addedIndex;

          /*
           * this part is necessary to check if newly created column name does
           * exist already if so the number will be increased until a unused
           * column name is found.
           */
          while (returnList.contains(newColName))
          {
            addedIndex++;
            newColName = returnList.get(curColIndex) + "#" + addedIndex;
          }

          returnList.set(curColIndex, newColName);
        }
      }
    }

    return returnList;
  }

  /**
   * Create a temporary input file, with a unique header line for the csv
   * importer
   * 
   * @param csvFile
   * @param newHeader
   * @throws IOException
   */
  public File createTempInputFile(final File csvFile,
      final List<String> newHeader) throws IOException
  {
    BufferedReader br = null;
    BufferedWriter bw = null;

    File tmp = new File(csvFile.toString() + "_tmp");

    try
    {
      br = new BufferedReader(new FileReader(csvFile));
      bw = new BufferedWriter(new FileWriter(tmp));

      bw.write(createNewHeader(newHeader) + "\n");
      br.readLine(); // read header and throw it away

      for (String line = br.readLine(); line != null;)
      {
        bw.write(line + "\n");
        line = br.readLine();
      }

    }
    finally
    {
      br.close();
      bw.close();
    }

    return tmp;
  }

  /**
   * based an the list a new header line will be created, just concate all
   * columns and add a delimiter
   * 
   * @param columns
   */
  private String createNewHeader(List<String> columns)
  {
    StringBuilder sb = new StringBuilder();
    for (String column : columns)
    {
      sb.append(column);
      sb.append(SEPARATOR);
    }

    return sb.substring(0, sb.length() - 1);
  }

  /**
   * check if for the given csv any columns aren't unique, if so create a new
   * file, with the same content but unique columns.
   * 
   * If return file, is the same as the supplied file, all columns were unique,
   * already.
   * 
   * @param csvFile
   */
  public File replaceDuplicateColumn(final File csvFile)
  {
    File returnFile = null;

    try
    {
      List<String> originalHeader = readHeader(csvFile);
      List<String> remDuplicateHeader = replaceDuplicates(originalHeader);

      if (originalHeader.equals(remDuplicateHeader))
      {
        returnFile = csvFile;
      }
      else
      {
        returnFile = createTempInputFile(csvFile, remDuplicateHeader);
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return returnFile;
  }
}
