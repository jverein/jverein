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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.GenericObjectNode;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.ResultSetExtractor;

public class SpendenbescheinigungNode implements GenericObjectNode
{
  private SpendenbescheinigungNode parent = null;

  private Mitglied mitglied = null;

  private Buchung buchung = null;

  private ArrayList<GenericObjectNode> childrens;

  private boolean checked;

  public static final int NONE = 0;

  public static final int ROOT = 1;

  public static final int MITGLIED = 2;

  public static final int BUCHUNG = 3;

  private int nodetype = NONE;

  public SpendenbescheinigungNode() throws RemoteException
  {
    childrens = new ArrayList<GenericObjectNode>();
    nodetype = ROOT;

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      public Object extract(ResultSet rs) throws SQLException
      {
        List<String> ids = new ArrayList<String>();
        while (rs.next())
        {
          ids.add(rs.getString(1));
        }
        return ids;
      }
    };
    String sql = "select mitglied.id from buchung "
        + "    JOIN buchungsart on buchung.buchungsart = buchungsart.id "
        + "    join mitgliedskonto on buchung.mitgliedskonto = mitgliedskonto.id "
        + "    join mitglied on mitgliedskonto.mitglied = mitglied.id "
        + "  where buchungsart.spende = 'TRUE' and buchung.spendenbescheinigung is null and buchung.mitgliedskonto is not null "
        + "  group by mitglied.name, mitglied.vorname, mitglied.id "
        + "  order by mitglied.name, mitglied.vorname, mitglied.id ";
    ArrayList<String> idliste = (ArrayList<String>) Einstellungen
        .getDBService().execute(sql, new Object[] {}, rs);

    for (String id : idliste)
    {
      Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
          Mitglied.class, id);
      childrens.add(new SpendenbescheinigungNode(m));
    }
  }

  private SpendenbescheinigungNode(Mitglied mitglied) throws RemoteException
  {
    this.mitglied = mitglied;

    childrens = new ArrayList<GenericObjectNode>();
    nodetype = MITGLIED;

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      public Object extract(ResultSet rs) throws SQLException
      {
        List<String> ids = new ArrayList<String>();
        while (rs.next())
        {
          ids.add(rs.getString(1));
        }
        return ids;
      }
    };
    String sql = "select buchung.id, buchung.datum from buchung "
        + "    JOIN buchungsart on buchung.buchungsart = buchungsart.id "
        + "    join mitgliedskonto on buchung.mitgliedskonto = mitgliedskonto.id "
        + "  where buchungsart.spende = 'TRUE' and mitgliedskonto.mitglied = ? and buchung.spendenbescheinigung is null and buchung.mitgliedskonto is not null "
        + "  order by buchung.datum";
    ArrayList<String> idliste = (ArrayList<String>) Einstellungen
        .getDBService().execute(sql, new Object[] { mitglied.getID() }, rs);

    for (String id : idliste)
    {
      Buchung buchung = (Buchung) Einstellungen.getDBService().createObject(
          Buchung.class, id);
      childrens.add(new SpendenbescheinigungNode(mitglied, buchung));
    }
  }

  private SpendenbescheinigungNode(Mitglied mitglied, Buchung buchung)
      throws RemoteException
  {
    this.mitglied = mitglied;
    this.buchung = buchung;

    childrens = new ArrayList<GenericObjectNode>();
    nodetype = BUCHUNG;
  }

  public GenericIterator getChildren() throws RemoteException
  {
    if (childrens == null)
    {
      return null;
    }
    return PseudoIterator.fromArray(childrens
        .toArray(new GenericObject[childrens.size()]));
  }

  public boolean removeChild(GenericObjectNode child)
  {
    return childrens.remove(child);
  }

  public SpendenbescheinigungNode getParent()
  {
    return parent;
  }

  public GenericIterator getPath()
  {
    return null;
  }

  public GenericIterator getPossibleParents()
  {
    return null;
  }

  public boolean hasChild(GenericObjectNode object)
  {
    return childrens.size() > 0;
  }

  public boolean equals(GenericObject other)
  {
    return false;
  }

  public Object getAttribute(String name) throws RemoteException
  {
    switch (nodetype)
    {
      case ROOT:
      {
        return "Spendenbescheinigungen";
      }
      case MITGLIED:
      {
        return mitglied.getNameVorname();
      }
      case BUCHUNG:
      {
        return new JVDateFormatTTMMJJJJ().format(buchung.getDatum())
            + ", "
            + buchung.getZweck()
            + ", "
            + (buchung.getZweck2().length() > 0 ? buchung.getZweck2() + ", "
                : "") + Einstellungen.DECIMALFORMAT.format(buchung.getBetrag());
      }
    }
    return "bla";
  }

  public String[] getAttributeNames()
  {
    return null;
  }

  public String getID()
  {
    return null;
  }

  public String getPrimaryAttribute()
  {
    return null;
  }

  public Object getObject()
  {
    switch (nodetype)
    {
      case MITGLIED:
      {
        return mitglied;
      }
      case BUCHUNG:
      {
        return buchung;
      }
    }
    return null;
  }

  public int getNodeType()
  {
    return nodetype;
  }

  public Mitglied getMitglied()
  {
    return this.mitglied;
  }

  public Buchung getBuchung()
  {
    return this.buchung;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

  public boolean isChecked()
  {
    return checked;
  }

  public String toString()
  {
    String ret = "";
    try
    {
      if (this.nodetype == ROOT)
      {
        return "--> ROOT";
      }
      if (this.nodetype == MITGLIED)
      {
        return "---> MITGLIED: " + mitglied.getNameVorname();
      }
      if (this.nodetype == BUCHUNG)
      {
        return "----> BUCHUNG" + buchung.getDatum() + ";" + buchung.getZweck()
            + ";" + buchung.getBetrag();
      }
    }
    catch (RemoteException e)
    {
      ret = e.getMessage();
    }
    return ret;
  }
}
