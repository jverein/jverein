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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedfotoImpl extends AbstractDBObject implements Mitgliedfoto
{

  private static final long serialVersionUID = 1603994510932244220L;

  public MitgliedfotoImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "mitgliedfoto";
  }

  @Override
  public String getPrimaryAttribute() throws RemoteException
  {
    return "mitglied";
  }

  @Override
  protected void deleteCheck() throws ApplicationException
  {
    //
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    updateCheck();
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    //
  }

  @Override
  protected Class getForeignObject(String arg0)
  {
    return null;
  }

  public byte[] getFoto() throws RemoteException
  {
    return (byte[]) this.getAttribute("foto");
  }

  public void setFoto(byte[] foto) throws RemoteException
  {
    setAttribute("foto", foto);
  }

  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", mitglied);
  }

}
