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
package de.jost_net.JVerein.gui.formatter;

import de.willuhn.jameica.gui.formatter.Formatter;

/**
 * Formatieren von einer mehrzeiligen Notiz zur Anzeige in einer Listenspalte.
 * Es wird nur die erste Zeile angezeigt, zusätzlich kann die maximale
 * Länge (in Zeichen) angegeben werden. Sind mehr Zeichen vorhanden, wird ein 
 * Ellipsis-Zeichen zur Markierung der Auslassung angezeigt. 
 * 
 * @author thooge
 *
 */
public class NotizFormatter implements Formatter
{
  
  private Integer maxlength = null;

  public NotizFormatter(Integer maxlength)
  {
    this.maxlength = maxlength;
  }
  
  @Override
  public String format(Object o)
  {
    String notiz = (String) o;
    if (notiz == null)
    {
      return "";
    }
    String[] lines = notiz.split("\r?\n|\r");
    if ((maxlength != null) && (lines[0].length() > maxlength))
    {
      // Abschneiden und Ellipsis anhängen
      return lines[0].substring(0, maxlength - 1) + "\u2026";
    }
    if (lines.length > 1)
    {
      return lines[0] + "\u2026";
    }
    return lines[0];
  }

}