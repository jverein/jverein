package de.jost_net.JVerein.DBTools;

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
 * $Log$
 **********************************************************************/

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
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
