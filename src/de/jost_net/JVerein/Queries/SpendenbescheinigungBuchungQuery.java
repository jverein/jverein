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
package de.jost_net.JVerein.Queries;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;

public class SpendenbescheinigungBuchungQuery
{
  private boolean and = false;

  private Spendenbescheinigung spb = null;

  private String sql = "";

  private ArrayList<Object> bedingungen = new ArrayList<Object>();

  private ArrayList<Buchung> ergebnis;

  private static final int ORDER_UMSATZID = 0;

  private static final int ORDER_DATUM = 1;

  private static final int ORDER_DATUM_AUSZUGSNUMMER_BLATTNUMMER = 2;

  private static final int ORDER_DATUM_NAME = 3;

  private static final int ORDER_ID = 4;

  private int order = ORDER_UMSATZID;

  public SpendenbescheinigungBuchungQuery(Spendenbescheinigung spb)
  {
    this.spb = spb;
  }

  public void setOrderID()
  {
    order = ORDER_ID;
  }

  public void setOrderDatum()
  {
    order = ORDER_DATUM;
  }

  public void setOrderDatumAuszugsnummerBlattnummer()
  {
    order = ORDER_DATUM_AUSZUGSNUMMER_BLATTNUMMER;
  }

  public void setOrderDatumName()
  {
    order = ORDER_DATUM_NAME;
  }

  public ArrayList<Buchung> get() throws RemoteException
  {
    and = false;
    bedingungen = new ArrayList<Object>();

    final DBService service = Einstellungen.getDBService();
    ergebnis = new ArrayList<Buchung>();
    sql = "select buchung.* ";
    sql += "from buchung ";

    if (spb != null)
    {
      addCondition("spendenbescheinigung = ? ", spb.getID());
    }
    switch (order)
    {
      case ORDER_UMSATZID:
      {
        sql += "ORDER BY umsatzid DESC";
        break;
      }
      case ORDER_DATUM:
      {
        sql += "ORDER BY datum";
        break;
      }
      case ORDER_DATUM_AUSZUGSNUMMER_BLATTNUMMER:
      {
        sql += "ORDER BY datum, auszugsnummer, blattnummer, id";
        break;
      }
      case ORDER_DATUM_NAME:
      {
        sql += "ORDER BY datum, name, id";
        break;
      }
      case ORDER_ID:
      {
        sql += "ORDER BY id";
        break;
      }

    }
    Logger.debug(sql);

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        ArrayList<Buchung> list = new ArrayList<Buchung>();
        while (rs.next())
        {
          list.add((Buchung) service.createObject(Buchung.class,
              rs.getString(1)));
        }
        return list;
      }
    };
    ergebnis = (ArrayList<Buchung>) service.execute(sql, bedingungen.toArray(),
        rs);
    return ergebnis;
  }

  public int getSize()
  {
    return ergebnis.size();
  }

  private void addCondition(String condition)
  {
    if (and)
    {
      sql += " AND ";
    }
    else
    {
      sql += "where ";
    }
    and = true;
    sql += condition;
  }

  private void addCondition(String condition, Object obj)
  {
    addCondition(condition);
    bedingungen.add(obj);
  }
}
