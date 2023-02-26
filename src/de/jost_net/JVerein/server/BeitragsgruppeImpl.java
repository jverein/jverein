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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeImpl extends AbstractDBObject
		implements Beitragsgruppe
{

	private static final long serialVersionUID = 1L;

	public BeitragsgruppeImpl()
			throws RemoteException
	{
		super();
	}

	@Override
	protected String getTableName()
	{
		return "beitragsgruppe";
	}

	@Override
	public String getPrimaryAttribute()
	{
		return "id";
	}

	@Override
	protected void deleteCheck()
	{
		//
	}

	@Override
	protected void insertCheck() throws ApplicationException
	{
		try
		{
			if (getBezeichnung() == null || getBezeichnung().length() == 0)
			{
				throw new ApplicationException("Bitte Bezeichnung eingeben");
			}
			switch (Einstellungen.getEinstellung().getBeitragsmodel())
			{
				case GLEICHERTERMINFUERALLE:
				case MONATLICH12631:
					if (getBetrag() < 0)
					{
						throw new ApplicationException("Betrag nicht gültig");
					}
					if (getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER
							&& getBetrag() != 0)
					{
						throw new ApplicationException(
								"Familien-Angehörige sind beitragsbefreit. Bitte als Betrag 0,00 eingeben.");
					}

					break;
				case FLEXIBEL:
					if (getBetragMonatlich() < 0 || getBetragVierteljaehrlich() < 0
							|| getBetragHalbjaehrlich() < 0 || getBetragJaehrlich() < 0)
					{
						throw new ApplicationException("Betrag nicht gültig");
					}
					if (getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER
							&& (getBetragMonatlich() != 0 || getBetragVierteljaehrlich() != 0
									|| getBetragHalbjaehrlich() != 0
									|| getBetragJaehrlich() != 0))
					{
						throw new ApplicationException(
								"Familien-Angehörige sind beitragsbefreit. Bitte als Betrag 0,00 eingeben.");
					}

					break;
			}
		}
		catch (RemoteException e)
		{
			Logger.error("insert check of mitglied failed", e);
			throw new ApplicationException(
					"Mitglied kann nicht gespeichert werden. Siehe system log");
		}
	}

	@Override
	protected void updateCheck() throws ApplicationException
	{
		insertCheck();
	}

	@Override
	protected Class<?> getForeignObject(String arg0)
	{
		if (arg0.equals("buchungsart"))
		{
			return Buchungsart.class;
		}
		return null;
	}

	@Override
	public String getBezeichnung() throws RemoteException
	{
		return (String) getAttribute("bezeichnung");
	}

	@Override
	public void setBezeichnung(String bezeichnung) throws RemoteException
	{
		setAttribute("bezeichnung", bezeichnung);
	}

	@Override
	public Boolean getSekundaer() throws RemoteException
	{
		return Util.getBoolean(getAttribute("sekundaer"));
	}

	@Override
	public void setSekundaer(Boolean sekundaer) throws RemoteException
	{
		setAttribute("sekundaer", Boolean.valueOf(sekundaer));
	}

	@Override
	public double getBetrag() throws RemoteException
	{
		Double d = (Double) getAttribute("betrag");
		if (d == null)
		{
			return 0;
		}
		return d.doubleValue();
	}

	@Override
	public void setBetrag(double d) throws RemoteException
	{
		setAttribute("betrag", Double.valueOf(d));
	}

	@Override
	public double getBetragMonatlich() throws RemoteException
	{
		Double d = (Double) getAttribute("betragmonatlich");
		if (d == null)
		{
			return 0;
		}
		return d.doubleValue();
	}

	@Override
	public void setBetragMonatlich(double d) throws RemoteException
	{
		setAttribute("betragmonatlich", Double.valueOf(d));
	}

	@Override
	public double getBetragVierteljaehrlich() throws RemoteException
	{
		Double d = (Double) getAttribute("betragvierteljaehrlich");
		if (d == null)
		{
			return 0;
		}
		return d.doubleValue();
	}

	@Override
	public void setBetragVierteljaehrlich(double d) throws RemoteException
	{
		setAttribute("betragvierteljaehrlich", Double.valueOf(d));
	}

	@Override
	public double getBetragHalbjaehrlich() throws RemoteException
	{
		Double d = (Double) getAttribute("betraghalbjaehrlich");
		if (d == null)
		{
			return 0;
		}
		return d.doubleValue();
	}

	@Override
	public void setBetragHalbjaehrlich(double d) throws RemoteException
	{
		setAttribute("betraghalbjaehrlich", Double.valueOf(d));
	}

	@Override
	public double getBetragJaehrlich() throws RemoteException
	{
		Double d = (Double) getAttribute("betragjaehrlich");
		if (d == null)
		{
			return 0;
		}
		return d.doubleValue();
	}

	@Override
	public void setBetragJaehrlich(double d) throws RemoteException
	{
		setAttribute("betragjaehrlich", Double.valueOf(d));
	}

	@Override
	public ArtBeitragsart getBeitragsArt() throws RemoteException
	{
		Integer i = (Integer) getAttribute("beitragsart");
		if (i == null)
		{
			i = new Integer("0");
		}
		return ArtBeitragsart.getByKey(i);
	}

	@Override
	public void setBeitragsArt(int art) throws RemoteException
	{
		setAttribute("beitragsart", art);
	}

	@Override
	public double getArbeitseinsatzStunden() throws RemoteException
	{
		Double d = (Double) getAttribute("arbeitseinsatzstunden");
		if (d == null)
		{
			return 0;
		}
		return d.doubleValue();
	}

	@Override
	public void setArbeitseinsatzStunden(double arbeitseinsatzStunden)
			throws RemoteException
	{
		setAttribute("arbeitseinsatzstunden",
				Double.valueOf(arbeitseinsatzStunden));
	}

	@Override
	public double getArbeitseinsatzBetrag() throws RemoteException
	{
		Double d = (Double) getAttribute("arbeitseinsatzbetrag");
		if (d == null)
		{
			return 0;
		}
		return d.doubleValue();
	}

	@Override
	public void setBuchungsart(Buchungsart buchungsart) throws RemoteException
	{
		setAttribute("buchungsart", buchungsart);
	}

	@Override
	public Buchungsart getBuchungsart() throws RemoteException
	{
		return (Buchungsart) getAttribute("buchungsart");
	}

	@Override
	public void setArbeitseinsatzBetrag(double arbeitseinsatzBetrag)
			throws RemoteException
	{
		setAttribute("arbeitseinsatzbetrag", Double.valueOf(arbeitseinsatzBetrag));
	}

	@Override
	public String getNotiz() throws RemoteException
	{
		return (String) getAttribute("notiz");
	}

	@Override
	public void setNotiz(String notiz) throws RemoteException
	{
		setAttribute("notiz", notiz);
	}

	@Override
	public Object getAttribute(String fieldName) throws RemoteException
	{
		return super.getAttribute(fieldName);
	}
}
