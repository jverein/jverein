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

public enum LastschriftVar
{
  ABRECHNUNGSLAUF_NR("lastschrift_abrechnungslauf_nr"), //
  ABRECHUNGSLAUF_DATUM("lastschrift_abrechnungslauf_datum"), //
  ABRECHNUNGSLAUF_FAELLIGKEIT("lastschrift_abrechnungslauf_faelligkeit"), //
  ANREDE_DU("lastschrift_anrede_du"), //
  ANREDE_FOERMLICH("lastschrift_anrede_foermlich"), //
  PERSONENART("lastschrift_personenart"), //
  ANREDE("lastschrift_anrede"), //
  TITEL("lastschrift_titel"), //
  NAME("lastschrift_name"), //
  VORNAME("lastschrift_vorname"), //
  STRASSE("lastschrift_strasse"), //
  ADRESSSIERUNGSZUSATZ("lastschrift_adressierungszusatz"), //
  PLZ("lastschrift_plz"), //
  ORT("lastschrift_ort"), //
  STAAT("lastschrift_staat"), //
  EMAIL("lastschrift_email"), //
  MANDATID("lastschrift_mandatid"), //
  MANDATDATUM("lastschrift_mandatdatum"), //
  BIC("lastschrift_bic"), //
  IBAN("lastschrift_iban"), //
  IBANMASKIERT("lastschrift_ibanmaskiert"), //
  VERWENDUNGSZWECK("lastschrift_verwendungszweck"), //
  BETRAG("lastschrift_betrag"), //
  EMPFAENGER("lastschrift_empfaenger");

  private String name;

  LastschriftVar(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
