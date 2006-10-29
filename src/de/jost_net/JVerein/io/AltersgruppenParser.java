/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.swt.graphics.Point;

public class AltersgruppenParser
{
  private Vector elemente;

  private int ei = 0;

  public AltersgruppenParser(String altersgruppe) throws RuntimeException
  {
    // Schritt 1: Zerlegen in die einzelnen Gruppen
    StringTokenizer stt = new StringTokenizer(altersgruppe, ",");
    Vector gruppen = new Vector();
    while (stt.hasMoreElements())
    {
      gruppen.addElement(stt.nextToken());
    }
    // Schritt 2: Zerlegen der Gruppen in ihre einzelnen Elemente
    elemente = new Vector();
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
