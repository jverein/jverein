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

public interface Adresstyp extends DBObject
{
  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  /**
   * JVerein-ID <br>
   * Mit der JVerein-ID werden Adresstypen mit festen Funktionen in JVerein
   * festgelegt. Beispiele: Mitglied, Spender.
   */
  public int getJVereinid() throws RemoteException;

  public void setJVereinid(int jvereinid) throws RemoteException;
}
