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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Zusatzfelder extends DBObject
{
  public Mitglied getMitglied() throws RemoteException;

  public void setMitglied(int mitglied) throws RemoteException;

  public Felddefinition getFelddefinition() throws RemoteException;

  public void setFelddefinition(int felddefiniton) throws RemoteException;

  public String getFeld() throws RemoteException;

  public void setFeld(String feld) throws RemoteException;

  public Date getFeldDatum() throws RemoteException;

  public void setFeldDatum(Date datum) throws RemoteException;

  public Integer getFeldGanzzahl() throws RemoteException;

  public void setFeldGanzzahl(Integer ganzzahl) throws RemoteException;

  public double getFeldGleitkommazahl() throws RemoteException;

  public void setFeldGleitkommazahl(double gleitkommazahl)
      throws RemoteException;

  public BigDecimal getFeldWaehrung() throws RemoteException;

  public void setFeldWaehrung(BigDecimal waehrung) throws RemoteException;

  public Boolean getFeldJaNein() throws RemoteException;

  public void setFeldJaNein(Boolean janein) throws RemoteException;

  public String getString() throws RemoteException;
}
