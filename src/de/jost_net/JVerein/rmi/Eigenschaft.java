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

public interface Eigenschaft extends DBObject
{
  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String name) throws RemoteException;

  public EigenschaftGruppe getEigenschaftGruppe() throws RemoteException;

  public int getEigenschaftGruppeId() throws RemoteException;

  public void setEigenschaftGruppe(Integer eigenschaftgruppe)
      throws RemoteException;

}
