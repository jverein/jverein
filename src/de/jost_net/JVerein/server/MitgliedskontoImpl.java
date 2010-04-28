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
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoImpl extends AbstractDBObject implements
    Mitgliedskonto
{
  private static final long serialVersionUID = -1234L;

  public MitgliedskontoImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "mitgliedskonto";
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
      if (getZweck1().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Verwendungszweck fehlt"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Mitgliedskonto kann nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
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

  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    setAttribute("mitglied", new Integer(mitglied.getID()));
  }

  public String getBuchungstyp() throws RemoteException
  {
    return (String) getAttributeType("buchungstyp");
  }

  public void setBuchungstyp(String buchungstyp) throws RemoteException
  {
    setAttribute("buchungstyp", buchungstyp);
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public String getZweck1() throws RemoteException
  {
    return (String) getAttribute("zweck1");
  }

  public void setZweck1(String zweck1) throws RemoteException
  {
    if (zweck1.length() > 27)
    {
      zweck1 = zweck1.substring(0, 27);
    }
    setAttribute("zweck1", zweck1);
  }

  public String getZweck2() throws RemoteException
  {
    return (String) getAttribute("zweck2");
  }

  public void setZweck2(String zweck2) throws RemoteException
  {
    if (zweck2.length() > 27)
    {
      zweck2 = zweck2.substring(0, 27);
    }
    setAttribute("zweck2", zweck2);
  }

  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
      return 0;
    return d.doubleValue();
  }

  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag", new Double(d));
  }

  public String getReferenz() throws RemoteException
  {
    return (String) getAttribute("referenz");
  }

  public void setReferenz(String referenz) throws RemoteException
  {
    setAttribute("referenz", referenz);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
