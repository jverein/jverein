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
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

/**
 * Schlüssel Einstellung.SEPAMandatIDSource
 * Diese Klasse hilft beim Einstellen wie die MandatsID für SEPA Lastschriften
 * erzeugt werden soll
 */
public class SepaMandatIdSource
{

  public static final int DBID = 1;

  public static final int EXTERNE_MITGLIEDSNUMMER = 2;

  private int mandatIDSource;

  public SepaMandatIdSource(int key)
  {
    this.mandatIDSource = key;
  }

  public int getKey()
  {
    return mandatIDSource;
  }

  public String getText()
  {
    return get(mandatIDSource);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case SepaMandatIdSource.DBID:
        return "Mitgliedsnummer (default)";
      case SepaMandatIdSource.EXTERNE_MITGLIEDSNUMMER:
        return "externe Mitgliedsnummer";
      default:
        return null;
    }
  }

  public static ArrayList<SepaMandatIdSource> getArray()
  {
    ArrayList<SepaMandatIdSource> ret = new ArrayList<SepaMandatIdSource>();
    ret.add(new SepaMandatIdSource(DBID));
    ret.add(new SepaMandatIdSource(EXTERNE_MITGLIEDSNUMMER));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SepaMandatIdSource)
    {
      SepaMandatIdSource v = (SepaMandatIdSource) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return mandatIDSource;
  }

  @Override
  public String toString()
  {
    return get(mandatIDSource);
  }
}
