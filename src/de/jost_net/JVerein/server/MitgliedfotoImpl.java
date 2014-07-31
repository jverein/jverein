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

import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.willuhn.datasource.db.AbstractDBObject;

public class MitgliedfotoImpl extends AbstractDBObject implements Mitgliedfoto
{

  private static final long serialVersionUID = 1603994510932244220L;

  public MitgliedfotoImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "mitgliedfoto";
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
    return null;
  }

  @Override
  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    setAttribute("mitglied", mitglied.getID());
  }

  @Override
  public byte[] getFoto() throws RemoteException
  {
    return (byte[]) this.getAttribute("foto");
  }

  @Override
  public void setFoto(byte[] foto) throws RemoteException
  {
    setAttribute("foto", foto);
  }

}
