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

  public String toString()
  {
    return get(zahlungsweg);
  }
}
