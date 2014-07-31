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
import de.jost_net.JVerein.keys.ArbeitsstundenModel;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class ArbeitseinsatzImpl extends AbstractDBObject implements
    Arbeitseinsatz
{

  private static final long serialVersionUID = 380278347818535726L;

  public ArbeitseinsatzImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "arbeitseinsatz";
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
    updateCheck();
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    try
    {
      if (getStunden() == null)
      {
        throw new ApplicationException("Keine Stunden angegeben");
      }
      if (getStunden() <= 0d)
      {
          if ( Einstellungen.getEinstellung().getArbeitsstundenmodel() == ArbeitsstundenModel.STANDARD )
          {
              throw new ApplicationException("Bitte mehr als 0 Stunden eingeben oder Arbeitsstundenmodel in Einstellung ändern");
          }
      }
      if (getDatum() == null)
      {
        throw new ApplicationException("Bitte Datum erfassen");
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
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
  public void setStunden(Double stunden) throws RemoteException
  {
    setAttribute("stunden", stunden);
  }

  @Override
  public Double getStunden() throws RemoteException
  {
    Double d = (Double) getAttribute("stunden");
    if (d == null)
    {
      return 0d;
    }
    return d.doubleValue();
  }

  @Override
  public String getBemerkung() throws RemoteException
  {
    return (String) getAttribute("bemerkung");
  }

  @Override
  public void setBemerkung(String bemerkung) throws RemoteException
  {
    setAttribute("bemerkung", bemerkung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
