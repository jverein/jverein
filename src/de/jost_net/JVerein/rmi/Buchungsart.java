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
 * Revision 1.7  2011-03-07 21:07:52  jost
 * Neu:  Automatische Spendenbescheinigungen: Eigenschaft Spende aufgenommen
 *
 * Revision 1.6  2009/09/10 18:19:20  jost
 * neu: Buchungsklassen
 *
 * Revision 1.5  2008/05/22 06:55:25  jost
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
