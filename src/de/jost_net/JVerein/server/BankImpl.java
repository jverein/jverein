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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Bank;
import de.willuhn.datasource.db.AbstractDBObject;

public class BankImpl extends AbstractDBObject implements Bank
{
  private static final long serialVersionUID = -5371916092773950947L;

  public BankImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "bank";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bic";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck()
  {
    //
  }

  @Override
  protected void updateCheck()
  {
    //
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
  public String getBLZ() throws RemoteException
  {
    return (String) getAttribute("blz");
  }

  @Override
  public void setBLZ(String blz) throws RemoteException
  {
    setAttribute("blz", blz);
  }

  @Override
  public String getBIC() throws RemoteException
  {
    return (String) getAttribute("bic");
  }

  @Override
  public void setBIC(String bic) throws RemoteException
  {
    setAttribute("bic", bic);
  }

}
