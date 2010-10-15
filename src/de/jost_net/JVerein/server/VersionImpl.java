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
 * Revision 1.2  2008-11-29 13:17:19  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2007/12/01 17:47:50  jost
 * Neue DB-Update-Mimik
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Version;
import de.willuhn.datasource.db.AbstractDBObject;

public class VersionImpl extends AbstractDBObject implements Version
{

  private static final long serialVersionUID = 1L;

  public VersionImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "version";
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
    //
  }

  @Override
  protected void updateCheck()
  {
    insertCheck();
  }

  @Override
  protected Class getForeignObject(String arg0)
  {
    return null;
  }

  public int getVersion() throws RemoteException
  {
    Integer i = (Integer) getAttribute("version");
    return i.intValue();
  }

  public void setVersion(int version) throws RemoteException
  {
    setAttribute("version", version);
  }
}
