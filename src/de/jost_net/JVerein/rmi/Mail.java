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
import java.sql.Timestamp;
import java.util.TreeSet;

import de.willuhn.datasource.rmi.DBObject;

public interface Mail extends DBObject
{
  public TreeSet<MailEmpfaenger> getEmpfaenger() throws RemoteException;

  public void setEmpfaenger(TreeSet<MailEmpfaenger> empfaenger)
      throws RemoteException;

  public TreeSet<MailAnhang> getAnhang() throws RemoteException;

  public void setAnhang(TreeSet<MailAnhang> anhang) throws RemoteException;

  public String getBetreff() throws RemoteException;

  public void setBetreff(String betreff) throws RemoteException;

  public String getTxt() throws RemoteException;

  public void setTxt(String txt) throws RemoteException;

  public Timestamp getBearbeitung() throws RemoteException;

  public void setBearbeitung(Timestamp bearbeitung) throws RemoteException;

  public Timestamp getVersand() throws RemoteException;

  public void setVersand(Timestamp versand) throws RemoteException;
}
