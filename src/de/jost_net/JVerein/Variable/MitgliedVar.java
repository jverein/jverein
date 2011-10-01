/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * heiner@jverein.de
 * www.jverein.de
 * All rights reserved
 **********************************************************************/
package de.jost_net.JVerein.Variable;

public enum MitgliedVar
{
  ADRESSIERUNGSZUSATZ("mitglied_adressierungszusatz"), //
  ADRESSTYP("mitglied_adresstyp"), //
  ANREDE("mitglied_anrede"), //
  ANREDE_DU("mitglied_anrede_du"), //
  ANREDE_FOERMLICH("mitglied_anrede_foermlich"), //
  AUSTRITT("mitglied_austritt"), //
  BEITRAGSGRUPPE_ARBEITSEINSATZ_BETRAG("mitglied_arbeitseinsatz_betrag"), //
  BEITRAGSGRUPPE_ARBEITSEINSATZ_STUNDEN("mitglied_arbeitseinsatz_stunden"), //
  BEITRAGSGRUPPE_BEZEICHNUNG("mitglied_beitragsgruppe_bezeichnung"), //
  BEITRAGSGRUPPE_BETRAG("mitglied_beitragsgruppe_betrag"), //
  BEITRAGSGRUPPE_ID("mitglied_beitragsgruppe.id"), //
  BLZ("mitglied_blz"), //
  EINTRITT("mitglied_eintritt"), //
  EINGABEDATUM("mitglied_eingabedatum"), //
  EMPFAENGER("mitglied_empfaenger"), //
  EMAIL("mitglied_email"), //
  EXTERNE_MITGLIEDSNUMMER("mitglied_externe_mitgliedsnummer"), //
  GEBURTSDATUM("mitglied_geburtsdatum"), //
  GESCHLECHT("mitglied_geschlecht"), //
  HANDY("mitglied_handy"), //
  IBAN("mitglied_iban"), //
  ID("mitglied_id"), //
  INDIVIDUELLERBEITRAG("mitglied_individuellerbeitrag"), //
  KONTO("mitglied_konto"), //
  KONTOINHABER("mitglied_kontoinhaber"), //
  KUENDIGUNG("mitglied_kuendigung"), //
  LETZTEAENDERUNG("mitglied_letzte.aenderung"), //
  NAME("mitglied_name"), //
  NAMEVORNAME("mitglied_namevorname"), //
  ORT("mitglied_ort"), //
  PERSONENART("mitglied_personenart"), //
  PLZ("mitglied_plz"), //
  STAAT("mitglied_staat"), //
  STERBETAG("mitglied_sterbetag"), //
  STRASSE("mitglied_strasse"), //
  TELEFONDIENSTLICH("mitglied_telefon.dienstlich"), //
  TELEFONPRIVAT("mitglied_telefon.privat"), //
  TITEL("mitglied_titel"), //
  VERMERK1("mitglied_vermerk1"), //
  VERMERK2("mitglied_vermerk2"), //
  VORNAME("mitglied_vorname"), //
  VORNAMENAME("mitglied_vornamename"), //
  ZAHLUNGSRHYTMUS("mitglied_zahlungsrhytmus"), //
  ZAHLUNGSWEG("mitglied_zahlungsweg"), //
  ZAHLUNGSWEGTEXT("mitglied_zahlungsweg_text"), //
  ZAHLERID("mitglied_zahlerid");

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
