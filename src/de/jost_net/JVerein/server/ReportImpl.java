/**********************************************************************
 * $Source$
 * $Revision: 1.2 
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

import de.jost_net.JVerein.rmi.Report;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ReportImpl extends AbstractDBObject implements Report
{
  private static final long serialVersionUID = 3915226732464968553L;

  public ReportImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "report";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "bezeichnung";
  }

  protected void deleteCheck() throws ApplicationException
  {
  }

  protected void insertCheck() throws ApplicationException
  {
    updateCheck();
  }

  protected void updateCheck() throws ApplicationException
  {
    try
    {
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException("Bitte Bezeichnung eingeben");
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Report kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
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

  public String getQuelle() throws RemoteException
  {
    return (String) this.getAttribute("quelle");
  }

  public void setQuelle(String quelle) throws RemoteException
  {
    setAttribute("quelle", quelle);
  }

  public byte[] getKompilat() throws RemoteException
  {
    return (byte[]) this.getAttribute("kompilat");
  }

  public void setKompilat(byte[] kompilat) throws RemoteException
  {
    setAttribute("kompilat", kompilat);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
