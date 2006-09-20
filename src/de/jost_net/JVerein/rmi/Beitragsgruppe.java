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

import de.willuhn.datasource.rmi.DBObject;

public interface Beitragsgruppe extends DBObject
{
  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;
}
