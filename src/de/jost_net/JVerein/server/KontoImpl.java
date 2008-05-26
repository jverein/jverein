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
 * Revision 1.1  2008/05/22 06:56:28  jost
 * Buchführung
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class KontoImpl extends AbstractDBObject implements Konto
{
  private static final long serialVersionUID = 1L;

  public KontoImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "konto";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "id";
  }

  protected void deleteCheck() throws ApplicationException
  {
  }

  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException("Bitte Bezeichnung eingeben");
      }
      if (getNummer() == null || getNummer().length() == 0)
      {
        throw new ApplicationException("Bitte Nummer eingeben");
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of konto failed", e);
      throw new ApplicationException(
          "Konto kann nicht gespeichert werden. Siehe system log");
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  protected Class getForeignObject(String arg0) throws RemoteException
  {
    return null;
  }

  public String getNummer() throws RemoteException
  {
    return (String) getAttribute("nummer");
  }

  public void setNummer(String nummer) throws RemoteException
  {
    setAttribute("nummer", nummer);
  }

  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  public Date getEroeffnung() throws RemoteException
  {
    return (Date) getAttribute("eroeffnung");
  }

  public void setEroeffnung(Date eroeffnungdatum) throws RemoteException
  {
    setAttribute("eroeffnung", eroeffnungdatum);
  }

  public Date getAufloesung() throws RemoteException
  {
    return (Date) getAttribute("aufloesung");
  }

  public void setAufloesung(Date aufloesungsdatum) throws RemoteException
  {
    setAttribute("aufloesung", aufloesungsdatum);
  }

  public Integer getHibiscusId() throws RemoteException
  {
    return (Integer) getAttribute("hibiscusid");
  }

  public void setHibiscusId(Integer id) throws RemoteException
  {
    setAttribute("hibiscusid", id);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
