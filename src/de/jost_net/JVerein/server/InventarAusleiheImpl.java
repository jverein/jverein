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
import de.jost_net.JVerein.rmi.InventarAusleihe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;

public class InventarAusleiheImpl extends AbstractDBObject implements
    InventarAusleihe
{

  private static final long serialVersionUID = 380278347818535726L;

  public InventarAusleiheImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "inventarausleihe";
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
    if ("inventar".equals(arg0))
    {
      return Inventar.class;
    }
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    return null;
  }

  @Override
  public Inventar getInventar() throws RemoteException
  {
    return (Inventar) getAttribute("inventar");
  }

  @Override
  public void setInventar(Inventar inventar) throws RemoteException
  {
    setAttribute("inventar", Integer.valueOf(inventar.getID()));
  }

  @Override
  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  @Override
  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    setAttribute("mitglied", Integer.valueOf(mitglied.getID()));
  }

  @Override
  public String getNichtmitglied() throws RemoteException
  {
    return (String) getAttribute("nichtmitglied");
  }

  @Override
  public void setNichtmitglied(String nichtmitglied) throws RemoteException
  {
    setAttribute("ausleihernichtmitglied", nichtmitglied);
  }

  @Override
  public Date getVon() throws RemoteException
  {
    return (Date) getAttribute("von");
  }

  @Override
  public void setVon(Date von) throws RemoteException
  {
    setAttribute("von", von);
  }

  @Override
  public Date getBis() throws RemoteException
  {
    return (Date) getAttribute("bis");
  }

  @Override
  public void setBis(Date bis) throws RemoteException
  {
    setAttribute("bis", bis);
  }

  @Override
  public Date getRueckgabedatum() throws RemoteException
  {
    return (Date) getAttribute("rueckgabedatum");
  }

  @Override
  public void setRueckgabedatum(Date rueckgabedatum) throws RemoteException
  {
    setAttribute("rueckgabedatum", rueckgabedatum);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
