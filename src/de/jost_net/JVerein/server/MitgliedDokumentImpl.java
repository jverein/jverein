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
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.MitgliedDokument;

public class MitgliedDokumentImpl extends AbstractDokumentImpl implements
    MitgliedDokument
{

  private static final long serialVersionUID = 1L;

  public MitgliedDokumentImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "mitglieddokument";
  }

}
