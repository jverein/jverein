/**********************************************************************
 * JVerein - Mitgliederverwaltung und einfache Buchhaltung für Vereine
 * Copyright (c) by Heiner Jostkleigrewe
 * Copyright (c) 2015 by Thomas Hooge
 * Main Project: heiner@jverein.dem  http://www.jverein.de/
 * Module Author: thomas@hoogi.de, http://www.hoogi.de/
 *
 * This file is part of JVerein.
 *
 * JVerein is free software: you can redistribute it and/or modify 
 * it under the terms of the  GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JVerein is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **********************************************************************/
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

/**
 * Schlüssel Sortierung der Buchungsart; Form der Anzeige
 */
public class BuchungsartSort
{

  public static final int NACH_BEZEICHNUNG = 1;

  public static final int NACH_NUMMER = 2;

  public static final int NACH_BEZEICHNUNG_NR = 3;

  private int buchungsartsort;

  public BuchungsartSort(int key)
  {
    this.buchungsartsort = key;
  }

  public int getKey()
  {
    return buchungsartsort;
  }

  public String getText()
  {
    return get(buchungsartsort);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case NACH_BEZEICHNUNG:
        return "Nach Bezeichnung";
      case NACH_NUMMER:
        return "Nach Nummer";
      case NACH_BEZEICHNUNG_NR:
        return "Nach Bezeichnung mit Nummer";
      default:
        return null;
    }
  }

  public static ArrayList<BuchungsartSort> getArray()
  {
    ArrayList<BuchungsartSort> ret = new ArrayList<>();
    ret.add(new BuchungsartSort(NACH_BEZEICHNUNG));
    ret.add(new BuchungsartSort(NACH_NUMMER));
    ret.add(new BuchungsartSort(NACH_BEZEICHNUNG_NR));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof BuchungsartSort)
    {
      BuchungsartSort v = (BuchungsartSort) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return buchungsartsort;
  }

  @Override
  public String toString()
  {
    return get(buchungsartsort);
  }
}
