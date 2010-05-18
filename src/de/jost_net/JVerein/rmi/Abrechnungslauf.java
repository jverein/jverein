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
 * Revision 1.1  2010/04/25 13:56:26  jost
 * Vorarbeiten Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Abrechnungslauf extends DBObject
{
  public Date getDatum() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public Integer getModus() throws RemoteException;

  public void setModus(Integer modus) throws RemoteException;

  public Date getStichtag() throws RemoteException;

  public void setStichtag(Date stichtag) throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public void setEingabedatum(Date eingabedatum) throws RemoteException;

  public String getZahlungsgrund() throws RemoteException;

  public void setZahlungsgrund(String zahlungsgrund) throws RemoteException;

  public boolean getZusatzbetraege() throws RemoteException;

  public void setZusatzbetraege(Boolean zusatzbetraege) throws RemoteException;

  public boolean getKursteilnehmer() throws RemoteException;

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException;

  public boolean getDtausdruck() throws RemoteException;

  public void setDtausdruck(Boolean dtausdruck) throws RemoteException;

  public Integer getAbbuchungsausgabe() throws RemoteException;

  public void setAbbuchungsausgabe(Integer abbuchungsausgabe)
      throws RemoteException;
}
