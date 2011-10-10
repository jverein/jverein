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

import java.io.IOException;
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
   * 
   * @param monitor
   */
  public AssignedColumnsIO()
  {

    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  /**
   * Read column assignments from file
   * 
   * @param file
   *          where the assignments are saved
   * @param availableColumns
   *          columns from the import file
   * @param tcr
   *          current assignments
   * @return if an error occurred the same tcr will be returned than provided,
   *         else a new one with all assignments saved
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
   * 
   * @param columns
   * @param bos
   * @param cNec
   * @throws IOException
   */
  private List<String> writeSpecifiedMap(final Map<String, String> columns,
      final boolean nNec)
  {

    List<String> conColAssign = new ArrayList<String>();
    Boolean n = new Boolean(nNec);

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
