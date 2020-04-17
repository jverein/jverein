/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.datasource.GenericObject;

/**
 * Hilfs-Objekt
 */
public class BuchungsklasseSaldoZeile implements GenericObject {

  private Buchungsklasse buchungsklasse;

  private Buchungsart buchungsart;

  private String text;

  private String text_buchungsart;

  private Double umbuchungen;

  private Double einnahmen;

  private Double ausgaben;

  private Integer anzahlbuchungen;

  public static final int UNDEFINED = 0;

  public static final int HEADER = 1;

  public static final int DETAIL = 2;

  public static final int SALDOFOOTER = 3;

  public static final int SALDOGEWINNVERLUST = 4;

  public static final int GESAMTSALDOFOOTER = 5;

  public static final int GESAMTGEWINNVERLUST = 6;

  public static final int STEUERHEADER = 7;

  public static final int STEUER = 8;

  public static final int NICHTZUGEORDNETEBUCHUNGEN = 9;

  private int status = UNDEFINED;

  public BuchungsklasseSaldoZeile(int status, Buchungsklasse buchungsklasse) {
    this.status = status;
    this.buchungsklasse = buchungsklasse;
    this.buchungsart = null;
    this.text = null;
    this.text_buchungsart = null;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
  }

  public BuchungsklasseSaldoZeile(int status, Buchungsart buchungsart, Double einnahmen,
      Double ausgaben, Double umbuchungen) {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = buchungsart;
    this.text = null;
    this.text_buchungsart = null;
    this.umbuchungen = new Double(umbuchungen);
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
  }

  public BuchungsklasseSaldoZeile(int status, String text, Double einnahmen, Double ausgaben,
      Double umbuchungen) {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.text_buchungsart = null;
    this.umbuchungen = new Double(umbuchungen);
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
  }

  public BuchungsklasseSaldoZeile(int status, String text, Double gewinnverlust) {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.text_buchungsart = null;
    this.umbuchungen = null;
    this.einnahmen = new Double(gewinnverlust);
    this.ausgaben = null;
  }

  public BuchungsklasseSaldoZeile(int status, String text, Integer anzahlbuchungen) {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.text_buchungsart = null;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
    this.anzahlbuchungen = anzahlbuchungen;
  }

  public BuchungsklasseSaldoZeile(int status, String text, String text_buchungsart,
      Double einnahmen, Double ausgaben) {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.text_buchungsart = text_buchungsart;
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
    this.umbuchungen = null;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public Object getAttribute(String arg0) throws RemoteException {
    if (arg0.equals("buchungsklassenbezeichnung")) {
      if (buchungsklasse == null && text != null) {
        return text;
      }
      return buchungsklasse != null ? buchungsklasse.getBezeichnung() : "";
    }
    if (arg0.equals("buchungsartbezeichnung")) {
      if (text_buchungsart != null) {
        return text_buchungsart;
      } else {
        return (buchungsart != null ? buchungsart.getBezeichnung() : "");

      }
    } else if (arg0.equals("einnahmen")) {
      return einnahmen;
    } else if (arg0.equals("ausgaben")) {
      return ausgaben;
    } else if (arg0.equals("umbuchungen")) {
      return umbuchungen;
    } else if (arg0.equals("anzahlbuchungen")) {
      return anzahlbuchungen;
    }
    throw new RemoteException(String.format("Ungültige Spaltenbezeichung: %s", arg0));
  }

  @Override
  public String[] getAttributeNames() {
    return new String[] {"buchungsklassenbezeichnung", "buchungsartbezeichnung", "anfangsbestand",
        "einnahmen", "ausgaben", "umbuchungen", "anzahlbuchungen"};
  }

  @Override
  public String getID() throws RemoteException {
    return buchungsklasse.getID();
  }

  @Override
  public String getPrimaryAttribute() {
    return "buchungsklasse";
  }

  @Override
  public boolean equals(GenericObject arg0) throws RemoteException {
    if (arg0 == null || !(arg0 instanceof BuchungsklasseSaldoZeile)) {
      return false;
    }
    return this.getID().equals(arg0.getID());
  }
}
