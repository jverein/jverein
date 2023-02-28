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

import com.itextpdf.text.pdf.BaseFont;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineVar;
import de.jost_net.JVerein.Variable.LastschriftVar;
import de.jost_net.JVerein.Variable.MitgliedVar;
import de.jost_net.JVerein.Variable.MitgliedskontoVar;
import de.jost_net.JVerein.Variable.SpendenbescheinigungVar;
import de.jost_net.JVerein.gui.action.FormularfeldAction;
import de.jost_net.JVerein.gui.menu.FormularfeldMenu;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.jost_net.JVerein.rmi.Lesefeld;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;

public class FormularfeldControl extends AbstractControl
{

	private de.willuhn.jameica.system.Settings settings;

	private TablePart formularfelderList;

	private SelectInput name;

	private IntegerInput seite;

	private DecimalInput x;

	private DecimalInput y;

	private SelectInput font;

	private IntegerInput fontsize;

	private Formular formular;

	private Formularfeld formularfeld;

	private TextInput formularTyp;

	private TextInput formularName;

	public static final String EMPFAENGER = "Empfänger";

	public static final String TAGESDATUM = "Tagesdatum";

	public static final String TAGESDATUMTT = "Tagesdatum TT";

	public static final String TAGESDATUMMM = "Tagesdatum MM";

	public static final String TAGESDATUMJJJJ = "Tagesdatum JJJJ";

	public static final String ZAHLUNGSGRUND = "Zahlungsgrund";

	public static final String ZAHLUNGSGRUND1 = "Zahlungsgrund 1";

	public static final String BUCHUNGSDATUM = "Buchungsdatum";

	public static final String BETRAG = "Betrag";

	public static final String ZAHLUNGSWEG = "Zahlungsweg";

	public static final String ID = "ID";

	public static final String EXTERNEMITGLIEDSNUMMER = "externe Mitgliedsnummer";

	public static final String ANREDE = "Anrede";

	public static final String TITEL = "Titel";

	public static final String NAME = "Name";

	public static final String VORNAME = "Vorname";

	public static final String ADRESSIERUNGSZUSATZ = "Adressierungszusatz";

	public static final String STRASSE = "Strasse";

	public static final String PLZ = "PLZ";

	public static final String ORT = "Ort";

	public static final String STAAT = "Staat";

	@Deprecated
	public static final String ZAHLUNGSRHYTMUS = "Zahlungsrhytmus";

	public static final String ZAHLUNGSRHYTHMUS = "Zahlungsrhythmus";

	public static final String KONTOINHABER = "Kontoinhaber";

	public static final String GEBURTSDATUM = "Geburtsdatum";

	public static final String GESCHLECHT = "Geschlecht";

	public static final String TELEFONPRIVAT = "Telefon privat";

	public static final String TELEFONDIENSTLICH = "Telefon dienstlich";

	public static final String HANDY = "Handy";

	public static final String EMAIL = "Email";

	public static final String EINTRITT = "Eintritt";

	public static final String BEITRAGSGRUPPE = "Beitragsgruppe";

	public static final String AUSTRITT = "Austritt";

	public static final String KUENDIGUNG = "Kündigung";

	public FormularfeldControl(AbstractView view, Formular formular)
	{
		super(view);
		settings = new de.willuhn.jameica.system.Settings(this.getClass());
		settings.setStoreWhenRead(true);
		this.formular = formular;
	}

	public Input getFormularTyp() throws RemoteException
	{
		if (null == formularTyp)
		{
			formularTyp = new TextInput(getFormularArtName());
			formularTyp.disable();
		}
		return formularTyp;
	}

	public Input getFormularName() throws RemoteException
	{
		if (null == formularName)
		{
			formularName = new TextInput(formular.getBezeichnung());
			formularName.disable();
		}
		return formularName;
	}

