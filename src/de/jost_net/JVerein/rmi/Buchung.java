/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.util.ApplicationException;

public interface Buchung extends DBObject {
    public void setID(String id) throws RemoteException;

    public Integer getUmsatzid() throws RemoteException;

    public void setUmsatzid(Integer umsatzid) throws RemoteException;

    public Konto getKonto() throws RemoteException;

    public void setKonto(Konto konto) throws RemoteException;

    public Integer getAuszugsnummer() throws RemoteException;

    public void setAuszugsnummer(Integer auszugsnummer) throws RemoteException;

    public Integer getBelegnummer() throws RemoteException;

    public void setBelegnummer(Integer belegnummer) throws RemoteException;

    public Integer getBlattnummer() throws RemoteException;

    public void setBlattnummer(Integer blattnummer) throws RemoteException;

    public String getName() throws RemoteException;

    public void setName(String name) throws RemoteException;

    public double getBetrag() throws RemoteException;

    public void setBetrag(double betrag) throws RemoteException;

    public String getZweck() throws RemoteException;

    public void setZweck(String zweck) throws RemoteException;

    public Date getDatum() throws RemoteException;

    public void setDatum(Date datum) throws RemoteException;

    public String getArt() throws RemoteException;

    public void setArt(String art) throws RemoteException;

    public String getKommentar() throws RemoteException;

    public void setKommentar(String kommentar) throws RemoteException;

    public Buchungsart getBuchungsart() throws RemoteException;

    public Long getBuchungsartId() throws RemoteException;

    public void setBuchungsart(Long buchungsart) throws RemoteException;

    public Abrechnungslauf getAbrechnungslauf() throws RemoteException;

    public Long getAbrechnungslaufID() throws RemoteException;

    public void setAbrechnungslauf(Abrechnungslauf abrechnungslauf) throws RemoteException;

    public void setAbrechnungslauf(Long abrechnungslauf) throws RemoteException;

    public Mitgliedskonto getMitgliedskonto() throws RemoteException;

    public Long getMitgliedskontoID() throws RemoteException;

    public void setMitgliedskonto(Mitgliedskonto mitgliedskonto) throws RemoteException;

    public void setMitgliedskontoID(Long mitgliedskonto) throws RemoteException;

    public Projekt getProjekt() throws RemoteException;

    public Long getProjektID() throws RemoteException;

    public void setProjekt(Projekt projekt) throws RemoteException;

    public void setProjektID(Long projekt) throws RemoteException;

    public Jahresabschluss getJahresabschluss() throws RemoteException;

    public Long getSplitId() throws RemoteException;

    public void setSplitId(Long splitid) throws RemoteException;

    public Integer getSplitTyp() throws RemoteException;

    public void setSplitTyp(Integer splittyp) throws RemoteException;

    public Spendenbescheinigung getSpendenbescheinigung() throws RemoteException;

    public void setSpendenbescheinigungId(Long spendenbescheinigung) throws RemoteException;

    public Map<String, Object> getMap(Map<String, Object> map) throws RemoteException;

    public Boolean getVerzicht() throws RemoteException;

    public void setVerzicht(Boolean verzicht) throws RemoteException;

    /**
     * Soll der Datensatz in die Datenbank geschrieben werden?<br>
     * 
     * @param speicherung true: ja, Normalfall <br>
     *                    false: nein, bei Splitbuchungen werden die Datensätze zunächst in einer
     *                    ArrayList gehalten und später in die Datenbank geschrieben.
     */
    public void setSpeicherung(boolean speicherung) throws RemoteException;

    public boolean getSpeicherung() throws RemoteException;

    public void setDelete(boolean delete) throws RemoteException;

    public boolean isToDelete() throws RemoteException;

    public void plausi() throws RemoteException, ApplicationException;

}
