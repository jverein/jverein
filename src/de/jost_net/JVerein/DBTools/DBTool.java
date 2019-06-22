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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.willuhn.logging.Logger;

/**
 * Tools, mit dessen Hilfe die Reihenfolge der Tabellen für das Diagnosebackup
 * ermittelt wird. Ansonsten gibt es Probleme mit der referenziellen Integrität.
 * 
 */
public class DBTool
{
  /**
   * Liste aller Tabellen
   */
  private ArrayList<String> tabellen = new ArrayList<>();

  private ArrayList<String> ausgabe = new ArrayList<>();

  public DBTool()
  {
    try
    {
      // Liste aller Tabellen aufbauen
      Connection con = getConnection();
      DatabaseMetaData meta = con.getMetaData();
      ResultSet rs = meta.getTables("%", "%", "%", new String[] { "TABLE" });
      while (rs.next())
      {
        tabellen.add(rs.getString("TABLE_NAME"));
      }
      System.out.println("Tabellen insgesamt:" + tabellen.size());

      // Alle Tabellen auf importierte Keys überprüfen
      while (ausgabe.size() < tabellen.size())
      {
        for (String t : tabellen)
        {
          if (ausgabe.contains(t))
          {
            continue;
          }
          rs = meta.getImportedKeys(null, null, t);

          boolean allexported = true;
          while (rs.next())
          {
            String imptab = rs.getString("PKTABLE_NAME");
            if (!ausgabe.contains(imptab))
            {
              allexported = false;
            }
          }
          if (allexported)
          {
            ausgabe.add(t);
          }
        }
      }
      for (String t : ausgabe)
      {
        System.out.println(t);
      }
      System.out.println("Ausgabetabellen: " + ausgabe.size());
      System.out.println("----------------------------");
      System.out.println("Fertig");
    }
    catch (ClassNotFoundException e)
    {
      Logger.error("Fehler", e);
    }
    catch (SQLException e)
    {
      Logger.error("Fehler", e);
    }
  }

  public Connection getConnection() throws ClassNotFoundException, SQLException
  {
    Class.forName("org.h2.Driver");
    return DriverManager.getConnection(
        "jdbc:h2:/Users/heiner/jameica.buch/jverein/h2db/jverein", "jverein",
        "jverein");
  }

  @SuppressWarnings("unused")
  public static void main(String[] a)
  {
    new DBTool();
  }

}
