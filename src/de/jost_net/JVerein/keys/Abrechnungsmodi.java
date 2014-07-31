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

/**
 * Abrechnungsmodi
 */
public class Abrechnungsmodi
{
  public static final int KEINBEITRAG = 0;

  public static final int ALLE = 1;

  public static final int JA = 5;

  public static final int HA = 6;

  public static final int VI = 7;

  public static final int JAHAVIMO = 8;

  public static final int JAVIMO = 9;

  public static final int HAVIMO = 10;

  public static final int VIMO = 11;

  public static final int MO = 12;

  public static final int EINGETRETENEMITGLIEDER = 99;

  private int abrechnungsmodus;

  public Abrechnungsmodi(int abrechnungsmodus)
  {
    this.abrechnungsmodus = abrechnungsmodus;
  }

  public int getKey()
  {
    return abrechnungsmodus;
  }

  public String getText()
  {
    return get(abrechnungsmodus);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case KEINBEITRAG:
        return "keine Beitragsabrechnung";
      case ALLE:
        return "Alle";
      case MO:
        return "Monatsbeiträge";
      case VI:
        return "Vierteljährlich";
      case HA:
        return "Halbjährlich";
      case JA:
        return "Jährlich";
      case JAHAVIMO:
        return "Jahres-, Halbjahres-, Vierteljahres- und Monatsbeiträge";
      case JAVIMO:
        return "Jahres-, Vierteljahres- und Monatsbeiträge";
      case HAVIMO:
        return "Halbjahres-, Vierteljahres- und Monatsbeiträge";
      case VIMO:
        return "Vierteljahres- und Monatsbeiträge";
      case EINGETRETENEMITGLIEDER:
        return "eingetretene Mitglieder";
      default:
        return null;
    }
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Abrechnungsmodi)
    {
      Abrechnungsmodi v = (Abrechnungsmodi) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return abrechnungsmodus;
  }

  @Override
  public String toString()
  {
    return get(abrechnungsmodus);
  }
}
