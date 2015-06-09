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

import de.jost_net.JVerein.rmi.InventarLagerort;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class InventarLagerortImpl extends AbstractDBObject implements
    InventarLagerort
{

  private static final long serialVersionUID = 380278347818535726L;

  public InventarLagerortImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "inventarlagerort";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bezeichnung";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    updateCheck();
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    try
    {
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException("Bitte Bezeichnung eingeben");
      }
    }
    catch (RemoteException e)
    {
      String fehler = "InventarLagerort kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    return null;
  }

  @Override
  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  @Override
  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
