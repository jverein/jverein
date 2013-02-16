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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.SEPAParam;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SEPAParamImpl extends AbstractDBObject implements SEPAParam
{
  private static final long serialVersionUID = -2225692364392921889L;

  public SEPAParamImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "sepaparam";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bezeichnung";
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
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Eigenschaft kann nicht gespeichert werden. Siehe system log"));
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getBezeichnung() == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Bezeichnung eingeben"));
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
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
  public void setBankIdentifierLength(Integer bankidentifierlength)
      throws RemoteException
  {
    setAttribute("bankidentifierlength", bankidentifierlength);
  }

  @Override
  public Integer getBankIdentifierLength() throws RemoteException
  {
    return (Integer) getAttribute("bankidentifierlength");
  }

  @Override
  public void setAccountLength(Integer accountlength) throws RemoteException
  {
    setAttribute("accountlength", accountlength);
  }

  @Override
  public Integer getAccountLength() throws RemoteException
  {
    return (Integer) getAttribute("accountlength");
  }

  @Override
  public void setBankIdentifierSample(String bankidentifiersample)
      throws RemoteException
  {
    setAttribute("bankidentifiersample", bankidentifiersample);
  }

  @Override
  public String getBankIdentifierSample() throws RemoteException
  {
    return (String) getAttribute("bankidentifiersample");
  }

  @Override
  public void setAccountSample(String accountsample) throws RemoteException
  {
    setAttribute("accountsample", accountsample);
  }

  @Override
  public String getAccountSample() throws RemoteException
  {
    return (String) getAttribute("accountsample");
  }

  @Override
  public void setIBANSample(String ibansample) throws RemoteException
  {
    setAttribute("ibansample", ibansample);
  }

  @Override
  public String getIBANSample() throws RemoteException
  {
    return (String) getAttribute("ibansample");
  }

}
