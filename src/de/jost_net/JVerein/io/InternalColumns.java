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
package de.jost_net.JVerein.io;

import java.util.LinkedList;
import java.util.List;

public enum InternalColumns
{

  ADRZUSATZ("Adressierungszusatz", false), //
  ANREDE("Anrede", true), //
  AUSTRITTSDATUM("Austritt", true), //
  BLZ("Bankleitzahl", true), //
  BEITRAG("Beitrag_1", true), //
  BEITRAGSART("Beitragsart_1", true), //
  BEITRAGINDI("individuellerbeitrag", false), //
  EINTRITTSDATUM("Eintritt", true), //
  EMAIL("Email", true), //
  GEBDATUM("Geburtsdatum", true), //
  GESCHLECHT("Geschlecht", true), //
  KONTONR("Kontonummer", true), //
  KONTOINHABER("Zahler", true), //
  KUENDIGUNGSDATUM("Kuendigung", true), //
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
  ZAHLRYTHM("Zahlungsrhytmus", false);

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
