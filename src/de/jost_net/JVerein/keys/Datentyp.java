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
 * Datentyp
 */
public class Datentyp
{

  public static final int ZEICHENFOLGE = 1;

  public static final int DATUM = 2;

  public static final int GANZZAHL = 3;

  public static final int WAEHRUNG = 5;

  public static final int JANEIN = 6;

  private int datentyp;

  public Datentyp(int datentyp)
  {
    this.datentyp = datentyp;
  }

  public int getKey()
  {
    return datentyp;
  }

  public String getText()
  {
    return get(datentyp);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case ZEICHENFOLGE:
        return "Zeichenfolge";
      case DATUM:
        return "Datum";
      case GANZZAHL:
        return "Ganzzahl";
      case WAEHRUNG:
        return "Währung";
      case JANEIN:
        return "Ja/Nein";
      default:
        return null;
    }
  }

  public static ArrayList<Datentyp> getArray()
  {
    ArrayList<Datentyp> ret = new ArrayList<>();
    ret.add(new Datentyp(ZEICHENFOLGE));
    ret.add(new Datentyp(DATUM));
    ret.add(new Datentyp(GANZZAHL));
    ret.add(new Datentyp(WAEHRUNG));
    ret.add(new Datentyp(JANEIN));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Datentyp)
    {
      Datentyp v = (Datentyp) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return datentyp;
  }

  @Override
  public String toString()
  {
    return get(datentyp);
  }
}
