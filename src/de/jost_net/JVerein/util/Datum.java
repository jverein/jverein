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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Altermodel;

public class Datum
{

  /**
   * Berechnung der Differenz in Monaten zwischen 2 Daten. Die Tagesangaben
   * bleiben unberücksichtigt.
   * 
   * @param datum1
   *          von-Datum
   * @param datum2
   *          bis-Datum
   * @return Anzahl Monate
   */
  public static int getDifferenzInMonaten(Date datum1, Date datum2)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(datum2);
    int diff = cal.get(Calendar.YEAR) * 12;
    diff = diff + cal.get(Calendar.MONTH);
    cal.setTime(datum1);
    diff = diff - (cal.get(Calendar.YEAR) * 12);
    diff = diff - cal.get(Calendar.MONTH);
    return diff;
  }

  public static boolean isImInterval(Date datum1, Date datum2, int intervall)
  {
    if (intervall == 0)
    {
      return true;
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(datum1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(datum2);
    if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH))
    {
      return false;
    }
    return getDifferenzInMonaten(datum1, datum2) % intervall == 0;
  }

  public static Date addInterval(Date letztefaelligkeit, int intervall)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(letztefaelligkeit);
    cal.add(Calendar.MONTH, intervall);
    return cal.getTime();
  }

  public static Date subtractInterval(Date letztefaelligkeit, int intervall,
      Date startdatum)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(letztefaelligkeit);
    cal.add(Calendar.MONTH, intervall * -1);
    if (cal.getTime().getTime() < startdatum.getTime())
    {
      return null;
    }
    else
    {
      return cal.getTime();
    }
  }

  public static Date addTage(Date datum, int tage)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(datum);
    cal.add(Calendar.DAY_OF_MONTH, tage);
    return cal.getTime();
  }

  public static Date toDate(String value) throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    return sdf.parse(value);
  }

  /**
   * Gibt das heutige Datum zurück. (Ohne Zeitanteil)
   */
  public final static Date getHeute()
  {
    return getDatumOhneZeit(new Date());
  }

  public final static Date getDatumOhneZeit(Date datum)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(datum);
    cal.set(Calendar.HOUR, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  /**
   * Gibt einen Leerstring aus, falls der Text null ist.
   * 
   * @return der Text oder Leerstring - niemals null.
   */
  public final static String formatDate(Date d)
  {
    return d == null ? "" : new JVDateFormatTTMMJJJJ().format(d);
  }

  public final static Integer getAlter(Date geburtstag, int altersModel)
  {
    if (Altermodel.JAHRES_ENDE == altersModel)
      return getAlter(geburtstag, getJahresEnde());
    if (Altermodel.JAHRES_START == altersModel)
      return getAlter(geburtstag, getJahresStart());
    return getAlter(geburtstag);
  }

  private static Date getJahresStart()
  {
    Calendar datum = Calendar.getInstance();
    int jahr = datum.get(Calendar.YEAR);
    datum.clear();
    datum.set(jahr, Calendar.JANUARY, 1);
    return datum.getTime();
  }

  private static Date getJahresEnde()
  {
    Calendar datum = Calendar.getInstance();
    int jahr = datum.get(Calendar.YEAR);
    datum.clear();
    datum.set(jahr, Calendar.DECEMBER, 31);
    return datum.getTime();
  }

  public final static Integer getAlter(Date geburtstag)
  {
    return getAlter(geburtstag, new Date());
  }

  /**
   * Gibt das Alter zum übergebenen Date zurück. oder null falls kein Geburtstag
   * übergeben wurde oder das Alter < 1 ist.
   * 
   * @param geburtstag
   * @return alter
   */
  public final static Integer getAlter(Date geburtstag, Date referenzDatum)
  {
    if (null == geburtstag)
      return null;
    if (Einstellungen.NODATE == geburtstag)
      return null;
    Calendar heute = Calendar.getInstance();
    heute.setTime(referenzDatum);
    Calendar birthDate = Calendar.getInstance();
    birthDate.setTime(geburtstag);
    int alter = heute.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
    if (heute.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)
        || (heute.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && heute
            .get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)))
    {
      --alter;
    }
    if (alter < 1)
      return null;
    return new Integer(alter);
  }
}
