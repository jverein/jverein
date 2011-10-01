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
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.swt.graphics.Point;

import de.jost_net.JVerein.JVereinPlugin;
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
        throw new RuntimeException(JVereinPlugin.getI18n().tr(
            "Ungültige Altersgruppe: {0}}",
            new String[] { gruppen.elementAt(i) }));
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
      throw new ApplicationException("Fehler in den Altergruppen "
          + e.getMessage());
    }
  }
}
