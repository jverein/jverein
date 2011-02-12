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
 * Revision 1.1  2011-01-27 22:24:30  jost
 * Neu: Speicherung von weiteren Adressen in der Mitgliedertabelle
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AdresstypImpl extends AbstractDBObject implements Adresstyp
{

  private static final long serialVersionUID = 500102542884220658L;

  public AdresstypImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "adresstyp";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bezeichnung";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Bezeichnung eingeben"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Adresstyp kann nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    return null;
  }

  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  public int getJVereinid() throws RemoteException
  {
    Integer i = (Integer) getAttribute("jvereinid");
    if (i == null)
      return 0;
    return i.intValue();
  }

  public void setJVereinid(int jvereinid) throws RemoteException
  {
    setAttribute("jvereinid", Integer.valueOf(jvereinid));
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
