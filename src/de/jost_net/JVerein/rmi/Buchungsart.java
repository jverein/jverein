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

import de.willuhn.datasource.rmi.DBObject;

public interface Buchungsart extends DBObject
{
  public int getNummer() throws RemoteException;

  public void setNummer(int nummer) throws RemoteException;

  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  public int getArt() throws RemoteException;

  public void setArt(int art) throws RemoteException;

  public Buchungsklasse getBuchungsklasse() throws RemoteException;

  public int getBuchungsklasseId() throws RemoteException;

  public void setBuchungsklasse(Integer buchungsklasse) throws RemoteException;

  public Boolean getSpende() throws RemoteException;

  public void setSpende(Boolean spende) throws RemoteException;
  
  public double getSteuersatz() throws RemoteException;

  public void setSteuersatz(double steuersatz) throws RemoteException;

  public Buchungsart getSteuerBuchungsart() throws RemoteException;

  public void setSteuerBuchungsart(String steuer_buchungsart) throws RemoteException;
}
