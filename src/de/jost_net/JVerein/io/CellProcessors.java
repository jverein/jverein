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

package de.jost_net.JVerein.io;

import java.util.Date;
import java.util.Map;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.ift.CellProcessor;

import de.jost_net.JVerein.Einstellungen;

public class CellProcessors
{
  public static CellProcessor[] createCellProcessors(Map<String, Object> map)
  {
	  return createCellProcessors(map, map.keySet().toArray(new String[0]));
  }
  
  public static CellProcessor[] createCellProcessors(Map<String, Object> map, String[] keys)
  {
    CellProcessor[] ret = new CellProcessor[keys.length];
    int i = 0;
    for (String elem : keys)
    {
      Object o = map.get(elem);
      if (o instanceof Double)
      {
        ret[i] = new FmtNumber(Einstellungen.DECIMALFORMAT);
      }
      else if (o instanceof Date)
      {
        ret[i] = new FmtDate("dd.MM.yyyy");
      }
      else
      {
        ret[i] = new ConvertNullTo("");
      }
      i++;
    }
    return ret;
  }

}
