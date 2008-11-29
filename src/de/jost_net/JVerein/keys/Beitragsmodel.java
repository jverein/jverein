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
public class Beitragsmodel
{
  public static final int JAEHRLICH = 1;

  public static final int HALBJAEHRLICH = 2;

  public static final int VIERTELJAEHRLICH = 3;

  public static final int MONATLICH = 4;

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
      case JAEHRLICH:
        return "jährlich";
      case HALBJAEHRLICH:
        return "halbjährlich";
      case VIERTELJAEHRLICH:
        return "vierteljährlich";
      case MONATLICH:
        return "monatlich";
      case MONATLICH12631:
        return "monatlich mit monatl., viertel-, halb- oder jährlicher Zahlungsweise";
      default:
        return null;
    }
  }

  public static ArrayList<Beitragsmodel> getArray()
  {
    ArrayList<Beitragsmodel> ret = new ArrayList<Beitragsmodel>();
    ret.add(new Beitragsmodel(JAEHRLICH));
    ret.add(new Beitragsmodel(HALBJAEHRLICH));
    ret.add(new Beitragsmodel(VIERTELJAEHRLICH));
    ret.add(new Beitragsmodel(MONATLICH));
    ret.add(new Beitragsmodel(MONATLICH12631));
    return ret;
  }

  public String toString()
  {
    return get(model);
  }
}
