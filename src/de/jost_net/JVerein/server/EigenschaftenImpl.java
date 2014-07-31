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

import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftenImpl extends AbstractDBObject implements
    Eigenschaften
{

  private static final long serialVersionUID = -5906609226109964967L;

  public EigenschaftenImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "eigenschaften";
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
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getEigenschaft() == null)
      {
        throw new ApplicationException("Bitte Eigenschaft eingeben");
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Eigenschaft kann nicht gespeichert werden. Siehe system log";
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
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    else if ("eigenschaft".equals(arg0))
    {
      return Eigenschaft.class;
    }
    return null;
  }

  @Override
  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  @Override
  public void setMitglied(String mitglied) throws RemoteException
  {
    setAttribute("mitglied", new Integer(mitglied));
  }

  @Override
  public Eigenschaft getEigenschaft() throws RemoteException
  {
    return (Eigenschaft) getAttribute("eigenschaft");
  }

  @Override
  public void setEigenschaft(String eigenschaft) throws RemoteException
  {
    setAttribute("eigenschaft", eigenschaft);
  }

}
