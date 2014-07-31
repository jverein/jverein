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

public interface QIFImportPos extends DBObject
{
  public static final String TABLE_NAME = "qifimportpos";

  public static final String COL_POSID = "id";

  public static final String COL_HEADID = "headid";

  public static final String COL_DATUM = "datum";

  public static final String COL_BETRAG = "betrag";

  public static final String COL_BELEG = "beleg";

  public static final String COL_NAME = "name";

  public static final String COL_ZWECK = "zweck";

  public static final String COL_QIF_BUCHART = "buchartex";

  public static final String COL_BUCHART = "buchart";

  public static final String COL_MITGLIEDBAR = "mitgliedbar";

  public static final String COL_MITGLIED = "mitglied";

  public static final String COL_SPERRE = "sperre";

  public static final String SPERRE_JA = "J";

  public static final String SPERRE_NEIN = "N";

  public static final String MITGLIEDBAR_JA = "J";

  public static final String MITGLIEDBAR_NEIN = "N";

  public static final String VIEW_QIFKONTO_NAME = "qifkonto";

  public static final String VIEW_BUCHART_BEZEICHNER = "klasse-art-bez";

  public static final String VIEW_MITGLIEDS_NAME = "mitglied-Name";

  public static final String VIEW_MITGLIED_BAR = "mitglied-barview";

  public static final String VIEW_SALDO = "saldo";

  public void setQIFImpHead(QIFImportHead head) throws RemoteException;

  public QIFImportHead getQIFImpHead() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public Date getDatum() throws RemoteException;

  public void setBetrag(Double betrag) throws RemoteException;

  public Double getBetrag() throws RemoteException;

  public void setBeleg(String beleg) throws RemoteException;

  public String getBeleg() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public String getName() throws RemoteException;

  public void setZweck(String zweck) throws RemoteException;

  public String getZweck() throws RemoteException;

  public void setQIFBuchart(String buchart) throws RemoteException;

  public String getQIFBuchart() throws RemoteException;

  public void setBuchungsart(Buchungsart buchungsart) throws RemoteException;

  public Buchungsart getBuchungsart() throws RemoteException;

  public Long getBuchungsartId() throws RemoteException;

  public void setGesperrt(Boolean sperren) throws RemoteException;

  public Boolean getGesperrt() throws RemoteException;

  public void setMitgliedZuordenbar(Boolean zuordenbar) throws RemoteException;

  public Boolean getMitgliedZuordenbar() throws RemoteException;

  public void setMitglied(Mitglied mitglied) throws RemoteException;

  public Mitglied getMitglied() throws RemoteException;
}
