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

public enum BuchungVar
{
  ABRECHNUNGSLAUF("buchung_abrechnungslauf"), //
  ART("buchung_art"), //
  AUSZUGSNUMMER("buchung_auszugsnummer"), //
  BETRAG("buchung_betrag"), //
  BLATTNUMMER("buchung_blattnummer"), //
  BUCHUNGSARBEZEICHNUNG("buchung_buchungsart_bezeichnung"), //
  BUCHUNGSARTNUMMER("buchung_buchungsart_nummer"), //
  BUCHUNGSKLASSEBEZEICHNUNG("buchung_buchungsklasse_bezeichnung"), //
  BUCHUNGSKLASSENUMMER("buchung_buchungsklasse_nummer"), //
  DATUM("buchung_datum"), //
  ID("buchung_id"), //
  JAHRESABSCHLUSS("buchung_jahresabschluss"), //
  KOMMENTAR("buchung_kommentar"), //
  KONTONUMMER("buchung_kontonummer"), //
  MITGLIEDSKONTO("buchung_mitgliedskonto"), //
  NAME("buchung_name"), //
  PROJEKTNUMMER("buchung_projektnummer"), //
  PROJEKTBEZEICHNUNG("buchung_projektbezeichnung"), //
  SPENDENBESCHEINIGUNG("buchung_spendenbescheinigung"), // ;
  ZWECK1("buchung_zweck1");

  private String name;

  BuchungVar(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
