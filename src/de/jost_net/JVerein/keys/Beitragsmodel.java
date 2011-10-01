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
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

import de.jost_net.JVerein.JVereinPlugin;

/**
 * Abrechnungsausgabe
 */
public class Beitragsmodel
{
  public static final int GLEICHERTERMINFUERALLE = 1;

  public static final int MONATLICH12631 = 5;

  private int model;

  public Beitragsmodel(int key)
  {
    this.model = key;
  }

  public int getKey()
  {
    return model;
  }

  public String getText()
  {
    return get(model);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case GLEICHERTERMINFUERALLE:
        return JVereinPlugin.getI18n().tr("Gleicher Termin für alle");
      case MONATLICH12631:
        return JVereinPlugin
            .getI18n()
            .tr("monatlich mit monatl., viertel-, halb- oder jährlicher Zahlungsweise");
      default:
        return null;
    }
  }

  public static ArrayList<Beitragsmodel> getArray()
  {
    ArrayList<Beitragsmodel> ret = new ArrayList<Beitragsmodel>();
    ret.add(new Beitragsmodel(GLEICHERTERMINFUERALLE));
    ret.add(new Beitragsmodel(MONATLICH12631));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Beitragsmodel)
    {
      Beitragsmodel v = (Beitragsmodel) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return model;
  }

  @Override
  public String toString()
  {
    return get(model);
  }
}
