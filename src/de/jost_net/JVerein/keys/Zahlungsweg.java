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
 * Schlüssel Zahlungsweg
 */
public class Zahlungsweg
{

  public static final int BASISLASTSCHRIFT = 1;

  public static final int ÜBERWEISUNG = 2;

  public static final int BARZAHLUNG = 3;

  private int zahlungsweg;

  public Zahlungsweg(int key)
  {
    this.zahlungsweg = key;
  }

  public int getKey()
  {
    return zahlungsweg;
  }

  public String getText()
  {
    return get(zahlungsweg);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case BASISLASTSCHRIFT:
        return "Basislastschrift";
      case ÜBERWEISUNG:
        return "Überweisung";
      case BARZAHLUNG:
        return "Barzahlung";
      default:
        return null;
    }
  }

  public static ArrayList<Zahlungsweg> getArray()
  {
    ArrayList<Zahlungsweg> ret = new ArrayList<Zahlungsweg>();
    ret.add(new Zahlungsweg(BASISLASTSCHRIFT));
    ret.add(new Zahlungsweg(ÜBERWEISUNG));
    ret.add(new Zahlungsweg(BARZAHLUNG));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Zahlungsweg)
    {
      Zahlungsweg v = (Zahlungsweg) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return zahlungsweg;
  }

  @Override
  public String toString()
  {
    return get(zahlungsweg);
  }
}
