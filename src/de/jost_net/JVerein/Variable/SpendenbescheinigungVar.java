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
package de.jost_net.JVerein.Variable;

import de.jost_net.JVerein.JVereinPlugin;

public enum SpendenbescheinigungVar
{
  EMPFAENGER(JVereinPlugin.getI18n().tr("spendenbescheinigung_empfaenger")), //
  BETRAG(JVereinPlugin.getI18n().tr("spendenbescheinigung_betrag")), //
  BETRAGINWORTEN(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_betraginworten")), //
  BESCHEINIGUNGDATUM(JVereinPlugin.getI18n().tr("spendenbescheinigung_datum")), //
  SPENDEART(JVereinPlugin.getI18n().tr("spendenbescheinigung_spendenart")), //
  SPENDEDATUM(JVereinPlugin.getI18n().tr("spendenbescheinigung_spendedatum")), //
  SPENDENZEITRAUM(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_spendenzeitraum")), //
  ERSATZAUFWENDUNGEN(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_ersatzaufwendungen")), //
  ERSATZAUFWENDUNGEN_JA(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_ersatzaufwendungen_ja")), //
  ERSATZAUFWENDUNGEN_NEIN(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_ersatzaufwendungen_nein")), //
  BUCHUNGSLISTE(JVereinPlugin.getI18n()
      .tr("spendenbescheinigung_buchungsliste")), //
  BEZEICHNUNGSACHZUWENDUNG(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_bezeichnungsachzuwendung")), //
  HERKUNFTSACHZUWENDUNG(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_herkunftsachzuwendung")), //
  UNTERLAGENWERTERMITTUNG(JVereinPlugin.getI18n().tr(
      "spendenbescheinigung_unterlagenwertermittlung"));//

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
