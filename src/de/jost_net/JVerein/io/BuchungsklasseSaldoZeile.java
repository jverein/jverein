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
 * Revision 1.2  2009/09/12 19:05:14  jost
 * neu: Buchungsklassen
 *
 * Revision 1.1  2009/09/10 18:19:09  jost
 * neu: Buchungsklassen
 *
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

  public static final int UNDEFINED = 0;

  public static final int HEADER = 1;

  public static final int DETAIL = 2;

  public static final int FOOTER = 3;
  
  public static final int FOOTER2 = 4;

  private int status = UNDEFINED;

  public BuchungsklasseSaldoZeile(Buchungsklasse buchungsklasse)
  {
    this.buchungsklasse = buchungsklasse;
    this.status = HEADER;
    this.buchungsart = null;
    this.text = null;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
  }

  public BuchungsklasseSaldoZeile(Buchungsart buchungsart, Double einnahmen,
      Double ausgaben, Double umbuchungen)
  {
    this.status = DETAIL;
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
    this.status = FOOTER;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = new Double(umbuchungen);
    this.einnahmen = new Double(einnahmen);
    this.ausgaben = new Double(ausgaben);
  }

  public BuchungsklasseSaldoZeile(String text, Double gewinnverlust)
  {
    this.status = FOOTER2;
    this.buchungsklasse = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = null;
    this.einnahmen = new Double(gewinnverlust);
    this.ausgaben = null;
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
