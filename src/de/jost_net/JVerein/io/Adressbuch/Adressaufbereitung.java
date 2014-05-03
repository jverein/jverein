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
package de.jost_net.JVerein.io.Adressbuch;

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.io.IAdresse;

public class Adressaufbereitung
{
  public static String getAdressfeld(IAdresse adr) throws RemoteException
  {
    String empfaenger = (adr.getAnrede() != null
        && adr.getAnrede().length() > 0 ? adr.getAnrede() + "\n" : "")
        + getVornameName(adr)
        + "\n"
        + (adr.getAdressierungszusatz() != null
            && adr.getAdressierungszusatz().length() > 0 ? adr
            .getAdressierungszusatz() + "\n" : "")
        + (adr.getStrasse() != null && adr.getStrasse().length() > 0 ? adr
            .getStrasse() + "\n" : "")
        + (adr.getPlz() != null && adr.getPlz().length() > 0 ? adr.getPlz()
            + " " : "")
        + (adr.getOrt() != null && adr.getOrt().length() > 0 ? adr.getOrt()
            : "");
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
    if (adresse == null)
    {
      adresse = "";
    }
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

  public static String getAnredeFoermlich(IAdresse adr) throws RemoteException
  {
    String anredefoermlich = "Sehr geehrte";
    if (adr.getGeschlecht() != null)
    {
      if (adr.getGeschlecht().equals(GeschlechtInput.MAENNLICH))
      {
        anredefoermlich += "r Herr " + getEmptyIfNull(adr.getTitel())
            + (getEmptyIfNull(adr.getTitel()).length() > 0 ? " " : "")
            + adr.getName() + ",";
      }
      else if (adr.getGeschlecht().equals(GeschlechtInput.WEIBLICH))
      {
        anredefoermlich += " Frau " + getEmptyIfNull(adr.getTitel())
            + (getEmptyIfNull(adr.getTitel()).length() > 0 ? " " : "")
            + adr.getName() + ",";
      }
      else if (adr.getGeschlecht().equals(GeschlechtInput.OHNEANGABE))
      {
        anredefoermlich = "Guten Tag " + getVornameName(adr) + ",";
      }
      else
      {
        anredefoermlich += " Damen und Herren,";
      }
    }
    else
    {
      anredefoermlich += " Damen und Herren,";
    }
    return anredefoermlich;
  }

  public static String getAnredeDu(IAdresse adr) throws RemoteException
  {
    String anrededu = "Hallo";
    if (adr.getPersonenart().equals("n"))
    {
      anrededu += " " + adr.getVorname();
    }
    anrededu += ",";
    return anrededu;
  }

  private static String getEmptyIfNull(String string)
  {
    if (string == null)
    {
      return "";
    }
    return string;
  }

}
