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

public interface ZusatzbetragAbrechnungslauf extends DBObject
{
  public Abrechnungslauf getAbrechnungslauf() throws RemoteException;

  public void setAbrechnungslauf(Abrechnungslauf abrechnungslauf)
      throws RemoteException;

  public Zusatzbetrag getZusatzbetrag() throws RemoteException;

  public void setZusatzbetrag(Zusatzbetrag zusatzbetrag) throws RemoteException;

  public Date getLetzteAusfuehrung() throws RemoteException;

  public void setLetzteAusfuehrung(Date letzteausfuehrung)
      throws RemoteException;

}
