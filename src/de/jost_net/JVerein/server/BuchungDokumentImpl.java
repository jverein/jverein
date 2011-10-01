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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.BuchungDokument;

public class BuchungDokumentImpl extends AbstractDokumentImpl implements
    BuchungDokument
{

  private static final long serialVersionUID = 1L;

  public BuchungDokumentImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "buchungdokument";
  }

}
