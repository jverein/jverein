/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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
 * Art der Beitragsart
 */
public class ArtBeitragsart
{
  public static final int NORMAL = 0;

  public static final int FAMILIE_ZAHLER = 1;

  public static final int FAMILIE_ANGEHOERIGER = 2;

  private int art;

  public ArtBeitragsart(int key)
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
      case NORMAL:
        return "Normal";
      case FAMILIE_ZAHLER:
        return "Familie: Zahler";
      case FAMILIE_ANGEHOERIGER:
        return "Familie: Angehöriger";
      default:
        return null;
    }
  }

  public static ArrayList<ArtBeitragsart> getArray()
  {
    ArrayList<ArtBeitragsart> ret = new ArrayList<ArtBeitragsart>();
    ret.add(new ArtBeitragsart(NORMAL));
    ret.add(new ArtBeitragsart(FAMILIE_ZAHLER));
    ret.add(new ArtBeitragsart(FAMILIE_ANGEHOERIGER));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ArtBeitragsart)
    {
      ArtBeitragsart v = (ArtBeitragsart) obj;
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
