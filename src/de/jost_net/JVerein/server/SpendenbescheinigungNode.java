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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
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

  /**
   * Selektiert über die Buchungen mit einer Buchungsart, die als Spende
   * markiert ist, alle Mitglieder, die eine Buchung im Mitgliedskonto
   * eingetragen haben. Die Buchungen dürfen noch nicht auf einer
   * Spendenbescheinigung eingetragen sein. Es werden nur die Mitglieder
   * selektiert, bei denen auch eine Adresse (Straße, PLZ, Ort) eingetragen ist.
   * Zusätzlich muss die Summe der Buchungen größer gleich dem in den
   * Einstellungen hinterlegten Mindestbetrag für Spendenbescheinigungen sein.
   * 
   * @param jahr
   *          Das Jahr der Buchung
   * @throws RemoteException
   */
  public SpendenbescheinigungNode(final int jahr) throws RemoteException
  {
    childrens = new ArrayList<>();
    nodetype = ROOT;
    double minBetrag = Einstellungen.getEinstellung()
        .getSpendenbescheinigungminbetrag();

    ResultSetExtractor rse = new ResultSetExtractor()
    {

      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        List<String> ids = new ArrayList<>();
        while (rs.next())
        {
          ids.add(rs.getString(1));
        }
        return ids;
      }
    };
    String sql = "SELECT mitglied.id, sum(buchung.betrag) " + "FROM buchung "
        + "  JOIN buchungsart ON buchung.buchungsart = buchungsart.id "
        + "  JOIN mitgliedskonto ON buchung.mitgliedskonto = mitgliedskonto.id "
        + "  JOIN mitglied ON mitgliedskonto.mitglied = mitglied.id "
        + "WHERE year(buchung.datum) = ? " + "  AND buchungsart.spende = true "
        + "  AND buchung.spendenbescheinigung IS NULL "
        + "  AND buchung.mitgliedskonto IS NOT NULL "
        // rdc: Nur Mitglieder mit bekannter Adresse
        + "  AND (mitglied.strasse IS NOT NULL OR LENGTH(mitglied.strasse) > 0) "
        + "  AND (mitglied.plz IS NOT NULL OR LENGTH(mitglied.plz) > 0) "
        + "  AND (mitglied.ort IS NOT NULL OR LENGTH(mitglied.ort) > 0) "
        + "GROUP BY mitglied.name, mitglied.vorname, mitglied.id "
        // rdc: Nur Spendenbescheinigungen, deren Betrag >= Mindestbetrag
        + "HAVING sum(buchung.betrag) >= ? "
        + "ORDER BY mitglied.name, mitglied.vorname, mitglied.id";
    @SuppressWarnings("unchecked")
    ArrayList<String> idliste = (ArrayList<String>) Einstellungen.getDBService()
        .execute(sql, new Object[] { jahr, minBetrag }, rse);

    for (String id : idliste)
    {
      Mitglied m = (Mitglied) Einstellungen.getDBService()
          .createObject(Mitglied.class, id);
      // rdc: hier nochmal prüfen, ob auch wirklich eine gültige Adresse
      // vorliegt.
      // rdc: Es kommen manchmal Datensätze ohne Straße/Ort ...
      if ((m.getStrasse() == null)
          || (m.getStrasse() != null && m.getStrasse().length() == 0))
        continue;
      if ((m.getPlz() == null)
          || (m.getPlz() != null && m.getPlz().length() == 0))
        continue;
      if ((m.getOrt() == null)
          || (m.getOrt() != null && m.getOrt().length() == 0))
        continue;
      childrens.add(new SpendenbescheinigungNode(m, jahr));
    }
  }

  /**
   * Selektiert zu einem Mitglied die Buchungen mit einer Buchungsart, die als
   * Spende markiert sind. Die Buchungen dürfen noch nicht auf einer
   * Spendenbescheinigung eingetragen sein.
   * 
   * @param mitglied
   *          Das Mitglied des Kontos, zu dem die Buchungen selektiert werden
   * @param jahr
   *          Das Jahr der Buchung
   * @throws RemoteException
   */
  private SpendenbescheinigungNode(Mitglied mitglied, final int jahr)
      throws RemoteException
  {
    this.mitglied = mitglied;

    childrens = new ArrayList<>();
    nodetype = MITGLIED;

    ResultSetExtractor rs = new ResultSetExtractor()
    {

      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        List<String> ids = new ArrayList<>();
        while (rs.next())
        {
          ids.add(rs.getString(1));
        }
        return ids;
      }
    };
    String sql = "SELECT buchung.id, buchung.datum FROM buchung "
        + "  JOIN buchungsart ON buchung.buchungsart = buchungsart.id "
        + "  JOIN mitgliedskonto ON buchung.mitgliedskonto = mitgliedskonto.id "
        + "WHERE year(buchung.datum) = ? " + "  AND buchungsart.spende = true "
        + "  AND mitgliedskonto.mitglied = ? "
        + "  AND buchung.spendenbescheinigung IS NULL "
        + "  AND buchung.mitgliedskonto IS NOT NULL "
        + "ORDER BY buchung.datum";
    @SuppressWarnings("unchecked")
    ArrayList<String> idliste = (ArrayList<String>) Einstellungen.getDBService()
        .execute(sql, new Object[] { jahr, mitglied.getID() }, rs);

    for (String id : idliste)
    {
      Buchung buchung = (Buchung) Einstellungen.getDBService()
          .createObject(Buchung.class, id);
      childrens.add(new SpendenbescheinigungNode(mitglied, buchung));
    }
  }

  private SpendenbescheinigungNode(Mitglied mitglied, Buchung buchung)
  {
    this.mitglied = mitglied;
    this.buchung = buchung;

    childrens = new ArrayList<>();
    nodetype = BUCHUNG;
  }

  @Override
  public GenericIterator<?> getChildren() throws RemoteException
  {
    if (childrens == null)
    {
      return null;
    }
    return PseudoIterator
        .fromArray(childrens.toArray(new GenericObject[childrens.size()]));
  }

  public boolean removeChild(GenericObjectNode child)
  {
    return childrens.remove(child);
  }

  @Override
  public SpendenbescheinigungNode getParent()
  {
    return parent;
  }

  @Override
  public GenericIterator<?> getPath()
  {
    return null;
  }

  @Override
  public GenericIterator<?> getPossibleParents()
  {
    return null;
  }

  @Override
  public boolean hasChild(GenericObjectNode object)
  {
    return childrens.size() > 0;
  }

  @Override
  public boolean equals(GenericObject other)
  {
    return false;
  }

  @Override
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
        GenericIterator it1 = getChildren();
        double betrag = 0.0;
        while (it1.hasNext())
        {
          SpendenbescheinigungNode sp1 = (SpendenbescheinigungNode) it1.next();
          if (sp1.getNodeType() == BUCHUNG)
          {
            betrag += sp1.getBuchung().getBetrag();
          }
        }
        return Adressaufbereitung.getNameVorname(mitglied) + " ("
            + Einstellungen.DECIMALFORMAT.format(betrag) + ")";
      }
      case BUCHUNG:
      {
        return new JVDateFormatTTMMJJJJ().format(buchung.getDatum()) + ", "
            + (buchung.getZweck() != null && buchung.getZweck().length() > 0
                ? buchung.getZweck()
                : "")
            + ", " + Einstellungen.DECIMALFORMAT.format(buchung.getBetrag());
      }
    }
    return "bla";
  }

  @Override
  public String[] getAttributeNames()
  {
    return null;
  }

  @Override
  public String getID()
  {
    return null;
  }

  @Override
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

  @Override
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
        return "---> MITGLIED: " + Adressaufbereitung.getNameVorname(mitglied);
      }
      if (this.nodetype == BUCHUNG)
      {
        return "----> BUCHUNG: " + buchung.getDatum() + ";" + buchung.getZweck()
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
