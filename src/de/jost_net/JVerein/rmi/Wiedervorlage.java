/**********************************************************************
 * $Source$
 *  * $Revision$
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
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Wiedervorlage extends DBObject
{
  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(int mitglied) throws RemoteException;

  public Date getDatum() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public String getVermerk() throws RemoteException;

  public void setVermerk(String vermerk) throws RemoteException;

  public Date getErledigung() throws RemoteException;

  public void setErledigung(Date erledigung) throws RemoteException;
}
