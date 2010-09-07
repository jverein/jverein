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
 * Revision 1.2  2010/01/01 20:11:00  jost
 * Typisierung der Zusatzfelder
 *
 * Revision 1.1  2008/04/10 19:03:06  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Zusatzfelder extends DBObject
{
  public String getID() throws RemoteException;

  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(int mitglied) throws RemoteException;

  public Felddefinition getFelddefinition() throws RemoteException;

  public void setFelddefinition(int felddefiniton) throws RemoteException;

  public String getFeld() throws RemoteException;

  public void setFeld(String feld) throws RemoteException;

  public Date getFeldDatum() throws RemoteException;

  public void setFeldDatum(Date datum) throws RemoteException;

  public Integer getFeldGanzzahl() throws RemoteException;

  public void setFeldGanzzahl(Integer ganzzahl) throws RemoteException;

  public double getFeldGleitkommazahl() throws RemoteException;

  public void setFeldGleitkommazahl(double gleitkommazahl)
      throws RemoteException;

  public BigDecimal getFeldWaehrung() throws RemoteException;

  public void setFeldWaehrung(BigDecimal waehrung) throws RemoteException;

  public Boolean getFeldJaNein() throws RemoteException;

  public void setFeldJaNein(Boolean janein) throws RemoteException;

  public String getString() throws RemoteException;
}
