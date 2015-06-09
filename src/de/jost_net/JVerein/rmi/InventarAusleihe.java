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
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface InventarAusleihe extends DBObject
{
  public Inventar getInventar() throws RemoteException;

  public void setInventar(Inventar inventar) throws RemoteException;

  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(Mitglied mitglied) throws RemoteException;

  public String getNichtmitglied() throws RemoteException;

  public void setNichtmitglied(String ausleihernichtmitglied)
      throws RemoteException;

  public Date getVon() throws RemoteException;

  public void setVon(Date von) throws RemoteException;

  public Date getBis() throws RemoteException;

  public void setBis(Date bis) throws RemoteException;

  public Date getRueckgabedatum() throws RemoteException;

  public void setRueckgabedatum(Date rueckgabedatum) throws RemoteException;

}
