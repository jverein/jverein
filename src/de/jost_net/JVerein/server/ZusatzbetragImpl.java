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
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.util.Datum;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ZusatzbetragImpl extends AbstractDBObject implements Zusatzbetrag
{

  private static final long serialVersionUID = 1L;

  public ZusatzbetragImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "zusatzabbuchung";
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
      if (getStartdatum() == null)
      {
        throw new ApplicationException("Bitte Startdatum eingeben");
      }
      if (getFaelligkeit() == null)
      {
        throw new ApplicationException("Bitte nächste Fälligkeit eingeben");
      }
      if (getIntervall() == null)
      {
        throw new ApplicationException("Bitte Intervall eingeben");
      }
      if (getBuchungstext() == null || getBuchungstext().length() == 0)
      {
        throw new ApplicationException("Bitte Buchungstext eingeben");
      }
      if (getEndedatum() != null)
      {
        if (!Datum
            .isImInterval(getStartdatum(), getEndedatum(), getIntervall()))
        {
          throw new ApplicationException("Endedatum liegt nicht im Intervall");
        }
      }
      if (getFaelligkeit().getTime() < getStartdatum().getTime())
      {
        throw new ApplicationException(
            "Das Fälligkeitsdatum darf nicht vor dem Startdatum liegen");
      }
      if (!Datum
          .isImInterval(getStartdatum(), getFaelligkeit(), getIntervall()))
      {
        throw new ApplicationException(
            "Nächste Fälligkeit liegt nicht im Intervall");
      }
      if (getBetrag() <= 0)
      {
        throw new ApplicationException("Betrag nicht gültig");
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Zusatzbetrag kann nicht gespeichert werden. Siehe system log";
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
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    return null;
  }

  @Override
  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  @Override
  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", Integer.valueOf(mitglied));
  }

  @Override
  public Date getFaelligkeit() throws RemoteException
  {
    return (Date) getAttribute("faelligkeit");
  }

  @Override
  public void setFaelligkeit(Date faelligkeit) throws RemoteException
  {
    setAttribute("faelligkeit", faelligkeit);
  }

  @Override
  public String getBuchungstext() throws RemoteException
  {
    return (String) getAttribute("buchungstext");
  }

  @Override
  public void setBuchungstext(String buchungstext) throws RemoteException
  {
    setAttribute("buchungstext", buchungstext);
  }

  @Override
  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
      return 0;
    return d.doubleValue();
  }

  @Override
  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag", new Double(d));
  }

  @Override
  public Date getAusfuehrung() throws RemoteException
  {
    return (Date) getAttribute("ausfuehrung");
  }

  @Override
  public Date getStartdatum() throws RemoteException
  {
    return (Date) getAttribute("startdatum");
  }

  @Override
  public void setStartdatum(Date value) throws RemoteException
  {
    setAttribute("startdatum", value);
  }

  @Override
  public Integer getIntervall() throws RemoteException
  {
    return (Integer) getAttribute("intervall");
  }

  @Override
  public String getIntervallText() throws RemoteException
  {
    return IntervallZusatzzahlung.get(getIntervall());
  }

  @Override
  public void setIntervall(Integer value) throws RemoteException
  {
    setAttribute("intervall", value);
  }

  @Override
  public Date getEndedatum() throws RemoteException
  {
    return (Date) getAttribute("endedatum");
  }

  @Override
  public void setEndedatum(Date value) throws RemoteException
  {
    setAttribute("endedatum", value);
  }

  @Override
  public void setAusfuehrung(Date ausfuehrung) throws RemoteException
  {
    setAttribute("ausfuehrung", ausfuehrung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("intervalltext"))
    {
      return getIntervallText();
    }
    return super.getAttribute(fieldName);
  }

  @Override
  public boolean isAktiv(Date datum) throws RemoteException
  {
    if (!getMitglied().isAngemeldet(datum))
    {
      if (!Einstellungen.getEinstellung().getZusatzbetragAusgetretene())
      {
        return false;
      }
    }
    // Einmalige Ausführung
    if (getIntervall().intValue() == IntervallZusatzzahlung.KEIN)
    {
      // Ist das Ausführungsdatum gesetzt?
      if (getAusfuehrung() == null)
      {
        if (getFaelligkeit().getTime() <= datum.getTime())
        {
          return true;
        }
        else
        {
          return false;
        }
      }
      else
      {
        // ja: nicht mehr ausführen
        return false;
      }
    }

    // Wenn das Endedatum gesetzt ist und das Ausführungsdatum liegt hinter
    // dem Endedatum: nicht mehr ausführen
    if (getEndedatum() != null && datum.getTime() >= getEndedatum().getTime())
    {
      return false;
    }
    if (getFaelligkeit().getTime() > datum.getTime())
    {
      return false;
    }
    return true;
  }

  @Override
  public void naechsteFaelligkeit() throws RemoteException
  {
    Date vorh = Datum.addInterval(getFaelligkeit(), getIntervall());
    if (vorh == null)
    {
      throw new RemoteException("Datum kann nicht weiter vorgesetzt werden");
    }
    else
    {
      setFaelligkeit(vorh);
    }

  }

  @Override
  public void vorherigeFaelligkeit() throws RemoteException
  {
    Date vorh = Datum.subtractInterval(getFaelligkeit(), getIntervall(),
        getStartdatum());
    if (vorh == null)
    {
      throw new RemoteException("Datum kann nicht weiter zurückgesetzt werden");
    }
    else
    {
      setFaelligkeit(vorh);
    }
  }
}
