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
 **********************************************************************/
package de.jost_net.JVerein.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;

public class Geschaeftsjahr
{
  private Date beginnGeschaeftsjahr;

  private Date endeGeschaeftsjahr;

  public Geschaeftsjahr(String tagmonat, int jahr) throws ParseException
  {
    beginnGeschaeftsjahr = Datum.toDate(Einstellungen.getBeginnGeschaeftsjahr()
        + jahr);
    Calendar cal = Calendar.getInstance();
    cal.setTime(beginnGeschaeftsjahr);
    cal.add(Calendar.YEAR, 1);
    cal.add(Calendar.DAY_OF_MONTH, -1);
    endeGeschaeftsjahr = cal.getTime();
  }

  public Date getBeginnGeschaeftsjahr()
  {
    return beginnGeschaeftsjahr;
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
