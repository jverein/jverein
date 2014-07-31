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
import java.util.Date;

import de.willuhn.datasource.rmi.DBIterator;

public class MitgliedUtils
{

  public static void setNurAktive(DBIterator it, Date datum)
      throws RemoteException
  {
    it.addFilter("(eintritt is null or eintritt <= ?)", new Object[] { datum});
    it.addFilter("(austritt is null or austritt > ?)", new Object[] { datum});
  }

  public static void setNurAktive(DBIterator it) throws RemoteException
  {
    setNurAktive(it, new Date());
  }

  public static void setMitglied(DBIterator it) throws RemoteException
  {
    it.addFilter("adresstyp = 1");
  }

  public static void setMitgliedOderSpender(DBIterator it)
      throws RemoteException
  {
    it.addFilter("(adresstyp = 1 or adresstyp = 2)");
  }

  public static void setMitgliedNatuerlichePerson(DBIterator it)
      throws RemoteException
  {
    it.addFilter("personenart = 'n'");
  }

  public static void setMitgliedJuristischePerson(DBIterator it)
      throws RemoteException
  {
    it.addFilter("personenart = 'j'");
  }

}
