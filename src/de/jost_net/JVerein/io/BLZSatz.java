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

import java.io.BufferedInputStream;
import java.io.IOException;

public class BLZSatz
{
  private BufferedInputStream bi;

  /**
   * Die Bankleitzahl dient der eindeutigen Identifizierung eines
   * Zahlungsdienstleisters.
   */
  private String blz;

  /**
   * Merkmal, ob bankleitzahlführender Zahlungsdienstleister ("1") oder nicht
   * ("2")
   */
  private String zahlungsdienstleister;

  /**
   * Bezeichnung des Zahlungsdienstleisters
   */
  private String bezeichnung;

  private String plz;

  private String ort;

  private String kurzbezeichnung;

  /**
   * Für den Kartenzahlungsverkehr mittels Bankkundenkarten die am
   * girocard-System teilnehmen, haben die Spitzenverbände des Kreditgewerbes
   * eine gesonderte Institutsnummerierung festgelegt; danach erhält der
   * kartenausgebende Zahlungsdienstleister eine fünfstellige Institutsnummer
   * für PAN (= Primary Account Number). Einer Institutsnummer für PAN ist immer
   * nur genau eine Bankleitzahl zugeordnet.
   */
  private String institutsnummerPAN;

  /**
   * Der Bank Identifier Code (BIC) besteht aus acht oder elf zusammenhängenden
   * Stellen und setzt sich aus den Komponenten BANKCODE (4 Stellen), LÄNDERCODE
   * (2 Stellen), ORTSCODE (2 Stellen) sowie ggf. einem FILIALCODE (3 Stellen)
   * zusammen.
   */
  private String bic;

  /**
   * Zahlungsdienstleister sind verpflichtet, zum Zahlungsverkehr ausschließlich
   * prüfziffergesicherte Kontonummern gemäß ihrer in der Bankleitzahlendatei
   * angegebenen Prüfzifferberechnungsmethode zu verwenden. Die Angabe der
   * Prüfzifferberechnungsmethode "09" (keine Prüfzifferberechnung) ist
   * zulässig.
   */
  private String pruefziffernmethode;

  /**
   * Bei jeder Neuanlage eines Datensatzes wird automatisiert eine eindeutige
   * Nummer vergeben. Eine einmal verwendete Nummer wird nicht noch einmal
   * vergeben.
   */
  private String nummer;

  /**
   * Seit dem letzten Abschluss der Bankleitzahlendatei neu hinzugekommene
   * Datensätze werden mit "A" (Addition), geänderte Datensätze mit "M"
   * (Modified), unveränderte Datensätze mit "U" (Unchanged) gekennzeichnet.
   * Gelöschte Datensätze werden mit "D" (Deletion) gekennzeichnet und sind -
   * als Hinweis - letztmalig in der Bankleitzahlendatei enthalten. Diese
   * Datensätze sind ab dem Gültigkeitstermin der Bankleitzahlendatei im
   * Zahlungsverkehr nicht mehr zu verwenden.
   */
  private String aenderungskennzeichen;

  /**
   * Zur frühzeitigen Information der Teilnehmer am Zahlungsverkehr und zur
   * Beschleunigung der Umstellung der Bankverbindung kann ein
   * Zahlungsdienstleister, der die Löschung einer Bankleitzahl mit dem Merkmal
   * "1" im Feld 2 beabsichtigt, die Löschung ankündigen. Die Ankündigung kann
   * erfolgen, sobald der Zahlungsdienstleister seine Kunden über die geänderte
   * Kontoverbindung informiert hat. Das Feld enthält das Merkmal "0" (keine
   * Angabe) oder "1" (Bankleitzahl im Feld 1 ist zur Löschung vorgesehen).
   */
  private String hinweisloeschung;

  /**
   * Das Feld enthält entweder den Wert "00000000" (Bankleitzahl ist nicht zur
   * Löschung vorgesehen bzw. der Zahlungsdienstleister hat keine
   * Nachfolge-Bankleitzahl veröffentlicht) oder die Angabe einer
   * "Bankleitzahl". Eine Bankleitzahl kann angegeben sein, wenn das Feld 2 das
   * Merkmal "1" enthält und entweder die bevorstehende Löschung der
   * Bankleitzahl angekündigt wurde (Feld 12 = "1") oder die Bankleitzahl zum
   * aktuellen Gültigkeitstermin gelöscht wird (Feld 11 = "D"). Auf Grund der
   * Veröffentlichung einer Nachfolge-Bankleitzahl können Anwender diese in
   * Zahlungsverkehrsdateien verwenden. Dazu wird in den Kontostammdaten – unter
   * Beibehaltung der Kontonummer - die zur Löschung angekündigte Bankleitzahl
   * bzw. die gelöschte Bankleitzahl im Feld 1 der Bankleitzahlendatei durch die
   * Nachfolge-Bankleitzahl dauerhaft ersetzt. Zahlungsdienstleister sind nicht
   * berechtigt, in Zahlungsverkehrsdateien Bankleitzahlen durch
   * Nachfolge-Bankleitzahlen zu ersetzen.
   */
  private String nachfolgeblz;

  public BLZSatz(BufferedInputStream bin) throws IOException
  {
    this.bi = bin;
    blz = getString(8);
    zahlungsdienstleister = getString(1);
    bezeichnung = getString(58);
    plz = getString(5);
    ort = getString(35);
    kurzbezeichnung = getString(27);
    institutsnummerPAN = getString(5);
    bic = getString(11);
    pruefziffernmethode = getString(2);
    nummer = getString(6);
    aenderungskennzeichen = getString(1);
    hinweisloeschung = getString(1);
    nachfolgeblz = getString(8);
    getString(2);// CRLF überlesen
  }

  private String getString(int len) throws IOException
  {
    byte[] b = new byte[len];
    bi.read(b);
    return new String(b);
  }

  public String getBlz()
  {
    return blz;
  }

  public String getZahlungsdienstleister()
  {
    return zahlungsdienstleister;
  }

  public String getBezeichnung()
  {
    return bezeichnung;
  }

  public String getPlz()
  {
    return plz;
  }

  public String getOrt()
  {
    return ort;
  }

  public String getKurzbezeichnung()
  {
    return kurzbezeichnung;
  }

  public String getInstitutsnummerPAN()
  {
    return institutsnummerPAN;
  }

  public String getBic()
  {
    return bic;
  }

  public String getPruefziffernmethode()
  {
    return pruefziffernmethode;
  }

  public String getNummer()
  {
    return nummer;
  }

  public String getAenderungskennzeichen()
  {
    return aenderungskennzeichen;
  }

  public String getHinweisloeschung()
  {
    return hinweisloeschung;
  }

  public String getNachfolgeblz()
  {
    return nachfolgeblz;
  }

  public boolean hasNext() throws IOException
  {
    return bi.available() > 0;
  }

  @Override
  public String toString()
  {
    return blz + ", " + bezeichnung + ", " + bic + ", " + zahlungsdienstleister;
  }
}
