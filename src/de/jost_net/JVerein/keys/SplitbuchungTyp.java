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
 * Typ der Splitbuchung
 */
public class SplitbuchungTyp
{
  public static final int HAUPT = 1;

  public static final int GEGEN = 2;

  public static final int SPLIT = 3;

  private int typ;

  public SplitbuchungTyp(int key)
  {
    this.typ = key;
  }

  public int getKey()
  {
    return typ;
  }

  public String getText()
  {
    return get(typ);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case HAUPT:
        return "Haupt";
      case GEGEN:
        return "Gegen";
      case SPLIT:
        return "Split";
      default:
        return null;
    }
  }

  public static ArrayList<SplitbuchungTyp> getArray()
  {
    ArrayList<SplitbuchungTyp> ret = new ArrayList<>();
    ret.add(new SplitbuchungTyp(HAUPT));
    ret.add(new SplitbuchungTyp(GEGEN));
    ret.add(new SplitbuchungTyp(SPLIT));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SplitbuchungTyp)
    {
      SplitbuchungTyp v = (SplitbuchungTyp) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return typ;
  }

  @Override
  public String toString()
  {
    return get(typ);
  }
}
