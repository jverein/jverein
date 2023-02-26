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
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.logging.Logger;

public class BuchungsHeaderControl extends AbstractControl
{
	private TextInput kontoNameInput;

	private TextInput aktJahrAnfangSaldoInput;

	private TextInput aktJahrEinnahmenInput;

	private TextInput aktJahrAusgabenInput;

	private TextInput aktJahrSaldoInput;

	private TextInput lastJahrAnfangSaldoInput;

	private TextInput lastJahrEinnahmenInput;

	private TextInput lastJahrAusgabenInput;

	private TextInput lastJahrSaldoInput;

	public BuchungsHeaderControl(AbstractView view, BuchungsControl control)
	{
		super(view);
		control.addKontoChangeListener(new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				Object data = event.data;
				if (data instanceof Konto)
					felderAktuallisieren((Konto) data);
			}
		});
	}

	private void felderAktuallisieren(Konto konto)
	{
		try
		{
			DatenSammler sammler = ladeAnfangsBestand(konto);
			sammler = ermittleSaldos(sammler);
			zeigeDaten(sammler);
		}
		catch (RemoteException ex)
		{
			Logger.warn(ex.getLocalizedMessage());
		}
	}

	private void zeigeDaten(DatenSammler sammler) throws RemoteException
	{
		getKontoNameInput().setValue(sammler.konto.getBezeichnung());

		JahresDaten aktJahrDaten = sammler.getAktJahr();
		getAktJahrAnfangsSaldoInput().setValue(aktJahrDaten.getAnfangsSaldoText());
		getAktJahrAnfangsSaldoInput()
				.setComment(aktJahrDaten.getAnfangsDatumText());

		getAktJahrEinnahmenInput().setValue(aktJahrDaten.getEinnahmenText());
		getAktJahrAusgabenInput().setValue(aktJahrDaten.getAusgabenText());
		getAktJahrSaldoInput().setValue(aktJahrDaten.getSaldoText());
		getAktJahrSaldoInput().setComment(aktJahrDaten.getSaldoDatumText());

		JahresDaten vorJahrDaten = sammler.getVorJahr();
		getVorJahrAnfangsSaldoInput().setValue(vorJahrDaten.getAnfangsSaldoText());
		getVorJahrAnfangsSaldoInput()
				.setComment(vorJahrDaten.getAnfangsDatumText());

		getVorJahrEinnahmenInput().setValue(vorJahrDaten.getEinnahmenText());
		getVorJahrAusgabenInput().setValue(vorJahrDaten.getAusgabenText());
		getVorJahrSaldoInput().setValue(vorJahrDaten.getSaldoText());
		getVorJahrSaldoInput().setComment(vorJahrDaten.getSaldoDatumText());
	}

	private DatenSammler ermittleSaldos(DatenSammler sammler)
			throws RemoteException
	{
		DBIterator<Buchung> iteratorBuchungen = Einstellungen.getDBService()
				.createList(Buchung.class);
		iteratorBuchungen.addFilter("konto = ?", sammler.konto.getID());
		iteratorBuchungen.addFilter("datum >= ?", sammler.gibStartDatum());

		while (iteratorBuchungen.hasNext())
		{
			Buchung buchung = iteratorBuchungen.next();
			sammler.addBuchung(buchung);
		}
		return sammler;
	}

	private DatenSammler ladeAnfangsBestand(Konto konto) throws RemoteException
	{
		DBIterator<Anfangsbestand> iteratorAnfangsBestand = Einstellungen
				.getDBService().createList(Anfangsbestand.class);
		iteratorAnfangsBestand.addFilter("Konto = ?", konto.getID());
		iteratorAnfangsBestand.setOrder("order by datum desc");

		DatenSammler sammler = new DatenSammler(konto);
		if (iteratorAnfangsBestand.hasNext())
			sammler.setAnfangsBestandAktJahr(iteratorAnfangsBestand.next());
		if (iteratorAnfangsBestand.hasNext())
			sammler.setAnfangsBestandVorJahr(iteratorAnfangsBestand.next());

		return sammler;
	}

	public Input getKontoNameInput()
	{
		if (null == kontoNameInput)
		{
			kontoNameInput = new TextInput("");
			kontoNameInput.disable();
		}
		return kontoNameInput;
	}

	public Input getAktJahrAnfangsSaldoInput()
	{
		if (null == aktJahrAnfangSaldoInput)
		{
			aktJahrAnfangSaldoInput = createTextInput();
		}
		return aktJahrAnfangSaldoInput;
	}

	public Input getAktJahrEinnahmenInput()
	{
		if (null == aktJahrEinnahmenInput)
		{
			aktJahrEinnahmenInput = createTextInput();
		}
		return aktJahrEinnahmenInput;
	}

	public Input getAktJahrAusgabenInput()
	{
		if (null == aktJahrAusgabenInput)
		{
			aktJahrAusgabenInput = createTextInput();
		}
		return aktJahrAusgabenInput;
	}

	public Input getAktJahrSaldoInput()
	{
		if (null == aktJahrSaldoInput)
		{
			aktJahrSaldoInput = createTextInput();
		}
		return aktJahrSaldoInput;
	}

	public Input getVorJahrAnfangsSaldoInput()
	{
		if (null == lastJahrAnfangSaldoInput)
		{
			lastJahrAnfangSaldoInput = createTextInput();
		}
		return lastJahrAnfangSaldoInput;
	}

	public Input getVorJahrEinnahmenInput()
	{
		if (null == lastJahrEinnahmenInput)
		{
			lastJahrEinnahmenInput = createTextInput();
		}
		return lastJahrEinnahmenInput;
	}

	public Input getVorJahrAusgabenInput()
	{
		if (null == lastJahrAusgabenInput)
		{
			lastJahrAusgabenInput = createTextInput();
		}
		return lastJahrAusgabenInput;
	}

	public Input getVorJahrSaldoInput()
	{
		if (null == lastJahrSaldoInput)
		{
			lastJahrSaldoInput = createTextInput();
		}
		return lastJahrSaldoInput;
	}

	private TextInput createTextInput()
	{
		TextInput input = new TextInput("");
		input.setComment("");
		input.disable();
		return input;
	}

	static class JahresDaten
	{
		private static CurrencyFormatter wertFormater = new CurrencyFormatter("",
				Einstellungen.DECIMALFORMAT);

		private static DateFormatter dateFormater = new DateFormatter(
				new JVDateFormatTTMMJJJJ());

		Anfangsbestand anfangsBestand;

		double einnahmen;

		double ausgaben;

		double saldo;

		Date juengstesBuchungsDatum;

		public JahresDaten()
		{
			einnahmen = 0d;
			ausgaben = 0d;
			saldo = 0d;
		}

		public void setAnfangsBestand(Anfangsbestand bestand) throws RemoteException
		{
			anfangsBestand = bestand;
			if (null != bestand)
				saldo = bestand.getBetrag();
		}

		public boolean hatAnfangsBestand()
		{
			if (null != anfangsBestand)
				return true;
			return false;
		}

		public void addBuchung(Buchung buchung) throws RemoteException
		{
			double wert = buchung.getBetrag();
			saldo += wert;
			if (wert > 0d)
				einnahmen += wert;
			else
				ausgaben += wert;

			Date datum = buchung.getDatum();
			if (null == juengstesBuchungsDatum)
				juengstesBuchungsDatum = datum;
			else if (juengstesBuchungsDatum.before(datum))
				juengstesBuchungsDatum = datum;
		}

		private String formatWert(double wert)
		{
			if (wert != 0d)
				return wertFormater.format(Double.valueOf(wert));
			return "\\";
		}

		private String formatDatum(Date datum)
		{
			if (null == datum)
				return " ";
			return dateFormater.format(datum);
		}

		public String getSaldoText()
		{
			return formatWert(saldo);
		}

		public String getSaldoDatumText()
		{
			return "letzte Buchung: " + formatDatum(juengstesBuchungsDatum);
		}

		public String getAusgabenText()
		{
			return formatWert(ausgaben);
		}

		public String getEinnahmenText()
		{
			return formatWert(einnahmen);
		}

		public String getAnfangsSaldoText() throws RemoteException
		{
			if (null == anfangsBestand)
				return "-";
			return formatWert(anfangsBestand.getBetrag());
		}

		public Date getAnfangsDatum() throws RemoteException
		{
			if (null == anfangsBestand)
				return null;
			return anfangsBestand.getDatum();
		}

		public String getAnfangsDatumText() throws RemoteException
		{
			if (null == anfangsBestand)
				return " ";
			return "am: " + formatDatum(anfangsBestand.getDatum());
		}
	}

	static class DatenSammler
	{
		Konto konto;

		JahresDaten aktJahr;

		JahresDaten vorJahr;

		long grenzeVorJahr;

		public DatenSammler(Konto konto)
		{
			this.konto = konto;
			aktJahr = new JahresDaten();
			vorJahr = new JahresDaten();
			grenzeVorJahr = 0;
		}

		/**
		 * Datum ab dem die Summen ermittelt werden sollen
		 * 
		 * @return
		 * @throws RemoteException
		 */
		public Date gibStartDatum() throws RemoteException
		{
			if (vorJahr.hatAnfangsBestand())
				return vorJahr.getAnfangsDatum();
			if (aktJahr.hatAnfangsBestand())
				return aktJahr.getAnfangsDatum();

			Calendar calendar = Calendar.getInstance();
			int jahr = calendar.get(Calendar.YEAR);
			calendar.clear();
			calendar.set(jahr, Calendar.JANUARY, 1);
			return calendar.getTime();
		}

		public void addBuchung(Buchung buchung) throws RemoteException
		{
			if (isFuerVorjahr(buchung))
				vorJahr.addBuchung(buchung);
			else
				aktJahr.addBuchung(buchung);
		}

		private boolean isFuerVorjahr(Buchung buchung) throws RemoteException
		{
			if (buchung.getDatum().getTime() < grenzeVorJahr)
				return true;
			return false;
		}

		public void setAnfangsBestandAktJahr(Anfangsbestand bestand)
				throws RemoteException
		{
			aktJahr.setAnfangsBestand(bestand);
			if (null != bestand)
			{
				grenzeVorJahr = bestand.getDatum().getTime();
			}
		}

		public void setAnfangsBestandVorJahr(Anfangsbestand bestand)
				throws RemoteException
		{
			vorJahr.setAnfangsBestand(bestand);
		}

		public JahresDaten getAktJahr()
		{
			return aktJahr;
		}

		public JahresDaten getVorJahr()
		{
			return vorJahr;
		}

	}
}
