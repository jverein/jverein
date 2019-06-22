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
package de.jost_net.JVerein.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Christian Lutz
 * 
 * @version 1.0 - 06.09.2011
 * 
 *          This class is a container for columns and their corresponding
 *          replacement. It is possible to define necessary columns and optional
 *          columns. Further more it is possible to move columns from one to the
 *          other etc.
 * 
 */
public class TableColumnReplacer
{

  private Map<String, String> necColumns;

  private Map<String, String> optColumns;

  public TableColumnReplacer()
  {
    this(new LinkedList<String>());
  }

  public TableColumnReplacer(final List<String> necessaryColumns)
  {
    this(necessaryColumns, new LinkedList<String>());
  }

  /**
   * Constructor
   * 
   * @param necessaryColumns
   * @param optionalColumns
   */
  public TableColumnReplacer(final List<String> necessaryColumns,
      final List<String> optionalColumns)
  {

    if (necessaryColumns == null || optionalColumns == null)
      throw new NullPointerException("Columns may not be null");

    /*
     * currently it could be possible to have the same column name in both
     * lists. This knowledge helps you to understand some code later.
     */

    necColumns = new HashMap<>();
    for (String key : necessaryColumns)
    {
      necColumns.put(key, null);
    }

    optColumns = new HashMap<>();
    for (String key : optionalColumns)
    {
      optColumns.put(key, null);
    }
  }

  /**
   * Copy Constructor
   * 
   * @param tcr
   */
  public TableColumnReplacer(final TableColumnReplacer tcr)
  {

    if (tcr == null)
      throw new NullPointerException("tcr may not be null");

    necColumns = new HashMap<>(tcr.necColumns);
    optColumns = new HashMap<>(tcr.optColumns);

  }

  /**
   * This method will create a new column in the specified area. But if it
   * already exist in the other area it just will be moved.
   * 
   * @param column
   *          to create or to move in the defined way
   * @param necessary
   *          <code>true</code>if you want it as necessary, <code>false</code>
   *          as optional
   */
  public void addColumn(final String column, final boolean necessary)
  {

    if (column == null)
      throw new NullPointerException("Column may not be null");
    if (column.trim().equals(""))
      throw new IllegalArgumentException("Column may not be empty");

    /* if the assignment is already correct just return */
    if ((necessary && necColumns.containsKey(column))
        || (!necessary && optColumns.containsKey(column)))
      return;

    /*
     * move it from one map to the other or create a new one if it didn't exist
     */
    if (necessary)
    {
      if (optColumns.containsKey(column))
      {
        necColumns.put(column, optColumns.get(column));
        optColumns.remove(column);
      }
      else
      {
        necColumns.put(column, null);
      }
    }
    else
    {
      if (necColumns.containsKey(column))
      {
        optColumns.put(column, necColumns.get(column));
        necColumns.remove(column);
      }
      else
      {
        optColumns.put(column, null);
      }
    }
  }

  /**
   * This method only checks if for every necessary column a replacement column
   * is defined.
   * 
   * @return <code>true</code> if for all necessary column a replacement is
   *         defined, otherwise <code>false</code>
   */
  public boolean allNecessaryColumnsAvailable()
  {

    for (String key : necColumns.keySet())
    {
      if (necColumns.get(key) == null)
        return false;
    }

    return true;
  }

  /**
   * 
   * 
   * @return a list with all column that are not assigned, yet, but are
   *         necessary
   */
  public List<String> getMissingNecessaryColumns()
  {

    LinkedList<String> tmp = new LinkedList<>();

    for (String key : necColumns.keySet())
    {
      if (necColumns.get(key) == null)
        tmp.add(key);
    }

    return tmp;
  }

  /**
   * 
   * @return all columns no matter if neccesary or optional if the have an
   *         replacement or not.
   */
  public List<String> getAllColumns()
  {

    LinkedList<String> tmp = new LinkedList<>();

    tmp.addAll(necColumns.keySet());
    tmp.addAll(optColumns.keySet());

    return tmp;
  }

  /**
   * This method returns map with all columns and the corresponding replacement.
   * If a for a column a replacement isn't defined it won't show up in the map.
   * To be sure all of as necessary defined columns have a new assigned name,
   * please use the method allNecessaryColumnsAvailable()
   * 
   * @return map of old and new column pairs
   */
  public Map<String, String> getNecessaryColumns()
  {

    Map<String, String> tmp = new HashMap<>();
    for (String key : necColumns.keySet())
    {
      if (necColumns.get(key) != null)
        tmp.put(key, necColumns.get(key));
    }

    return tmp;
  }

  /**
   * This method will return all Columns, which have a new assigned column name
   * and are as optional defined.
   * 
   * @return map with only optinal columns an their corresponding replacement.
   */
  public Map<String, String> getOptionalColumns()
  {

    HashMap<String, String> tmp = new HashMap<>();
    for (String key : optColumns.keySet())
    {
      if (optColumns.get(key) != null)
        tmp.put(key, optColumns.get(key));
    }

    return tmp;
  }

  /**
   * Return the new column name for a specified column.
   * 
   * @param column
   *          the name will be replaced with the returned one
   * @return new column name or <code>null</code> if either the column isn't
   *         specified or has no replacement name
   */
  public String getReplacementForColumn(final String column)
  {

    if (column == null)
      throw new NullPointerException("Column may not be null");

    if (necColumns.containsKey(column))
      return necColumns.get(column);
    if (optColumns.containsKey(column))
      return optColumns.get(column);

    return null;
  }

  /**
   * Checks if specified column is already defined
   * 
   * @param column
   */
  public boolean isColumnDefined(final String column)
  {

    if (column == null)
      throw new NullPointerException("Column may not be null");

    if (necColumns.containsKey(column))
      return true;
    if (optColumns.containsKey(column))
      return true;

    return false;
  }

  /**
   * 
   * 
   * @param column
   * @param necessary
   * @return <code>true</code> if column is necessary otherwise
   *         <code>false</code>. Or it will return <code>false</code> due to the
   *         column isn't defined
   */
  public boolean isColumn(final String column, final boolean necessary)
  {

    if (column == null)
      throw new NullPointerException("Column may not be null");

    if (necessary)
    {
      return necColumns.containsKey(column);
    }
    else
    {
      return optColumns.containsKey(column);
    }

  }

  /**
   * Remove a specified column from the list, doesn't matter if necessary or
   * optional.
   * 
   * @param column
   *          to be removed
   */
  public void removeColumn(final String column)
  {

    if (column == null)
      throw new NullPointerException("Column may not be null");

    necColumns.remove(column);
    optColumns.remove(column);
  }

  /**
   * This method sets the corresponding replacement for a defined column
   * 
   * @param column
   * @param replacement
   */
  public boolean setColumnReplacement(final String column,
      final String replacement)
  {

    boolean valueSet = false;

    if (necColumns.containsKey(column))
    {
      necColumns.put(column, replacement);
      valueSet = true;
    }
    if (optColumns.containsKey(column))
    {
      optColumns.put(column, replacement);
      valueSet = true;
    }

    return valueSet;
  }

}
