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

import de.jost_net.JVerein.rmi.Version;
import de.willuhn.datasource.db.AbstractDBObject;

public class VersionImpl extends AbstractDBObject implements Version
{

  private static final long serialVersionUID = 1L;

  public VersionImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "version";
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
    //
  }

  @Override
  protected void updateCheck()
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    return null;
  }

  @Override
  public int getVersion() throws RemoteException
  {
    Integer i = (Integer) getAttribute("version");
    return i.intValue();
  }

  @Override
  public void setVersion(int version) throws RemoteException
  {
    setAttribute("version", version);
  }
}
