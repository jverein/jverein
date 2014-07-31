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
package de.jost_net.JVerein.util;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;

public class Geschaeftsjahr
{
  private Date beginnGeschaeftsjahr;

  private int beginnGeschaeftsjahrjahr;

  private Date endeGeschaeftsjahr;

  public Geschaeftsjahr(int jahr) throws ParseException, RemoteException
  {
    beginnGeschaeftsjahr = Datum.toDate(Einstellungen.getEinstellung()
        .getBeginnGeschaeftsjahr() + jahr);
    Calendar cal = Calendar.getInstance();
    cal.setTime(beginnGeschaeftsjahr);
    beginnGeschaeftsjahrjahr = cal.get(Calendar.YEAR);
    cal.add(Calendar.YEAR, 1);
    cal.add(Calendar.DAY_OF_MONTH, -1);
    endeGeschaeftsjahr = cal.getTime();
  }

  /**
   * Geschäftsjahr zu einem vorgegebenen Datum ermitteln
   * 
   * @throws RemoteException
   */
  public Geschaeftsjahr(Date datum) throws ParseException, RemoteException
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(datum);
    beginnGeschaeftsjahr = Datum.toDate(Einstellungen.getEinstellung()
        .getBeginnGeschaeftsjahr() + cal.get(Calendar.YEAR));
    if (datum.before(beginnGeschaeftsjahr))
    {
      cal.add(Calendar.YEAR, -1);
      beginnGeschaeftsjahr = Datum.toDate(Einstellungen.getEinstellung()
          .getBeginnGeschaeftsjahr() + cal.get(Calendar.YEAR));
    }
    cal.setTime(beginnGeschaeftsjahr);
    cal.add(Calendar.YEAR, 1);
    cal.add(Calendar.DAY_OF_MONTH, -1);
    endeGeschaeftsjahr = cal.getTime();
  }

  public Date getBeginnGeschaeftsjahr()
  {
    return beginnGeschaeftsjahr;
  }

  public int getBeginnGeschaeftsjahrjahr()
  {
    return beginnGeschaeftsjahrjahr;
  }

  public Date getEndeGeschaeftsjahr()
  {
    return endeGeschaeftsjahr;
  }

  @Override
  public String toString()
  {
    return new JVDateFormatTTMMJJJJ().format(beginnGeschaeftsjahr) + " - "
        + new JVDateFormatTTMMJJJJ().format(endeGeschaeftsjahr);
  }
}
