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

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;

/**
 * Hilfs-Objekt
 */
public class SaldoZeile implements GenericObject
{

  private Konto konto;

  private Double anfangsbestand;

  private Double einnahmen;

  private Double ausgaben;

  private Double umbuchungen;

  private Double endbestand;

  private String bemerkung = "";

  public SaldoZeile(Konto konto, Double anfangsbestand, Double einnahmen,
      Double ausgaben, Double umbuchungen, Double endbestand)
  {
    this.konto = konto;
    this.anfangsbestand = anfangsbestand;
    this.einnahmen = einnahmen;
    this.ausgaben = ausgaben;
    this.umbuchungen = umbuchungen;
    this.endbestand = endbestand;
  }

  public SaldoZeile(Geschaeftsjahr gj, Konto konto) throws RemoteException
  {
    this.konto = konto;
    DBService service = Einstellungen.getDBService();
    DBIterator anf = service.createList(Anfangsbestand.class);
    anf.addFilter("konto = ? ", new Object[] { konto.getID()});
    anf.addFilter(
        "datum >= ? AND datum <= ?",
        new Object[] { gj.getBeginnGeschaeftsjahr(), gj.getEndeGeschaeftsjahr()});
    if (anf.hasNext())
    {
      Anfangsbestand a = (Anfangsbestand) anf.next();
      anfangsbestand = a.getBetrag();
    }
    else
    {
      anfangsbestand = 0d;
      bemerkung += "kein Anfangsbestand vorhanden  ";
    }
    String sql = "select sum(betrag) from buchung, buchungsart "
        + "where datum >= ? and datum <= ? AND konto = ? "
        + "and buchung.buchungsart = buchungsart.id " + "and buchungsart.art=?";

    ResultSetExtractor rs = new ResultSetExtractor()
    {

      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        if (!rs.next())
        {
          return new Double(0);
        }
        return new Double(rs.getDouble(1));
      }
    };
    einnahmen = (Double) service.execute(sql,
        new Object[] { gj.getBeginnGeschaeftsjahr(),
            gj.getEndeGeschaeftsjahr(), konto.getID(), 0}, rs);
    ausgaben = (Double) service.execute(sql,
        new Object[] { gj.getBeginnGeschaeftsjahr(),
            gj.getEndeGeschaeftsjahr(), konto.getID(), 1}, rs);
    umbuchungen = (Double) service.execute(sql,
        new Object[] { gj.getBeginnGeschaeftsjahr(),
            gj.getEndeGeschaeftsjahr(), konto.getID(), 2}, rs);
    endbestand = anfangsbestand + einnahmen + ausgaben + umbuchungen;
  }

  @Override
  public Object getAttribute(String arg0) throws RemoteException
  {
    if (arg0.equals("konto"))
    {
      return konto;
    }
    else if (arg0.equals("kontonummer"))
    {
      return konto.getNummer();
    }
    else if (arg0.equals("kontobezeichnung"))
    {
      return konto.getBezeichnung();
    }
    else if (arg0.equals("anfangsbestand"))
    {
      return anfangsbestand;
    }
    else if (arg0.equals("einnahmen"))
    {
      return einnahmen;
    }
    else if (arg0.equals("ausgaben"))
    {
      return ausgaben;
    }
    else if (arg0.equals("umbuchungen"))
    {
      return umbuchungen;
    }
    else if (arg0.equals("endbestand"))
    {
      return endbestand;
    }
    else if (arg0.equals("bemerkung"))
    {
      return bemerkung;
    }
    throw new RemoteException("Ungültige Spaltenbezeichung: " + arg0);
  }

  @Override
  public String[] getAttributeNames()
  {
    return new String[] { "kontonummer", "kontobezeichnung", "anfangsbestand",
        "einnahmen", "ausgaben", "umbuchungen", "endbestand", "bemerkung"};
  }

  @Override
  public String getID() throws RemoteException
  {
    return konto.getNummer();
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "kontonummer";
  }

  @Override
  public boolean equals(GenericObject arg0) throws RemoteException
  {
    if (arg0 == null || !(arg0 instanceof SaldoZeile))
    {
      return false;
    }
    return this.getID().equals(arg0.getID());
  }
}