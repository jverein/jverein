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

import de.jost_net.JVerein.JVereinPlugin;

/**
 * Abrechnungsausgabe
 */
public class Spendenart
{
  public static final int GELDSPENDE = 1;

  public static final int SACHSPENDE = 2;

  private int art;

  public Spendenart(int key)
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
      case GELDSPENDE:
        return JVereinPlugin.getI18n().tr("Geldspende");
      case SACHSPENDE:
        return JVereinPlugin.getI18n().tr("Sachspende");
      default:
        return null;
    }
  }

  public static ArrayList<Spendenart> getArray()
  {
    ArrayList<Spendenart> ret = new ArrayList<Spendenart>();
    ret.add(new Spendenart(GELDSPENDE));
    ret.add(new Spendenart(SACHSPENDE));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Spendenart)
    {
      Spendenart v = (Spendenart) obj;
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
