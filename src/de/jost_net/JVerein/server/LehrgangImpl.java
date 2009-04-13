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
import java.util.Date;

import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.Lehrgangsart;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class LehrgangImpl extends AbstractDBObject implements Lehrgang
{
  private static final long serialVersionUID = 380278347818535726L;

  public LehrgangImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "lehrgang";
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
    // try
    // {
    // }
    // catch (RemoteException e)
    // {
    // String fehler =
    // "Lehrgangsart kann nicht gespeichert werden. Siehe system log";
    // Logger.error(fehler, e);
    // throw new ApplicationException(fehler);
    // }
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
  {
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    if ("lehrgangsart".equals(arg0))
    {
      return Lehrgangsart.class;
    }

    return null;
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", new Integer(mitglied));
  }

  public Lehrgangsart getLehrgangsart() throws RemoteException
  {
    return (Lehrgangsart) getAttribute("lehrgangsart");
  }

  public void setLehrgangsart(int lehrgangsart) throws RemoteException
  {
    setAttribute("lehrgangsart", new Integer(lehrgangsart));
  }

  public Date getVon() throws RemoteException
  {
    return (Date) getAttribute("von");
  }

  public void setVon(Date von) throws RemoteException
  {
    setAttribute("von", von);
  }

  public Date getBis() throws RemoteException
  {
    return (Date) getAttribute("bis");
  }

  public void setBis(Date bis) throws RemoteException
  {
    setAttribute("bis", bis);
  }

  public String getVeranstalter() throws RemoteException
  {
    return (String) getAttribute("veranstalter");
  }

  public void setVeranstalter(String veranstalter) throws RemoteException
  {
    setAttribute("veranstalter", veranstalter);
  }

  public String getErgebnis() throws RemoteException
  {
    return (String) getAttribute("ergebnis");
  }

  public void setErgebnis(String ergebnis) throws RemoteException
  {
    setAttribute("ergebnis", ergebnis);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
