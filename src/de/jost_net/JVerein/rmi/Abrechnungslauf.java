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

public interface Abrechnungslauf extends DBObject
{

  public void setID(String id) throws RemoteException;

  public Integer getNr() throws RemoteException;

  public Date getDatum() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public Integer getModus() throws RemoteException;

  public void setModus(Integer modus) throws RemoteException;

  public Date getFaelligkeit() throws RemoteException;

  public void setFaelligkeit(Date faelligkeit) throws RemoteException;

  public Date getStichtag() throws RemoteException;

  public void setStichtag(Date stichtag) throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public void setEingabedatum(Date eingabedatum) throws RemoteException;

  public Date getAustrittsdatum() throws RemoteException;

  public void setAustrittsdatum(Date austrittsdatum) throws RemoteException;

  public String getZahlungsgrund() throws RemoteException;

  public void setZahlungsgrund(String zahlungsgrund) throws RemoteException;

  public Boolean getZusatzbetraege() throws RemoteException;

  public void setZusatzbetraege(Boolean zusatzbetraege) throws RemoteException;

  public Boolean getKursteilnehmer() throws RemoteException;

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException;

  public Boolean getDtausdruck() throws RemoteException;

  public void setDtausdruck(Boolean dtausdruck) throws RemoteException;

  public Integer getAbbuchungsausgabe() throws RemoteException;

  public void setAbbuchungsausgabe(Integer abbuchungsausgabe)
      throws RemoteException;

  public Boolean getAbgeschlossen() throws RemoteException;

  public void setAbgeschlossen(Boolean abgeschlossen) throws RemoteException;

  public String getBemerkung() throws RemoteException;

  public void setBemerkung(String bemerkung) throws RemoteException;

}
