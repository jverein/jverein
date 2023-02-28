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
import java.text.ParseException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class JahresabschlussImpl extends AbstractDBObject
		implements Jahresabschluss
{

	private static final long serialVersionUID = 1L;

	public JahresabschlussImpl()
			throws RemoteException
	{
		super();
	}

	@Override
	protected String getTableName()
	{
		return "jahresabschluss";
	}

	@Override
	public String getPrimaryAttribute()
	{
		return "id";
	}

	@Override
	protected void deleteCheck() throws ApplicationException
	{
		try
		{
			DBIterator<Jahresabschluss> it = Einstellungen.getDBService()
					.createList(Jahresabschluss.class);
			it.addFilter("von > ?", new Object[]
			{
					getVon()
			});
			if (it.hasNext())
			{
				throw new ApplicationException(
						"Jahresabschluss kann nicht gelöscht werden. Es existieren neuere Abschlüsse!");
			}
		}
		catch (RemoteException e)
		{
			String msg = "Jahresabschluss kann nicht gelöscht werden. Siehe system log";
			throw new ApplicationException(msg);
		}

	}

	@Override
	protected void insertCheck() throws ApplicationException
	{
		try
		{
			if (hasBuchungenOhneBuchungsart())
			{
				throw new ApplicationException(
						"Achtung! Es existieren noch Buchungen ohne Buchungsart. Kein Abschluss möglich!");
			}
			if (getName() == null || getName().length() == 0)
			{
				throw new ApplicationException(
						"Name des Verantwortlichen für den Abschluss fehlt");
			}
			Konto k = (Konto) Einstellungen.getDBService().createObject(Konto.class,
					null);
			Geschaeftsjahr gj = new Geschaeftsjahr(getVon());
			DBIterator<Konto> it = k.getKontenEinesJahres(gj);
			while (it.hasNext())
			{
				Konto k1 = it.next();
				DBIterator<Anfangsbestand> anfangsbestaende = Einstellungen
						.getDBService().createList(Anfangsbestand.class);
				anfangsbestaende.addFilter("konto = ?", new Object[]
				{
						k1.getID()
				});
				anfangsbestaende.addFilter("datum >= ?", new Object[]
				{
						gj.getBeginnGeschaeftsjahr()
				});
				if (!anfangsbestaende.hasNext())
				{
					throw new ApplicationException(
							String.format("Für Konto %s %s fehlt der Anfangsbestand.",
									k1.getNummer(), k1.getBezeichnung()));
				}
			}
		}
		catch (ParseException e)
		{
			throw new ApplicationException("Ungültiges von-Datum");
		}
		catch (RemoteException e)
		{
			Logger.error("Fehler", e);
			String msg = "Jahresabschluss kann nicht gespeichert werden. Siehe system log";
			throw new ApplicationException(msg);
		}
	}

	@Override
	protected void updateCheck() throws ApplicationException
	{
		insertCheck();
	}

	private boolean hasBuchungenOhneBuchungsart() throws RemoteException
	{
		DBIterator<
				Buchung> it = Einstellungen.getDBService().createList(Buchung.class);
		it.addFilter("datum >= ?", new Object[]
		{
				getVon()
		});
		it.addFilter("datum <= ?", new Object[]
		{
				getBis()
		});
		it.addFilter("buchungsart is null");
		return it.hasNext();
	}

	@Override
	protected Class<?> getForeignObject(String field)
	{
		return null;
	}

	@Override
	public Date getVon() throws RemoteException
	{
		return (Date) getAttribute("von");
	}

	@Override
	public void setVon(Date von) throws RemoteException
	{
		setAttribute("von", von);
	}

	@Override
	public Date getBis() throws RemoteException
	{
		return (Date) getAttribute("bis");
	}

	@Override
	public void setBis(Date bis) throws RemoteException
	{
		setAttribute("bis", bis);
	}

	@Override
	public Date getDatum() throws RemoteException
	{
		return (Date) getAttribute("datum");
	}

	@Override
	public void setDatum(Date datum) throws RemoteException
	{
		setAttribute("datum", datum);
	}

	@Override
	public String getName() throws RemoteException
	{
		return (String) getAttribute("name");
	}

	@Override
	public void setName(String name) throws RemoteException
	{
		setAttribute("name", name);
	}

	@Override
	public Object getAttribute(String fieldName) throws RemoteException
	{
		return super.getAttribute(fieldName);
	}
}
