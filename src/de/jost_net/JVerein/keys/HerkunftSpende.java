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
 * Herkunft Spende
 */
public class HerkunftSpende
{

  public static final int BETRIEBSVERMOEGEN = 1;

  public static final int PRIVATVERMOEGEN = 2;

  public static final int KEINEANGABEN = 3;

  private int art;

  public HerkunftSpende(int key)
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
      case BETRIEBSVERMOEGEN:
        return "Betriebsvermögen";
      case PRIVATVERMOEGEN:
        return "Privatvermögen";
      case KEINEANGABEN:
        return "keine Angaben";
      default:
        return null;
    }
  }

  public static ArrayList<HerkunftSpende> getArray()
  {
    ArrayList<HerkunftSpende> ret = new ArrayList<HerkunftSpende>();
    ret.add(new HerkunftSpende(BETRIEBSVERMOEGEN));
    ret.add(new HerkunftSpende(PRIVATVERMOEGEN));
    ret.add(new HerkunftSpende(KEINEANGABEN));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof HerkunftSpende)
    {
      HerkunftSpende v = (HerkunftSpende) obj;
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
