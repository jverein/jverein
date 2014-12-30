/**********************************************************************
 * Copyright (c) by Thomas Laubrock
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
 * Auswahl der Buchungsart im Buchungsdialog
 */
public class BuchungBuchungsartAuswahl
{
  public static final int SearchInput = 0;

  public static final int ComboBox = 1;

  private int auswahl;

  public BuchungBuchungsartAuswahl(int key)
  {
    this.auswahl = key;
  }

  public int getKey()
  {
    return auswahl;
  }

  public String getText()
  {
    return get(auswahl);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case SearchInput:
        return "Suche bei Eingabe";
      case ComboBox:
        return "Anzeige der kompletten Liste";
      default:
        return null;
    }
  }

  public static ArrayList<BuchungBuchungsartAuswahl> getArray()
  {
    ArrayList<BuchungBuchungsartAuswahl> ret = new ArrayList<BuchungBuchungsartAuswahl>();
    ret.add(new BuchungBuchungsartAuswahl(SearchInput));
    ret.add(new BuchungBuchungsartAuswahl(ComboBox));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof BuchungBuchungsartAuswahl)
    {
    	BuchungBuchungsartAuswahl v = (BuchungBuchungsartAuswahl) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return auswahl;
  }

  @Override
  public String toString()
  {
    return get(auswahl);
  }
}
