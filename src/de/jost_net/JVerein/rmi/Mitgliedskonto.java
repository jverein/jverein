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

public interface Mitgliedskonto extends DBObject
{
  public Abrechnungslauf getAbrechnungslauf() throws RemoteException;

  public void setAbrechnungslauf(Abrechnungslauf abrechnungslauf)
      throws RemoteException;

  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(Mitglied mitglied) throws RemoteException;

  public Date getDatum() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public String getZweck1() throws RemoteException;

  public void setZweck1(String zweck1) throws RemoteException;

  public Integer getZahlungsweg() throws RemoteException;

  public void setZahlungsweg(Integer zahlungsweg) throws RemoteException;

  public void setBetrag(Double betrag) throws RemoteException;

  public Double getBetrag() throws RemoteException;

  public Double getIstSumme() throws RemoteException;
}
