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

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.datasource.GenericObject;

/**
 * Hilfs-Objekt
 */
public class BuchungsklasseSaldoZeile implements GenericObject
{

  private Buchungsklasse buchungsklasse;

  private Buchungsart buchungsart;

  private String text;

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

  public static final int NICHTZUGEORDNETEBUCHUNGEN = 7;

  private int status = UNDEFINED;

  public BuchungsklasseSaldoZeile(int status, Buchungsklasse buchungsklasse)
  {
    this.buchungsklasse = buchungsklasse;
    this.status = status;
    this.buchungsart = null;
    this.text = null;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
  }

  public BuchungsklasseSaldoZeile(int status, Buchungsart buchungsart,
      Double einnahmen, Double ausgaben, Double umbuchungen)
  {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = buchungsart;
    this.text = null;
    this.umbuchungen = new Double(umbuchungen);
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
  }

  public BuchungsklasseSaldoZeile(int status, String text, Double einnahmen,
      Double ausgaben, Double umbuchungen)
  {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = new Double(umbuchungen);
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
  }

  public BuchungsklasseSaldoZeile(int status, String text, Double gewinnverlust)
  {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = null;
    this.einnahmen = new Double(gewinnverlust);
    this.ausgaben = null;
  }

  public BuchungsklasseSaldoZeile(int status, String text,
      Integer anzahlbuchungen)
  {
    this.status = status;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
    this.anzahlbuchungen = anzahlbuchungen;
  }

  public int getStatus()
  {
    return status;
  }

  public Object getAttribute(String arg0) throws RemoteException
  {
    if (arg0.equals("buchungsklassenbezeichnung"))
    {
      if (buchungsklasse == null && text != null)
      {
        return text;
      }
      return buchungsklasse != null ? buchungsklasse.getBezeichnung() : "";
    }
    if (arg0.equals("buchungsartbezeichnung"))
    {
      return buchungsart != null ? buchungsart.getBezeichnung() : "";
    }
    else if (arg0.equals("einnahmen"))
    {
      return einnahmen;
    }
    else if (arg0.equals("ausgaben"))
    {
      return ausgaben;
    }
    else if (arg0.equals("umbuchungen"))
    {
      return umbuchungen;
    }
    else if (arg0.equals("anzahlbuchungen"))
    {
      return anzahlbuchungen;
    }
    throw new RemoteException("Ungültige Spaltenbezeichung: " + arg0);
  }

  public String[] getAttributeNames()
  {
    return new String[] { "buchungsklassenbezeichnung",
        "buchungsartbezeichnung", "anfangsbestand", "einnahmen", "ausgaben",
        "umbuchungen", "anzahlbuchungen" };
  }

  public String getID() throws RemoteException
  {
    return buchungsklasse.getID();
  }

  public String getPrimaryAttribute()
  {
    return "buchungsklasse";
  }

  public boolean equals(GenericObject arg0) throws RemoteException
  {
    if (arg0 == null || !(arg0 instanceof BuchungsklasseSaldoZeile))
    {
      return false;
    }
    return this.getID().equals(arg0.getID());
  }
}
