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
package de.jost_net.JVerein.DBTools;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.willuhn.logging.Logger;

/**
 * Tools, mit dessen Hilfe die Reihenfolge der Tabellen für das Diagnosebackup
 * ermittelt wird. Ansonsten gibt es Probleme mit der referenziellen Integrität.
 * 
 */
public class DBTool2
{
  public DBTool2(String url, String file)
  {
    try
    {
      PrintWriter pw = new PrintWriter(file);
      Connection con = getConnection(url);
      DatabaseMetaData meta = con.getMetaData();
      ResultSet rsT = meta.getTables("%", "%", "%", new String[] { "TABLE" });
      while (rsT.next())
      {
        pw.format("Tabelle %s\n", rsT.getString("TABLE_NAME"));
        ResultSet rsC = meta.getColumns("%", "%", rsT.getString("TABLE_NAME"),
            "%");
        while (rsC.next())
        {
          pw.format("C %s, %s, %s, %s, %s\n", rsC.getString("COLUMN_NAME"),
              rsC.getString("TYPE_NAME"), rsC.getString("COLUMN_SIZE"),
              rsC.getString("IS_NULLABLE"), rsC.getString("IS_AUTOINCREMENT"));
        }
        ResultSet rsI = meta.getIndexInfo("%", "%", rsT.getString("TABLE_NAME"),
            false, true);
        while (rsI.next())
        {
          pw.format("I %s\n", rsI.getString("COLUMN_NAME"));
        }
        ResultSet rsIK = meta.getImportedKeys("%", "%",
            rsT.getString("TABLE_NAME"));
        while (rsIK.next())
        {
          pw.format("IK %s, %s, %s, %s, %s, %s\n",
              rsIK.getString("PKTABLE_NAME"), rsIK.getString("PKCOLUMN_NAME"),
              rsIK.getString("FKTABLE_NAME"), rsIK.getString("FKCOLUMN_NAME"),
              rsIK.getString("UPDATE_RULE"), rsIK.getString("DELETE_RULE"));
        }
        ResultSet rsEK = meta.getExportedKeys("%", "%",
            rsT.getString("TABLE_NAME"));
        while (rsEK.next())
        {
          pw.format("EK %s, %s, %s, %s, %s, %s\n",
              rsEK.getString("PKTABLE_NAME"), rsEK.getString("PKCOLUMN_NAME"),
              rsEK.getString("FKTABLE_NAME"), rsEK.getString("FKCOLUMN_NAME"),
              rsEK.getString("UPDATE_RULE"), rsEK.getString("DELETE_RULE"));
        }
        pw.println("----");
      }
      pw.close();
    }
    catch (ClassNotFoundException e)
    {
      Logger.error("Fehler", e);
    }
    catch (SQLException e)
    {
      Logger.error("Fehler", e);
    }
    catch (FileNotFoundException e)
    {
      Logger.error("Fehler", e);
    }
  }

  public Connection getConnection(String url)
      throws ClassNotFoundException, SQLException
  {
    Class.forName("org.h2.Driver");
    return DriverManager.getConnection(url, "jverein", "jverein");
  }

  @SuppressWarnings("unused")
  public static void main(String[] a)
  {
    new DBTool2(
        "jdbc:h2:/Users/heiner/jameica.buch/jverein.20141012/h2db/jverein",
        "db.txt");
    new DBTool2("jdbc:h2:/Users/heiner/jameica.buch/jverein/h2db/jverein",
        "db2.txt");
  }

}
