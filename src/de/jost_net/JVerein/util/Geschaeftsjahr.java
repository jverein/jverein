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
 * Revision 1.6  2011-02-12 09:44:07  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.5  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.4  2008-11-16 16:59:30  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.3  2008/06/28 17:08:12  jost
 * refactoring
 *
 * Revision 1.2  2008/05/26 18:59:31  jost
 * neue Methode
 *
 * Revision 1.1  2008/05/25 19:37:40  jost
 * Neu
 *
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
