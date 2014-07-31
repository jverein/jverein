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
package de.jost_net.JVerein.Variable;

import java.util.Map;

import org.apache.velocity.VelocityContext;

public class VarTools
{
  /**
   * Übertragung einer Map<String, Object> in einen VelocityContext
   */
  public static void add(VelocityContext context, Map<String, Object> map)
  {
    for (String key : map.keySet())
    {
      context.put(key, map.get(key));
    }
  }

  public static String maskieren(String wert)
  {
    StringBuffer sb = new StringBuffer(wert);
    for (int i = 0; i < wert.length() - 4; i++)
    {
      sb.replace(i, i + 1, "X");
    }
    return sb.toString();
  }

}
