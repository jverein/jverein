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
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.rmi.DBObject;

public interface SEPAParam extends DBObject
{
  public void setID(String id) throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  public String getBezeichnung() throws RemoteException;

  public void setBankIdentifierLength(Integer bankidentifierlength)
      throws RemoteException;

  public Integer getBankIdentifierLength() throws RemoteException;

  public void setAccountLength(Integer accountlength) throws RemoteException;

  public Integer getAccountLength() throws RemoteException;

  public void setBankIdentifierSample(String bankidentifiersample)
      throws RemoteException;

  public String getBankIdentifierSample() throws RemoteException;

  public void setAccountSample(String accountsample) throws RemoteException;

  public String getAccountSample() throws RemoteException;

  public void setIBANSample(String ibansample) throws RemoteException;

  public String getIBANSample() throws RemoteException;

}