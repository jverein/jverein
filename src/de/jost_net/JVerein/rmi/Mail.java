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
import java.sql.Timestamp;
import java.util.TreeSet;

import de.willuhn.datasource.rmi.DBObject;

public interface Mail extends DBObject
{
  public TreeSet<MailEmpfaenger> getEmpfaenger() throws RemoteException;

  public void setEmpfaenger(TreeSet<MailEmpfaenger> empfaenger)
      throws RemoteException;

  public TreeSet<MailAnhang> getAnhang() throws RemoteException;

  public void setAnhang(TreeSet<MailAnhang> anhang) throws RemoteException;

  public String getBetreff() throws RemoteException;

  public void setBetreff(String betreff) throws RemoteException;

  public String getTxt() throws RemoteException;

  public void setTxt(String txt) throws RemoteException;

  public Timestamp getBearbeitung() throws RemoteException;

  public void setBearbeitung(Timestamp bearbeitung) throws RemoteException;

  public Timestamp getVersand() throws RemoteException;

  public void setVersand(Timestamp versand) throws RemoteException;
}
