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

public enum SpendenbescheinigungVar {
  EMPFAENGER("spendenbescheinigung_empfaenger"), //
  BETRAG("spendenbescheinigung_betrag"), //
  BETRAGINWORTEN("spendenbescheinigung_betraginworten"), //
  BESCHEINIGUNGDATUM("spendenbescheinigung_datum"), //
  SPENDEART("spendenbescheinigung_spendenart"), //
  SPENDEDATUM("spendenbescheinigung_spendedatum"), //
  SPENDENZEITRAUM("spendenbescheinigung_spendenzeitraum"), //
  ERSATZAUFWENDUNGEN("spendenbescheinigung_ersatzaufwendungen"), //
  ERSATZAUFWENDUNGEN_JA("spendenbescheinigung_ersatzaufwendungen_ja"), //
  ERSATZAUFWENDUNGEN_NEIN("spendenbescheinigung_ersatzaufwendungen_nein"), //
  BUCHUNGSLISTE("spendenbescheinigung_buchungsliste"), //
  BUCHUNGSLISTE_DATEN("spendenbescheinigung_buchungsliste_daten"), //
  BUCHUNGSLISTE_ART("spendenbescheinigung_buchungsliste_art"), //
  BUCHUNGSLISTE_VERZICHT("spendenbescheinigung_buchungsliste_verzicht"), //
  BUCHUNGSLISTE_BETRAG("spendenbescheinigung_buchungsliste_betrag"), //
  BEZEICHNUNGSACHZUWENDUNG("spendenbescheinigung_bezeichnungsachzuwendung"), //
  HERKUNFTSACHZUWENDUNG("spendenbescheinigung_herkunftsachzuwendung"), //
  UNTERLAGENWERTERMITTUNG("spendenbescheinigung_unterlagenwertermittlung");//

  private String name;

  SpendenbescheinigungVar(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
