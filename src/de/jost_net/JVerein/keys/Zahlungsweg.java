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
 * Revision 1.1  2008/11/29 13:13:36  jost
 * Refactoring: Code-Optimierung
 *
 **********************************************************************/
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

/**
 * Schlüssel Zahlungsweg
 */
public class Zahlungsweg
{
  public static final int ABBUCHUNG = 1;

  public static final int ÜBERWEISUNG = 2;

  public static final int BARZAHLUNG = 3;

  private int zahlungsweg;

  public Zahlungsweg(int key)
  {
    this.zahlungsweg = key;
  }

  public int getKey()
  {
    return zahlungsweg;
  }

  public String getText()
  {
    return get(zahlungsweg);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case ABBUCHUNG:
        return "Abbuchung";
      case ÜBERWEISUNG:
        return "Überweisung";
      case BARZAHLUNG:
        return "Barzahlung";
      default:
        return null;
    }
  }

  public static ArrayList<Zahlungsweg> getArray()
  {
    ArrayList<Zahlungsweg> ret = new ArrayList<Zahlungsweg>();
    ret.add(new Zahlungsweg(ABBUCHUNG));
    ret.add(new Zahlungsweg(ÜBERWEISUNG));
    ret.add(new Zahlungsweg(BARZAHLUNG));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Zahlungsweg)
    {
      Zahlungsweg v = (Zahlungsweg) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public String toString()
  {
    return get(zahlungsweg);
  }
}
