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
 * Schlüssel Zahlungsrhythmus
 */
public class Zahlungsrhythmus
{

  public static final int JAEHRLICH = 12;

  public static final int HALBJAEHRLICH = 6;

  public static final int VIERTELJAEHRLICH = 3;

  public static final int MONATLICH = 1;

  private int zahlungsrhytmus;

  public Zahlungsrhythmus(int key)
  {
    this.zahlungsrhytmus = key;
  }

  public int getKey()
  {
    return zahlungsrhytmus;
  }

  public String getText()
  {
    return get(zahlungsrhytmus);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case Zahlungsrhythmus.JAEHRLICH:
        return "jährlich";
      case Zahlungsrhythmus.HALBJAEHRLICH:
        return "halbjährlich";
      case Zahlungsrhythmus.VIERTELJAEHRLICH:
        return "vierteljährlich";
      case Zahlungsrhythmus.MONATLICH:
        return "monatlich";
      default:
        return null;
    }
  }

  public static ArrayList<Zahlungsrhythmus> getArray()
  {
    ArrayList<Zahlungsrhythmus> ret = new ArrayList<Zahlungsrhythmus>();
    ret.add(new Zahlungsrhythmus(JAEHRLICH));
    ret.add(new Zahlungsrhythmus(HALBJAEHRLICH));
    ret.add(new Zahlungsrhythmus(VIERTELJAEHRLICH));
    ret.add(new Zahlungsrhythmus(MONATLICH));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Zahlungsrhythmus)
    {
      Zahlungsrhythmus v = (Zahlungsrhythmus) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return zahlungsrhytmus;
  }

  @Override
  public String toString()
  {
    return get(zahlungsrhytmus);
  }
}
