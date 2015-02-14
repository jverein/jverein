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

/**
 * Sortierung einer Ausgabe Z.B. Reihenfolge der Seiten in einem Dokument mit
 * mehreren Rechnungen
 * 
 * @author thooge
 * 
 */
public enum Ausgabesortierung
{

  NAME(1, "Nach Name und Vorname"), //
  PLZ(2, "Nach Postleitzahl und Straße");

  private final String text;

  private final int key;

  Ausgabesortierung(int key, String text)
  {
    this.key = key;
    this.text = text;
  }

  public int getKey()
  {
    return key;
  }

  public String getText()
  {
    return text;
  }

  public static Ausgabesortierung getByKey(int key)
  {
    for (Ausgabesortierung sort : Ausgabesortierung.values())
    {
      if (sort.getKey() == key)
      {
        return sort;
      }
    }
    return null;
  }

  @Override
  public String toString()
  {
    return getText();
  }
}
