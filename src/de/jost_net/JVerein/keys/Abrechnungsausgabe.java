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
 * Abrechnungsausgabe
 */
public class Abrechnungsausgabe
{
  public static final int DTAUS = 1;

  public static final int HIBISCUS_EINZELBUCHUNGEN = 2;

  public static final int HIBISCUS_SAMMELBUCHUNG = 3;

  private int ausgabe;

  public Abrechnungsausgabe(int key)
  {
    this.ausgabe = key;
  }

  public int getKey()
  {
    return ausgabe;
  }

  public String getText()
  {
    return get(ausgabe);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case DTAUS:
        return "DTAUS-Datei";
      case HIBISCUS_EINZELBUCHUNGEN:
        return "Hibiscus (Einzelbuchungen)";
      case HIBISCUS_SAMMELBUCHUNG:
        return "Hibiscus (Sammelbuchungen)";
      default:
        return null;
    }
  }

  public static ArrayList<Abrechnungsausgabe> getArray()
  {
    ArrayList<Abrechnungsausgabe> ret = new ArrayList<Abrechnungsausgabe>();
    ret.add(new Abrechnungsausgabe(DTAUS));
    ret.add(new Abrechnungsausgabe(HIBISCUS_EINZELBUCHUNGEN));
    ret.add(new Abrechnungsausgabe(HIBISCUS_SAMMELBUCHUNG));
    return ret;
  }

  public String toString()
  {
    return get(ausgabe);
  }
}
