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

/**
 * @author Rolf Mamat
 */
public interface MitgliedNextBGruppe extends DBObject
{
  public static final String TABLE_NAME = "mitgliednextbgruppe";

  public static final String COL_ID = "id";

  public static final String COL_MITGLIED = "mitglied";

  public static final String COL_BEITRAGSGRUPPE = "beitragsgruppe";

  public static final String COL_BEMERKUNG = "bemerkung";

  public static final String COL_AB_DATUM = "abdatum";

  public static final String VIEW_BEITRAGSGRUPPE = "gruppenname";

  public static final String VIEW_NAME_VORNAME = "namevorname";

  public static final String VIEW_AKT_BEITRAGSGRUPPE = "akt_gruppenname";

  public void setMitglied(Mitglied mitglied) throws RemoteException;

  public Mitglied getMitglied() throws RemoteException;

  public void setBeitragsgruppe(Beitragsgruppe beitragsGruppe)
      throws RemoteException;

  public Beitragsgruppe getBeitragsgruppe() throws RemoteException;

  public void setBemerkung(String bemerkung) throws RemoteException;

  public String getBemerkung() throws RemoteException;

  public void setAbDatum(Date datum) throws RemoteException;

  public Date getAbDatum() throws RemoteException;
}
