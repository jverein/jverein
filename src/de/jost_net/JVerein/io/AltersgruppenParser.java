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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import de.jost_net.JVerein.util.VonBis;

public class AltersgruppenParser
{
  private Iterator<VonBis> iterator;

  public AltersgruppenParser(String altersgruppe) throws RuntimeException
  {
    ArrayList<VonBis> elemente;
    if (altersgruppe == null)
    {
      throw new RuntimeException("Altersgruppe(n) müssen gefüllt sein!");
    }
    // Schritt 1: Zerlegen in die einzelnen Gruppen
    StringTokenizer stt = new StringTokenizer(altersgruppe, ",");
    ArrayList<String> gruppen = new ArrayList<>();
    while (stt.hasMoreElements())
    {
      String token = stt.nextToken().trim();
      gruppen.add(token);
    }
    // Schritt 2: Zerlegen der Gruppen in ihre einzelnen Elemente
    elemente = new ArrayList<>();
    for (int i = 0; i < gruppen.size(); i++)
    {
      stt = new StringTokenizer(gruppen.get(i), "-");
      if (stt.countTokens() != 2)
      {
        throw new RuntimeException(String.format(
            "Ungültige Altersgruppe: %s \nDie Eingaben müssen folgendes Format haben: 1-6,7-13,14-22,23-50",
            gruppen.get(i)));
      }
      try
      {
        VonBis vb = new VonBis(Integer.parseInt(stt.nextToken().trim()),
            Integer.parseInt(stt.nextToken().trim()));
        elemente.add(vb);
      }
      catch (NumberFormatException e)
      {
        throw new RuntimeException(
            "Fehler in den Altergruppen" + " " + e.getMessage());
      }
      iterator = elemente.iterator();
    }
  }

  public boolean hasNext()
  {
    return iterator.hasNext();
  }

  public VonBis getNext()
  {
    return iterator.next();
  }
}
