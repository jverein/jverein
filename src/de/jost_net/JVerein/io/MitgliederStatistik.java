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
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.JVerein.util.VonBis;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliederStatistik
{

	public MitgliederStatistik(final File file, Date stichtag)
			throws ApplicationException
	{
		try
		{
			if (stichtag == null)
			{
				throw new ApplicationException("Stichtag ist leer");
			}
			FileOutputStream fos = new FileOutputStream(file);
			String subtitle = "";
			if (stichtag != null)
			{
				subtitle = "Stichtag: " + new JVDateFormatTTMMJJJJ().format(stichtag);
			}
			Reporter reporter = new Reporter(fos, "Mitgliederstatistik", subtitle, 3);

			Paragraph pAltersgruppen = new Paragraph("\n" + "Altersgruppen",
					Reporter.getFreeSans(11));
			reporter.add(pAltersgruppen);

			reporter.addHeaderColumn("Altersgruppe", Element.ALIGN_CENTER, 100,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("Insgesamt", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("männlich", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("weiblich", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("ohne Angabe", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.createHeader(70f, Element.ALIGN_LEFT);

			AltersgruppenParser ap = new AltersgruppenParser(
					Einstellungen.getEinstellung().getAltersgruppen());
			while (ap.hasNext())
			{
				VonBis vb = ap.getNext();
				addAltersgruppe(reporter, vb, stichtag);
			}
			addAltersgruppe(reporter, new VonBis(-1, -1), stichtag);
			addAltersgruppe(reporter, new VonBis(0, 199), stichtag);
			reporter.closeTable();

			Paragraph pBeitragsgruppen = new Paragraph("\n" + "Beitragsgruppen",
					Reporter.getFreeSans(11));
			reporter.add(pBeitragsgruppen);

			reporter.addHeaderColumn("Beitragsgruppe", Element.ALIGN_CENTER, 100,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("Insgesamt", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("männlich", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("weiblich", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.addHeaderColumn("ohne Angabe", Element.ALIGN_CENTER, 30,
					BaseColor.LIGHT_GRAY);
			reporter.createHeader(70f, Element.ALIGN_LEFT);

			DBIterator<Beitragsgruppe> beitragsgruppen = Einstellungen.getDBService()
					.createList(Beitragsgruppe.class);
			beitragsgruppen.setOrder("order by bezeichnung");
			while (beitragsgruppen.hasNext())
			{
				Beitragsgruppe bg = beitragsgruppen.next();
				addBeitragsgruppe(reporter, bg, stichtag);
			}
			addBeitragsgruppe(reporter, null, stichtag);
			reporter.closeTable();

			try
			{
				JVDateFormatTTMMJJJJ ttmmjj = new JVDateFormatTTMMJJJJ();
				Geschaeftsjahr gj = new Geschaeftsjahr(stichtag);
				Paragraph pGuV = new Paragraph(
						"\n" + String.format("Anmeldungen/Abmeldungen (%s - %s)",
								ttmmjj.format(gj.getBeginnGeschaeftsjahr()),
								ttmmjj.format(gj.getEndeGeschaeftsjahr())),
						Reporter.getFreeSans(11));
				reporter.add(pGuV);
				reporter.addHeaderColumn("Text", Element.ALIGN_CENTER, 100,
						BaseColor.LIGHT_GRAY);
				reporter.addHeaderColumn("Anzahl", Element.ALIGN_CENTER, 30,
						BaseColor.LIGHT_GRAY);
				reporter.createHeader(70f, Element.ALIGN_LEFT);
				reporter.addColumn("Anmeldungen", Element.ALIGN_LEFT);
				reporter.addColumn(getAnmeldungen(gj) + "", Element.ALIGN_RIGHT);
				reporter.addColumn("Abmeldungen", Element.ALIGN_LEFT);
				reporter.addColumn(getAbmeldungen(gj) + "", Element.ALIGN_RIGHT);
				reporter.closeTable();
			}
			catch (ParseException e)
			{
				Logger.error("Fehler", e);
				throw new ApplicationException(e);
			}
			reporter.close();
			fos.close();
			FileViewer.show(file);
		}
		catch (Exception e)
		{
			Logger.error("Fehler", e);
			throw new ApplicationException("Fehler", e);
		}
	}

	private void addAltersgruppe(Reporter reporter, VonBis vb, Date stichtag)
			throws RemoteException
	{
		if (vb.getVon() == -1)
		{
			reporter.addColumn("ohne Geburtsdatum", Element.ALIGN_LEFT);
		}
		else if (vb.getVon() == 0 && vb.getBis() == 199)
		{
			reporter.addColumn("Insgesamt", Element.ALIGN_LEFT);
		}
		else
		{
			reporter.addColumn(
					String.format("Altersgruppe %d - %d", vb.getVon(), vb.getBis()),
					Element.ALIGN_LEFT);
		}
		reporter.addColumn(getAltersgruppe(vb, null, stichtag) + "",
				Element.ALIGN_RIGHT);
		reporter.addColumn(
				getAltersgruppe(vb, GeschlechtInput.MAENNLICH, stichtag) + "",
				Element.ALIGN_RIGHT);
		reporter.addColumn(
				getAltersgruppe(vb, GeschlechtInput.WEIBLICH, stichtag) + "",
				Element.ALIGN_RIGHT);
		reporter.addColumn(
				getAltersgruppe(vb, GeschlechtInput.OHNEANGABE, stichtag) + "",
				Element.ALIGN_RIGHT);
	}

	private void addBeitragsgruppe(Reporter reporter, Beitragsgruppe bg,
			Date stichtag) throws RemoteException
	{
		if (bg == null)
		{
			reporter.addColumn("Insgesamt", Element.ALIGN_LEFT);
		}
		else
		{
			reporter.addColumn(bg.getBezeichnung(), Element.ALIGN_LEFT);
		}
		reporter.addColumn(getBeitragsgruppe(bg, null, stichtag) + "",
				Element.ALIGN_RIGHT);
		reporter.addColumn(
				getBeitragsgruppe(bg, GeschlechtInput.MAENNLICH, stichtag) + "",
				Element.ALIGN_RIGHT);
		reporter.addColumn(
				getBeitragsgruppe(bg, GeschlechtInput.WEIBLICH, stichtag) + "",
				Element.ALIGN_RIGHT);
		reporter.addColumn(
				getBeitragsgruppe(bg, GeschlechtInput.OHNEANGABE, stichtag) + "",
				Element.ALIGN_RIGHT);
	}

	/**
	 * Anzahl der Mitglieder in einer Altersgruppe ermitteln
	 * 
	 * @param von        Alter in Jahren
	 * @param bis        Alter in Jahren
	 * @param geschlecht m, w oder null
	 * @return Anzahl der Mitglieder
	 */
	private int getAltersgruppe(VonBis vb, String geschlecht, Date stichtag)
			throws RemoteException
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(stichtag);
		boolean schalttag = false;

		/**
		 * Pruefung, ob 29. Februar als Stichtag gewaehlt wurde
		 */
		if (cal.get(GregorianCalendar.MONTH) == GregorianCalendar.FEBRUARY
				&& cal.get(GregorianCalendar.DAY_OF_MONTH) == 29)
		{
			schalttag = true;
		}

		GregorianCalendar calVon = (GregorianCalendar) cal.clone();
		calVon.add(GregorianCalendar.YEAR, (vb.getBis() + 1) * -1);

		/**
		 * Im Normalfall (Stichtag != 29.02.), muss immer ein Tag addiert werden, da
		 * sonst ein Tag zu viel - naemlich das gesetzte Stichtagsdatum - mit
		 * erfasst wuerde. Ebenfalls muss ein Tag addiert werden, wenn ein Schalttag
		 * gesetzt ist und gleichzeitig calVon ein Schaltjahr ist. In den
		 * verbleibenden Faellen (wenn Schalttag gesetzt ist und calVon) kein
		 * Schaltjahr ist, muss nichts getan werden, da automatisch der 01.03
		 * referenziert wird.
		 */
		if ((calVon.isLeapYear(calVon.get(GregorianCalendar.YEAR)) && schalttag)
				|| !schalttag)
		{
			calVon.add(GregorianCalendar.DATE, 1);
		}
		java.sql.Date vd = new java.sql.Date(calVon.getTimeInMillis());

		GregorianCalendar calBis = (GregorianCalendar) cal.clone();
		calBis.add(GregorianCalendar.YEAR, vb.getVon() * -1);

		/**
		 * Im Fall, dass calBis kein Schaltjahr ist und der 29. Februar als Stichtag
		 * gesetzt ist, muss ein Tag subrahiert werden, da calVon dann den 01.03.
		 * referenziert (und noch mit erfassen wuerde), ansonsten muss nichts
		 * gemacht werden.
		 */
		if (schalttag && !calBis.isLeapYear(calBis.get(GregorianCalendar.YEAR)))
		{
			calBis.add(GregorianCalendar.DATE, -1);
		}
		java.sql.Date bd = new java.sql.Date(calBis.getTimeInMillis());

		DBIterator<Mitglied> list = Einstellungen.getDBService()
				.createList(Mitglied.class);
		if (vb.getVon() == -1)
		{
			list.addFilter("geburtsdatum is null");
		}
		else if (vb.getBis() != 199)
		{
			list.addFilter("geburtsdatum >= ?", new Object[]
			{
					vd
			});
			list.addFilter("geburtsdatum <= ?", new Object[]
			{
					bd
			});
		}
		MitgliedUtils.setNurAktive(list, stichtag);
		MitgliedUtils.setMitglied(list);

		/**
		 * Filter bereits ueber "setNurAktive" gesetzt
		 */
		// list.addFilter("(eintritt is null or eintritt <= ?)", new Object[] {
		// stichtag });

		if (geschlecht != null && (geschlecht.equals(GeschlechtInput.MAENNLICH)
				|| geschlecht.equals(GeschlechtInput.WEIBLICH)))
		{
			list.addFilter("geschlecht = ?", new Object[]
			{
					geschlecht
			});
		}
		else
		{
			if (geschlecht != null && geschlecht.equals(GeschlechtInput.OHNEANGABE))
			{
				list.addFilter("(geschlecht = ? OR geschlecht is null)", new Object[]
				{
						geschlecht
				});
			}
		}

		return list.size();
	}

	private int getBeitragsgruppe(Beitragsgruppe bg, String geschlecht,
			Date stichtag) throws RemoteException
	{
		DBIterator<Mitglied> list = Einstellungen.getDBService()
				.createList(Mitglied.class);
		MitgliedUtils.setNurAktive(list, stichtag);
		MitgliedUtils.setMitglied(list);

		/**
		 * Filter bereits ueber "setNurAktive" gesetzt
		 */
		// list.addFilter("(eintritt is null or eintritt <= ?)",
		// new Object[] { stichtag });

		if (bg != null)
		{
			list.addFilter("beitragsgruppe = ?", new Object[]
			{
					new Integer(bg.getID())
			});
		}
		if (geschlecht != null && (geschlecht.equals(GeschlechtInput.MAENNLICH)
				|| geschlecht.equals(GeschlechtInput.WEIBLICH)))
		{
			list.addFilter("geschlecht = ?", new Object[]
			{
					geschlecht
			});
		}
		else
		{
			if (geschlecht != null && geschlecht.equals(GeschlechtInput.OHNEANGABE))
			{
				list.addFilter("(geschlecht = ? OR geschlecht is null)", new Object[]
				{
						geschlecht
				});
			}
		}
		return list.size();
	}

	private int getAnmeldungen(Geschaeftsjahr gj) throws RemoteException
	{
		DBIterator<Mitglied> list = Einstellungen.getDBService()
				.createList(Mitglied.class);
		MitgliedUtils.setMitglied(list);
		list.addFilter("eintritt >= ? ", new Object[]
		{
				gj.getBeginnGeschaeftsjahr()
		});
		list.addFilter("eintritt <= ? ", new Object[]
		{
				gj.getEndeGeschaeftsjahr()
		});
		return list.size();
	}

	private int getAbmeldungen(Geschaeftsjahr gj) throws RemoteException
	{
		DBIterator<Mitglied> list = Einstellungen.getDBService()
				.createList(Mitglied.class);
		MitgliedUtils.setMitglied(list);
		list.addFilter("austritt >= ? ", new Object[]
		{
				gj.getBeginnGeschaeftsjahr()
		});
		list.addFilter("austritt <= ? ", new Object[]
		{
				gj.getEndeGeschaeftsjahr()
		});
		return list.size();
	}
}
