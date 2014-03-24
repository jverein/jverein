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
package de.jost_net.JVerein.util;

public class StringTool
{
  public static String toNotNullString(String string)
  {
    return (string == null ? "" : string);
  }

  /**
   * Formatiert eine Zeichenkette so, dass sie eine feste Länge (len) bekommt,
   * wobei Leerzeichen am Ende der Zeichenkette angehängt werden.
   * 
   * @param string
   *          Die zu formatierende Zeichenkette
   * @param len
   *          Die Länge der formatierten Zeichenkette
   * @return Die formatierte Zeichenkette
   */
  public static String rpad(String string, int len)
  {
    return rpad(string, len, " ");
  }

  /**
   * Formatiert eine Zeichenkette so, dass sie eine feste Länge (len) bekommt,
   * wobei Leerzeichen am Ende der Zeichenkette angehängt werden.
   * 
   * @param string
   *          Die zu formatierende Zeichenkette
   * @param len
   *          Die Länge der formatierten Zeichenkette
   * @param fillChar
   *          Zeichenkette, die zum Füllen verwendet wrden soll
   * @return Die formatierte Zeichenkette
   */
  public static String rpad(String string, int len, String fillChar)
  {
    while (string.length() < len)
      string += fillChar;
    return string;
  }

  /**
   * Formatiert eine Zeichenkette so, dass sie eine feste Länge (len) bekommt,
   * wobei Leerzeichen am Anfang der Zeichenkette angehängt werden.
   * 
   * @param string
   *          Die zu formatierende Zeichenkette
   * @param len
   *          Die Länge der formatierten Zeichenkette
   * @return Die formatierte Zeichenkette
   */
  public static String lpad(String string, int len)
  {
    return lpad(string, len, " ");
  }

  /**
   * Formatiert eine Zeichenkette so, dass sie eine feste Länge (len) bekommt,
   * wobei Leerzeichen am Anfang der Zeichenkette angehängt werden.
   * 
   * @param string
   *          Die zu formatierende Zeichenkette
   * @param len
   *          Die Länge der formatierten Zeichenkette
   * @param fillChar
   *          Zeichenkette, die zum Füllen verwendet wrden soll
   * @return Die formatierte Zeichenkette
   */
  public static String lpad(String string, int len, String fillChar)
  {
    while (string.length() < len)
      string = fillChar + string;
    return string;
  }
  
  public static String getStringWithMaxLength(String in, int max)
  {
    if (in.length()>max)
    {
      return in.substring(0,max);
    }
    return in;
  }
}
