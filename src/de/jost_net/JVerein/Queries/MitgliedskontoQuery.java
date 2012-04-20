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
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;

public class MitgliedskontoQuery
{
  private Mitglied mitglied;

  protected Date vonDatum;

  protected Date bisDatum;

  protected String differenz;

  private boolean and = false;

  private String sql = "";

  private ArrayList<Object> bedingungen = new ArrayList<Object>();

  private ArrayList<Mitgliedskonto> ergebnis;

  public MitgliedskontoQuery(Mitglied m, Date vonDatum, Date bisDatum,
      String differenz)
  {
    this.mitglied = m;
    this.vonDatum = vonDatum;
    this.bisDatum = bisDatum;
    this.differenz = differenz;
  }

  public ArrayList<Mitgliedskonto> get() throws RemoteException
  {
    final DBService service = Einstellungen.getDBService();
    ergebnis = new ArrayList<Mitgliedskonto>();
    sql = "select mitgliedskonto.*, sum(buchung.betrag) ";
    sql += "from mitgliedskonto ";
    sql += "join buchung on mitgliedskonto.id = buchung.MITGLIEDSKONTO ";

    addCondition("mitglied = ?", mitglied.getID());

    java.sql.Date vd = null;
    try
    {
      vd = new java.sql.Date(vonDatum.getTime());
    }
    catch (NullPointerException e)
    {
      throw new RemoteException("von-Datum fehlt");
    }

    java.sql.Date bd = null;
    try
    {
      bd = new java.sql.Date(bisDatum.getTime());
    }
    catch (NullPointerException e)
    {
      throw new RemoteException("bis-Datum fehlt");
    }

    addCondition("mitgliedskonto.datum >= ? ", vd);
    addCondition("mitgliedskonto.datum <= ? ", bd);

    sql += "group by buchung.mitgliedskonto ";

    if (differenz.equals("Fehlbetrag"))
    {
      sql += "having sum(buchung.betrag) < mitgliedskonto.betrag ";
    }
    if (differenz.equals("Überzahlung"))
    {
      sql += "having sum(buchung.betrag) > mitgliedskonto.betrag ";
    }

    // sql += "ORDER BY datum DESC";

    Logger.debug(sql);

    ResultSetExtractor rs = new ResultSetExtractor()
    {

      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        ArrayList<Mitgliedskonto> list = new ArrayList<Mitgliedskonto>();
        while (rs.next())
        {
          list.add((Mitgliedskonto) service.createObject(Mitgliedskonto.class,
              rs.getString(1)));
        }
        return list;
      }
    };
    ergebnis = (ArrayList<Mitgliedskonto>) service.execute(sql,
        bedingungen.toArray(), rs);
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

  // private void addCondition(String condition, Object[] obj)
  // {
  // addCondition(condition);
  // for (Object o : obj)
  // {
  // bedingungen.add(o);
  // }
  // }
}
