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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.QIFImportHead;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;

public class QIFImportHeadImpl extends AbstractDBObject implements QIFImportHead
{
	private static final long serialVersionUID = 162362596025205401L;

	public QIFImportHeadImpl()
			throws RemoteException
	{
		super();
	}

	@Override
	protected String getTableName()
	{
		return TABLE_NAME;
	}

	@Override
	public String getPrimaryAttribute() throws RemoteException
	{
		return SHOW_SELECT_TEXT;
	}

	@Override
	public void setName(String name) throws RemoteException
	{
		setAttribute(COL_NAME, name);
	}

	@Override
	public String getName() throws RemoteException
	{
		return (String) getAttribute(COL_NAME);
	}

	@Override
	public void setBeschreibung(String beschreibung) throws RemoteException
	{
		setAttribute(COL_BESCHREIBUNG, beschreibung);
	}

	@Override
	public String getBeschreibung() throws RemoteException
	{
		return (String) getAttribute(COL_BESCHREIBUNG);
	}

	@Override
	public void setStartSaldo(double betrag) throws RemoteException
	{
		setAttribute(COL_START_SALDO, Double.valueOf(betrag));
	}

	@Override
	public double getStartSaldo() throws RemoteException
	{
		Double wert = (Double) getAttribute(COL_START_SALDO);
		if (null == wert)
			return 0;
		return wert.doubleValue();
	}

	@Override
	public void setStartDate(Date datum) throws RemoteException
	{
		setAttribute(COL_START_DATE, datum);
	}

	@Override
	public Date getStartDate() throws RemoteException
	{
		return (Date) getAttribute(COL_START_DATE);
	}

	@Override
	public void setKonto(Konto konto) throws RemoteException
	{
		if (null != konto)
			setAttribute(COL_KONTO, new Integer(konto.getID()));
		else
			setAttribute(COL_KONTO, null);
	}

	@Override
	public Konto getKonto() throws RemoteException
	{
		Integer kontoId = (Integer) getAttribute(COL_KONTO);
		if (null == kontoId)
			return null;
		return (Konto) Einstellungen.getDBService().createObject(Konto.class,
				kontoId.toString());
	}

	@Override
	public void setImportDatum(Date datum) throws RemoteException
	{
		setAttribute(COL_IMPORT_DATUM, datum);
	}

	@Override
	public Date getImportDatum() throws RemoteException
	{
		return (Date) getAttribute(COL_IMPORT_DATUM);
	}

	@Override
	public void setImportFile(String fileName) throws RemoteException
	{
		setAttribute(COL_IMPORT_FILE, fileName);
	}

	@Override
	public String getImportFile() throws RemoteException
	{
		return (String) getAttribute(COL_IMPORT_FILE);
	}

	@Override
	public void setProcessDate(Date datum) throws RemoteException
	{
		setAttribute(COL_PROCESS_DATE, datum);
	}

	@Override
	public Date getProcessDate() throws RemoteException
	{
		return (Date) getAttribute(COL_PROCESS_DATE);
	}

	@Override
	public Object getAttribute(String fieldName) throws RemoteException
	{
		if (SHOW_SELECT_TEXT.equals(fieldName))
		{
			String beschreibung = getBeschreibung();
			if (null == beschreibung)
				return getName();

			String text = getName() + " - " + beschreibung;
			return text;
		}
		return super.getAttribute(fieldName);
	}

	@Override
	public String toString()
	{
		try
		{
			return getName() + " - " + getImportDatum().toString();
		}
		catch (Throwable ex)
		{
			Logger.error("Fehler", ex);
		}
		return super.toString();
	}

}
