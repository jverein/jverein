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

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Projekt;
import de.willuhn.datasource.GenericObject;

/**
 * Hilfs-Objekt
 */
public class ProjektSaldoZeile implements GenericObject
{

  private Projekt projekt;

  private Buchungsart buchungsart;

  private String text;

  private Double umbuchungen;

  private Double einnahmen;

  private Double ausgaben;
  
  public static final int UNDEFINED = 0;

  public static final int HEADER = 1;

  public static final int DETAIL = 2;

  public static final int SALDOFOOTER = 3;

  public static final int SALDOGEWINNVERLUST = 4;

  public static final int GESAMTSALDOFOOTER = 5;

  public static final int GESAMTSALDOGEWINNVERLUST = 6;

  public static final int NICHTZUGEORDNETEBUCHUNGEN = 7;

  private int status = UNDEFINED;

  public ProjektSaldoZeile(int status, Projekt projekt)
  {
    this.projekt = projekt;
    this.status = status;
    this.buchungsart = null;
    this.text = null;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
  }

  public ProjektSaldoZeile(int status, Buchungsart buchungsart,
      Double einnahmen, Double ausgaben, Double umbuchungen)
  {
    this.status = status;
    this.projekt = null;
    this.buchungsart = buchungsart;
    this.text = null;
    this.umbuchungen = Double.valueOf(umbuchungen);
    this.einnahmen = Double.valueOf(einnahmen);
    this.ausgaben =Double.valueOf(ausgaben);
  }

  public ProjektSaldoZeile(int status, String text, Double einnahmen,
      Double ausgaben, Double umbuchungen)
  {
    this.status = status;
    this.projekt = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = Double.valueOf(umbuchungen);
    this.einnahmen = Double.valueOf(einnahmen);
    this.ausgaben = Double.valueOf(ausgaben);
  }

  public ProjektSaldoZeile(int status, String text, Double gewinnverlust)
  {
    this.status = status;
    this.projekt = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = null;
    this.einnahmen = Double.valueOf(gewinnverlust);
    this.ausgaben = null;
  }

  public ProjektSaldoZeile(int status, String text)
  {
    this.status = status;
    this.projekt = null;
    this.buchungsart = null;
    this.text = text;
    this.umbuchungen = null;
    this.einnahmen = null;
    this.ausgaben = null;
  }

  public int getStatus()
  {
    return status;
  }

  @Override
  public Object getAttribute(String arg0) throws RemoteException
  {
    if (arg0.equals("projektbezeichnung"))
    {
      if (projekt == null && text != null)
      {
        return text;
      }
      return projekt != null ? projekt.getBezeichnung() : "";
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
    throw new RemoteException(String.format("Ung?ltige Spaltenbezeichung: %s",
        arg0));
  }

  @Override
  public String[] getAttributeNames()
  {
    return new String[] { "projektbezeichnung",
        "buchungsartbezeichnung", "einnahmen", "ausgaben",
        "umbuchungen" };
  }

  @Override
  public String getID() throws RemoteException
  {
    return projekt.getID();
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "projektbezeichnung";
  }

  @Override
  public boolean equals(GenericObject arg0) throws RemoteException
  {
    if (arg0 == null || !(arg0 instanceof ProjektSaldoZeile))
    {
      return false;
    }
    return this.getID().equals(arg0.getID());
  }
}