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
 * Revision 1.4  2009/03/02 19:22:41  jost
 * Bug #15335
 *
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
import de.jost_net.JVerein.JVereinPlugin;
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
    add(JVereinPlugin.getI18n().tr("externe Mitgliedsnummer"),
        "externemitgliedsnummer", false);
    add(JVereinPlugin.getI18n().tr("Anrede"), "anrede", false);
    add(JVereinPlugin.getI18n().tr("Titel"), "titel", false);
    add(JVereinPlugin.getI18n().tr("Name"), "name", true);
    add(JVereinPlugin.getI18n().tr("Vorname"), "vorname", true);
    add(JVereinPlugin.getI18n().tr("Adressierungszusatz"),
        "adressierungszusatz", false);
    add(JVereinPlugin.getI18n().tr("Straße"), "strasse", true);
    add(JVereinPlugin.getI18n().tr("PLZ"), "plz", false);
    add(JVereinPlugin.getI18n().tr("Ort"), "ort", true);
    add(JVereinPlugin.getI18n().tr("Zahlungsweg"), "zahlungsweg", false,
        new ZahlungswegFormatter(), Column.ALIGN_LEFT);
    add(JVereinPlugin.getI18n().tr("Zahlungsrhytmus"), "zahlungsrhytmus",
        false, new ZahlungsrhytmusFormatter(), Column.ALIGN_LEFT);
    add(JVereinPlugin.getI18n().tr("BLZ"), "blz", false);
    add(JVereinPlugin.getI18n().tr("Konto"), "konto", false);
    add(JVereinPlugin.getI18n().tr("Kontoinhaber"), "kontoinhaber", false);
    add(JVereinPlugin.getI18n().tr("Geburtsdatum"), "geburtsdatum", true,
        new DateFormatter(Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add(JVereinPlugin.getI18n().tr("Geschlecht"), "geschlecht", false);
    add(JVereinPlugin.getI18n().tr("Telefon privat"), "telefonprivat", true);
    add(JVereinPlugin.getI18n().tr("Telefon dienstlich"), "telefondienstlich",
        false);
    add(JVereinPlugin.getI18n().tr("Handy"), "handy", false);
    add(JVereinPlugin.getI18n().tr("Email"), "email", false);
    add(JVereinPlugin.getI18n().tr("Eintritt"), "eintritt", true,
        new DateFormatter(Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add(JVereinPlugin.getI18n().tr("Beitragsgruppe"), "beitragsgruppe", false,
        new BeitragsgruppeFormatter(), Column.ALIGN_LEFT);
    add(JVereinPlugin.getI18n().tr("Austritt"), "austritt", true,
        new DateFormatter(Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add(JVereinPlugin.getI18n().tr("Kündigung"), "kuendigung", false,
        new DateFormatter(Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
    add(JVereinPlugin.getI18n().tr("Eingabedatum"), "eingabedatum", false,
        new DateFormatter(Einstellungen.DATEFORMAT), Column.ALIGN_AUTO);
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
