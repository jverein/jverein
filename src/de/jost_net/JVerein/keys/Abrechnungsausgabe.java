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
 * Abrechnungsausgabe
 */
public class Abrechnungsausgabe
{
  public static final int DTAUS = 1;

  public static final int HIBISCUS_EINZELBUCHUNGEN = 2;

  public static final int HIBISCUS_SAMMELBUCHUNG = 3;

  private int ausgabe;

  public Abrechnungsausgabe(int key)
  {
    this.ausgabe = key;
  }

  public int getKey()
  {
    return ausgabe;
  }

  public String getText()
  {
    return get(ausgabe);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case DTAUS:
        return "Datei";
      case HIBISCUS_EINZELBUCHUNGEN:
        return "Hibiscus (Einzelbuchungen)";
      case HIBISCUS_SAMMELBUCHUNG:
        return "Hibiscus (Sammelbuchungen)";
      default:
        return null;
    }
  }

  public static ArrayList<Abrechnungsausgabe> getArray()
  {
    ArrayList<Abrechnungsausgabe> ret = new ArrayList<Abrechnungsausgabe>();
    ret.add(new Abrechnungsausgabe(DTAUS));
    ret.add(new Abrechnungsausgabe(HIBISCUS_EINZELBUCHUNGEN));
    ret.add(new Abrechnungsausgabe(HIBISCUS_SAMMELBUCHUNG));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Abrechnungsausgabe)
    {
      Abrechnungsausgabe v = (Abrechnungsausgabe) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return ausgabe;
  }

  @Override
  public String toString()
  {
    return get(ausgabe);
  }
}
