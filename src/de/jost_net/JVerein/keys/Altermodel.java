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
 * Gegen welches Datum wird das Alter des Mitglieds für die Anzeige berechnet.
 */
public class Altermodel
{
  public static final int AKTUELLES_DATUM = 1;

  public static final int JAHRES_START = 2;

  public static final int JAHRES_ENDE = 3;

  private int model;

  public Altermodel(int key)
  {
    this.model = key;
  }

  public int getKey()
  {
    return model;
  }

  public String getText()
  {
    return get(model);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case AKTUELLES_DATUM:
        return "Aktuelles Datum";
      case JAHRES_START:
        return "Jahres-Start";
      case JAHRES_ENDE:
        return "Jahres-Ende";
      default:
        return null;
    }
  }

  public static ArrayList<Altermodel> getArray()
  {
    ArrayList<Altermodel> ret = new ArrayList<>();
    ret.add(new Altermodel(AKTUELLES_DATUM));
    ret.add(new Altermodel(JAHRES_START));
    ret.add(new Altermodel(JAHRES_ENDE));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Altermodel)
    {
      Altermodel v = (Altermodel) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return model;
  }

  @Override
  public String toString()
  {
    return get(model);
  }
}
