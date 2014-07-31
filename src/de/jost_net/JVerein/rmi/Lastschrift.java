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

import de.jost_net.JVerein.io.IAdresse;
import de.willuhn.datasource.rmi.DBObject;

public interface Lastschrift extends DBObject, IAdresse
{

  public Abrechnungslauf getAbrechnungslauf() throws RemoteException;

  public void setAbrechnungslauf(int abrechnungslauf) throws RemoteException;

  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(int mitglied) throws RemoteException;

  public Kursteilnehmer getKursteilnehmer() throws RemoteException;

  public void setKursteilnehmer(int kursteilnehmer) throws RemoteException;

  @Override
  public String getPersonenart() throws RemoteException;

  public void setPersonenart(String personenart) throws RemoteException;

  @Override
  public String getAnrede() throws RemoteException;

  public void setAnrede(String anrede) throws RemoteException;

  @Override
  public String getTitel() throws RemoteException;

  public void setTitel(String titel) throws RemoteException;

  @Override
  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  @Override
  public String getVorname() throws RemoteException;

  public void setVorname(String vorname) throws RemoteException;

  @Override
  public String getStrasse() throws RemoteException;

  public void setStrasse(String strasse) throws RemoteException;

  @Override
  public String getAdressierungszusatz() throws RemoteException;

  public void setAdressierungszusatz(String adressierungszusatz)
      throws RemoteException;

  @Override
  public String getPlz() throws RemoteException;

  public void setPlz(String plz) throws RemoteException;

  @Override
  public String getOrt() throws RemoteException;

  public void setOrt(String ort) throws RemoteException;

  @Override
  public String getStaat() throws RemoteException;

  public void setStaat(String staat) throws RemoteException;

  public String getEmail() throws RemoteException;

  public void setEmail(String email) throws RemoteException;

  public String getMandatID() throws RemoteException;

  public void setMandatID(String mandatid) throws RemoteException;

  public Date getMandatDatum() throws RemoteException;

  public void setMandatDatum(Date mandatdatum) throws RemoteException;

  public String getMandatSequence() throws RemoteException;

  public void setMandatSequence(String mandatsequence) throws RemoteException;

  public String getBIC() throws RemoteException;

  public void setBIC(String bic) throws RemoteException;

  public String getIBAN() throws RemoteException;

  public void setIBAN(String iban) throws RemoteException;

  public String getVerwendungszweck() throws RemoteException;

  public void setVerwendungszweck(String verwendungszweck)
      throws RemoteException;

  public Double getBetrag() throws RemoteException;

  public void setBetrag(Double betrag) throws RemoteException;

  @Override
  public String getGeschlecht() throws RemoteException;

  public void setGeschlecht(String geschlecht) throws RemoteException;

  public void set(IAdresse adr) throws RemoteException;

}
