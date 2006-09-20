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

import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzabbuchung;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ZusatzabbuchungImpl extends AbstractDBObject implements
    Zusatzabbuchung
{
  private static final long serialVersionUID = 1L;

  public ZusatzabbuchungImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "zusatzabbuchung";
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
      if (getFaelligkeit() == null)
      {
        throw new ApplicationException("Bitte Fälligkeit eingeben");
      }
      if (getBuchungstext() == null || getBuchungstext().length() == 0)
      {
        throw new ApplicationException("Bitte Buchungstext eingeben");
      }
      if (getBetrag() < 0)
      {
        throw new ApplicationException("Betrag nicht gültig");
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Zusatzabbuchung kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

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

  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", mitglied);
  }

  public Date getFaelligkeit() throws RemoteException
  {
    return (Date) getAttribute("faelligkeit");
  }

  public void setFaelligkeit(Date faelligkeit) throws RemoteException
  {
    setAttribute("faelligkeit", faelligkeit);
  }

  public String getBuchungstext() throws RemoteException
  {
    return (String) getAttribute("buchungstext");
  }

  public void setBuchungstext(String buchungstext) throws RemoteException
  {
    setAttribute("buchungstext", buchungstext);
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

  public Date getAusfuehrung() throws RemoteException
  {
    return (Date) getAttribute("ausfuehrung");
  }

  public void setAusfuehrung(Date ausfuehrung) throws RemoteException
  {
    setAttribute("ausfuehrung", ausfuehrung);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
