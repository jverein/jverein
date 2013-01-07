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
import java.util.Map;

import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.util.ApplicationException;

public interface Mitglied extends DBObject
{
  public void setExterneMitgliedsnummer(Integer extnr) throws RemoteException;

  public Integer getExterneMitgliedsnummer() throws RemoteException;

  public void setID(String id) throws RemoteException;

  public void setAdresstyp(Integer adresstyp) throws RemoteException;

  public Adresstyp getAdresstyp() throws RemoteException;

  public String getPersonenart() throws RemoteException;

  public void setPersonenart(String personenart) throws RemoteException;

  public String getAnrede() throws RemoteException;

  public void setAnrede(String anrede) throws RemoteException;

  public String getTitel() throws RemoteException;

  public void setTitel(String titel) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public String getVorname() throws RemoteException;

  public void setVorname(String vorname) throws RemoteException;

  public String getAdressierungszusatz() throws RemoteException;

  public void setAdressierungszusatz(String adressierungszusatz)
      throws RemoteException;

  public String getStrasse() throws RemoteException;

  public void setStrasse(String strasse) throws RemoteException;

  public String getPlz() throws RemoteException;

  public void setPlz(String plz) throws RemoteException;

  public String getOrt() throws RemoteException;

  public void setOrt(String ort) throws RemoteException;

  public String getStaat() throws RemoteException;

  public void setStaat(String staat) throws RemoteException;

  public Integer getZahlungsweg() throws RemoteException;

  public void setZahlungsweg(Integer zahlungsweg) throws RemoteException;

  public Integer getZahlungsrhytmus() throws RemoteException;

  public void setZahlungsrhytmus(Integer zahlungsrhytmus)
      throws RemoteException;

  public String getBic() throws RemoteException;

  public void setBic(String bic) throws RemoteException;

  public String getIban() throws RemoteException;

  public void setIban(String iban) throws RemoteException;

  public String getBlz() throws RemoteException;

  public void setBlz(String blz) throws RemoteException;

  public String getKonto() throws RemoteException;

  public void setKonto(String konto) throws RemoteException;

  public String getKontoinhaber() throws RemoteException;

  public void setKontoinhaber(String kontoinhaber) throws RemoteException;

  public Date getGeburtsdatum() throws RemoteException;

  public void setGeburtsdatum(Date geburtsdatum) throws RemoteException;

  public void setGeburtsdatum(String geburtsdatum) throws RemoteException;

  public String getGeschlecht() throws RemoteException;

  public void setGeschlecht(String geschlecht) throws RemoteException;

  public String getTelefonprivat() throws RemoteException;

  public void setTelefonprivat(String telefonprivat) throws RemoteException;

  public String getTelefondienstlich() throws RemoteException;

  public void setTelefondienstlich(String telefondienstlich)
      throws RemoteException;

  public String getHandy() throws RemoteException;

  public void setHandy(String handy) throws RemoteException;

  public String getEmail() throws RemoteException;

  public void setEmail(String email) throws RemoteException;

  public Date getEintritt() throws RemoteException;

  public void setEintritt(Date eintritt) throws RemoteException;

  public void setEintritt(String eintritt) throws RemoteException;

  public Beitragsgruppe getBeitragsgruppe() throws RemoteException;

  public int getBeitragsgruppeId() throws RemoteException;

  public void setBeitragsgruppe(Integer beitragsgruppe) throws RemoteException;

  public Double getIndividuellerBeitrag() throws RemoteException;

  public void setIndividuellerBeitrag(double individuellerbeitrag)
      throws RemoteException;

  public Integer getZahlerID() throws RemoteException;

  public void setZahlerID(Integer id) throws RemoteException;

  public Date getAustritt() throws RemoteException;

  public void setAustritt(Date austritt) throws RemoteException;

  public void setAustritt(String austritt) throws RemoteException;

  public Date getKuendigung() throws RemoteException;

  public void setKuendigung(Date kuendigung) throws RemoteException;

  public void setKuendigung(String kuendigung) throws RemoteException;

  public Date getSterbetag() throws RemoteException;

  public void setSterbetag(Date sterbetag) throws RemoteException;

  public void setSterbetag(String sterbetag) throws RemoteException;

  public String getVermerk1() throws RemoteException;

  public void setVermerk1(String vermerk1) throws RemoteException;

  public String getVermerk2() throws RemoteException;

  public void setVermerk2(String vermerk2) throws RemoteException;

  public void insert() throws RemoteException, ApplicationException;

  public void setEingabedatum() throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public void setLetzteAenderung() throws RemoteException;

  public Date getLetzteAenderung() throws RemoteException;

  public String getNameVorname() throws RemoteException;

  public String getVornameName() throws RemoteException;

  public String getAnschrift() throws RemoteException;

  public Mitgliedfoto getFoto() throws RemoteException;

  public void setFoto(Mitgliedfoto foto) throws RemoteException;

  public String getEmpfaenger() throws RemoteException;

  public boolean isAngemeldet(Date stichtag) throws RemoteException;

  public void addVariable(String name, String wert) throws RemoteException;

  public Map<String, Object> getMap(Map<String, Object> map)
      throws RemoteException;

  public Map<String, Object> getMap(Map<String, Object> map,
      boolean ohneLesefelder) throws RemoteException;

}
