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
package de.jost_net.JVerein.io;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.GenericObject;

public class BLZUpdate implements GenericObject
{
  private String id;

  private Mitglied mitglied;

  private String newBLZ;

  public BLZUpdate(Mitglied mitglied, String newBLZ) throws RemoteException
  {
    this.id = mitglied.getID();
    this.mitglied = mitglied;
    this.newBLZ = newBLZ;
  }

  public boolean equals(GenericObject arg0) throws RemoteException
  {
    return id.equals(arg0.getID());
  }

  public Object getAttribute(String arg0) throws RemoteException
  {
    if (arg0.equals("id"))
    {
      return id;
    }
    else if (arg0.equals("mitglied"))
    {
      return mitglied;
    }
    else if (arg0.equals("namevorname"))
    {
      return mitglied.getNameVorname();
    }
    else if (arg0.equals("oldblz"))
    {
      return mitglied.getBlz();
    }
    else if (arg0.equals("newblz"))
    {
      return newBLZ;
    }
    return null;
  }

  public String[] getAttributeNames() throws RemoteException
  {
    return new String[] { "id", "mitglied", "namevorname", "newblz", "oldblz" };
  }

  public String getID() throws RemoteException
  {
    return id;
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return mitglied.getNameVorname();
  }

  public String toString()
  {
    return newBLZ;
  }

}
