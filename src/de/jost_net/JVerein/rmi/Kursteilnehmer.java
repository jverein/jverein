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
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.io.IBankverbindung;
import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.util.ApplicationException;

public interface Kursteilnehmer extends DBObject, IBankverbindung
{
  public void setID(String id) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public String getVZweck1() throws RemoteException;

  public void setVZweck1(String vzweck1) throws RemoteException;

  public String getVZweck2() throws RemoteException;

  public void setVZweck2(String vzweck2) throws RemoteException;

  @Override
  public String getBic() throws RemoteException;

  @Override
  public void setBic(String bic) throws RemoteException;

  @Override
  public String getIban() throws RemoteException;

  @Override
  public void setIban(String iban) throws RemoteException;

  @Override
  public String getBlz() throws RemoteException;

  @Override
  public void setBlz(String blz) throws RemoteException;

  @Override
  public String getKonto() throws RemoteException;

  @Override
  public void setKonto(String konto) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public Date getGeburtsdatum() throws RemoteException;

  public void setGeburtsdatum(Date geburtsdatum) throws RemoteException;

  public void setGeburtsdatum(String geburtsdatum) throws RemoteException;

  public String getGeschlecht() throws RemoteException;

  public void setGeschlecht(String geschlecht) throws RemoteException;

  public void setEingabedatum() throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public void setAbbudatum() throws RemoteException;

  public void resetAbbudatum() throws RemoteException;

  public Date getAbbudatum() throws RemoteException;

  public void insert() throws RemoteException, ApplicationException;

}
