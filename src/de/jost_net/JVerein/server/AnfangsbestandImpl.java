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
 * Revision 1.3  2008/06/28 17:06:45  jost
 * Bearbeiten nur, wenn kein Jahresabschluss vorliegt.
 *
 * Revision 1.2  2008/05/24 14:19:52  jost
 * Debug-Infos entfernt.
 *
 * Revision 1.1  2008/05/22 06:55:50  jost
 * BuchfÃ¼hrung
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AnfangsbestandImpl extends AbstractDBObject implements
    Anfangsbestand
{
  private static final long serialVersionUID = 1L;

  public AnfangsbestandImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "anfangsbestand";
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
      checkDate1();
      checkDateInsert();
    }
    catch (RemoteException e)
    {
      String msg = "Anfangsbestand kann nicht gespeichert werden. Siehe system log";
      Logger.error(msg, e);
      throw new ApplicationException(msg);
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    try
    {
      checkDate1();
    }
    catch (RemoteException e)
    {
      String msg = "Anfangsbestand kann nicht gespeichert werden. Siehe system log";
      Logger.error(msg, e);
      throw new ApplicationException(msg);
    }
  }

  private void checkDate1() throws RemoteException, ApplicationException
  {
    if (getDatum() == null)
    {
      throw new ApplicationException("Bitte Datum eingeben");
    }
    if (getDatum().after(new Date()))
    {
      throw new ApplicationException("Keine Anfangsbestände in der Zukunft");
    }
    Jahresabschluss ja = getJahresabschluss();
    if (ja != null)
    {
      throw new ApplicationException("Der Zeitraum ist bereits abgeschlossen.");
    }
  }

  private void checkDateInsert() throws RemoteException, ApplicationException
  {
    try
    {
      Date beginngeschaeftsjahr = Einstellungen.DATEFORMAT.parse(Einstellungen
          .getEinstellung().getBeginnGeschaeftsjahr()
          + "2009");
      DBIterator it = Einstellungen.getDBService().createList(
          Anfangsbestand.class);
      it.addFilter("konto = ?", new Object[] { getKonto().getID() });
      it.addFilter("datum >= ?", new Object[] { getDatum() });
      it.setOrder("order by datum desc");
      if (it.size() > 0)
      {
        Anfangsbestand anf = (Anfangsbestand) it.next();
        throw new ApplicationException("Datum muss nach dem "
            + Einstellungen.DATEFORMAT.format(anf.getDatum()) + " liegen");
      }
      it = Einstellungen.getDBService().createList(Anfangsbestand.class);
      it.addFilter("konto = ?", new Object[] { getKonto().getID() });
      if (it.size() == 0)
      {
        return;
      }
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(beginngeschaeftsjahr);
      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(getDatum());
      if (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
          && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
      {
        return;
      }
      throw new ApplicationException(
          "Tag und Monat müssen mit dem Beginn des Geschäftsjahres übereinstimmen.");
    }
    catch (ParseException e)
    {
      throw new ApplicationException(
          "Beginn des Geschäftsjahres ist in den Einstellungen nicht gesetzt.");
    }
  }

  protected Class getForeignObject(String field) throws RemoteException
  {
    if ("konto".equals(field))
    {
      return Konto.class;
    }
    return null;
  }

  public Konto getKonto() throws RemoteException
  {
    return (Konto) getAttribute("konto");
  }

  public String getKontoText() throws RemoteException
  {
    return (String) getAttribute("kontotext");
  }

  public void setKonto(Konto konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
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
    if (fieldName.equals("kontotext"))
    {
      return getKonto().getNummer() + " " + getKonto().getBezeichnung();
    }
    else
    {
      return super.getAttribute(fieldName);
    }
  }

  public Jahresabschluss getJahresabschluss() throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(
        Jahresabschluss.class);
    it.addFilter("von <= ?", new Object[] { (Date) getDatum() });
    it.addFilter("bis >= ?", new Object[] { (Date) getDatum() });
    if (it.hasNext())
    {
      Jahresabschluss ja = (Jahresabschluss) it.next();
      return ja;
    }
    return null;
  }

}
