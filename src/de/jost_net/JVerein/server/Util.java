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

public class Util
{

  public static boolean getBoolean(Object o) throws RemoteException
  {
    if (o == null)
    {
      return false;
    }
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof Byte)
    {
      Byte b = (Byte) o;
      return (b == 1);
    }
    if (o instanceof String)
    {
      String v = (String) o;
      return (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("j") || v
          .equalsIgnoreCase("1"));
    }
    throw new RemoteException("Weder null, noch String oder Boolean");
  }
}
