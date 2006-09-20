/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Buchung extends DBObject
{
  public String getUmsatzid() throws RemoteException;

  public void setUmsatzid(String umsatzid) throws RemoteException;

  public String getKonto() throws RemoteException;

  public void setKonto(String konto) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public String getZweck() throws RemoteException;

  public void setZweck(String zweck) throws RemoteException;

  public String getZweck2() throws RemoteException;

  public void setZweck2(String zweck2) throws RemoteException;

  public Date getDatum() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public double getSaldo() throws RemoteException;

  public void setSaldo(double saldo) throws RemoteException;

  public String getArt() throws RemoteException;

  public void setArt(String art) throws RemoteException;

  public String getKommentar() throws RemoteException;

  public void setKommentar(String kommentar) throws RemoteException;

  public Buchungsart getBuchungsart() throws RemoteException;

  public int getBuchungsartId() throws RemoteException;

  public void setBuchungsart(Integer buchungsart) throws RemoteException;

}
