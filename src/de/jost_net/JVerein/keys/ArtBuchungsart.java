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
 * Revision 1.3  2009/06/11 21:04:03  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2008/12/13 16:23:45  jost
 * equals()-Methode implementiert.
 *
 * Revision 1.1  2008/11/29 13:12:47  jost
 * Refactoring: Code-Optimierung
 *
 **********************************************************************/
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

import de.jost_net.JVerein.JVereinPlugin;

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
        return JVereinPlugin.getI18n().tr("Einnahme");
      case AUSGABE:
        return JVereinPlugin.getI18n().tr("Ausgabe");
      case UMBUCHUNG:
        return JVereinPlugin.getI18n().tr("Umbuchung");
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

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ArtBuchungsart)
    {
      ArtBuchungsart v = (ArtBuchungsart) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return art;
  }

  @Override
  public String toString()
  {
    return get(art);
  }
}
