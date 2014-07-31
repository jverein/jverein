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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;

public class ZusatzfelderImpl extends AbstractDBObject implements Zusatzfelder
{

  private static final long serialVersionUID = 1L;

  public ZusatzfelderImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "zusatzfelder";
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
  protected void insertCheck()
  {
    // try
    // {
    // //
    // }
    // catch (RemoteException e)
    // {
    // String fehler = "Zusatzfeld kann nicht gespeichert werden. Siehe system
    // log";
    // Logger.error(fehler, e);
    // throw new ApplicationException(fehler);
    // }
  }

  @Override
  protected void updateCheck()
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
    if ("felddefinition".equals(arg0))
    {
      return Felddefinition.class;
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
  public Felddefinition getFelddefinition() throws RemoteException
  {
    return (Felddefinition) getAttribute("felddefinition");
  }

  @Override
  public void setFelddefinition(int felddefinition) throws RemoteException
  {
    setAttribute("felddefinition", Integer.valueOf(felddefinition));
  }

  @Override
  public String getFeld() throws RemoteException
  {
    return (String) getAttribute("feld");
  }

  @Override
  public void setFeld(String feld) throws RemoteException
  {
    setAttribute("feld", feld);
  }

  @Override
  public Date getFeldDatum() throws RemoteException
  {
    return (Date) getAttribute("felddatum");
  }

  @Override
  public void setFeldDatum(Date datum) throws RemoteException
  {
    setAttribute("felddatum", datum);
  }

  @Override
  public Integer getFeldGanzzahl() throws RemoteException
  {
    return (Integer) getAttribute("feldganzzahl");
  }

  @Override
  public void setFeldGanzzahl(Integer ganzzahl) throws RemoteException
  {
    setAttribute("feldganzzahl", ganzzahl);
  }

  @Override
  public double getFeldGleitkommazahl() throws RemoteException
  {
    return (Double) getAttribute("feldgleitkommazahl");
  }

  @Override
  public void setFeldGleitkommazahl(double gleitkommazahl)
      throws RemoteException
  {
    setAttribute("feldgleitkommazahl", gleitkommazahl);
  }

  @Override
  public BigDecimal getFeldWaehrung() throws RemoteException
  {
    return (BigDecimal) getAttribute("feldwaehrung");
  }

  @Override
  public void setFeldWaehrung(BigDecimal waehrung) throws RemoteException
  {
    setAttribute("feldwaehrung", waehrung);
  }

  @Override
  public Boolean getFeldJaNein() throws RemoteException
  {
    return Util.getBoolean(getAttribute("feldjanein"));
  }

  @Override
  public void setFeldJaNein(Boolean janein) throws RemoteException
  {
    setAttribute("feldjanein", janein);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  @Override
  public String getString()
  {
    try
    {
      int typ = getFelddefinition().getDatentyp();
      switch (typ)
      {
        case Datentyp.DATUM:
          if (getFeldDatum() != null)
          {
            return new JVDateFormatTTMMJJJJ().format(getFeldDatum());
          }
          else
          {
            return "";
          }
        case Datentyp.GANZZAHL:
          return getFeldGanzzahl() + "";
        case Datentyp.JANEIN:
          return getFeldJaNein() ? "ja"
              : "nein";
        case Datentyp.WAEHRUNG:
          if (getFeldWaehrung() != null)
          {
            return Einstellungen.DECIMALFORMAT.format(getFeldWaehrung());
          }
          else
          {
            return "";
          }
        case Datentyp.ZEICHENFOLGE:
          return getFeld();
        default:
          return "ungültiger Datentyp";
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      return e.getMessage();
    }
  }
}
