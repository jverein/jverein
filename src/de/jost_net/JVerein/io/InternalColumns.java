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
package de.jost_net.JVerein.io;

import java.util.LinkedList;
import java.util.List;

import de.jost_net.JVerein.JVereinPlugin;

public enum InternalColumns
{
  ADRESSTYP(JVereinPlugin.getI18n().tr("Adresstyp"), false), //
  ADRZUSATZ(JVereinPlugin.getI18n().tr("Adressierungszusatz"), false), //
  ANREDE(JVereinPlugin.getI18n().tr("Anrede"), true), //
  AUSTRITTSDATUM(JVereinPlugin.getI18n().tr("Austritt"), true), //
  BLZ(JVereinPlugin.getI18n().tr("Bankleitzahl"), true), //
  BEITRAG(JVereinPlugin.getI18n().tr("Beitrag_1"), true), //
  BEITRAGSART(JVereinPlugin.getI18n().tr("Beitragsart_1"), true), //
  BEITRAGINDI(JVereinPlugin.getI18n().tr("individuellerbeitrag"), false), //
  EINTRITTSDATUM(JVereinPlugin.getI18n().tr("Eintritt"), true), //
  EMAIL(JVereinPlugin.getI18n().tr("Email"), true), //
  GEBDATUM(JVereinPlugin.getI18n().tr("Geburtsdatum"), true), //
  GESCHLECHT(JVereinPlugin.getI18n().tr("Geschlecht"), true), //
  KONTONR(JVereinPlugin.getI18n().tr("Kontonummer"), true), //
  KONTOINHABER(JVereinPlugin.getI18n().tr("Zahler"), true), //
  KUENDIGUNGSDATUM(JVereinPlugin.getI18n().tr("Kuendigung"), true), //
  MITGLIEDSNR(JVereinPlugin.getI18n().tr("Mitglieds_Nr"), true), //
  NACHNAME(JVereinPlugin.getI18n().tr("Nachname"), true), //
  ORT(JVereinPlugin.getI18n().tr("Ort"), true), //
  PERSONENART(JVereinPlugin.getI18n().tr("Personenart"), false), //
  PLZ(JVereinPlugin.getI18n().tr("Plz"), true), //
  STAAT(JVereinPlugin.getI18n().tr("Staat"), false), //
  STRASSE(JVereinPlugin.getI18n().tr("Strasse"), true), //
  TELEPRIVAT(JVereinPlugin.getI18n().tr("Telefon_privat"), true), //
  TELEDIENST(JVereinPlugin.getI18n().tr("Telefon_dienstlich"), true), //
  TELEMOBIL(JVereinPlugin.getI18n().tr("Handy"), false), //
  TITEL(JVereinPlugin.getI18n().tr("Titel"), true), //
  STERBEDATUM(JVereinPlugin.getI18n().tr("Sterbetag"), true), //
  VERMERKA(JVereinPlugin.getI18n().tr("Vermerk1"), false), //
  VERMERKB(JVereinPlugin.getI18n().tr("Vermerk2"), false), //
  VORNAME(JVereinPlugin.getI18n().tr("Vorname"), true), //
  ZAHLART(JVereinPlugin.getI18n().tr("Zahlungsart"), true), //
  ZAHLRYTHM(JVereinPlugin.getI18n().tr("Zahlungsrhytmus"), false);

  private final String colname;

  private final boolean nec;

  private InternalColumns(final String columnname, final boolean necessary)
  {
    this.colname = columnname;
    this.nec = necessary;
  }

  public String getColumnName()
  {
    return colname;
  }

  public boolean isNecessary()
  {
    return nec;
  }

  @Override
  public String toString()
  {
    return colname;
  }

  /**
   * 
   * @return a list with all necessary columns
   */
  public static List<String> getNecessaryColumns()
  {
    LinkedList<String> necList = new LinkedList<String>();

    for (InternalColumns tmp : InternalColumns.values())
    {
      if (tmp.isNecessary())
        necList.add(tmp.getColumnName());
    }
    return necList;
  }

  /**
   * 
   * @return a list with all optional columns
   */
  public static List<String> getOptionalColumns()
  {
    LinkedList<String> necList = new LinkedList<String>();

    for (InternalColumns tmp : InternalColumns.values())
    {
      if (!tmp.isNecessary())
        necList.add(tmp.getColumnName());
    }
    return necList;
  }
}
