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
 * Revision 1.2  2009/01/26 18:48:21  jost
 * Neu: Ersatz Aufwendungen
 *
 * Revision 1.1  2008/07/18 20:17:09  jost
 * Neu: Spendenbescheinigung
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Spendenbescheinigung extends DBObject
{
  public String getZeile1() throws RemoteException;

  public void setZeile1(String zeile) throws RemoteException;

  public String getZeile2() throws RemoteException;

  public void setZeile2(String zeile) throws RemoteException;

  public String getZeile3() throws RemoteException;

  public void setZeile3(String zeile) throws RemoteException;

  public String getZeile4() throws RemoteException;

  public void setZeile4(String zeile) throws RemoteException;

  public String getZeile5() throws RemoteException;

  public void setZeile5(String zeile) throws RemoteException;

  public String getZeile6() throws RemoteException;

  public void setZeile6(String zeile) throws RemoteException;

  public String getZeile7() throws RemoteException;

  public void setZeile7(String zeile) throws RemoteException;

  public Date getSpendedatum() throws RemoteException;

  public void setSpendedatum(Date datum) throws RemoteException;

  public Date getBescheinigungsdatum() throws RemoteException;

  public void setBescheinigungsdatum(Date datum) throws RemoteException;

  public Double getBetrag() throws RemoteException;

  public void setBetrag(Double betrag) throws RemoteException;

  public Formular getFormular() throws RemoteException;

  public void setFormular(Formular formular) throws RemoteException;

  public boolean getErsatzAufwendungen() throws RemoteException;

  public void setErsatzAufwendungen(Boolean ersatzaufwendungen)
      throws RemoteException;

  public Mitglied getMitglied() throws RemoteException;

  public int getMitgliedID() throws RemoteException;

  public void setMitglied(Mitglied mitglied) throws RemoteException;

  public void setMitgliedID(Integer mitglied) throws RemoteException;

}
