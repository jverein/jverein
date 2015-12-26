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

import de.jost_net.JVerein.rmi.AbstractDokument;
import de.willuhn.datasource.db.AbstractDBObject;

public abstract class AbstractDokumentImpl extends AbstractDBObject implements
    AbstractDokument
{

  private static final long serialVersionUID = 1L;

  public AbstractDokumentImpl() throws RemoteException
  {
    super();
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck()
  {
    insertCheck();
  }

  @Override
  protected void insertCheck()
  {
    //
  }

  @Override
  protected void updateCheck()
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    return null;
  }

  @Override
  public Long getReferenz() throws RemoteException
  {
    return (Long) getAttribute("referenz");
  }

  @Override
  public void setReferenz(Long referenz) throws RemoteException
  {
    setAttribute("referenz", referenz);
  }

  @Override
  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  @Override
  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  @Override
  public String getBemerkung() throws RemoteException
  {
    return (String) getAttribute("bemerkung");
  }

  @Override
  public void setBemerkung(String bemerkung) throws RemoteException
  {
    setAttribute("bemerkung", bemerkung);
  }

  @Override
  public String getUUID() throws RemoteException
  {
    return (String) getAttribute("uuid");
  }

  @Override
  public void setUUID(String uuid) throws RemoteException
  {
    setAttribute("uuid", uuid);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
