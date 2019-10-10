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

public enum AbrechnungsParameterVar
{

  ABBUCHUNGSMODUS("abrechnungsparameter_abrechnungsmodus"), //
  ABRECHNUNGSMONAT("abrechnungsparameter_abrechnungsmonat"), //
  FAELLIGKEIT("abrechnungsparameter_faelligkeit"), //
  STICHTAG("abrechnungsparameter_stichtag"), //
  VONDATUM("abrechnungsparameter_vondatum"), //
  BISDATUM("abrechnungsparameter_bisdatum"), //
  VERWENDUNGSZWECK("abrechnungsparameter_verwendungszweck"), //
  ZUSATZBETRAEGE("abrechnungsparameter_zusatzbetraege"), //
  KURSTEILNEHMER("abrechnungsparameter_kursteilnehmer"), //
  KOMPAKTEABBUCHUNG("abrechnungsparameter_kompakteabbuchung"), //
  SEPAPRINT("abrechnungsparameter_sepaprint");

  private String name;

  AbrechnungsParameterVar(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
