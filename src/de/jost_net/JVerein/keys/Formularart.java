/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
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

  public static final int SAMMELSPENDENBESCHEINIGUNG = 5;

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
      case SAMMELSPENDENBESCHEINIGUNG:
        return JVereinPlugin.getI18n().tr("Sammelbestätigung");
      default:
        return null;
    }
  }

  public static ArrayList<Formularart> getArray()
  {
    ArrayList<Formularart> ret = new ArrayList<Formularart>();
    ret.add(new Formularart(SPENDENBESCHEINIGUNG));
    ret.add(new Formularart(SAMMELSPENDENBESCHEINIGUNG));
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
