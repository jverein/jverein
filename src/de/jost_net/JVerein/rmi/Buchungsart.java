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

public interface Buchungsart extends DBObject
{
  public int getNummer() throws RemoteException;

  public void setNummer(int nummer) throws RemoteException;

  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  public int getArt() throws RemoteException;

  public void setArt(int art) throws RemoteException;

  public Buchungsklasse getBuchungsklasse() throws RemoteException;

  public int getBuchungsklasseId() throws RemoteException;

  public void setBuchungsklasse(Integer buchungsklasse) throws RemoteException;

  public Boolean getSpende() throws RemoteException;

  public void setSpende(Boolean spende) throws RemoteException;

}
