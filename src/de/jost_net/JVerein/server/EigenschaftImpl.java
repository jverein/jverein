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
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftImpl extends AbstractDBObject implements Eigenschaft
{

  private static final long serialVersionUID = 1L;

  public EigenschaftImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "eigenschaft";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      plausi();
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of eigenschaft failed", e);
      throw new ApplicationException(
          "Eigenschaft kann nicht gespeichert werden. Siehe system log");
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getBezeichnung() == null)
    {
      throw new ApplicationException("Bitte Bezeichnung eingeben");
    }
    if (getEigenschaftGruppe() == null)
    {
      throw new ApplicationException("Bitte Eigenschaftengruppe auswählen");
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    if ("eigenschaftgruppe".equals(field))
    {
      return EigenschaftGruppe.class;
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
  public EigenschaftGruppe getEigenschaftGruppe() throws RemoteException
  {
    return (EigenschaftGruppe) getAttribute("eigenschaftgruppe");
  }

  @Override
  public int getEigenschaftGruppeId() throws RemoteException
  {
    return Integer.parseInt(getEigenschaftGruppe().getID());
  }

  @Override
  public void setEigenschaftGruppe(Integer eigenschaftgruppe)
      throws RemoteException
  {
    setAttribute("eigenschaftgruppe", eigenschaftgruppe);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
