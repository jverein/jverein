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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.jost_net.JVerein.util.TableColumnReplacer;
import de.willuhn.jameica.system.Settings;

/**
 * This class reads and writes the assignments made by the import
 * 
 * @author Christian Lutz
 * 
 */
public class AssignedColumnsIO
{

  private final String ATTASSIGNMENTKEY = "colAssignments";

  private final Settings settings;

  /**
   * Constructor
   */
  public AssignedColumnsIO()
  {

    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  /**
   * Read column assignments from file
   * 
   */
  public TableColumnReplacer readAssignments(
      final List<String> availableColumns, final TableColumnReplacer tcr)
  {

    TableColumnReplacer columns = new TableColumnReplacer(tcr);

    String[] colAssignments = settings.getList(this.ATTASSIGNMENTKEY, null);

    if (colAssignments == null)
      return tcr;

    for (String sb : colAssignments)
    {

      /*
       * first column is column name in the program second column is the column
       * name from the import file third is just a reminder for necessary or
       * not, but this is currently not used.
       */
      String[] colAssign = sb.toString().trim().split(";");

      if (availableColumns.contains(colAssign[1])
          && columns.isColumnDefined(colAssign[0]))
      {

        columns.setColumnReplacement(colAssign[0], colAssign[1]);
      }

    }

    return columns;
  }

  /**
   * write assignments from the TableColumnReplacer to Settings
   * 
   * @param columns
   */
  public void writeAssignments(final TableColumnReplacer columns)
  {

    List<String> conColumns = writeSpecifiedMap(columns.getNecessaryColumns(),
        true);
    conColumns.addAll(writeSpecifiedMap(columns.getOptionalColumns(), false));

    String[] saveCol = new String[conColumns.size()];
    for (int i = 0; i < conColumns.size(); i++)
    {
      saveCol[i] = conColumns.get(i);
    }

    settings.setAttribute(this.ATTASSIGNMENTKEY, saveCol);

  }

  /**
   * create a list with all columns assigned in one line.
   */
  private List<String> writeSpecifiedMap(final Map<String, String> columns,
      final boolean nNec)
  {

    List<String> conColAssign = new ArrayList<>();
    Boolean n =  Boolean.valueOf(nNec);

    for (String key : columns.keySet())
    {
      StringBuilder sb = new StringBuilder();

      sb.append(key);
      sb.append(";");
      sb.append(columns.get(key));
      sb.append(";");
      sb.append(n.toString());

      conColAssign.add(sb.toString());
    }

    return conColAssign;
  }

}