	public Formularfeld getFormularfeld()
	{
		if (formularfeld != null)
		{
			return formularfeld;
		}
		formularfeld = (Formularfeld) getCurrentObject();
		return formularfeld;
	}

	public Formular getFormular()
	{
		return formular;
	}

	public SelectInput getName() throws Exception
	{
		if (name != null)
		{
			return name;
		}
		ArrayList<String> namen = new ArrayList<>();
		if (formular.getArt() == FormularArt.SPENDENBESCHEINIGUNG)
		{
			for (AllgemeineVar av : AllgemeineVar.values())
			{
				namen.add(av.getName());
			}
			for (SpendenbescheinigungVar spv : SpendenbescheinigungVar.values())
			{
				namen.add(spv.getName());
			}
		}
		if (formular.getArt() == FormularArt.SAMMELSPENDENBESCHEINIGUNG)
		{
			for (AllgemeineVar av : AllgemeineVar.values())
			{
				namen.add(av.getName());
			}
			for (SpendenbescheinigungVar spv : SpendenbescheinigungVar.values())
			{
				namen.add(spv.getName());
			}
		}
		if (formular.getArt() == FormularArt.FREIESFORMULAR)
		{
			for (AllgemeineVar av : AllgemeineVar.values())
			{
				namen.add(av.getName());
			}
			for (MitgliedVar mv : MitgliedVar.values())
			{
				namen.add(mv.getName());
			}
		}
		if (formular.getArt() == FormularArt.SEPA_PRENOTIFICATION)
		{
			for (AllgemeineVar av : AllgemeineVar.values())
			{
				namen.add(av.getName());
			}
			for (LastschriftVar lsv : LastschriftVar.values())
			{
				namen.add(lsv.getName());
			}
		}
		if (formular.getArt() == FormularArt.RECHNUNG
				|| formular.getArt() == FormularArt.MAHNUNG)
		{
			for (AllgemeineVar av : AllgemeineVar.values())
			{
				namen.add(av.getName());
			}
			for (MitgliedVar mv : MitgliedVar.values())
			{
				namen.add(mv.getName());
			}
			for (MitgliedskontoVar mkv : MitgliedskontoVar.values())
			{
				namen.add(mkv.getName());
			}

		}
		if (formular.getArt() == FormularArt.FREIESFORMULAR
				|| formular.getArt() == FormularArt.RECHNUNG
				|| formular.getArt() == FormularArt.MAHNUNG)
		{
			DBIterator<Lesefeld> itlesefelder = Einstellungen.getDBService()
					.createList(Lesefeld.class);
			while (itlesefelder.hasNext())
			{
				Lesefeld lesefeld = itlesefelder.next();
				namen.add(Einstellungen.LESEFELD_PRE + lesefeld.getBezeichnung());
			}

			DBIterator<Felddefinition> zusatzfelder = Einstellungen.getDBService()
					.createList(Felddefinition.class);
			while (zusatzfelder.hasNext())
			{
				Felddefinition zusatzfeld = zusatzfelder.next();
				namen.add(Einstellungen.ZUSATZFELD_PRE + zusatzfeld.getName());
			}
		}
		name = new SelectInput(namen, getFormularfeld().getName());
		return name;
	}

	public IntegerInput getSeite() throws RemoteException
	{
		if (seite != null)
		{
			return seite;
		}
		seite = new IntegerInput(getFormularfeld().getSeite());
		seite.setComment("Seite");
		return seite;
	}

	public DecimalInput getX() throws RemoteException
	{
		if (x != null)
		{
			return x;
		}
		x = new DecimalInput(getFormularfeld().getX(), Einstellungen.DECIMALFORMAT);
		x.setComment("Millimeter");
		return x;
	}

	public DecimalInput getY() throws RemoteException
	{
		if (y != null)
		{
			return y;
		}
		y = new DecimalInput(getFormularfeld().getY(), Einstellungen.DECIMALFORMAT);
		y.setComment("Millimeter");
		return y;
	}

