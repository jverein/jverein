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
package de.jost_net.JVerein.util;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.formatter.BeitragsgruppeFormatter;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.gui.formatter.ZahlungsrhytmusFormatter;
import de.jost_net.JVerein.gui.formatter.ZahlungswegFormatter;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.parts.Column;

public class MitgliedSpaltenauswahl extends Spaltenauswahl
{

  public MitgliedSpaltenauswahl()
  {
    super("mitglied");
    add("ID", "idint", false, true);
    add("externe Mitgliedsnummer", "externemitgliedsnummer", false, false);
    add("Anrede", "anrede", false, true);
    add("Titel", "titel", false, true);
    add("Name", "name", true, true);
    add("Vorname", "vorname", true, true);
    add("Adressierungszusatz", "adressierungszusatz", false, true);
    add("Straße", "strasse", true, true);
    add("PLZ", "plz", false, true);
    add("Ort", "ort", true, true);
    add("Zahlungsweg", "zahlungsweg", false, new ZahlungswegFormatter(),
        Column.ALIGN_LEFT, false);
    add("Zahlungsrhytmus", "zahlungsrhytmus", false,
        new ZahlungsrhytmusFormatter(), Column.ALIGN_LEFT, false);
    add("Datum des Mandats", "mandatdatum", false, false);
    add("BIC", "bic", false, true);
    add("IBAN", "iban", false, true);
    add("BLZ", "blz", false, true);
    add("Konto", "konto", false, true);
    add("Kontoinhaber", "kontoinhaber", false, true);
    add("Geburtsdatum", "geburtsdatum", true, new DateFormatter(
        new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, true);
    add("Geschlecht", "geschlecht", false, true);
    add("Telefon privat", "telefonprivat", true, true);
    add("Telefon dienstlich", "telefondienstlich", false, true);
    add("Handy", "handy", false, true);
    add("Email", "email", false, true);
    add("Eintritt", "eintritt", true, new DateFormatter(
        new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, false);
    add("Beitragsgruppe", "beitragsgruppe", false,
        new BeitragsgruppeFormatter(), Column.ALIGN_LEFT, false);
    add("Austritt", "austritt", true, new DateFormatter(
        new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, false);
    add("Kündigung", "kuendigung", false, new DateFormatter(
        new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, false);
    add("Eingabedatum", "eingabedatum", false, new DateFormatter(
        new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, true);
    add("letzte Änderung", "letzteaenderung", false, new DateFormatter(
        new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, true);
    try
    {
      DBIterator it = Einstellungen.getDBService().createList(
          Felddefinition.class);
      while (it.hasNext())
      {
        Felddefinition fd = (Felddefinition) it.next();
        switch (fd.getDatentyp())
        {
          case Datentyp.DATUM:
            add(fd.getLabel(), "zusatzfelder." + fd.getName(), false,
                new DateFormatter(new JVDateFormatTTMMJJJJ()),
                Column.ALIGN_AUTO, true);
            break;
          case Datentyp.WAEHRUNG:
            add(fd.getLabel(), "zusatzfelder." + fd.getName(), false,
                new CurrencyFormatter("", Einstellungen.DECIMALFORMAT),
                Column.ALIGN_AUTO, true);
            break;
          case Datentyp.JANEIN:
            add(fd.getLabel(), "zusatzfelder." + fd.getName(), false,
                new JaNeinFormatter(), Column.ALIGN_AUTO, true);
            break;
          default:
            add(fd.getLabel(), "zusatzfelder." + fd.getName(), false, true);
            break;
        }
      }
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
}
