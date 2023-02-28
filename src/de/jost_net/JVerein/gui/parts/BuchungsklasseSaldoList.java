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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.BuchungsklasseSaldoZeile;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.util.ApplicationException;

public class BuchungsklasseSaldoList extends TablePart implements Part
{

	private TablePart saldoList;

	private Date datumvon = null;

	private Date datumbis = null;

	public BuchungsklasseSaldoList(Action action, Date datumvon, Date datumbis)
	{
		super(action);
		this.datumvon = datumvon;
		this.datumbis = datumbis;
	}

	public Part getSaldoList() throws ApplicationException
	{
		ArrayList<BuchungsklasseSaldoZeile> zeile = null;
		try
		{
			zeile = getInfo();

			if (saldoList == null)
			{
				GenericIterator<?> gi = PseudoIterator
						.fromArray(zeile.toArray(new GenericObject[zeile.size()]));

				saldoList = new TablePart(gi, null)
				{
					@Override
					protected void orderBy(int index)
					{
						return;
					}
				};
				saldoList.addColumn("Buchungsklasse", "buchungsklassenbezeichnung",
						null, false);
				saldoList.addColumn("Buchungsart", "buchungsartbezeichnung");
				saldoList.addColumn("Einnahmen", "einnahmen",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Ausgaben", "ausgaben",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Umbuchungen", "umbuchungen",
						new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
						Column.ALIGN_RIGHT);
				saldoList.addColumn("Anzahl", "anzahlbuchungen");
				saldoList.setRememberColWidths(true);
				saldoList.setSummary(false);
			}
			else
			{
				saldoList.removeAll();
				for (BuchungsklasseSaldoZeile sz : zeile)
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

	public ArrayList<BuchungsklasseSaldoZeile> getInfo() throws RemoteException
	{
		ArrayList<BuchungsklasseSaldoZeile> zeile = new ArrayList<>();
		Buchungsklasse buchungsklasse = null;
		Buchungsart buchungsart = null;
		Double einnahmen;
		Double ausgaben;
		Double umbuchungen;
		Double suBukEinnahmen = Double.valueOf(0);
		Double suBukAusgaben = Double.valueOf(0);
		Double suBukUmbuchungen = Double.valueOf(0);
		Double suEinnahmen = Double.valueOf(0);
		Double suAusgaben = Double.valueOf(0);
		Double suUmbuchungen = Double.valueOf(0);

		ResultSetExtractor rsd = new ResultSetExtractor()
		{
			@Override
			public Object extract(ResultSet rs) throws SQLException
			{
				if (!rs.next())
				{
					return Double.valueOf(0);
				}
				return Double.valueOf(rs.getDouble(1));
			}
		};
		ResultSetExtractor rsi = new ResultSetExtractor()
		{
			@Override
			public Object extract(ResultSet rs) throws SQLException
			{
				if (!rs.next())
				{
					return Integer.valueOf(0);
				}
				return Integer.valueOf(rs.getInt(1));
			}
		};

		DBService service = Einstellungen.getDBService();
		DBIterator<Buchungsklasse> buchungsklassenIt = service
				.createList(Buchungsklasse.class);
		buchungsklassenIt.setOrder("ORDER BY nummer");
		while (buchungsklassenIt.hasNext())
		{
			buchungsklasse = buchungsklassenIt.next();
			zeile.add(new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.HEADER,
					buchungsklasse));
			DBIterator<
					Buchungsart> buchungsartenIt = service.createList(Buchungsart.class);
			buchungsartenIt.addFilter("buchungsklasse = ?", new Object[]
			{
					buchungsklasse.getID()
			});
			buchungsartenIt.setOrder("order by nummer");
			suBukEinnahmen = Double.valueOf(0);
			suBukAusgaben = Double.valueOf(0);
			suBukUmbuchungen = Double.valueOf(0);
			boolean ausgabe = false;

			while (buchungsartenIt.hasNext())
			{
				buchungsart = buchungsartenIt.next();
				String sqlc = "select count(*) from buchung, buchungsart "
						+ "where datum >= ? and datum <= ?  "
						+ "and buchung.buchungsart = buchungsart.id "
						+ "and buchungsart.id = ?";
				int anz = (Integer) service.execute(sqlc, new Object[]
				{
						datumvon, datumbis, buchungsart.getID()
				}, rsi);
				if (anz == 0)
				{
					continue;
				}
				ausgabe = true;
				String sql = "select sum(betrag) from buchung, buchungsart "
						+ "where datum >= ? and datum <= ?  "
						+ "and buchung.buchungsart = buchungsart.id "
						+ "and buchungsart.id = ? " + "and buchungsart.art = ?";
				einnahmen = (Double) service.execute(sql, new Object[]
				{
						datumvon, datumbis, buchungsart.getID(), 0
				}, rsd);
				suBukEinnahmen += einnahmen;
				ausgaben = (Double) service.execute(sql, new Object[]
				{
						datumvon, datumbis, buchungsart.getID(), 1
				}, rsd);
				suBukAusgaben += ausgaben;
				umbuchungen = (Double) service.execute(sql, new Object[]
				{
						datumvon, datumbis, buchungsart.getID(), 2
				}, rsd);
				suBukUmbuchungen += umbuchungen;
				zeile.add(new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.DETAIL,
						buchungsart, einnahmen, ausgaben, umbuchungen));
			}
			suEinnahmen += suBukEinnahmen;
			suAusgaben += suBukAusgaben;
			suUmbuchungen += suBukUmbuchungen;
			if (!ausgabe
					&& Einstellungen.getEinstellung().getUnterdrueckungOhneBuchung())
			{
				zeile.remove(zeile.size() - 1);
				continue;
			}

			zeile.add(
					new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.SALDOFOOTER,
							"Saldo" + " " + buchungsklasse.getBezeichnung(), suBukEinnahmen,
							suBukAusgaben, suBukUmbuchungen));
			zeile.add(new BuchungsklasseSaldoZeile(
					BuchungsklasseSaldoZeile.SALDOGEWINNVERLUST,
					"Gewinn/Verlust" + " " + buchungsklasse.getBezeichnung(),
					suBukEinnahmen + suBukAusgaben + suBukUmbuchungen));
		}
		String sql = "select sum(betrag) from buchung, buchungsart "
				+ "where datum >= ? and datum <= ?  "
				+ "and buchung.buchungsart = buchungsart.id "
				+ "and buchungsart.buchungsklasse is null and buchungsart.art = ?";
		einnahmen = (Double) service.execute(sql, new Object[]
		{
				datumvon, datumbis, 0
		}, rsd);
		suBukEinnahmen += einnahmen;
		suEinnahmen += einnahmen;
		ausgaben = (Double) service.execute(sql, new Object[]
		{
				datumvon, datumbis, 1
		}, rsd);
		suBukAusgaben += ausgaben;
		suAusgaben += ausgaben;
		umbuchungen = (Double) service.execute(sql, new Object[]
		{
				datumvon, datumbis, 2
		}, rsd);
		suBukUmbuchungen += umbuchungen;
		suUmbuchungen += umbuchungen;
		if (einnahmen != 0 || ausgaben != 0 || umbuchungen != 0)
		{
			Buchungsklasse b = (Buchungsklasse) service
					.createObject(Buchungsklasse.class, null);
			b.setBezeichnung("Nicht zugeordnet");
			zeile.add(
					new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.HEADER, b));
			zeile.add(new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.DETAIL,
					"Nicht zugeordnet", einnahmen, ausgaben, umbuchungen));
		}
		zeile.add(
				new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.GESAMTSALDOFOOTER,
						"Gesamtsaldo" + " ", suEinnahmen, suAusgaben, suUmbuchungen));
		zeile.add(new BuchungsklasseSaldoZeile(
				BuchungsklasseSaldoZeile.GESAMTGEWINNVERLUST, "Gesamt Gewinn/Verlust ",
				suEinnahmen + suAusgaben + suUmbuchungen));

		sql = "select count(*) from buchung " + "where datum >= ? and datum <= ?  "
				+ "and buchung.buchungsart is null";
		Integer anzahl = (Integer) service.execute(sql, new Object[]
		{
				datumvon, datumbis
		}, rsi);
		if (anzahl > 0)
		{
			zeile.add(new BuchungsklasseSaldoZeile(
					BuchungsklasseSaldoZeile.NICHTZUGEORDNETEBUCHUNGEN,
					"Anzahl Buchungen ohne Buchungsart", anzahl));
		}
		return zeile;
	}

	public void setDatumvon(Date datumvon)
	{
		this.datumvon = datumvon;
	}

	public void setDatumbis(Date datumbis)
	{
		this.datumbis = datumbis;
	}

	@Override
	public void removeAll()
	{
		saldoList.removeAll();
	}

	@Override
	public synchronized void paint(Composite parent) throws RemoteException
	{
		super.paint(parent);
	}

}
