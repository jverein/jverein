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
import java.util.Date;

import de.jost_net.JVerein.rmi.Inventar;
import de.jost_net.JVerein.rmi.InventarLagerort;
import de.willuhn.datasource.db.AbstractDBObject;

public class InventarImpl extends AbstractDBObject implements Inventar
{

  private static final long serialVersionUID = 380278347818535726L;

  public InventarImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "inventar";
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
    if ("inventarlagerort".equals(arg0))
    {
      return InventarLagerort.class;
    }
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
  public InventarLagerort getLagerort() throws RemoteException
  {
    return (InventarLagerort) getAttribute("lagerort");
  }

  @Override
  public void setLagerort(InventarLagerort lagerort) throws RemoteException
  {
    setAttribute("lagerort", Integer.valueOf(lagerort.getID()));
  }

  @Override
  public Date getAnschaffungsdatum() throws RemoteException
  {
    return (Date) getAttribute("anschaffungsdatum");
  }

  @Override
  public void setAnschaffungsdatum(Date anschaffungsdatum)
      throws RemoteException
  {
    setAttribute("anschaffungsdatum", anschaffungsdatum);
  }

  @Override
  public Double getAnschaffungswert() throws RemoteException
  {
    return (Double) getAttribute("anschaffungswert");
  }

  @Override
  public void setAnschaffungswert(Double anschaffungswert)
      throws RemoteException
  {
    setAttribute("anschaffungswert", anschaffungswert);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
