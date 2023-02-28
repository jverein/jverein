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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.GenericObjectNode;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;

public class MitgliedskontoNode implements GenericObjectNode
{

	public static final int UNBEKANNT = 0;

	public static final int MITGLIED = 1;

	public static final int SOLL = 2;

	public static final int IST = 3;

	private int type = UNBEKANNT;

	private String id;

	private Mitglied mitglied;

	private String name = null;

	private Date datum = null;

	private String zweck1 = null;

	private Integer zahlungsweg = null;

	private Double soll = null;

	private Double ist = null;

	private MitgliedskontoNode parent = null;

	private ArrayList<MitgliedskontoNode> children;

	public MitgliedskontoNode(Mitglied m)
			throws RemoteException
	{
		this(m, null, null);
	}

	public MitgliedskontoNode(Mitglied m, Date von, Date bis)
			throws RemoteException
	{
		type = MITGLIED;
		this.mitglied = m;
		this.zahlungsweg = m.getZahlungsweg();
		this.name = Adressaufbereitung.getNameVorname(m);
		this.soll = Double.valueOf(0);
		this.ist = Double.valueOf(0);
		this.children = new ArrayList<>();
		this.id = m.getID();
		DBIterator<Mitgliedskonto> it = Einstellungen.getDBService()
				.createList(Mitgliedskonto.class);
		it.addFilter("mitglied = ?", new Object[]
		{
				m.getID()
		});
		if (von != null)
		{
			it.addFilter("datum >= ?", von);
		}
		if (bis != null)
		{
			it.addFilter("datum <= ?", bis);
		}
		it.setOrder("order by datum desc");
		while (it.hasNext())
		{
			Mitgliedskonto mk = it.next();
			soll += mk.getBetrag();
			MitgliedskontoNode mkn = new MitgliedskontoNode(this, mk);
			children.add(mkn);
		}
		for (MitgliedskontoNode node : children)
		{
			for (MitgliedskontoNode nodeist : node.children)
			{
				ist += nodeist.ist;
			}
		}
	}

	public MitgliedskontoNode(MitgliedskontoNode parent, Mitgliedskonto mk)
			throws RemoteException
	{
		this.type = SOLL;
		this.parent = parent;
		this.id = mk.getID();
		this.mitglied = mk.getMitglied();
		this.zahlungsweg = mk.getZahlungsweg();
		this.datum = mk.getDatum();
		this.zweck1 = mk.getZweck1();
		this.soll = mk.getBetrag();
		this.ist = mk.getIstSumme();
		if (this.type == SOLL)
		{
			this.children = new ArrayList<>();
			DBIterator<
					Buchung> it = Einstellungen.getDBService().createList(Buchung.class);
			it.addFilter("mitgliedskonto = ?", new Object[]
			{
					mk.getID()
			});
			it.setOrder("order by datum desc");
			ist = 0d;
			while (it.hasNext())
			{
				Buchung bist = it.next();
				MitgliedskontoNode mkn = new MitgliedskontoNode(this, mk, bist);
				ist += bist.getBetrag();
				children.add(mkn);
			}
		}

	}

	public MitgliedskontoNode(MitgliedskontoNode parent, Mitgliedskonto mk,
			Buchung bist)
			throws RemoteException
	{
		this.type = IST;
		this.parent = parent;
		this.id = bist.getID();
		this.mitglied = mk.getMitglied();
		this.zahlungsweg = mk.getZahlungsweg();
		this.datum = bist.getDatum();
		this.zweck1 = bist.getZweck();
		this.ist = bist.getBetrag();
	}

	public int getType()
	{
		return type;
	}

	public Mitglied getMitglied()
	{
		return mitglied;
	}

	@Override
	public String getPrimaryAttribute()
	{
		return null;
	}

	@Override
	public String getID()
	{
		return id;
	}

	@Override
	public String[] getAttributeNames()
	{
		return new String[]
		{
				"name", "datum", "zweck1", "zahlungsweg", "soll", "ist", "differenz"
		};
	}

	@Override
	public Object getAttribute(String name)
	{
		if (name.equals("name"))
		{
			return this.name;
		}
		else if (name.equals("datum"))
		{
			return datum;
		}
		else if (name.equals("zweck1"))
		{
			return zweck1;
		}
		else if (name.equals("zahlungsweg"))
		{
			return zahlungsweg;
		}
		else if (name.equals("soll"))
		{
			return soll;
		}
		else if (name.equals("ist"))
		{
			return ist;
		}
		else if (name.equals("differenz"))
		{
			if (ist == null)
			{
				ist = Double.valueOf(0);
			}
			if (type == MITGLIED || type == SOLL)
			{
				double differenz = ist - soll;
				return differenz != 0d ? differenz : null;
			}
			else
			{
				return null;
			}
		}
		return null;
	}

	@Override
	public boolean equals(GenericObject other)
	{
		return false;
	}

	@Override
	public boolean hasChild(GenericObjectNode object)
	{
		return children.size() > 0;
	}

	@Override
	public GenericIterator<?> getPossibleParents()
	{
		return null;
	}

	@Override
	public GenericIterator<?> getPath()
	{
		return null;
	}

	@Override
	public GenericObjectNode getParent()
	{
		return parent;
	}

	@Override
	public GenericIterator<?> getChildren() throws RemoteException
	{
		if (children != null)
		{
			return PseudoIterator
					.fromArray(children.toArray(new GenericObject[children.size()]));
		}
		return null;
	}

	public void remove()
	{
		if (parent != null)
		{
			parent.children.remove(this);
		}
	}

}
