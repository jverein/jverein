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
package de.jost_net.JVerein.gui.formatter;

import de.willuhn.jameica.gui.formatter.Formatter;

public class JaNeinFormatter implements Formatter
{
  @Override
  public String format(Object o)
  {
    if (o == null)
    {
      return " ";
    }
    if (o instanceof Byte)
    {
      Byte b = (Byte) o;
      if (b.equals(Byte.valueOf("1")))
      {
        return "Ja";
      }
    }
    if (o instanceof Boolean)
    {
      Boolean b = (Boolean) o;
      if (b)
      {
        return "Ja";
      }
    }
    if (o instanceof String)
    {
      String s = (String) o;
      if (s.equalsIgnoreCase("TRUE"))
      {
        return "Ja";
      }
    }
    return "Nein";
  }
}
