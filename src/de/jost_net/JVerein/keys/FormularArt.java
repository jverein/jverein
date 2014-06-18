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

public enum FormularArt
{
  SPENDENBESCHEINIGUNG(1, "Spendenbescheinigung"), RECHNUNG(2, "Rechnung"), MAHNUNG(
      3, "Mahnung"), FREIESFORMULAR(4, "Freies Formular"), SAMMELSPENDENBESCHEINIGUNG(
      5, "Sammelspendenbescheinigung"), SEPA_PRENOTIFICATION(6,
      "SEPA-Prenotification");
  private final String text;

  private final int key;

  FormularArt(int key, String text)
  {
    this.key = key;
    this.text = text;
  }

  public int getKey()
  {
    return key;
  }

  public String getText()
  {
    return text;
  }

  public static FormularArt getByKey(int key)
  {
    for (FormularArt form : FormularArt.values())
    {
      if (form.getKey() == key)
      {
        return form;
      }
    }
    return null;
  }

  @Override
  public String toString()
  {
    return getText();
  }
}
