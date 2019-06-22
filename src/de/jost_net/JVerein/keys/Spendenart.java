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
 * Abrechnungsausgabe
 */
public class Spendenart
{

  public static final int GELDSPENDE = 1;

  public static final int SACHSPENDE = 2;

  private int art;

  public Spendenart(int key)
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
      case GELDSPENDE:
        return "Geldspende";
      case SACHSPENDE:
        return "Sachspende";
      default:
        return null;
    }
  }

  public static ArrayList<Spendenart> getArray()
  {
    ArrayList<Spendenart> ret = new ArrayList<>();
    ret.add(new Spendenart(GELDSPENDE));
    ret.add(new Spendenart(SACHSPENDE));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Spendenart)
    {
      Spendenart v = (Spendenart) obj;
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
