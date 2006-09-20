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

public interface Zusatzabbuchung extends DBObject
{
  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(int mitglied) throws RemoteException;

  public Date getFaelligkeit() throws RemoteException;

  public void setFaelligkeit(Date faelligkeit) throws RemoteException;

  public String getBuchungstext() throws RemoteException;

  public void setBuchungstext(String buchungstext) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public Date getAusfuehrung() throws RemoteException;

  public void setAusfuehrung(Date ausfuehrung) throws RemoteException;
}
