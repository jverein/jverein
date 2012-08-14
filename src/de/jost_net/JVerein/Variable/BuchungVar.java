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

public enum BuchungVar
{
  ABRECHNUNGSLAUF(JVereinPlugin.getI18n().tr("buchung_abrechnungslauf")), //
  ART(JVereinPlugin.getI18n().tr("buchung_art")), //
  AUSZUGSNUMMER(JVereinPlugin.getI18n().tr("buchung_auszugsnummer")), //
  BETRAG(JVereinPlugin.getI18n().tr("buchung_betrag")), //
  BLATTNUMMER(JVereinPlugin.getI18n().tr("buchung_blattnummer")), //
  BUCHUNGSARBEZEICHNUNG(JVereinPlugin.getI18n().tr(
      "buchung_buchungsart_bezeichnung")), //
  BUCHUNGSARTNUMMER(JVereinPlugin.getI18n().tr("buchung_buchungsart_nummer")), //
  BUCHUNGSKLASSEBEZEICHNUNG(JVereinPlugin.getI18n().tr(
      "buchung_buchungsklasse_bezeichnung")), //
  BUCHUNGSKLASSENUMMER(JVereinPlugin.getI18n().tr(
      "buchung_buchungsklasse_nummer")), //
  DATUM(JVereinPlugin.getI18n().tr("buchung_datum")), //
  JAHRESABSCHLUSS(JVereinPlugin.getI18n().tr("buchung_jahresabschluss")), //
  KOMMENTAR(JVereinPlugin.getI18n().tr("buchung_kommentar")), //
  KONTONUMMER(JVereinPlugin.getI18n().tr("buchung_kontonummer")), //
  MITGLIEDSKONTO(JVereinPlugin.getI18n().tr("buchung_mitgliedskonto")), //
  NAME(JVereinPlugin.getI18n().tr("buchung_name")), //
  PROJEKTNUMMER(JVereinPlugin.getI18n().tr("buchung_projektnummer")), //
  PROJEKTBEZEICHNUNG(JVereinPlugin.getI18n().tr("buchung_projektbezeichnung")), //
  SPENDENBESCHEINIGUNG(JVereinPlugin.getI18n().tr(
      "buchung_spendenbescheinigung")), // ;
  ZWECK1(JVereinPlugin.getI18n().tr("buchung_zweck1"));

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
