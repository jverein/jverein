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

public interface IAdresse
{
  /**
   * N = Natürliche Person, J = Juristische Person
   */
  public String getPersonenart() throws RemoteException;

  public String getAnrede() throws RemoteException;

  public String getTitel() throws RemoteException;

  public String getName() throws RemoteException;

  public String getVorname() throws RemoteException;

  public String getStrasse() throws RemoteException;

  public String getAdressierungszusatz() throws RemoteException;

  public String getPlz() throws RemoteException;

  public String getOrt() throws RemoteException;

  public String getStaat() throws RemoteException;

  public String getGeschlecht() throws RemoteException;
}
