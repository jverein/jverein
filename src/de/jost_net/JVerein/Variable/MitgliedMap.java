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
package de.jost_net.JVerein.Variable;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.io.BeitragsUtil;
import de.jost_net.JVerein.io.VelocityTool;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.keys.Zahlungstermin;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.LesefeldAuswerter;
import de.jost_net.JVerein.util.StringTool;
import de.jost_net.OBanToo.SEPA.BankenDaten.Bank;
import de.jost_net.OBanToo.SEPA.BankenDaten.Banken;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;

public class MitgliedMap
{

	public MitgliedMap()
	{
		//
	}

	public Map<String, Object> getMap(Mitglied m, Map<String, Object> inma)
			throws RemoteException
	{
		return getMap(m, inma, false);
	}

	public Map<String, Object> getMap(Mitglied m, Map<String, Object> inma,
			boolean ohneLesefelder) throws RemoteException
	{
		Map<String, Object> map = null;

		if (inma == null)
		{
			map = new HashMap<>();
		}
		else
		{
			map = inma;
		}
		if (m.getID() == null)
		{
			m.setAdressierungszusatz("3. Hinterhof");
			m.setAdresstyp(1);
			m.setAnrede("Herrn");
			m.setAustritt("01.04.2011");
			DBIterator<Beitragsgruppe> it = Einstellungen.getDBService()
					.createList(Beitragsgruppe.class);
			Beitragsgruppe bg = it.next();
			m.setBeitragsgruppe(Integer.parseInt(bg.getID()));
			m.setBic("XXXXXXXXXXX");
			m.setEingabedatum();
			m.setEintritt("05.02.1999");
			m.setEmail("willi.wichtig@jverein.de");
			m.setExterneMitgliedsnummer("123456");
			m.setGeburtsdatum("02.03.1980");
			m.setGeschlecht(GeschlechtInput.MAENNLICH);
			m.setHandy("0170/123456789");
			m.setIban("DE89370400440532013000");
			m.setID("1");
			m.setIndividuellerBeitrag(123.45);
			m.setKtoiPersonenart("n");
			m.setKtoiAnrede("Herrn");
			m.setKtoiTitel("Dr. Dr.");
			m.setKtoiName("Wichtig");
			m.setKtoiVorname("Willi");
			m.setKtoiStrasse("Bahnhofstr. 22");
			m.setAdressierungszusatz("Hinterhof bei Lieschen Müller");
			m.setPlz("12345");
			m.setOrt("Testenhausen");
			m.setKuendigung("21.02.2011");
			m.setLetzteAenderung();
			m.setName("Wichtig");
			m.setOrt("Testenhausen");
			m.setPersonenart("n");
			m.setPlz("12345");
			m.setStaat("Deutschland");
			m.setSterbetag(new Date());
			m.setStrasse("Hafengasse 124");
			m.setTelefondienstlich("123455600");
			m.setTelefonprivat("123456");
			m.setTitel("Dr.");
			m.setVermerk1("Vermerk 1");
			m.setVermerk2("Vermerk 2");
			m.setVorname("Willi");
			m.setZahlungsrhythmus(12);
			m.setZahlungstermin(Zahlungstermin.VIERTELJAEHRLICH1.getKey());
			m.setZahlungsweg(1);
			m.setZahlungstermin(Zahlungstermin.HALBJAEHRLICH4.getKey());
		}
		map.put(MitgliedVar.ADRESSIERUNGSZUSATZ.getName(),
				StringTool.toNotNullString(m.getAdressierungszusatz()));
		map.put(MitgliedVar.ADRESSTYP.getName(),
				StringTool.toNotNullString(m.getAdresstyp().getID()));
		map.put(MitgliedVar.ANREDE.getName(),
				StringTool.toNotNullString(m.getAnrede()));
		map.put(MitgliedVar.ANREDE_FOERMLICH.getName(),
				Adressaufbereitung.getAnredeFoermlich(m));
		map.put(MitgliedVar.ANREDE_DU.getName(), Adressaufbereitung.getAnredeDu(m));
		map.put(MitgliedVar.AUSTRITT.getName(), Datum.formatDate(m.getAustritt()));
		map.put(MitgliedVar.BEITRAGSGRUPPE_ARBEITSEINSATZ_BETRAG.getName(),
				m.getBeitragsgruppe() != null
						? Einstellungen.DECIMALFORMAT
								.format(m.getBeitragsgruppe().getArbeitseinsatzBetrag())
						: "");
		map.put(MitgliedVar.BEITRAGSGRUPPE_ARBEITSEINSATZ_STUNDEN.getName(),
				m.getBeitragsgruppe() != null
						? Einstellungen.DECIMALFORMAT
								.format(m.getBeitragsgruppe().getArbeitseinsatzStunden())
						: "");
		try
		{
			map.put(MitgliedVar.BEITRAGSGRUPPE_BETRAG.getName(),
					m.getBeitragsgruppe() != null
							? Einstellungen.DECIMALFORMAT.format(BeitragsUtil.getBeitrag(
									Einstellungen.getEinstellung().getBeitragsmodel(),
									m.getZahlungstermin(), m.getZahlungsrhythmus().getKey(),
									m.getBeitragsgruppe(), new Date(), m.getEintritt(),
									m.getAustritt()))
							: "");
		}
		catch (NullPointerException e)
		{
			Logger.error("NullPointerException:" + m.getName());
		}
		map.put(MitgliedVar.BEITRAGSGRUPPE_BEZEICHNUNG.getName(),
				m.getBeitragsgruppe() != null ? m.getBeitragsgruppe().getBezeichnung()
						: "");
		map.put(MitgliedVar.BEITRAGSGRUPPE_ID.getName(),
				m.getBeitragsgruppe() != null ? m.getBeitragsgruppe().getID() : "");
		map.put(MitgliedVar.MANDATDATUM.getName(), m.getMandatDatum());
		map.put(MitgliedVar.MANDATID.getName(), m.getMandatID());
		map.put(MitgliedVar.BIC.getName(), m.getBic());
		map.put(MitgliedVar.EINGABEDATUM.getName(),
				Datum.formatDate(m.getEingabedatum()));
		map.put(MitgliedVar.EINTRITT.getName(), Datum.formatDate(m.getEintritt()));
		map.put(MitgliedVar.EMAIL.getName(), m.getEmail());
		map.put(MitgliedVar.EMPFAENGER.getName(),
				Adressaufbereitung.getAdressfeld(m));
		map.put(MitgliedVar.EXTERNE_MITGLIEDSNUMMER.getName(),
				m.getExterneMitgliedsnummer());
		map.put(MitgliedVar.GEBURTSDATUM.getName(),
				Datum.formatDate(m.getGeburtsdatum()));
		map.put(MitgliedVar.GESCHLECHT.getName(), m.getGeschlecht());
		map.put(MitgliedVar.HANDY.getName(), m.getHandy());
		map.put(MitgliedVar.IBANMASKIERT.getName(),
				VarTools.maskieren(m.getIban()));
		map.put(MitgliedVar.IBAN.getName(), m.getIban());
		map.put(MitgliedVar.ID.getName(), m.getID());
		map.put(MitgliedVar.INDIVIDUELLERBEITRAG.getName(),
				Einstellungen.DECIMALFORMAT.format(m.getIndividuellerBeitrag()));
		map.put(MitgliedVar.BANKNAME.getName(), getBankname(m));
		map.put(MitgliedVar.KONTOINHABER_ADRESSIERUNGSZUSATZ.getName(),
				m.getKtoiAdressierungszusatz());
		map.put(MitgliedVar.KONTOINHABER_ANREDE.getName(), m.getKtoiAnrede());
		map.put(MitgliedVar.KONTOINHABER_EMAIL.getName(), m.getKtoiEmail());
		map.put(MitgliedVar.KONTOINHABER_NAME.getName(), m.getKtoiName());
		map.put(MitgliedVar.KONTOINHABER_ORT.getName(), m.getKtoiOrt());
		map.put(MitgliedVar.KONTOINHABER_PERSONENART.getName(),
				m.getKtoiPersonenart());
		map.put(MitgliedVar.KONTOINHABER_PLZ.getName(), m.getKtoiPlz());
		map.put(MitgliedVar.KONTOINHABER_STAAT.getName(), m.getKtoiStaat());
		map.put(MitgliedVar.KONTOINHABER_STRASSE.getName(), m.getKtoiStrasse());
		map.put(MitgliedVar.KONTOINHABER_TITEL.getName(), m.getKtoiTitel());
		map.put(MitgliedVar.KONTOINHABER_VORNAME.getName(), m.getKtoiVorname());
		map.put(MitgliedVar.KUENDIGUNG.getName(),
				Datum.formatDate(m.getKuendigung()));
		map.put(MitgliedVar.LETZTEAENDERUNG.getName(),
				Datum.formatDate(m.getLetzteAenderung()));
		map.put(MitgliedVar.NAME.getName(), m.getName());
		map.put(MitgliedVar.NAMEVORNAME.getName(),
				Adressaufbereitung.getNameVorname(m));
		map.put(MitgliedVar.ORT.getName(), m.getOrt());
		map.put(MitgliedVar.PERSONENART.getName(), m.getPersonenart());
		map.put(MitgliedVar.PLZ.getName(), m.getPlz());
		map.put(MitgliedVar.STAAT.getName(), m.getStaat());
		map.put(MitgliedVar.STERBETAG.getName(),
				Datum.formatDate(m.getSterbetag()));
		map.put(MitgliedVar.STRASSE.getName(), m.getStrasse());
		map.put(MitgliedVar.TELEFONDIENSTLICH.getName(), m.getTelefondienstlich());
		map.put(MitgliedVar.TELEFONPRIVAT.getName(), m.getTelefonprivat());
		map.put(MitgliedVar.TITEL.getName(), m.getTitel());
		map.put(MitgliedVar.VERMERK1.getName(), m.getVermerk1());
		map.put(MitgliedVar.VERMERK2.getName(), m.getVermerk2());
		map.put(MitgliedVar.VORNAME.getName(), m.getVorname());
		map.put(MitgliedVar.VORNAMENAME.getName(),
				Adressaufbereitung.getVornameName(m));
		map.put(MitgliedVar.ZAHLERID.getName(), m.getZahlerID());
		map.put(MitgliedVar.ZAHLUNGSRHYTMUS.getName(),
				m.getZahlungsrhythmus() + "");
		map.put(MitgliedVar.ZAHLUNGSRHYTHMUS.getName(),
				m.getZahlungsrhythmus() + "");
		map.put(MitgliedVar.ZAHLUNGSTERMIN.getName(),
				m.getZahlungstermin() != null ? m.getZahlungstermin().getText() : "");
		map.put(MitgliedVar.ZAHLUNGSWEG.getName(), m.getZahlungsweg() + "");

		String zahlungsweg = "";
		switch (m.getZahlungsweg())
		{
			case Zahlungsweg.BASISLASTSCHRIFT:
			{
				zahlungsweg = Einstellungen.getEinstellung().getRechnungTextAbbuchung();
				zahlungsweg = zahlungsweg.replaceAll("\\$\\{BIC\\}", m.getBic());
				zahlungsweg = zahlungsweg.replaceAll("\\$\\{IBAN\\}", m.getIban());
				zahlungsweg = zahlungsweg.replaceAll("\\$\\{MANDATID\\}",
						m.getMandatID());
				break;
			}
			case Zahlungsweg.BARZAHLUNG:
			{
				zahlungsweg = Einstellungen.getEinstellung().getRechnungTextBar();
				break;
			}
			case Zahlungsweg.ÜBERWEISUNG:
			{
				zahlungsweg = Einstellungen.getEinstellung()
						.getRechnungTextUeberweisung();
				break;
			}
		}
		try
		{
			zahlungsweg = VelocityTool.eval(map, zahlungsweg);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		map.put(MitgliedVar.ZAHLUNGSWEGTEXT.getName(), zahlungsweg);

		HashMap<String, String> format = new HashMap<>();
		DBIterator<Felddefinition> itfd = Einstellungen.getDBService()
				.createList(Felddefinition.class);
		while (itfd.hasNext())
		{
			Felddefinition fd = itfd.next();
			DBIterator<Zusatzfelder> itzus = Einstellungen.getDBService()
					.createList(Zusatzfelder.class);
			itzus.addFilter("mitglied = ? and felddefinition = ? ", new Object[]
			{
					m.getID(), fd.getID()
			});
			Zusatzfelder z = null;
			if (itzus.hasNext())
			{
				z = itzus.next();
			}
			else
			{
				z = (Zusatzfelder) Einstellungen.getDBService()
						.createObject(Zusatzfelder.class, null);
			}

			switch (fd.getDatentyp())
			{
				case Datentyp.DATUM:
					map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
							Datum.formatDate(z.getFeldDatum()));
					format.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "DATE");
					break;
				case Datentyp.JANEIN:
					map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
							z.getFeldJaNein() ? "X" : " ");
					break;
				case Datentyp.GANZZAHL:
					map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
							z.getFeldGanzzahl() + "");
					format.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "INTEGER");
					break;
				case Datentyp.WAEHRUNG:
					if (z.getFeldWaehrung() != null)
					{
						map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
								Einstellungen.DECIMALFORMAT.format(z.getFeldWaehrung()));
						format.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "DOUBLE");
					}
					else
					{
						map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "");
					}
					break;
				case Datentyp.ZEICHENFOLGE:
					map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), z.getFeld());
					break;
			}
		}

		DBIterator<Eigenschaft> iteig = Einstellungen.getDBService()
				.createList(Eigenschaft.class);
		while (iteig.hasNext())
		{
			Eigenschaft eig = iteig.next();
			DBIterator<Eigenschaften> iteigm = Einstellungen.getDBService()
					.createList(Eigenschaften.class);
			iteigm.addFilter("mitglied = ? and eigenschaft = ?", new Object[]
			{
					m.getID(), eig.getID()
			});
			String val = "";
			if (iteigm.size() > 0)
			{
				val = "X";
			}
			map.put("mitglied_eigenschaft_" + eig.getBezeichnung(), val);
		}

		for (String varname : m.getVariablen().keySet())
		{
			map.put(varname, m.getVariablen().get(varname));
		}

		if (!ohneLesefelder)
		{
			// Füge Lesefelder diesem Mitglied-Objekt hinzu.
			LesefeldAuswerter l = new LesefeldAuswerter();
			l.setLesefelderDefinitionsFromDatabase();
			l.setMap(map);
			map.putAll(l.getLesefelderMap());
		}

		return map;
	}

	private Object getBankname(Mitglied m) throws RemoteException
	{
		String bic = m.getBic();
		if (null != bic)
		{
			Bank bank = Banken.getBankByBIC(bic);
			if (null != bank)
			{
				return formatBankname(bank);
			}
		}
		return null;
	}

	private String formatBankname(Bank bank)
	{
		String name = bank.getBezeichnung();
		if (null != name)
		{
			return name.trim();
		}
		return null;
	}

}
