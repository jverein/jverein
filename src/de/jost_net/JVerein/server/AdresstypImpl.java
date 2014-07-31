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

import de.jost_net.JVerein.rmi.Adresstyp;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AdresstypImpl extends AbstractDBObject implements Adresstyp
{

  private static final long serialVersionUID = 500102542884220658L;

  public AdresstypImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "adresstyp";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bezeichnung";
  }

  @Override
  protected void deleteCheck() throws ApplicationException
  {
    try
    {
      if (getJVereinid() > 0)
      {
        throw new ApplicationException(
            "Dieser Datensatz darf nicht gelöscht werden!");
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException(e);
    }
  }

  @Override
  protected void insertCheck() throws ApplicationException
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
      String fehler = "Adresstyp kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
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
  public String getBezeichnungPlural() throws RemoteException
  {
    return (String) getAttribute("bezeichnungplural");
  }

  @Override
  public void setBezeichnungPlural(String bezeichnungplural)
      throws RemoteException
  {
    setAttribute("bezeichnungplural", bezeichnungplural);
  }

  @Override
  public int getJVereinid() throws RemoteException
  {
    Integer i = (Integer) getAttribute("jvereinid");
    if (i == null)
      return 0;
    return i.intValue();
  }

  @Override
  public void setJVereinid(int jvereinid) throws RemoteException
  {
    setAttribute("jvereinid", Integer.valueOf(jvereinid));
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
