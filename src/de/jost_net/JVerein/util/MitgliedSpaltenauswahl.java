/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.3  2009/02/12 22:19:12  jost
 * Anrede aufgenommen.
 *
 * Revision 1.2  2008/11/30 18:58:59  jost
 * Neu: Konfiguration der Spalten einer Tabelle
 *
 * Revision 1.1  2008/11/29 13:18:07  jost
 * Neu: Konfiguration der Spalten einer Tabelle
 *
 **********************************************************************/
package de.jost_net.JVerein.util;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.formatter.BeitragsgruppeFormatter;
import de.jost_net.JVerein.gui.formatter.ZahlungsrhytmusFormatter;
import de.jost_net.JVerein.gui.formatter.ZahlungswegFormatter;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.parts.Column;

public class MitgliedSpaltenauswahl extends Spaltenauswahl
{
  public MitgliedSpaltenauswahl()
  {
    super("mitglied");
    add("ID", "id", false);
    add("externe Mitgliedsnummer", "externemitgliedsnummer", false);
    add("Anrede", "anrede", false);
    add("Titel", "titel", false);
    add("Name", "name", true);
    add("Vorname", "vorname", true);
    add("Adressierungszusatz", "adressierungszusatz", false);
    add("Straße", "strasse", true);
    add("PLZ", "plz", false);
    add("Ort", "ort", true);
    add("Zahlungsweg", "zahlungsweg", false, new ZahlungswegFormatter(),
        Column.ALIGN_LEFT);
    add("Zahlungsrhytmus", "zahlungsrhytmus", false,
        new ZahlungsrhytmusFormatter(), Column.ALIGN_LEFT);
    add("BLZ", "blz", false);
    add("Konto", "konto", false);
    add("Kontoinhaber", "kontoinhaber", false);
    add("Geburtsdatum", "geburtsdatum", true, new DateFormatter(
        Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add("Geschlecht", "geschlecht", false);
    add("Telefon privat", "telefonprivat", true);
    add("Telefon dienstlich", "telefondienstlich", false);
    add("Handy", "handy", false);
    add("Email", "email", false);
    add("Eintritt", "eintritt", true, new DateFormatter(
        Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add("Beitragsgruppe", "beitragsgruppe", false,
        new BeitragsgruppeFormatter(), Column.ALIGN_LEFT);
    add("Austritt", "austritt", true, new DateFormatter(
        Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add("Kündigung", "kuendigung", false, new DateFormatter(
        Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add("Eingabedatum", "eingabedatum", false, new DateFormatter(
        Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    try
    {
      DBIterator it = Einstellungen.getDBService().createList(
          Felddefinition.class);
      while (it.hasNext())
      {
        Felddefinition fd = (Felddefinition) it.next();
        add(fd.getLabel(), "zusatzfelder." + fd.getName(), false);
      }
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }

  }
}
