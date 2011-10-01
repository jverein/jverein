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
 **********************************************************************/
package de.jost_net.JVerein.util;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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
    add(JVereinPlugin.getI18n().tr("externe Mitgliedsnummer"),
        "externemitgliedsnummer", false, false);
    add(JVereinPlugin.getI18n().tr("Anrede"), "anrede", false, true);
    add(JVereinPlugin.getI18n().tr("Titel"), "titel", false, true);
    add(JVereinPlugin.getI18n().tr("Name"), "name", true, true);
    add(JVereinPlugin.getI18n().tr("Vorname"), "vorname", true, true);
    add(JVereinPlugin.getI18n().tr("Adressierungszusatz"),
        "adressierungszusatz", false, true);
    add(JVereinPlugin.getI18n().tr("Straße"), "strasse", true, true);
    add(JVereinPlugin.getI18n().tr("PLZ"), "plz", false, true);
    add(JVereinPlugin.getI18n().tr("Ort"), "ort", true, true);
    add(JVereinPlugin.getI18n().tr("Zahlungsweg"), "zahlungsweg", false,
        new ZahlungswegFormatter(), Column.ALIGN_LEFT, false);
    add(JVereinPlugin.getI18n().tr("Zahlungsrhytmus"), "zahlungsrhytmus",
        false, new ZahlungsrhytmusFormatter(), Column.ALIGN_LEFT, false);
    add(JVereinPlugin.getI18n().tr("BLZ"), "blz", false, true);
    add(JVereinPlugin.getI18n().tr("Konto"), "konto", false, true);
    add(JVereinPlugin.getI18n().tr("Kontoinhaber"), "kontoinhaber", false, true);
    add(JVereinPlugin.getI18n().tr("Geburtsdatum"), "geburtsdatum", true,
        new DateFormatter(new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, true);
    add(JVereinPlugin.getI18n().tr("Geschlecht"), "geschlecht", false, true);
    add(JVereinPlugin.getI18n().tr("Telefon privat"), "telefonprivat", true,
        true);
    add(JVereinPlugin.getI18n().tr("Telefon dienstlich"), "telefondienstlich",
        false, true);
    add(JVereinPlugin.getI18n().tr("Handy"), "handy", false, true);
    add(JVereinPlugin.getI18n().tr("Email"), "email", false, true);
    add(JVereinPlugin.getI18n().tr("Eintritt"), "eintritt", true,
        new DateFormatter(new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, false);
    add(JVereinPlugin.getI18n().tr("Beitragsgruppe"), "beitragsgruppe", false,
        new BeitragsgruppeFormatter(), Column.ALIGN_LEFT, false);
    add(JVereinPlugin.getI18n().tr("Austritt"), "austritt", true,
        new DateFormatter(new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, false);
    add(JVereinPlugin.getI18n().tr("Kündigung"), "kuendigung", false,
        new DateFormatter(new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, false);
    add(JVereinPlugin.getI18n().tr("Eingabedatum"), "eingabedatum", false,
        new DateFormatter(new JVDateFormatTTMMJJJJ()), Column.ALIGN_AUTO, true);
    add(JVereinPlugin.getI18n().tr("letzte Änderung"), "letzteaenderung",
        false, new DateFormatter(new JVDateFormatTTMMJJJJ()),
        Column.ALIGN_AUTO, true);
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
