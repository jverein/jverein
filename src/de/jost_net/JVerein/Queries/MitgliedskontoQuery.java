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
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl.DIFFERENZ;
import de.jost_net.JVerein.keys.Zahlungsweg;
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

  protected DIFFERENZ differenz;

  protected Boolean ohneAbbucher;

  private boolean and = false;

  private String sql = "";

  private ArrayList<Object> bedingungen = new ArrayList<Object>();

  private ArrayList<Mitgliedskonto> ergebnis;

  public MitgliedskontoQuery(Mitglied m, Date vonDatum, Date bisDatum,
      DIFFERENZ differenz, Boolean ohneAbbucher)
  {
    this.mitglied = m;
    this.vonDatum = vonDatum;
    this.bisDatum = bisDatum;
    this.differenz = differenz;
    this.ohneAbbucher = ohneAbbucher;
  }

  @SuppressWarnings("unchecked")
  public ArrayList<Mitgliedskonto> get() throws RemoteException
  {
    final DBService service = Einstellungen.getDBService();
    ergebnis = new ArrayList<Mitgliedskonto>();
    sql = "select mitgliedskonto.*, sum(buchung.betrag) ";
    sql += "from mitgliedskonto ";
    sql += "left join buchung on mitgliedskonto.id = buchung.MITGLIEDSKONTO ";

    addCondition("mitgliedskonto.mitglied = ?", mitglied.getID());

    try
    {
      java.sql.Date vd = new java.sql.Date(vonDatum.getTime());
      addCondition("mitgliedskonto.datum >= ? ", vd);
    }
    catch (NullPointerException e)
    {
      Logger.info("Export Mitgliedskonto ohne 'von-Datum'");
    }

    try
    {
      java.sql.Date bd = new java.sql.Date(bisDatum.getTime());
      addCondition("mitgliedskonto.datum <= ? ", bd);
    }
    catch (NullPointerException e)
    {
      Logger.info("Export Mitgliedskonto ohne 'bis-Datum'");
    }

    if (ohneAbbucher.booleanValue())
    {
      addCondition("mitgliedskonto.zahlungsweg <> ? ",
          Zahlungsweg.BASISLASTSCHRIFT);
    }

    sql += "group by mitgliedskonto.id ";

    if (DIFFERENZ.FEHLBETRAG == differenz)
    {
      sql += "having sum(buchung.betrag) < mitgliedskonto.betrag or sum(buchung.betrag) is null";
    }
    if (DIFFERENZ.UEBERZAHLUNG == differenz)
    {
      sql += "having sum(buchung.betrag) > mitgliedskonto.betrag ";
    }

    // sql += "ORDER BY datum DESC";

    Logger.debug(sql);

    ResultSetExtractor rs = new ResultSetExtractor()
    {

      @Override
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
}
