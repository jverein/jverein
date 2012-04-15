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
package de.jost_net.JVerein.Queries;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Projekt;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;

public class BuchungQuery
{
  private Date datumvon;

  private Date datumbis;

  private Konto konto;

  public Buchungsart buchungart;

  private Projekt projekt;

  public String text;

  private boolean and = false;

  private String sql = "";

  private ArrayList<Object> bedingungen = new ArrayList<Object>();

  private ArrayList<Buchung> ergebnis;

  private static final int ORDER_UMSATZID = 0;

  private static final int ORDER_DATUM = 1;

  private static final int ORDER_DATUM_AUSZUGSNUMMER_BLATTNUMMER = 2;

  private static final int ORDER_DATUM_NAME = 3;

  private static final int ORDER_ID = 4;

  private int order = ORDER_UMSATZID;

  public BuchungQuery(Date datumvon, Date datumbis, Konto konto,
      Buchungsart buchungsart, Projekt projekt, String text)
  {
    this.datumvon = datumvon;
    this.datumbis = datumbis;
    this.konto = konto;
    this.buchungart = buchungsart;
    this.projekt = projekt;
    this.text = text;
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

  public Date getDatumvon()
  {
    return datumvon;
  }

  public Date getDatumbis()
  {
    return datumbis;
  }

  public Konto getKonto()
  {
    return konto;
  }

  public Buchungsart getBuchungsart()
  {
    return buchungart;
  }

  public Projekt getProjekt()
  {
    return projekt;
  }

  public String getText()
  {
    return text;
  }

  public ArrayList<Buchung> get() throws RemoteException
  {
    and = false;
    bedingungen = new ArrayList<Object>();

    final DBService service = Einstellungen.getDBService();
    ergebnis = new ArrayList<Buchung>();
    sql = "select buchung.* ";
    sql += "from buchung ";

    addCondition("datum >= ? ", datumvon);
    addCondition("datum <= ? ", datumbis);
    if (konto != null)
    {
      addCondition("konto = ? ", konto.getID());
    }
    if (buchungart != null)
    {
      if (buchungart.getNummer() == -1)
      {
        addCondition("buchungsart is null ");
      }
      else if (buchungart.getNummer() >= 0)
      {
        addCondition("buchungsart = ? ", buchungart.getID());
      }
    }

    if (projekt != null)
    {
      addCondition("projekt = ?", projekt.getID());
    }

    if (text.length() > 0)
    {
      String ttext = text.toUpperCase();
      ttext = "%" + ttext + "%";
      addCondition(
          "(upper(name) like ? or upper(zweck) like ? or upper(zweck2) like ? or upper(kommentar) like ?) ",
          new Object[] { ttext, ttext, ttext, ttext });
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

    }
    Logger.debug(sql);

    ResultSetExtractor rs = new ResultSetExtractor()
    {

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

  public String getSubtitle() throws RemoteException
  {
    String subtitle = "vom " + new JVDateFormatTTMMJJJJ().format(getDatumvon())
        + " bis " + new JVDateFormatTTMMJJJJ().format(getDatumbis());
    if (getKonto() != null)
    {
      subtitle += " für Konto " + getKonto().getNummer() + " - "
          + getKonto().getBezeichnung();
    }
    if (getProjekt() != null)
    {
      subtitle += ", Projekt " + getProjekt().getBezeichnung();
    }
    if (getText() != null && getText().length() > 0)
    {
      subtitle += ", Text=" + getText();
    }

    return subtitle;
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

  private void addCondition(String condition, Object[] obj)
  {
    addCondition(condition);
    for (Object o : obj)
    {
      bedingungen.add(o);
    }
  }
}
