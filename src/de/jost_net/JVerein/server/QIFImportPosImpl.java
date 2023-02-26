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
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.QIFImportHead;
import de.jost_net.JVerein.rmi.QIFImportPos;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class QIFImportPosImpl extends AbstractDBObject implements QIFImportPos
{
	private static final long serialVersionUID = -371377436256187302L;

	private transient double saldo;

	public QIFImportPosImpl()
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
		return getIDField();
	}

	@Override
	protected String getIDField()
	{
		return COL_POSID;
	}

	@Override
	public void setQIFImpHead(QIFImportHead head) throws RemoteException
	{
		if (null == head)
			throw new RemoteException("QIFImportHead fehlt!");
		setAttribute(COL_HEADID, new Integer(head.getID()));
	}

	@Override
	public QIFImportHead getQIFImpHead() throws RemoteException
	{
		Integer headId = (Integer) getAttribute(COL_HEADID);
		if (null == headId)
		{
			return null;
		}
		return (QIFImportHead) Einstellungen.getDBService()
				.createObject(QIFImportHead.class, headId.toString());
	}

	@Override
	public void setDatum(Date datum) throws RemoteException
	{
		setAttribute(COL_DATUM, datum);
	}

	@Override
	public Date getDatum() throws RemoteException
	{
		return (Date) getAttribute(COL_DATUM);
	}

	@Override
	public void setBetrag(Double betrag) throws RemoteException
	{
		setAttribute(COL_BETRAG, betrag);
	}

	@Override
	public Double getBetrag() throws RemoteException
	{
		return (Double) getAttribute(COL_BETRAG);
	}

	@Override
	public void setBeleg(String beleg) throws RemoteException
	{
		setAttribute(COL_BELEG, beleg);
	}

	@Override
	public String getBeleg() throws RemoteException
	{
		return (String) getAttribute(COL_BELEG);
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
	public void setZweck(String zweck) throws RemoteException
	{
		setAttribute(COL_ZWECK, zweck);
	}

	@Override
	public String getZweck() throws RemoteException
	{
		return (String) getAttribute(COL_ZWECK);
	}

	@Override
	public void setQIFBuchart(String buchart) throws RemoteException
	{
		setAttribute(COL_QIF_BUCHART, buchart);
	}

	@Override
	public String getQIFBuchart() throws RemoteException
	{
		return (String) getAttribute(COL_QIF_BUCHART);
	}

	@Override
	public void setBuchungsart(Buchungsart buchungsart) throws RemoteException
	{
		if (null != buchungsart)
		{
			setAttribute(COL_BUCHART, new Integer(buchungsart.getID()));
		}
		else
		{
			setAttribute(COL_BUCHART, null);
		}
	}

	@Override
	public Buchungsart getBuchungsart() throws RemoteException
	{
		Integer buchartId = (Integer) getAttribute(COL_BUCHART);
		if (null == buchartId)
			return null;
		Buchungsart buchungsart = (Buchungsart) Einstellungen.getDBService()
				.createObject(Buchungsart.class, buchartId.toString());
		return buchungsart;
	}

	@Override
	public Long getBuchungsartId() throws RemoteException
	{
		Integer buchartId = (Integer) getAttribute(COL_BUCHART);
		if (null == buchartId)
			return null;
		return new Long(buchartId.longValue());
	}

	@Override
	public Object getAttribute(String fieldName) throws RemoteException
	{
		if (VIEW_BUCHART_BEZEICHNER.equals(fieldName))
		{
			Buchungsart buchungsart = getBuchungsart();
			if (null == buchungsart)
				return null;
			return buchungsart.getAttribute(fieldName);
		}
		if (VIEW_QIFKONTO_NAME.equals(fieldName))
		{
			QIFImportHead head = getQIFImpHead();
			if (null == head)
				return null;
			return head.getName();
		}
		if (VIEW_MITGLIEDS_NAME.equals(fieldName))
		{
			Mitglied mitglied = getMitglied();
			if (null == mitglied)
				return null;
			return mitglied.getVorname() + " " + mitglied.getName();
		}

		if (VIEW_MITGLIED_BAR.equals(fieldName))
		{
			if (getGesperrt().booleanValue())
				return null;
			if (getMitgliedZuordenbar().booleanValue())
				return "Buchung im Mitgliedskonto möglich";
			return " - ";
		}
		if (VIEW_SALDO.equals(fieldName))
		{
			return getSaldo();
		}

		return super.getAttribute(fieldName);
	}

	@Override
	public void setGesperrt(Boolean sperren) throws RemoteException
	{
		if (null == sperren || sperren.booleanValue() == false)
			setAttribute(COL_SPERRE, SPERRE_NEIN);
		else
			setAttribute(COL_SPERRE, SPERRE_JA);
	}

	@Override
	public Boolean getGesperrt() throws RemoteException
	{
		String sperre = (String) getAttribute(COL_SPERRE);

		if (SPERRE_JA.equals(sperre))
			return Boolean.TRUE;
		return Boolean.FALSE;
	}

	@Override
	public void setMitgliedZuordenbar(Boolean zuordenbar) throws RemoteException
	{
		if (null == zuordenbar || zuordenbar.booleanValue() == false)
			setAttribute(COL_MITGLIEDBAR, MITGLIEDBAR_NEIN);
		else
			setAttribute(COL_MITGLIEDBAR, MITGLIEDBAR_JA);
	}

	@Override
	public Boolean getMitgliedZuordenbar() throws RemoteException
	{
		String zuordenbar = (String) getAttribute(COL_MITGLIEDBAR);
		if (MITGLIEDBAR_JA.equals(zuordenbar))
			return Boolean.TRUE;
		return Boolean.FALSE;
	}

	@Override
	public void setMitglied(Mitglied mitglied) throws RemoteException
	{
		if (null == mitglied)
			setAttribute(COL_MITGLIED, null);
		else
			setAttribute(COL_MITGLIED, mitglied.getID());
	}

	@Override
	public Mitglied getMitglied() throws RemoteException
	{
		Integer id = (Integer) getAttribute(COL_MITGLIED);
		if (null == id)
			return null;
		return (Mitglied) Einstellungen.getDBService().createObject(Mitglied.class,
				id.toString());
	}

	@Override
	protected void insertCheck() throws ApplicationException
	{
		try
		{
			if (getAttribute(COL_MITGLIEDBAR) == null)
				setAttribute(COL_MITGLIEDBAR, MITGLIEDBAR_NEIN);
			if (getAttribute(COL_SPERRE) == null)
				setAttribute(COL_SPERRE, SPERRE_NEIN);
		}
		catch (RemoteException ex)
		{
			throw new ApplicationException("Defaultwerte können nicht gesetzt werden",
					ex);
		}
	}

	public void setSaldo(double wert)
	{
		saldo = wert;
	}

	public Double getSaldo()
	{
		return Double.valueOf(saldo);
	}

}
