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

public interface QIFImportHead extends DBObject
{
  public static final String TABLE_NAME = "qifimporthead";

  public static final String COL_ID = "id";

  public static final String COL_NAME = "name";

  public static final String COL_BESCHREIBUNG = "beschreibung";

  public static final String COL_START_SALDO = "startsaldo";

  public static final String COL_START_DATE = "startdate";

  public static final String COL_KONTO = "konto";

  public static final String COL_IMPORT_DATUM = "importdatum";

  public static final String COL_IMPORT_FILE = "importfile";

  public static final String COL_PROCESS_DATE = "processdate";

  public static final String SHOW_SELECT_TEXT = "select_text";

  public void setName(String name) throws RemoteException;

  public String getName() throws RemoteException;

  public void setBeschreibung(String beschreibung) throws RemoteException;

  public String getBeschreibung() throws RemoteException;

  public void setStartSaldo(double betrag) throws RemoteException;

  public double getStartSaldo() throws RemoteException;

  public void setStartDate(Date datum) throws RemoteException;

  public Date getStartDate() throws RemoteException;

  public void setKonto(Konto konto) throws RemoteException;

  public Konto getKonto() throws RemoteException;

  public void setImportDatum(Date datum) throws RemoteException;

  public Date getImportDatum() throws RemoteException;

  public void setImportFile(String fileName) throws RemoteException;

  public String getImportFile() throws RemoteException;

  public void setProcessDate(Date datum) throws RemoteException;

  public Date getProcessDate() throws RemoteException;
}
