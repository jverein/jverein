/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
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
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AnfangsbestandImpl extends AbstractDBObject
    implements Anfangsbestand
{

  private static final long serialVersionUID = 1L;

  public AnfangsbestandImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "anfangsbestand";
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

  @Override
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
      Date beginngeschaeftsjahr = new JVDateFormatTTMMJJJJ().parse(
          Einstellungen.getEinstellung().getBeginnGeschaeftsjahr() + "2009");
      DBIterator<Anfangsbestand> it = Einstellungen.getDBService()
          .createList(Anfangsbestand.class);
      it.addFilter("konto = ?", new Object[] { getKonto().getID() });
      it.addFilter("datum >= ?", new Object[] { getDatum() });
      it.setOrder("order by datum desc");
      if (it.size() > 0)
      {
        Anfangsbestand anf =  it.next();
        throw new ApplicationException(
            String.format("Datum muss nach dem %s liegen",
                new JVDateFormatTTMMJJJJ().format(anf.getDatum())));
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

  @Override
  protected Class<?> getForeignObject(String field)
  {
    if ("konto".equals(field))
    {
      return Konto.class;
    }
    return null;
  }

  @Override
  public Konto getKonto() throws RemoteException
  {
    return (Konto) getAttribute("konto");
  }

  @Override
  public String getKontoText() throws RemoteException
  {
    return (String) getAttribute("kontotext");
  }

  @Override
  public void setKonto(Konto konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  @Override
  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  @Override
  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  @Override
  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
    {
      return 0;
    }
    return d.doubleValue();
  }

  @Override
  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag", Double.valueOf(d));
  }

  @Override
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

  @Override
  public Jahresabschluss getJahresabschluss() throws RemoteException
  {
    DBIterator<Jahresabschluss> it = Einstellungen.getDBService()
        .createList(Jahresabschluss.class);
    it.addFilter("von <= ?", new Object[] { getDatum() });
    it.addFilter("bis >= ?", new Object[] { getDatum() });
    if (it.hasNext())
    {
      Jahresabschluss ja = it.next();
      return ja;
    }
    return null;
  }

}
