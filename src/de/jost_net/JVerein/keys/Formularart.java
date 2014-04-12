/**********************************************************************
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

/**
 * Formularart
 */
public class Formularart
{

  public static enum FormularArtEnum
  {
    SPENDENBESCHEINIGUNG, RECHNUNG, MAHNUNG, FREIESFORMULAR, SAMMELSPENDENBESCHEINIGUNG, SEPA_PRENOTIFICATION
  };

  private FormularArtEnum formularart;

  public Formularart(FormularArtEnum key)
  {
    this.formularart = key;
  }

  public FormularArtEnum getKey()
  {
    return formularart;
  }

  public String getText()
  {
    return get(formularart);
  }

  public static String get(FormularArtEnum key)
  {
    switch (key)
    {
      case SPENDENBESCHEINIGUNG:
        return "Spendenbescheinigung";
      case RECHNUNG:
        return "Rechnung";
      case MAHNUNG:
        return "Mahnung";
      case FREIESFORMULAR:
        return "Freies Formular";
      case SAMMELSPENDENBESCHEINIGUNG:
        return "Sammelbestätigung";
      case SEPA_PRENOTIFICATION:
        return "SEPA Pre-Notification";
      default:
        return "";
    }
  }

  public static ArrayList<Formularart> getArray()
  {
    ArrayList<Formularart> ret = new ArrayList<Formularart>();
    for (FormularArtEnum e : FormularArtEnum.values())
    {
      ret.add(new Formularart(e));
    }
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
    return formularart.ordinal();
  }

  @Override
  public String toString()
  {
    return get(formularart);
  }
}
