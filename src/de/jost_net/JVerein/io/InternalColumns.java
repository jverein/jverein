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
package de.jost_net.JVerein.io;

import java.util.LinkedList;
import java.util.List;

public enum InternalColumns
{
  ADRESSTYP("Adresstyp", false), //
  ADRZUSATZ("Adressierungszusatz", false), //
  ANREDE("Anrede", true), //
  AUSTRITTSDATUM("Austritt", true), //
  BIC("BIC", false), // TODO true), //
  BLZ("Bankleitzahl", true), //
  BEITRAG("Beitrag_1", true), //
  BEITRAGSART("Beitragsart_1", true), //
  BEITRAGINDI("individuellerbeitrag", false), //
  EINTRITTSDATUM("Eintritt", true), //
  EMAIL("Email", true), //
  GEBDATUM("Geburtsdatum", true), //
  GESCHLECHT("Geschlecht", true), //
  IBAN("IBAN", true), //
  KONTONR("Kontonummer", true), //
  KTOIPERSONENART("KtoiPersonenart", false), //
  KTOIANREDE("KtoiAnrede", false), //
  KTOITITEL("KtoiTitel", false), //
  KTOINAME("KtoiName", false), //
  KTOIVORNAME("KtoiVorname", false), //
  KTOISTRASSE("KtoiStrasse", false), //
  KTOIADRESSIERUNGSZUSATZ("KtoiAdressierungszusatz", false), //
  KTOIPLZ("KtoiPlz", false), //
  KTOIORT("KtoiOrt", false), //
  KTOISTAAT("KtoiStaat", false), //
  KTOIEMAIL("KtoiEMail", false), //
  KUENDIGUNGSDATUM("Kuendigung", true), //
  MANDATDATUM("Mandat_Datum", true), //
  MANDATSEQUENCE("Mandat_Sequence", true), //
  MANDATVERSION("Mandat_Version", true), //
  MITGLIEDSNR("Mitglieds_Nr", true), //
  NACHNAME("Nachname", true), //
  ORT("Ort", true), //
  PERSONENART("Personenart", false), //
  PLZ("Plz", true), //
  STAAT("Staat", false), //
  STRASSE("Strasse", true), //
  TELEPRIVAT("Telefon_privat", true), //
  TELEDIENST("Telefon_dienstlich", true), //
  TELEMOBIL("Handy", false), //
  TITEL("Titel", true), //
  STERBEDATUM("Sterbetag", true), //
  VERMERKA("Vermerk1", false), //
  VERMERKB("Vermerk2", false), //
  VORNAME("Vorname", true), //
  ZAHLART("Zahlungsart", true), //
  ZAHLRYTHM("Zahlungsrhytmus", false), //
  ZAHLUNGSTERMIN("Zahlungstermin", false);

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
