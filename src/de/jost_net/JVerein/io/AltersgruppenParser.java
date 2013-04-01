/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.swt.graphics.Point;

import de.willuhn.util.ApplicationException;

public class AltersgruppenParser
{

  private Vector<String> elemente;

  private int ei = 0;

  public AltersgruppenParser(String altersgruppe) throws RuntimeException
  {
    // Schritt 1: Zerlegen in die einzelnen Gruppen
    StringTokenizer stt = new StringTokenizer(altersgruppe, ",");
    Vector<String> gruppen = new Vector<String>();
    while (stt.hasMoreElements())
    {
      String token = stt.nextToken().trim();
      gruppen.addElement(token);
    }
    // Schritt 2: Zerlegen der Gruppen in ihre einzelnen Elemente
    elemente = new Vector<String>();
    for (int i = 0; i < gruppen.size(); i++)
    {
      stt = new StringTokenizer(gruppen.elementAt(i), "-");
      if (stt.countTokens() != 2)
      {
        throw new RuntimeException(
            MessageFormat
                .format(
                    "Ungültige Altersgruppe: {0} \nDie Eingaben müssen folgendes Format haben: 1-6,7-13,14-22,23-50",
                    new Object[] { gruppen.elementAt(i) }));
      }
      elemente.addElement(stt.nextToken().trim());
      elemente.addElement(stt.nextToken());
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

  public Point getNext() throws ApplicationException
  {
    Point p = new Point(getValue(), getValue());
    return p;
  }

  private int getValue() throws ApplicationException
  {
    try
    {
      int value = Integer.parseInt(elemente.elementAt(ei).trim());
      ei++;
      return value;
    }
    catch (NumberFormatException e)
    {
      throw new ApplicationException("Fehler in den Altergruppen" + " "
          + e.getMessage());
    }
  }
}
