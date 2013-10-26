package de.jost_net.JVerein.DBTools;

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
  private ArrayList<Tabelle> tabellen = new ArrayList<DBTool.Tabelle>();

  private ArrayList<Tabelle> ausgabe1 = new ArrayList<Tabelle>();

  private ArrayList<String> ausgabe2 = new ArrayList<String>();

  public DBTool()
  {
    try
    {
      Connection con = getConnection();
      DatabaseMetaData meta = con.getMetaData();
      ResultSet rs = meta.getTables(null, null, "%", new String[] { "TABLE" });
      while (rs.next())
      {
        Tabelle t = new Tabelle(rs.getString("TABLE_NAME"));
        tabellen.add(t);
      }

      for (Tabelle t : tabellen)
      {
        rs = meta.getImportedKeys(null, null, t.getName());
        while (rs.next())
        {
          t.add(rs.getString("PKTABLE_NAME"));
        }
        // rs = meta.getExportedKeys(null, null, t.getName());
        // while (rs.next())
        // {
        // t.add(rs.getString("FKTABLE_NAME"));
        // }
        System.out.println(t);
      }
      int anzahlAusgegeben = 0;
      while (anzahlAusgegeben < tabellen.size())
      {
        for (Tabelle t : tabellen)
        {
          if (t.isAusgegeben())
          {
            continue;
          }
          boolean ausgeben = true;
          for (String ref : t.getReferenzen())
          {
            if (!ausgabe2.contains(ref))
            {
              ausgeben = false;
            }
          }
          if (ausgeben)
          {
            ausgabe1.add(t);
            ausgabe2.add(t.getName());
            t.setAusgegeben(true);
            anzahlAusgegeben++;
          }
        }
      }
      System.out.println("----------------------------");
      for (Tabelle t : ausgabe1)
      {
        System.out.println(t);
      }
      System.out.println(ausgabe1.size());
      System.out.println(tabellen.size());
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
        "jdbc:h2:C:/Users/heiner/jameica.dlrg/jverein/h2db/jverein", "jverein",
        "jverein");
  }

  public static void main(String[] a)
  {
    new DBTool();
  }

  public class Tabelle
  {
    private boolean ausgegeben = false;

    private String name;

    private ArrayList<String> referenzen;

    public Tabelle(String name)
    {
      this.name = name;
      referenzen = new ArrayList<String>();
    }

    public void add(String referenz)
    {
      referenzen.add(referenz);
    }

    public String getName()
    {
      return name;
    }

    public ArrayList<String> getReferenzen()
    {
      return referenzen;
    }

    public void setAusgegeben(boolean ausgegeben)
    {
      this.ausgegeben = ausgegeben;
    }

    public boolean isAusgegeben()
    {
      return ausgegeben;
    }

    @Override
    public String toString()
    {
      String ret = name;
      for (String ref : referenzen)
      {
        ret += "->" + ref;
      }
      return ret;
    }
  }
}
