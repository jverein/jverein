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
package de.jost_net.JVerein.gui.view;

public class DokumentationUtil
{


  private static final String PRE = "https://doku.jverein.de/";
  
  private static final String ALLGEMEIN = "allgemein/";
  
  private static final String FUNKTIONEN = "allgemeine-funktionen/";
  
  private static final String ADMIN = "administration/";
  
  private static final String AUSWERTUNGEN = "auswertungen/";
  
  private static final String ABRECH = "abrech/";
  
  private static final String BUCHF = "buchf/";
  
  private static final String ADMBUCHF = "admbuchf/";

  

  public static final String ADRESSEN = PRE + FUNKTIONEN + "adressen";

  public static final String ABRECHNUNG = PRE + FUNKTIONEN + ABRECH + "abrechnung";

  public static final String ABRECHNUNGSLAUF = PRE + FUNKTIONEN + ABRECH + "abrechnungslauf";

  public static final String ADRESSTYPEN = PRE + FUNKTIONEN + ADMIN + "adresstypen";

  public static final String ARBEITSEINSATZ = PRE + FUNKTIONEN + "arbeitseinsatz";

  public static final String RECHNUNG = PRE + FUNKTIONEN + "rechnungen";

  public static final String MAHNUNG = PRE + FUNKTIONEN + "mahnungen";

  public static final String PRENOTIFICATION = PRE + FUNKTIONEN + ABRECH + "pre-notification";

  public static final String SEPABUGS = PRE + FUNKTIONEN + ABRECH + "sepa-bugs";

  public static final String AUSWERTUNGKURSTEILNEHMER = PRE + FUNKTIONEN + AUSWERTUNGEN
      + "auswertung-kursteilnehmer";

  public static final String AUSWERTUNGMITGLIEDER = PRE + FUNKTIONEN + AUSWERTUNGEN
      + "auswertung-mitglieder";

  public static final String BEITRAGSGRUPPEN = PRE + FUNKTIONEN + ADMIN + "beitragsgruppen";

  public static final String BUCHUNGSART = PRE + FUNKTIONEN + ADMIN + ADMBUCHF + "buchungsart.html";

  public static final String BUCHUNGSIMPORT = PRE + FUNKTIONEN + BUCHF + "buchungsimport";

  public static final String BUCHUNGSUEBERNAHME = PRE + FUNKTIONEN + BUCHF
      + "buchungsubernahme";

  public static final String BUCHUNGSKLASSEN = PRE + FUNKTIONEN + BUCHF + "buchungsklasse";

  public static final String EIGENSCHAFT = PRE + FUNKTIONEN + ADMIN + "eigenschaften";

  public static final String EIGENSCHAFTGRUPPE = PRE + FUNKTIONEN + ADMIN
      + "eigenschaften-gruppen";

  public static final String FAMILIENBEITRAG = PRE + ALLGEMEIN + "familientarife";

  public static final String FELDDEFINITIONEN = PRE + FUNKTIONEN + ADMIN + "felddefinitionen";

  public static final String LESEFELDER = PRE + FUNKTIONEN + ADMIN + "lesefelder";

  public static final String FORMULARE = PRE + FUNKTIONEN + ADMIN + "formulare";

  public static final String EINSTELLUNGEN = PRE + FUNKTIONEN + ADMIN + "einstellungen";

  public static final String IMPORT = PRE + ADMIN + "import";

  public static final String JUBILAEEN = PRE + FUNKTIONEN + AUSWERTUNGEN + "jubilaen";

  public static final String LEHRGANG = PRE + FUNKTIONEN + ADMIN + "lehrgange";

  public static final String KONTENRAHMEN = PRE + FUNKTIONEN + ADMIN + ADMBUCHF
      + "kontenrahmen-import-export";

  public static final String KONTOAUSZUG = PRE + FUNKTIONEN + BUCHF + "kontoauszug";

  public static final String MAIL = PRE + FUNKTIONEN + "mail";

  public static final String MITGLIED = PRE + FUNKTIONEN + "mitglieder";

  public static final String MITGLIEDSKONTO_AUSWAHL = PRE
      + "mitgliedskonto#mitgliedskontozuordnen";

  public static final String MITGLIEDSKONTO_UEBERSICHT = PRE + FUNKTIONEN
      + "mitgliedskonto#mitgliedskontouebersicht";

  public static final String KURSTEILNEHMER = PRE + FUNKTIONEN + "kursteilnehmer";

  public static final String PROJEKTE = PRE + FUNKTIONEN + BUCHF + "projekte";

  public static final String STATISTIKMITGLIEDER = PRE + FUNKTIONEN + AUSWERTUNGEN + "statistik";

  public static final String STATISTIKJAHRGAENGE = PRE + FUNKTIONEN + AUSWERTUNGEN
      + "statistik-jahrgange";

  public static final String SUCHPROFIL = PRE + FUNKTIONEN + "suchprofil";

  public static final String WIEDERVORLAGE = PRE + FUNKTIONEN + "wiedervorlage";

  public static final String ZUSATZBETRAEGE = PRE + FUNKTIONEN + "zusatzbetrage";

  public static final String ZUSATZBETRAEGEIMPORT = PRE + FUNKTIONEN
      + "zusatzbetrage-importieren";

  public static final String KONTEN = PRE + FUNKTIONEN + BUCHF + "konten";

  public static final String ANFANGSBESTAENDE = PRE + FUNKTIONEN + BUCHF + "anfangsbestand";

  public static final String BUCHUNGEN = PRE + FUNKTIONEN + BUCHF + "buchungen";

  public static final String SPLITBUCHUNG = PRE + FUNKTIONEN + BUCHF + "splittbuchungen";

  public static final String JAHRESSALDO = PRE + FUNKTIONEN + BUCHF + "jahressaldo";

  public static final String JAHRESABSCHLUSS = PRE + FUNKTIONEN + BUCHF + "jahresabschluss";

  public static final String SPENDENBESCHEINIGUNG = PRE + FUNKTIONEN
      + "spendenbescheinigung";

  public static final String QIFIMPORT = PRE + FUNKTIONEN + ADMIN + ADMBUCHF + "qif-import";

}
