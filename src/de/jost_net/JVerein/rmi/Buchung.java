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
 * Revision 1.5  2008/05/22 06:55:11  jost
 * Buchführung
 *
 * Revision 1.4  2008/03/16 07:37:55  jost
 * Reaktivierung Buchführung
 *
 * Revision 1.2  2007/02/23 20:28:24  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:35  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Buchung extends DBObject
{
  public Integer getUmsatzid() throws RemoteException;

  public void setUmsatzid(Integer umsatzid) throws RemoteException;

  public Konto getKonto() throws RemoteException;

  public void setKonto(Konto konto) throws RemoteException;

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

  public String getArt() throws RemoteException;

  public void setArt(String art) throws RemoteException;

  public String getKommentar() throws RemoteException;

  public void setKommentar(String kommentar) throws RemoteException;

  public Buchungsart getBuchungsart() throws RemoteException;

  public int getBuchungsartId() throws RemoteException;

  public void setBuchungsart(Integer buchungsart) throws RemoteException;

}
