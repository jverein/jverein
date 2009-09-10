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

  public BuchungsklasseSaldoZeile(Buchungsklasse buchungsklasse)
  {
    this.buchungsklasse = buchungsklasse;
    this.buchungsart = null;
    this.text = null;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
  }

  public BuchungsklasseSaldoZeile(Buchungsart buchungsart, Double einnahmen,
      Double ausgaben, Double umbuchungen)
  {
    this.buchungsklasse = null;
    this.buchungsart = buchungsart;
    this.text = null;
    this.umbuchungen = new Double(umbuchungen);
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
  }

  public BuchungsklasseSaldoZeile(String text, Double einnahmen,
      Double ausgaben, Double umbuchungen)
  {
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = new Double(umbuchungen);
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
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
    throw new RemoteException("Ungültige Spaltenbezeichung: " + arg0);
  }

  public String[] getAttributeNames() throws RemoteException
  {
    return new String[] { "buchungsklassenbezeichnung",
        "buchungsartbezeichnung", "anfangsbestand", "einnahmen", "ausgaben",
        "umbuchungen" };
  }

  public String getID() throws RemoteException
  {
    return buchungsklasse.getID();
  }

  public String getPrimaryAttribute() throws RemoteException
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