	public SelectInput getFont() throws RemoteException
	{
		if (font != null)
		{
			return font;
		}
		ArrayList<String> fonts = new ArrayList<>();
		fonts.add("FreeSans");
		fonts.add("FreeSans-Bold");
		fonts.add("FreeSans-BoldOblique");
		fonts.add("FreeSans-Oblique");
		fonts.add(BaseFont.HELVETICA);
		fonts.add(BaseFont.HELVETICA_BOLD);
		fonts.add(BaseFont.HELVETICA_BOLDOBLIQUE);
		fonts.add(BaseFont.HELVETICA_OBLIQUE);
		fonts.add(BaseFont.TIMES_ROMAN);
		fonts.add(BaseFont.TIMES_BOLD);
		fonts.add(BaseFont.TIMES_ITALIC);
		fonts.add(BaseFont.TIMES_BOLDITALIC);
		fonts.add(BaseFont.COURIER);
		fonts.add(BaseFont.COURIER_BOLD);
		fonts.add(BaseFont.COURIER_OBLIQUE);
		fonts.add(BaseFont.COURIER_BOLDOBLIQUE);
		font = new SelectInput(fonts, getFormularfeld().getFont());
		return font;
	}

	public IntegerInput getFontsize() throws RemoteException
	{
		if (fontsize != null)
		{
			return fontsize;
		}
		fontsize = new IntegerInput(getFormularfeld().getFontsize());
		return fontsize;
	}

	/**
	 * This method stores the project using the current values.
	 */
	public void handleStore()
	{
		try
		{
			Formularfeld f = getFormularfeld();
			f.setFormular(getFormular());
			f.setName((String) getName().getValue());
			f.setSeite((Integer) getSeite().getValue());
			f.setX((Double) getX().getValue());
			f.setY((Double) getY().getValue());
			f.setFont((String) getFont().getValue());
			f.setFontsize((Integer) getFontsize().getValue());
			f.store();

			GUI.getStatusBar().setSuccessText("Formularfeld gespeichert");
		}
		catch (RemoteException e)
		{
			String fehler = "Fehler beim Speichern des Formularfeldes";
			Logger.error(fehler, e);
			GUI.getStatusBar().setErrorText(fehler);
		}
		catch (Exception e)
		{
			GUI.getStatusBar().setErrorText(e.getMessage());
		}
	}

	public Part getFormularfeldList() throws RemoteException
	{
		DBService service = Einstellungen.getDBService();
		DBIterator<
				Formularfeld> formularfelder = service.createList(Formularfeld.class);
		formularfelder.addFilter("formular = ?", new Object[]
		{
				formular.getID()
		});
		formularfelder.setOrder("ORDER BY seite, x, y");

		formularfelderList = new TablePart(formularfelder,
				new FormularfeldAction());
		formularfelderList.addColumn("Name", "name");
		formularfelderList.addColumn("Seite", "seite");
		formularfelderList.addColumn("von links", "x");
		formularfelderList.addColumn("von unten", "y");
		formularfelderList.addColumn("Font", "font");
		formularfelderList.addColumn("Fonthöhe", "fontsize");

		formularfelderList.setRememberColWidths(true);
		formularfelderList.setContextMenu(new FormularfeldMenu());
		formularfelderList.setRememberOrder(true);
		formularfelderList.setSummary(false);
		return formularfelderList;
	}

	public void refreshTable() throws RemoteException
	{
		formularfelderList.removeAll();
		DBIterator<Formularfeld> formularfelder = Einstellungen.getDBService()
				.createList(Formularfeld.class);
		formularfelder.addFilter("formular = ?", new Object[]
		{
				formular.getID()
		});
		formularfelder.setOrder("ORDER BY x, y");
		while (formularfelder.hasNext())
		{
			formularfelderList.addItem(formularfelder.next());
		}
	}

	private String getFormularArtName() throws RemoteException
	{
		return formular.getArt().getText();
	}

}
