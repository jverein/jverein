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
 * Revision 1.1  2008/11/29 13:13:15  jost
 * Refactoring: Code-Optimierung
 *
 **********************************************************************/
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

/**
 * Intervall Zusatzzahlungen
 */
public class IntervallZusatzzahlung
{
  public static final int KEIN = 0;

  public static final int MONATLICH = 1;

  public static final int ZWEIMONATLICH = 2;

  public static final int VIERTELJAEHRLICH = 3;

  public static final int HALBJAEHRLICH = 6;

  public static final int JAEHRLICH = 12;

  private int intervall;

  public IntervallZusatzzahlung(int key)
  {
    this.intervall = key;
  }

  public int getKey()
  {
    return intervall;
  }

  public String getText()
  {
    return get(intervall);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case KEIN:
        return "kein";
      case MONATLICH:
        return "monatlich";
      case ZWEIMONATLICH:
        return "zweimonatlich";
      case VIERTELJAEHRLICH:
        return "vierteljährlich";
      case HALBJAEHRLICH:
        return "halbjährlich";
      case JAEHRLICH:
        return "jährlich";
      default:
        return null;
    }
  }

  public static ArrayList<IntervallZusatzzahlung> getArray()
  {
    ArrayList<IntervallZusatzzahlung> ret = new ArrayList<IntervallZusatzzahlung>();
    ret.add(new IntervallZusatzzahlung(KEIN));
    ret.add(new IntervallZusatzzahlung(MONATLICH));
    ret.add(new IntervallZusatzzahlung(ZWEIMONATLICH));
    ret.add(new IntervallZusatzzahlung(VIERTELJAEHRLICH));
    ret.add(new IntervallZusatzzahlung(HALBJAEHRLICH));
    ret.add(new IntervallZusatzzahlung(JAEHRLICH));
    ret.add(new IntervallZusatzzahlung(MONATLICH));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof IntervallZusatzzahlung)
    {
      IntervallZusatzzahlung v = (IntervallZusatzzahlung) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public String toString()
  {
    return get(intervall);
  }
}
