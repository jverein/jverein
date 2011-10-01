/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.rmi.DBObject;

public interface Eigenschaften extends DBObject
{
  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(String mitglied) throws RemoteException;

  public void setEigenschaft(String eigenschaft) throws RemoteException;

  public Eigenschaft getEigenschaft() throws RemoteException;
}
