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
 * Revision 1.2  2008/12/13 16:23:06  jost
 * equals()-Methode implementiert.
 *
 * Revision 1.1  2008/11/29 13:12:38  jost
 * Refactoring: Code-Optimierung
 *
 **********************************************************************/
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

import de.jost_net.JVerein.JVereinPlugin;

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
        return JVereinPlugin.getI18n().tr("Hibiscus (Einzelbuchungen)");
      case HIBISCUS_SAMMELBUCHUNG:
        return JVereinPlugin.getI18n().tr("Hibiscus (Sammelbuchungen)");
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

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Abrechnungsausgabe)
    {
      Abrechnungsausgabe v = (Abrechnungsausgabe) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return ausgabe;
  }

  @Override
  public String toString()
  {
    return get(ausgabe);
  }
}
