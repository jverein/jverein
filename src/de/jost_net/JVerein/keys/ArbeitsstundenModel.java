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
 * Schlüssel Einstellung.Arbeitsmodel
 * Diese Klasse hilft beim Einstellen wie mit Arbeitsstunden umgegangen werden soll
 */
public class ArbeitsstundenModel
{

  public static final int STANDARD = 1;

  public static final int ERLAUBE_NEGATIV = 2;

  private int arbeitsModel;

  public ArbeitsstundenModel(int key)
  {
    this.arbeitsModel = key;
  }

  public int getKey()
  {
    return arbeitsModel;
  }

  public String getText()
  {
    return get(arbeitsModel);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case ArbeitsstundenModel.STANDARD:
        return "Standardverfahren";
      case ArbeitsstundenModel.ERLAUBE_NEGATIV:
        return "negative Stunden erlaubt";
      default:
        return null;
    }
  }

  public static ArrayList<ArbeitsstundenModel> getArray()
  {
    ArrayList<ArbeitsstundenModel> ret = new ArrayList<ArbeitsstundenModel>();
    ret.add(new ArbeitsstundenModel(STANDARD));
    ret.add(new ArbeitsstundenModel(ERLAUBE_NEGATIV));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ArbeitsstundenModel)
    {
      ArbeitsstundenModel v = (ArbeitsstundenModel) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return arbeitsModel;
  }

  @Override
  public String toString()
  {
    return get(arbeitsModel);
  }
}
