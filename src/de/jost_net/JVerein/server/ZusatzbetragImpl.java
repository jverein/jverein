/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

import de.jost_net.JVerein.JVereinPlugin;
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
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Startdatum eingeben"));
      }
      if (getFaelligkeit() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte nächste Fälligkeit eingeben"));
      }
      if (getIntervall() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Intervall eingeben"));
      }
      if (getBuchungstext() == null || getBuchungstext().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Buchungstext eingeben"));
      }
      if (getEndedatum() != null)
      {
        if (!Datum
            .isImInterval(getStartdatum(), getEndedatum(), getIntervall()))
        {
          throw new ApplicationException(JVereinPlugin.getI18n().tr(
              "Endedatum liegt nicht im Intervall"));
        }
      }
      if (getFaelligkeit().getTime() < getStartdatum().getTime())
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Das Fälligkeitsdatum darf nicht vor dem Startdatum liegen"));
      }
      if (!Datum
          .isImInterval(getStartdatum(), getFaelligkeit(), getIntervall()))
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Nächste Fälligkeit liegt nicht im Intervall"));
      }
      if (getBetrag() <= 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Betrag nicht gültig"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Zusatzbetrag kann nicht gespeichert werden. Siehe system log");
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

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", Integer.valueOf(mitglied));
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

  public String getBuchungstext2() throws RemoteException
  {
    return (String) getAttribute("buchungstext2");
  }

  public void setBuchungstext2(String buchungstext2) throws RemoteException
  {
    setAttribute("buchungstext2", buchungstext2);
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

  public Date getStartdatum() throws RemoteException
  {
    return (Date) getAttribute("startdatum");
  }

  public void setStartdatum(Date value) throws RemoteException
  {
    setAttribute("startdatum", value);
  }

  public Integer getIntervall() throws RemoteException
  {
    return (Integer) getAttribute("intervall");
  }

  public String getIntervallText() throws RemoteException
  {
    return IntervallZusatzzahlung.get(getIntervall());
  }

  public void setIntervall(Integer value) throws RemoteException
  {
    setAttribute("intervall", value);
  }

  public Date getEndedatum() throws RemoteException
  {
    return (Date) getAttribute("endedatum");
  }

  public void setEndedatum(Date value) throws RemoteException
  {
    setAttribute("endedatum", value);
  }

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
    if (fieldName.equals("aktiv"))
    {
      return isAktiv();
    }
    return super.getAttribute(fieldName);
  }

  public boolean isAktiv() throws RemoteException
  {
    // Wenn der Auftrag noch nie ausgeführt wurde, ist er auszuführen
    // if (getAusfuehrung() == null)
    // {
    // return true;
    // }
    // Einmalige Ausführung
    if (getIntervall().intValue() == IntervallZusatzzahlung.KEIN)
    {
      // Ist das Ausführungsdatum gesetzt?
      if (getAusfuehrung() == null)
      {
        if (getFaelligkeit().getTime() <= Datum.getHeute().getTime())
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
    if (getEndedatum() != null && getAusfuehrung() != null
        && getAusfuehrung().getTime() >= getEndedatum().getTime())
    {
      return false;
    }
    if (getFaelligkeit().getTime() <= Datum.getHeute().getTime())
    {
      return true;
    }

    return false;
  }
}
