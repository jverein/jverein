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

import de.jost_net.JVerein.io.ILastschrift;
import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.util.ApplicationException;

public interface Kursteilnehmer extends DBObject, ILastschrift
{

  public void setID(String id) throws RemoteException;

  public void setPersonenart(String personenart) throws RemoteException;

  public void setAnrede(String anrede) throws RemoteException;

  public void setTitel(String titel) throws RemoteException;

  public void setName(String name) throws RemoteException;

  public void setVorname(String vorname) throws RemoteException;

  public void setStrasse(String strasse) throws RemoteException;

  public void setAdressierungszuatz(String adressierungszusatz)
      throws RemoteException;

  public void setPlz(String plz) throws RemoteException;

  public void setOrt(String ort) throws RemoteException;

  public void setStaat(String staat) throws RemoteException;

  public String getEmail() throws RemoteException;

  public void setEmail(String email) throws RemoteException;

  public String getVZweck1() throws RemoteException;

  public void setVZweck1(String vzweck1) throws RemoteException;

  public void setMandatDatum(Date mandatdatum) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public Date getGeburtsdatum() throws RemoteException;

  public void setGeburtsdatum(Date geburtsdatum) throws RemoteException;

  public void setGeburtsdatum(String geburtsdatum) throws RemoteException;

  @Override
  public String getGeschlecht() throws RemoteException;

  public void setGeschlecht(String geschlecht) throws RemoteException;

  public void setEingabedatum() throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public void setAbbudatum() throws RemoteException;

  public void resetAbbudatum() throws RemoteException;

  public Date getAbbudatum() throws RemoteException;

  public void insert() throws RemoteException, ApplicationException;

}
