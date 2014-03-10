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
package de.jost_net.JVerein.io.Adressbuch;

import java.rmi.RemoteException;

import de.jost_net.JVerein.io.IAdresse;

public class Adressaufbereitung
{

  public static String getAdressfeld(IAdresse adr) throws RemoteException
  {
    String empfaenger = adr.getAnrede()
        + "\n"
        + getVornameName(adr)
        + "\n"
        + (adr.getAdressierungszusatz() != null
            && adr.getAdressierungszusatz().length() > 0
            ? adr.getAdressierungszusatz() + "\n" : "") + adr.getStrasse()
        + "\n" + adr.getPlz() + " " + adr.getOrt();
    if (adr.getStaat() != null && adr.getStaat().length() > 0)
    {
      empfaenger += "\n" + adr.getStaat();
    }
    return empfaenger;

  }

  /**
   * Gibt den Namen aufbereitet zurück, Meier, Dr. Willi
   */
  public static String getNameVorname(IAdresse adr) throws RemoteException
  {
    String ret = adr.getName() + ", ";
    if (adr.getTitel() != null && adr.getTitel().length() > 0)
    {
      ret += adr.getTitel() + " ";
    }
    ret += adr.getVorname();
    return ret;
  }

  /**
   * Gibt den Namen aufbereitet zurück: Dr. Willi Meier
   */
  public static String getVornameName(IAdresse adr) throws RemoteException
  {
    String ret = "";
    if (adr.getPersonenart().equals("n"))
    {
      ret = adr.getTitel();
      if (ret == null)
      {
        ret = "";
      }
      if (ret.length() > 0)
      {
        ret += " ";
      }
      ret += adr.getVorname() + " " + adr.getName();
    }
    else
    {
      ret = adr.getName()
          + (adr.getVorname().length() > 0 ? ("\n" + adr.getVorname()) : "");
    }
    return ret;
  }

  public static String getAnschrift(IAdresse adr) throws RemoteException
  {
    final String plzOrt = addAnschriftFeld(adr.getPlz(), " ", adr.getOrt());
    String adresse = addAnschriftFeld(adr.getAdressierungszusatz(), ", ",
        adr.getStrasse());
    adresse = addAnschriftFeld(adresse, ", ", plzOrt);
    adresse = addAnschriftFeld(adresse, ", ", adr.getStaat());
    return adresse;
  }

  private static String addAnschriftFeld(String anschrift, String trenner,
      String feldWert)
  {
    if (null == feldWert)
      return anschrift;
    if (feldWert.length() == 0)
      return anschrift;
    if (null == anschrift)
      return feldWert;
    if (anschrift.length() == 0)
      return feldWert;
    return anschrift + trenner + feldWert;
  }

}
