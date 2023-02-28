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
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.SaldoZeile;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class JahressaldoList extends TablePart implements Part
{

	private TablePart saldoList;

	private Geschaeftsjahr gj = null;

	public JahressaldoList(Action action, int jahr)
			throws RemoteException
	{
		super(action);
		try
		{
			gj = new Geschaeftsjahr(jahr);
		}
		catch (ParseException e)
		{
			Logger.error("Fehler", e);
		}
	}

	public JahressaldoList(Action action, Geschaeftsjahr gj)
	{
		super(action);
		this.gj = gj;
	}

	public Part getSaldoList() throws ApplicationException
	{
		ArrayList<SaldoZeile> zeile = null;
		try
		{
			zeile = getInfo();

			if (saldoList == null)
			{
				GenericIterator<?> gi = PseudoIterator
						.fromArray(zeile.toArray(new GenericObject[zeile.size()]));

				saldoList = new TablePart(gi, null);
				saldoList.addColumn("Kontonummer", "kontonummer", null, false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Bezeichnung", "kontobezeichnung");
				saldoList.addColumn("Anfangsbestand", "anfangsbestand",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Einnahmen", "einnahmen",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Ausgaben", "ausgaben",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Umbuchungen", "umbuchungen",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Endbestand", "endbestand",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Bemerkung", "bemerkung");
				saldoList.setRememberColWidths(true);
				saldoList.setSummary(false);
			}
			else
			{
				saldoList.removeAll();
				for (SaldoZeile sz : zeile)
				{
					saldoList.addItem(sz);
				}
			}
		}
		catch (RemoteException e)
		{
			throw new ApplicationException("Fehler aufgetreten" + e.getMessage());
		}
		return saldoList;
	}

	public ArrayList<SaldoZeile> getInfo() throws RemoteException
	{
		ArrayList<SaldoZeile> zeile = new ArrayList<>();
		Konto k = (Konto) Einstellungen.getDBService().createObject(Konto.class,
				null);
		DBIterator<Konto> konten = k.getKontenEinesJahres(gj);
		double anfangsbestand = 0;
		double einnahmen = 0;
		double ausgaben = 0;
		double umbuchungen = 0;
		double endbestand = 0;
		double jahressaldo = 0;
		if (gj != null)
		{
			while (konten.hasNext())
			{
				SaldoZeile sz = new SaldoZeile(gj, konten.next());
				anfangsbestand += (Double) sz.getAttribute("anfangsbestand");
				einnahmen += (Double) sz.getAttribute("einnahmen");
				ausgaben += (Double) sz.getAttribute("ausgaben");
				umbuchungen += (Double) sz.getAttribute("umbuchungen");
				endbestand += (Double) sz.getAttribute("endbestand");
				jahressaldo += (Double) sz.getAttribute("endbestand")
						- (Double) sz.getAttribute("anfangsbestand");
				zeile.add(sz);
			}
		}
		k = (Konto) Einstellungen.getDBService().createObject(Konto.class, null);
		k.setNummer("");
		k.setBezeichnung("Summe");
		zeile.add(new SaldoZeile(k, anfangsbestand, einnahmen, ausgaben,
				umbuchungen, endbestand));
		k = (Konto) Einstellungen.getDBService().createObject(Konto.class, null);
		k.setNummer("");
		k.setBezeichnung("Überschuss/Verlust(-)");
		zeile.add(new SaldoZeile(k, null, null, null, null, jahressaldo));

		return zeile;
	}

	public void setGeschaeftsjahr(Geschaeftsjahr gj)
	{
		this.gj = gj;
	}

	@Override
	public void removeAll()
	{
		saldoList.removeAll();
	}

	public void addItem(SaldoZeile sz) throws RemoteException
	{
		saldoList.addItem(sz);
	}

	@Override
	public synchronized void paint(Composite parent) throws RemoteException
	{
		super.paint(parent);
	}

}
