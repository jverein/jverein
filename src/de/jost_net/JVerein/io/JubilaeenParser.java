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

import java.util.StringTokenizer;
import java.util.Vector;

public class JubilaeenParser
{

  private Vector<Integer> elemente;

  private int ei = 0;

  public JubilaeenParser(String jubilaeen) throws RuntimeException
  {
    // Zerlegen in die einzelnen Elemente
    StringTokenizer stt = new StringTokenizer(jubilaeen, ",");
    elemente = new Vector<>();
    while (stt.hasMoreElements())
    {
      String element = stt.nextToken();
      try
      {
        Integer jubilaeum = Integer.parseInt(element);
        elemente.add(jubilaeum);
      }
      catch (NumberFormatException e)
      {
        throw new RuntimeException(
            String.format("Ungültiger Eintrag: %s", element));
      }
    }
  }

  public boolean hasNext()
  {
    if (ei < elemente.size())
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public int getNext()
  {
    Integer i = elemente.elementAt(ei);
    ei++;
    return i.intValue();
  }
}
