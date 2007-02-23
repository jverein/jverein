/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.2  2006/12/23 16:47:50  jost
 * Java 1.5 Kompatibilität
 *
 * Revision 1.1  2006/10/29 07:49:56  jost
 * Neu: Mitgliederstatistik
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.swt.graphics.Point;

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
      gruppen.addElement(stt.nextToken());
    }
    // Schritt 2: Zerlegen der Gruppen in ihre einzelnen Elemente
    elemente = new Vector<String>();
    for (int i = 0; i < gruppen.size(); i++)
    {
      stt = new StringTokenizer((String) gruppen.elementAt(i), "-");
      if (stt.countTokens() != 2)
      {
        throw new RuntimeException("Ungültige Altersgruppe: "
            + (String) gruppen.elementAt(i));
      }
      elemente.addElement(stt.nextToken());
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

  public Point getNext()
  {
    Point p = new Point(getValue(), getValue());
    return p;
  }

  private int getValue()
  {
    int value = Integer.parseInt((String) elemente.elementAt(ei));
    ei++;
    return value;
  }
}
