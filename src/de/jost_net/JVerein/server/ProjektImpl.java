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
import de.jost_net.JVerein.rmi.Projekt;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ProjektImpl extends AbstractDBObject implements Projekt
{

  private static final long serialVersionUID = 1L;

  private static final String STARTDATUM = "startdatum";
  private static final String ENDEDATUM = "endedatum";

  public ProjektImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "projekt";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bezeichnung";
  }

  @Override
  protected void deleteCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      plausi();
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of projekt failed", e);
      throw new ApplicationException(
          "Projekt kann nicht gespeichert werden. Siehe system log");
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getBezeichnung() == null)
    {
      throw new ApplicationException("Bitte Bezeichnung eingeben");
    }

    if ( isStartDatumGesetzt() && isEndeDatumGesetzt() && getEndeDatum().before( getStartDatum() ) )
    {
      throw new ApplicationException( "Endedatum muss nach dem Startdatum liegen" );
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    return null;
  }

  @Override
  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  @Override
  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  @Override
  public Date getStartDatum() throws RemoteException {
    Date d = (Date) getAttribute(STARTDATUM);
    if ( d == null ) {
        return Einstellungen.NODATE;
    }

    return d;
  }

  @Override
  public void setStartDatum( Date startDatum ) throws RemoteException
  {
    setAttribute( STARTDATUM, startDatum );
  }

  @Override
  public void setStartDatum( String startDatum ) throws RemoteException
  {
    setAttribute( STARTDATUM, toDate(startDatum) );
  }

  @Override
  public Date getEndeDatum() throws RemoteException
  {
    Date d = (Date) getAttribute(ENDEDATUM);
    if( d == null ) {
        return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setEndeDatum( Date endeDatum ) throws RemoteException
  {
    setAttribute(ENDEDATUM, endeDatum);
  }

  @Override
  public void setEndeDatum(String endeDatum) throws RemoteException
  {
    setAttribute(ENDEDATUM, toDate(endeDatum));
  }

  @Override
  public boolean isStartDatumGesetzt() throws RemoteException {
      return getAttribute( STARTDATUM ) != null;
  }

  @Override
  public boolean isEndeDatumGesetzt() throws RemoteException {
      return getAttribute( ENDEDATUM ) != null;
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  private Date toDate(String datum)
  {
    Date d = null;

    try
    {
      d = new JVDateFormatTTMMJJJJ().parse(datum);
    }
    catch (Exception e)
    {
      //
    }
    return d;
  }

}
