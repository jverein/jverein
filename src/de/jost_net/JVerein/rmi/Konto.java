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
 * $Log$
 * Revision 1.1  2008/05/22 06:55:38  jost
 * Buchführung
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Konto extends DBObject
{
  public String getID() throws RemoteException;

  public String getNummer() throws RemoteException;

  public void setNummer(String nummer) throws RemoteException;

  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  public Date getEroeffnung() throws RemoteException;

  public void setEroeffnung(Date eroeffnung) throws RemoteException;

  public Date getAufloesung() throws RemoteException;

  public void setAufloesung(Date aufloesungsdatum) throws RemoteException;

  public Integer getHibiscusId() throws RemoteException;

  public void setHibiscusId(Integer HibiscusId) throws RemoteException;
}
