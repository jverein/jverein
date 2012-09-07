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

public enum MitgliedVar
{
  ADRESSIERUNGSZUSATZ(JVereinPlugin.getI18n()
      .tr("mitglied_adressierungszusatz")), //
  ADRESSTYP(JVereinPlugin.getI18n().tr("mitglied_adresstyp")), //
  ANREDE(JVereinPlugin.getI18n().tr("mitglied_anrede")), //
  ANREDE_DU(JVereinPlugin.getI18n().tr("mitglied_anrede_du")), //
  ANREDE_FOERMLICH(JVereinPlugin.getI18n().tr("mitglied_anrede_foermlich")), //
  AUSTRITT(JVereinPlugin.getI18n().tr("mitglied_austritt")), //
  BEITRAGSGRUPPE_ARBEITSEINSATZ_BETRAG(JVereinPlugin.getI18n().tr(
      "mitglied_arbeitseinsatz_betrag")), //
  BEITRAGSGRUPPE_ARBEITSEINSATZ_STUNDEN(JVereinPlugin.getI18n().tr(
      "mitglied_arbeitseinsatz_stunden")), //
  BEITRAGSGRUPPE_BEZEICHNUNG(JVereinPlugin.getI18n().tr(
      "mitglied_beitragsgruppe_bezeichnung")), //
  BEITRAGSGRUPPE_BETRAG(JVereinPlugin.getI18n().tr(
      "mitglied_beitragsgruppe_betrag")), //
  BEITRAGSGRUPPE_ID(JVereinPlugin.getI18n().tr("mitglied_beitragsgruppe_id")), //
  BLZ(JVereinPlugin.getI18n().tr("mitglied_blz")), //
  EINTRITT(JVereinPlugin.getI18n().tr("mitglied_eintritt")), //
  EINGABEDATUM(JVereinPlugin.getI18n().tr("mitglied_eingabedatum")), //
  EMPFAENGER(JVereinPlugin.getI18n().tr("mitglied_empfaenger")), //
  EMAIL(JVereinPlugin.getI18n().tr("mitglied_email")), //
  EXTERNE_MITGLIEDSNUMMER(JVereinPlugin.getI18n().tr(
      "mitglied_externe_mitgliedsnummer")), //
  GEBURTSDATUM(JVereinPlugin.getI18n().tr("mitglied_geburtsdatum")), //
  GESCHLECHT(JVereinPlugin.getI18n().tr("mitglied_geschlecht")), //
  HANDY(JVereinPlugin.getI18n().tr("mitglied_handy")), //
  IBAN(JVereinPlugin.getI18n().tr("mitglied_iban")), //
  ID(JVereinPlugin.getI18n().tr("mitglied_id")), //
  INDIVIDUELLERBEITRAG(JVereinPlugin.getI18n().tr(
      "mitglied_individuellerbeitrag")), //
  KONTO(JVereinPlugin.getI18n().tr("mitglied_konto")), //
  KONTOINHABER(JVereinPlugin.getI18n().tr("mitglied_kontoinhaber")), //
  KUENDIGUNG(JVereinPlugin.getI18n().tr("mitglied_kuendigung")), //
  LETZTEAENDERUNG(JVereinPlugin.getI18n().tr("mitglied_letzte.aenderung")), //
  NAME(JVereinPlugin.getI18n().tr("mitglied_name")), //
  NAMEVORNAME(JVereinPlugin.getI18n().tr("mitglied_namevorname")), //
  ORT(JVereinPlugin.getI18n().tr("mitglied_ort")), //
  PERSONENART(JVereinPlugin.getI18n().tr("mitglied_personenart")), //
  PLZ(JVereinPlugin.getI18n().tr("mitglied_plz")), //
  STAAT(JVereinPlugin.getI18n().tr("mitglied_staat")), //
  STERBETAG(JVereinPlugin.getI18n().tr("mitglied_sterbetag")), //
  STRASSE(JVereinPlugin.getI18n().tr("mitglied_strasse")), //
  TELEFONDIENSTLICH(JVereinPlugin.getI18n().tr("mitglied_telefon_dienstlich")), //
  TELEFONPRIVAT(JVereinPlugin.getI18n().tr("mitglied_telefon_privat")), //
  TITEL(JVereinPlugin.getI18n().tr("mitglied_titel")), //
  VERMERK1(JVereinPlugin.getI18n().tr("mitglied_vermerk1")), //
  VERMERK2(JVereinPlugin.getI18n().tr("mitglied_vermerk2")), //
  VORNAME(JVereinPlugin.getI18n().tr("mitglied_vorname")), //
  VORNAMENAME(JVereinPlugin.getI18n().tr("mitglied_vornamename")), //
  ZAHLUNGSRHYTMUS(JVereinPlugin.getI18n().tr("mitglied_zahlungsrhytmus")), //
  ZAHLUNGSWEG(JVereinPlugin.getI18n().tr("mitglied_zahlungsweg")), //
  ZAHLUNGSWEGTEXT(JVereinPlugin.getI18n().tr("mitglied_zahlungsweg_text")), //
  ZAHLERID(JVereinPlugin.getI18n().tr("mitglied_zahlerid"));

  private String name;

  MitgliedVar(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}