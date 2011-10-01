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

public interface Beitragsgruppe extends DBObject
{
  public String getID() throws RemoteException;

  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public int getBeitragsArt() throws RemoteException;

  public void setBeitragsArt(int art) throws RemoteException;

  public double getArbeitseinsatzStunden() throws RemoteException;

  public void setArbeitseinsatzStunden(double arbeitseinsatzStunden)
      throws RemoteException;

  public double getArbeitseinsatzBetrag() throws RemoteException;

  public void setArbeitseinsatzBetrag(double arbeitseinsatzBetrag)
      throws RemoteException;

  public Buchungsart getBuchungsart() throws RemoteException;

  public void setBuchungsart(Buchungsart buchungsart) throws RemoteException;

}
