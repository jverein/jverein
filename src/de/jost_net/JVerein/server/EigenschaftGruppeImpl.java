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

import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftGruppeImpl extends AbstractDBObject implements
    EigenschaftGruppe
{

  private static final long serialVersionUID = -5906609226109964967L;

  public EigenschaftGruppeImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "eigenschaftgruppe";
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
      if (getBezeichnung() == null)
      {
        throw new ApplicationException("Bitte Bezeichnung eingeben");
      }
    }
    catch (RemoteException e)
    {
      String fehler = "EigenschaftGruppe kann nicht gespeichert werden. Siehe system log";
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
  public Boolean getPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("pflicht"));
  }

  @Override
  public void setPflicht(Boolean pflicht) throws RemoteException
  {
    setAttribute("pflicht", pflicht);
  }

  @Override
  public Boolean getMax1() throws RemoteException
  {
    return Util.getBoolean(getAttribute("max1"));
  }

  @Override
  public void setMax1(Boolean max1) throws RemoteException
  {
    setAttribute("max1", max1);
  }

}
