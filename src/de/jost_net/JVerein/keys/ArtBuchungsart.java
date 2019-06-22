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
 * Art der Buchungsart
 */
public class ArtBuchungsart
{
  public static final int EINNAHME = 0;

  public static final int AUSGABE = 1;

  public static final int UMBUCHUNG = 2;

  private int art;

  public ArtBuchungsart(int key)
  {
    this.art = key;
  }

  public int getKey()
  {
    return art;
  }

  public String getText()
  {
    return get(art);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case EINNAHME:
        return "Einnahme";
      case AUSGABE:
        return "Ausgabe";
      case UMBUCHUNG:
        return "Umbuchung";
      default:
        return null;
    }
  }

  public static ArrayList<ArtBuchungsart> getArray()
  {
    ArrayList<ArtBuchungsart> ret = new ArrayList<>();
    ret.add(new ArtBuchungsart(EINNAHME));
    ret.add(new ArtBuchungsart(AUSGABE));
    ret.add(new ArtBuchungsart(UMBUCHUNG));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ArtBuchungsart)
    {
      ArtBuchungsart v = (ArtBuchungsart) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return art;
  }

  @Override
  public String toString()
  {
    return get(art);
  }
}
