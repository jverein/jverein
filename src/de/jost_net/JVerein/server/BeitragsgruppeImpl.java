/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeImpl extends AbstractDBObject implements
    Beitragsgruppe
{
  private static final long serialVersionUID = 1L;

  public BeitragsgruppeImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "beitragsgruppe";
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
      if (getBetrag() < 0)
      {
        throw new ApplicationException("Betrag nicht gültig");
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of mitglied failed", e);
      throw new ApplicationException(
          "Mitglied kann nicht gespeichert werden. Siehe system log");
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

  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
    {
      return 0;
    }
    return d.doubleValue();
  }

  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag", new Double(d));
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
