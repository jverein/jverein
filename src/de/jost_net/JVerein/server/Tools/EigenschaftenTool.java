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
package de.jost_net.JVerein.server.Tools;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.willuhn.datasource.rmi.ResultSetExtractor;

public class EigenschaftenTool
{
  @SuppressWarnings("unchecked")
  public ArrayList<String> getEigenschaften(String mitgliedid)
      throws RemoteException
  {
    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        ArrayList<String> ei = new ArrayList<>();
        while (rs.next())
        {
          ei.add(rs.getString(1));
          ei.add(rs.getString(2));
        }
        return ei;
      }
    };

    String sql = "select eigenschaftgruppe.bezeichnung, eigenschaft.BEZEICHNUNG  "
        + "from eigenschaften, eigenschaft, eigenschaftgruppe "
        + "where eigenschaften.eigenschaft = eigenschaft.id and mitglied = ? "
        + " and eigenschaft.eigenschaftgruppe = eigenschaftgruppe.id "
        + "order by eigenschaftgruppe.bezeichnung,eigenschaft.bezeichnung ";
    return (ArrayList<String>) Einstellungen.getDBService().execute(sql,
        new Object[] { mitgliedid }, rs);
  }
}
