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

import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.willuhn.datasource.rmi.DBObject;

public interface Beitragsgruppe extends DBObject
{
  public String getBezeichnung() throws RemoteException;

  public void setBezeichnung(String bezeichnung) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public double getBetragMonatlich() throws RemoteException;

  public void setBetragMonatlich(double betrag) throws RemoteException;

  public double getBetragVierteljaehrlich() throws RemoteException;

  public void setBetragVierteljaehrlich(double betrag) throws RemoteException;

  public double getBetragHalbjaehrlich() throws RemoteException;

  public void setBetragHalbjaehrlich(double betrag) throws RemoteException;

  public double getBetragJaehrlich() throws RemoteException;

  public void setBetragJaehrlich(double betrag) throws RemoteException;

  public ArtBeitragsart getBeitragsArt() throws RemoteException;

  public void setBeitragsArt(int art) throws RemoteException;

  public double getArbeitseinsatzStunden() throws RemoteException;

  public void setArbeitseinsatzStunden(double arbeitseinsatzStunden)
      throws RemoteException;

  public double getArbeitseinsatzBetrag() throws RemoteException;

  public void setArbeitseinsatzBetrag(double arbeitseinsatzBetrag)
      throws RemoteException;

  public Buchungsart getBuchungsart() throws RemoteException;

  public void setBuchungsart(Buchungsart buchungsart) throws RemoteException;

}
