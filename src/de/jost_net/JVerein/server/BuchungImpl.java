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
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungImpl extends AbstractDBObject implements Buchung
{
  private static final long serialVersionUID = 1L;

  public BuchungImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "buchung";
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
      plausi();
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of buchung failed", e);
      throw new ApplicationException(
          "Buchung kann nicht gespeichert werden. Siehe system log");
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getKonto() == null || getKonto().length() == 0)
    {
      throw new ApplicationException("Bitte Konto eingeben");
    }
    if (getDatum() == null)
    {
      throw new ApplicationException("Bitte Datum eingeben");
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    try
    {
      plausi();
    }
    catch (RemoteException e)
    {
      Logger.error("update check of buchung failed", e);
      throw new ApplicationException(
          "Buchung kann nicht gespeichert werden. Siehe system log");
    }
  }

  protected Class getForeignObject(String field) throws RemoteException
  {
    if ("buchungsart".equals(field))
    {
      return Buchungsart.class;
    }
    return null;
  }

  public String getUmsatzid() throws RemoteException
  {
    return (String) getAttribute("umsatzid");
  }

  public void setUmsatzid(String umsatzid) throws RemoteException
  {
    setAttribute("umsatzid", umsatzid);
  }

  public String getKonto() throws RemoteException
  {
    return (String) getAttribute("konto");
  }

  public void setKonto(String konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
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

  public String getZweck() throws RemoteException
  {
    return (String) getAttribute("zweck");
  }

  public void setZweck(String zweck) throws RemoteException
  {
    setAttribute("zweck", zweck);
  }

  public String getZweck2() throws RemoteException
  {
    return (String) getAttribute("zweck2");
  }

  public void setZweck2(String zweck2) throws RemoteException
  {
    setAttribute("zweck2", zweck2);
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public void setDatum(String datum) throws RemoteException
  {
    setAttribute("datum", toDate(datum));
  }

  public double getSaldo() throws RemoteException
  {
    Double d = (Double) getAttribute("Saldo");
    if (d == null)
    {
      return 0;
    }
    return d.doubleValue();
  }

  public void setSaldo(double d) throws RemoteException
  {
    setAttribute("saldo", new Double(d));
  }

  public String getArt() throws RemoteException
  {
    return (String) getAttribute("art");
  }

  public void setArt(String art) throws RemoteException
  {
    setAttribute("art", art);
  }

  public String getKommentar() throws RemoteException
  {
    return (String) getAttribute("kommentar");
  }

  public void setKommentar(String kommentar) throws RemoteException
  {
    setAttribute("kommentar", kommentar);
  }

  public Buchungsart getBuchungsart() throws RemoteException
  {
    return (Buchungsart) getAttribute("buchungsart");
  }

  public int getBuchungsartId() throws RemoteException
  {
    return Integer.parseInt(getBuchungsart().getID());
  }

  public void setBuchungsart(Integer buchungsart) throws RemoteException
  {
    setAttribute("buchungsart", buchungsart);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  private Date toDate(String datum)
  {
    Date d = null;

    try
    {
      d = Einstellungen.DATEFORMAT.parse(datum);
    }
    catch (Exception e)
    {
      //
    }
    return d;
  }
}
