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
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface AbstractDokument extends DBObject
{
  public Integer getReferenz() throws RemoteException;

  public void setReferenz(Integer referenz) throws RemoteException;

  public Date getDatum() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public String getBemerkung() throws RemoteException;

  public void setBemerkung(String bemerkung) throws RemoteException;

  public String getUUID() throws RemoteException;

  public void setUUID(String uuid) throws RemoteException;
}
