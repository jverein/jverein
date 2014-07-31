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
package de.jost_net.JVerein.Variable;

public enum MitgliedskontoVar
{
  ZAHLUNGSGRUND("mitgliedskonto_zahlungsgrund"), //
  ZAHLUNGSGRUND1("mitgliedskonto_zahlungsgrund1"), //
  ZAHLUNGSGRUND2("mitgliedskonto_zahlungsgrund2"), //
  BUCHUNGSDATUM("mitgliedskonto_buchungsdatum"), //
  BETRAG("mitgliedskonto_betrag"), //
  IST("mitgliedskonto_ist"), //
  DIFFERENZ("mitgliedskonto_differenz"), //
  STAND("mitgliedskonto_stand"), //
  SUMME_OFFEN("mitgliedskonto_summe_offen");

  private String name;

  MitgliedskontoVar(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
