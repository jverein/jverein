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
 * Revision 1.2  2010-08-27 19:09:46  jost
 * neu: Mitgliedsfoto
 *
 * Revision 1.1  2010-08-26 11:14:57  jost
 * Neu: Fotos von Mitgliedern
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.willuhn.datasource.db.AbstractDBObject;

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
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck()
  {
    updateCheck();
  }

  @Override
  protected void updateCheck()
  {
    //
  }

  @Override
  protected Class getForeignObject(String arg0)
  {
    return null;
  }

  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    setAttribute("mitglied", mitglied.getID());
  }

  public byte[] getFoto() throws RemoteException
  {
    return (byte[]) this.getAttribute("foto");
  }

  public void setFoto(byte[] foto) throws RemoteException
  {
    setAttribute("foto", foto);
  }

}
