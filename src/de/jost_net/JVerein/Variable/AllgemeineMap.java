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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.util.JVDateFormatJJJJ;
import de.jost_net.JVerein.util.JVDateFormatMMJJJJ;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;

public class AllgemeineMap
{

  private JVDateFormatTTMMJJJJ ttmmjjjj = new JVDateFormatTTMMJJJJ();

  private JVDateFormatMMJJJJ mmjjjj = new JVDateFormatMMJJJJ();

  private JVDateFormatJJJJ jjjj = new JVDateFormatJJJJ();

  public AllgemeineMap()
  {

  }

  public Map<String, Object> getMap(Map<String, Object> inma)
  {
    Map<String, Object> map = null;
    if (inma == null)
    {
      map = new HashMap<String, Object>();
    }
    else
    {
      map = inma;
    }
    map.put(AllgemeineVar.TAGESDATUM.getName(), ttmmjjjj.format(new Date()));
    map.put(AllgemeineVar.AKTUELLESJAHR.getName(), jjjj.format(new Date()));
    map.put(AllgemeineVar.AKTUELLERMONAT.getName(), mmjjjj.format(new Date()));
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MONTH, 1);
    map.put(AllgemeineVar.FOLGEMONAT.getName(),
        mmjjjj.format(calendar.getTime()));
    calendar.add(Calendar.MONTH, -2);
    map.put(AllgemeineVar.VORMONAT.getName(), mmjjjj.format(calendar.getTime()));
    calendar.add(Calendar.MONTH, -1);
    calendar.add(Calendar.YEAR, 1);
    map.put(AllgemeineVar.FOLGEJAHR.getName(), jjjj.format(calendar.getTime()));
    return map;
  }
}
