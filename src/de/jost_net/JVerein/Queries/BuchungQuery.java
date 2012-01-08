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
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

public class BuchungQuery
{

  private BuchungsControl control;

  private boolean and = false;

  private String sql = "";

  private ArrayList<Object> bedingungen = new ArrayList<Object>();

  private ArrayList<Buchung> ergebnis;

  public BuchungQuery(BuchungsControl control)
  {
    this.control = control;
  }

  public ArrayList<Buchung> get() throws RemoteException
  {
    final DBService service = Einstellungen.getDBService();
    ergebnis = new ArrayList<Buchung>();
    sql = "select buchung.* ";
    sql += "from buchung ";
    Settings settings = control.getSettings();

    java.sql.Date vd = new java.sql.Date(getDatumvon().getTime());
    settings.setAttribute("vondatum",
        new JVDateFormatTTMMJJJJ().format(getDatumvon().getTime()));
    java.sql.Date bd = new java.sql.Date(getDatumbis().getTime());
    settings.setAttribute("bisdatum",
        new JVDateFormatTTMMJJJJ().format(getDatumbis().getTime()));

    Konto k = null;
    if (control.getSuchKonto().getValue() != null)
    {
      k = (Konto) control.getSuchKonto().getValue();
      settings.setAttribute("suchkontoid", k.getID());
    }
    else
    {
      settings.setAttribute("suchkontoid", "");
    }

    Buchungsart b = (Buchungsart) control.getSuchBuchungsart().getValue();
    if (b != null && b.getNummer() >= 0)
    {
      b = (Buchungsart) control.getSuchBuchungsart().getValue();
      settings.setAttribute(BuchungsControl.BUCHUNGSART, b.getNummer());
    }
    else
    {
      settings.setAttribute(BuchungsControl.BUCHUNGSART, -2);
    }

    addCondition("datum >= ? ", vd);
    addCondition("datum <= ? ", bd);
    if (k != null)
    {
      addCondition("konto = ? ", k.getID());
    }
    if (b != null)
    {
      if (b.getNummer() == -1)
      {
        addCondition("buchungsart is null ");
      }
      else if (b.getNummer() >= 0)
      {
        addCondition("buchungsart = ? ", b.getID());
      }
    }

    String sute = (String) control.getSuchtext().getValue();
    if (sute.length() > 0)
    {
      settings.setAttribute("suchtext", sute);
      sute = sute.toUpperCase();
      sute = "%" + sute + "%";
      addCondition(
          "(upper(name) like ? or upper(zweck) like ? or upper(zweck2) like ? or upper(kommentar) like ?) ",
          new Object[] { sute, sute, sute, sute });
    }
    else
    {
      settings.setAttribute("suchtext", "");
    }
    sql += "ORDER BY umsatzid DESC";

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

  public Date getDatumvon()
  {
    return (Date) control.getVondatum().getValue();
  }

  public Date getDatumbis()
  {
    return (Date) control.getBisdatum().getValue();
  }

  public Konto getKonto() throws RemoteException
  {
    return (control.getSuchKonto().getValue() != null ? (Konto) control
        .getSuchKonto().getValue() : null);
  }

  public Buchungsart getBuchungart() throws RemoteException
  {
    return (control.getSuchBuchungsart().getValue() != null ? (Buchungsart) control
        .getSuchBuchungsart().getValue() : null);
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
