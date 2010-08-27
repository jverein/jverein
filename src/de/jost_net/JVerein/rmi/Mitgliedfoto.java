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
 * Revision 1.1  2010-08-26 11:14:49  jost
 * Neu: Fotos von Mitgliedern
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.rmi.DBObject;

public interface Mitgliedfoto extends DBObject
{
  public void setID(String id) throws RemoteException;
  
  public void setMitglied(Mitglied mitglied) throws RemoteException;

  public byte[] getFoto() throws RemoteException;

  public void setFoto(byte[] foto) throws RemoteException;
}
