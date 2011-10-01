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
 * Formularart
 */
public class Formularart
{
  public static final int SPENDENBESCHEINIGUNG = 1;

  public static final int RECHNUNG = 2;

  public static final int MAHNUNG = 3;

  public static final int FREIESFORMULAR = 4;

  private int formularart;

  public Formularart(int key)
  {
    this.formularart = key;
  }

  public int getKey()
  {
    return formularart;
  }

  public String getText()
  {
    return get(formularart);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case SPENDENBESCHEINIGUNG:
        return JVereinPlugin.getI18n().tr("Spendenbescheinigung");
      case RECHNUNG:
        return JVereinPlugin.getI18n().tr("Rechnung");
      case MAHNUNG:
        return JVereinPlugin.getI18n().tr("Mahnung");
      case FREIESFORMULAR:
        return JVereinPlugin.getI18n().tr("Freies Formular");
      default:
        return null;
    }
  }

  public static ArrayList<Formularart> getArray()
  {
    ArrayList<Formularart> ret = new ArrayList<Formularart>();
    ret.add(new Formularart(SPENDENBESCHEINIGUNG));
    ret.add(new Formularart(RECHNUNG));
    ret.add(new Formularart(MAHNUNG));
    ret.add(new Formularart(FREIESFORMULAR));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Formularart)
    {
      Formularart v = (Formularart) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return formularart;
  }

  @Override
  public String toString()
  {
    return get(formularart);
  }
}
