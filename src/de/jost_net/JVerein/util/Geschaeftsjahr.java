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
 * Revision 1.1  2008/05/25 19:37:40  jost
 * Neu
 *
 **********************************************************************/
package de.jost_net.JVerein.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;

public class Geschaeftsjahr
{
  private Date beginnGeschaeftsjahr;

  private int beginnGeschaeftsjahrjahr;

  private Date endeGeschaeftsjahr;

  public Geschaeftsjahr(String tagmonat, int jahr) throws ParseException
  {
    beginnGeschaeftsjahr = Datum.toDate(Einstellungen.getBeginnGeschaeftsjahr()
        + jahr);
    Calendar cal = Calendar.getInstance();
    cal.setTime(beginnGeschaeftsjahr);
    beginnGeschaeftsjahrjahr = cal.get(Calendar.YEAR);
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

  public String toString()
  {
    return Einstellungen.DATEFORMAT.format(beginnGeschaeftsjahr) + " - "
        + Einstellungen.DATEFORMAT.format(endeGeschaeftsjahr);
  }
}
