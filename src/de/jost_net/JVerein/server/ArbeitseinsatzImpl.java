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
import java.util.Date;

import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class ArbeitseinsatzImpl extends AbstractDBObject implements
    Arbeitseinsatz
{

  private static final long serialVersionUID = 380278347818535726L;

  public ArbeitseinsatzImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "arbeitseinsatz";
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
  protected void insertCheck() throws ApplicationException
  {
    updateCheck();
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    try
    {
      if (getStunden() == null)
      {
        throw new ApplicationException("Keine Stunden angegeben");
      }
      if (getStunden() <= 0d)
      {
        throw new ApplicationException("Bitte mehr als 0 Stunden eingeben");
      }
      if (getDatum() == null)
      {
        throw new ApplicationException("Bitte Datum erfassen");
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    return null;
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", Integer.valueOf(mitglied));
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public void setStunden(Double stunden) throws RemoteException
  {
    setAttribute("stunden", stunden);
  }

  public Double getStunden() throws RemoteException
  {
    Double d = (Double) getAttribute("stunden");
    if (d == null)
    {
      return 0d;
    }
    return d.doubleValue();
  }

  public String getBemerkung() throws RemoteException
  {
    return (String) getAttribute("bemerkung");
  }

  public void setBemerkung(String bemerkung) throws RemoteException
  {
    setAttribute("bemerkung", bemerkung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
