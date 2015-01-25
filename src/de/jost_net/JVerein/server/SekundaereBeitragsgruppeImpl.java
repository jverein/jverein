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

import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.SekundaereBeitragsgruppe;
import de.willuhn.datasource.db.AbstractDBObject;

public class SekundaereBeitragsgruppeImpl extends AbstractDBObject implements
    SekundaereBeitragsgruppe
{

  private static final long serialVersionUID = 380278347818535726L;

  public SekundaereBeitragsgruppeImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "sekundaerebeitragsgruppe";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck()
  {
    updateCheck();
  }

  @Override
  protected void updateCheck()
  {
    //
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    if ("beitragsgruppe".equals(arg0))
    {
      return Beitragsgruppe.class;
    }
    return null;
  }

  @Override
  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  @Override
  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", Integer.valueOf(mitglied));
  }

  @Override
  public Beitragsgruppe getBeitragsgruppe() throws RemoteException
  {
    return (Beitragsgruppe) getAttribute("beitragsgruppe");
  }

  @Override
  public void setBeitragsgruppe(int beitragsgruppe) throws RemoteException
  {
    setAttribute("beitragsgruppe", Integer.valueOf(beitragsgruppe));
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("beitragsgruppebezeichnung"))
    {
      return getBeitragsgruppe().getBezeichnung();
    }
    return super.getAttribute(fieldName);
  }

}
