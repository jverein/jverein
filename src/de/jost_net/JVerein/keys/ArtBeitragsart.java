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
 * Revision 1.1  2008/11/29 13:12:47  jost
 * Refactoring: Code-Optimierung
 *
 **********************************************************************/
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

/**
 * Art der Beitragsart
 */
public class ArtBeitragsart
{
  public static final int NORMAL = 0;

  public static final int FAMILIE_ZAHLER = 1;

  public static final int FAMILIE_ANGEHOERIGER = 2;

  private int art;

  public ArtBeitragsart(int key)
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
      case NORMAL:
        return "Normal";
      case FAMILIE_ZAHLER:
        return "Familie: Zahler";
      case FAMILIE_ANGEHOERIGER:
        return "Familie: Angehöriger";
      default:
        return null;
    }
  }

  public static ArrayList<ArtBeitragsart> getArray()
  {
    ArrayList<ArtBeitragsart> ret = new ArrayList<ArtBeitragsart>();
    ret.add(new ArtBeitragsart(NORMAL));
    ret.add(new ArtBeitragsart(FAMILIE_ZAHLER));
    ret.add(new ArtBeitragsart(FAMILIE_ANGEHOERIGER));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ArtBeitragsart)
    {
      ArtBeitragsart v = (ArtBeitragsart) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public String toString()
  {
    return get(art);
  }
}
