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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Auswertung;
import de.jost_net.JVerein.rmi.AuswertungPos;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AuswertungPosImpl extends AbstractDBObject implements
    AuswertungPos
{
  private static final long serialVersionUID = -5906609226109964967L;

  public AuswertungPosImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "auswertungpos";
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
    try
    {
      if (getFeld() == null || getFeld().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Feld eingeben"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Auswertung kann nicht gespeichert werden. Siehe system log");
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
    if (arg0.equals("auswertung"))
    {
      return Auswertung.class;
    }
    return null;
  }

  public Auswertung getAuswertung() throws RemoteException
  {
    return (Auswertung) getAttribute("auswertung");
  }

  public void setAuswertung(Auswertung auswertung) throws RemoteException
  {
    setAttribute("auswertung", auswertung.getID());
  }

  public String getFeld() throws RemoteException
  {
    return (String) getAttribute("feld");
  }

  public void setFeld(String feld) throws RemoteException
  {
    setAttribute("feld", feld);
  }

  public String getZeichenfolge() throws RemoteException
  {
    return (String) getAttribute("zeichenfolge");
  }

  public void setZeichenfolge(String zeichenfolge) throws RemoteException
  {
    setAttribute("zeichenfolge", zeichenfolge);
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public Integer getGanzzahl() throws RemoteException
  {
    return (Integer) getAttribute("ganzzahl");
  }

  public void setGanzzahl(Integer ganzzahl) throws RemoteException
  {
    setAttribute("ganzzahl", ganzzahl);
  }

  public boolean getJanein() throws RemoteException
  {
    return Util.getBoolean(getAttribute("janein"));
  }

  public void setJanein(boolean janein) throws RemoteException
  {
    setAttribute("janein", Boolean.valueOf(janein));
  }

}
