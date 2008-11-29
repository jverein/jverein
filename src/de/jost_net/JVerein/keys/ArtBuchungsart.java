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
 * Art der Buchungsart
 */
public class ArtBuchungsart
{
  public static final int EINNAHME = 0;

  public static final int AUSGABE = 1;

  public static final int UMBUCHUNG = 2;

  private int art;

  public ArtBuchungsart(int key)
  {
    this.art = key;
  }

  public int getKey()
  {
    return art;
  }

  public String getText()
  {
    return get(art);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case EINNAHME:
        return "Einnahme";
      case AUSGABE:
        return "Ausgabe";
      case UMBUCHUNG:
        return "Umbuchung";
      default:
        return null;
    }
  }

  public static ArrayList<ArtBuchungsart> getArray()
  {
    ArrayList<ArtBuchungsart> ret = new ArrayList<ArtBuchungsart>();
    ret.add(new ArtBuchungsart(EINNAHME));
    ret.add(new ArtBuchungsart(AUSGABE));
    ret.add(new ArtBuchungsart(UMBUCHUNG));
    return ret;
  }

  public String toString()
  {
    return get(art);
  }
}
