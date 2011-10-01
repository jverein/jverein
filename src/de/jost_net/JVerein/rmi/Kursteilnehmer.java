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
import de.willuhn.util.ApplicationException;

public interface Kursteilnehmer extends DBObject
{
  public void setID(String id) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public String getVZweck1() throws RemoteException;

  public void setVZweck1(String vzweck1) throws RemoteException;

  public String getVZweck2() throws RemoteException;

  public void setVZweck2(String vzweck2) throws RemoteException;

  public String getBlz() throws RemoteException;

  public void setBlz(String blz) throws RemoteException;

  public String getKonto() throws RemoteException;

  public void setKonto(String konto) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public Date getGeburtsdatum() throws RemoteException;

  public void setGeburtsdatum(Date geburtsdatum) throws RemoteException;

  public void setGeburtsdatum(String geburtsdatum) throws RemoteException;

  public String getGeschlecht() throws RemoteException;

  public void setGeschlecht(String geschlecht) throws RemoteException;

  public void setEingabedatum() throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public void setAbbudatum() throws RemoteException;

  public void resetAbbudatum() throws RemoteException;

  public Date getAbbudatum() throws RemoteException;

  public void insert() throws RemoteException, ApplicationException;

}
