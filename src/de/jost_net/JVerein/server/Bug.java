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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.io.IAdresse;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;

public class Bug
{
  private IAdresse object;

  private String meldung;

  private int klassifikation;

  public static final int ERROR = 1;

  public static final int WARNING = 2;

  public static final int HINT = 3;

  public Bug(IAdresse object, String meldung, int klassifikation)
  {
    this.object = object;
    this.meldung = meldung;
    this.klassifikation = klassifikation;
  }

  public Object getObject()
  {
    return object;
  }

  public String getName() throws RemoteException
  {
    return Adressaufbereitung.getNameVorname(object);
  }

  public String getMeldung()
  {
    return meldung;
  }

  public int getKlassifikation()
  {
    return klassifikation;
  }

  public String getKlassifikationText()
  {
    switch (klassifikation)
    {
      case ERROR:
        return "Fehler";
      case WARNING:
        return "Warnung";
      case HINT:
        return "Hinweis";
    }
    return "Programmfehler!";
  }
}