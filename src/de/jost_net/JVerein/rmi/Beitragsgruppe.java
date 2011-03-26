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
 * Revision 1.5  2010-11-17 04:51:54  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 * Revision 1.4  2008/01/25 16:06:28  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 * Revision 1.3  2007/03/25 17:04:01  jost
 * Beitragsart aufgenommen.
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
