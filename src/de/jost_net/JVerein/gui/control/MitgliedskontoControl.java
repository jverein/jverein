/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe, Leonardo Mörlein
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.MitgliedskontoMessage;
import de.jost_net.JVerein.gui.formatter.ZahlungswegFormatter;
import de.jost_net.JVerein.gui.input.BuchungsartInput;
import de.jost_net.JVerein.gui.input.FormularInput;
import de.jost_net.JVerein.gui.menu.MitgliedskontoMenu;
import de.jost_net.JVerein.io.Kontoauszug;
import de.jost_net.JVerein.io.Mahnungsausgabe;
import de.jost_net.JVerein.io.Rechnungsausgabe;
import de.jost_net.JVerein.keys.Ausgabeart;
import de.jost_net.JVerein.keys.Ausgabesortierung;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.TreeFormatter;
import de.willuhn.jameica.gui.input.AbstractInput;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoControl extends AbstractControl
{
	public enum DIFFERENZ
	{
		EGAL("egal"), FEHLBETRAG("Fehlbetrag"), UEBERZAHLUNG("Überzahlung");

		private final String titel;

		private DIFFERENZ(String titel)
		{
			this.titel = titel;
		}

		@Override
		public String toString()
		{
			return titel;
		}

		public static DIFFERENZ fromString(final String text)
		{
			for (DIFFERENZ item : DIFFERENZ.values())
			{
				if (item.titel.equals(text))
					return item;
			}
			return null;
		}
	}

	private Settings settings;

	private DateInput datum = null;

	private TextAreaInput zweck1;

	private SelectInput zahlungsweg;

	private DecimalInput betrag;

	private AbstractInput buchungsart;

	private FormularInput formular = null;

	private Mitgliedskonto mkto;

	private TablePart mitgliedskontoList;

	private TablePart mitgliedskontoList2;

	private TreePart mitgliedskontoTree;

	public static final String DATUM_MITGLIEDSKONTO = "datum.mitgliedskonto.";

	//
	// public static final String DATUM_RECHNUNG = "datum.rechnung.";
	//
	// public static final String DATUM_MAHNUNG = "datum.mahnung.";

	public enum TYP
	{
		RECHNUNG, MAHNUNG
	}

	private String datumverwendung = null;

	private DateInput vondatum = null;

	private DateInput bisdatum = null;

	private CheckboxInput ohneabbucher = null;

	private SelectInput ausgabeart = null;

	private SelectInput ausgabesortierung = null;

	private TextInput suchname = null;

	private TextInput suchname2 = null;

	private SelectInput differenz = null;

	private CheckboxInput spezialsuche = null;

	private TextInput betreff = null;

	private TextAreaInput txt = null;

	// private CheckboxInput offenePosten = null;

	private MitgliedskontoMessageConsumer mc = null;

	private Action action;

	public MitgliedskontoControl(AbstractView view)
	{
		super(view);
		settings = new de.willuhn.jameica.system.Settings(this.getClass());
		settings.setStoreWhenRead(true);
	}

	public Mitgliedskonto getMitgliedskonto()
	{
		if (mkto != null)
		{
			return mkto;
		}
		mkto = (Mitgliedskonto) getCurrentObject();
		return mkto;
	}

	public Settings getSettings()
	{
		return settings;
	}

	public DateInput getDatum() throws RemoteException
	{
		if (datum != null)
		{
			return datum;
		}

		Date d = new Date();
		if (getMitgliedskonto() != null)
		{
			d = getMitgliedskonto().getDatum();
		}

		this.datum = new DateInput(d, new JVDateFormatTTMMJJJJ());
		this.datum.setTitle("Datum");
		this.datum.setText("Bitte Datum wählen");
		this.datum.addListener(new Listener()
		{

			@Override
			public void handleEvent(Event event)
			{
				Date date = (Date) datum.getValue();
				if (date == null)
				{
					return;
				}
			}
		});
		this.datum.setMandatory(true);
		return datum;
	}

	public TextAreaInput getZweck1() throws RemoteException
	{
		if (zweck1 != null)
		{
			return zweck1;
		}
		String z = "";
		if (getMitgliedskonto() != null)
		{
			z = getMitgliedskonto().getZweck1();
		}
		zweck1 = new TextAreaInput(z, 500);
		zweck1.setHeight(50);
		zweck1.setMandatory(true);
		return zweck1;
	}

	public SelectInput getZahlungsweg() throws RemoteException
	{
		if (zahlungsweg != null)
		{
			return zahlungsweg;
		}
		Integer z = null;
		if (getMitgliedskonto() != null)
		{
			z = getMitgliedskonto().getZahlungsweg();
		}
		zahlungsweg = new SelectInput(Zahlungsweg.getArray(),
				z == null
						? new Zahlungsweg(Einstellungen.getEinstellung().getZahlungsweg())
						: new Zahlungsweg(getMitgliedskonto().getZahlungsweg()));
		zahlungsweg.setName("Zahlungsweg");
		return zahlungsweg;
	}

	public DecimalInput getBetrag() throws RemoteException
	{
		if (betrag != null)
		{
			return betrag;
		}
		Double b = Double.valueOf(0);
		if (getMitgliedskonto() != null)
		{
			b = getMitgliedskonto().getBetrag();
		}
		betrag = new DecimalInput(b, Einstellungen.DECIMALFORMAT);
		return betrag;
	}

	public Input getBuchungsart() throws RemoteException
	{
		if (buchungsart != null && !buchungsart.getControl().isDisposed())
		{
			return buchungsart;
		}
		buchungsart = new BuchungsartInput().getBuchungsartInput(buchungsart,
				getMitgliedskonto().getBuchungsart());
		return buchungsart;
	}

	public FormularInput getFormular(FormularArt mahnung) throws RemoteException
	{
		if (formular != null)
		{
			return formular;
		}
		formular = new FormularInput(mahnung);
		return formular;
	}

	public String getDatumverwendung()
	{
		return this.datumverwendung;
	}

	public DateInput getVondatum(String datumverwendung)
	{
		if (vondatum != null)
		{
			return vondatum;
		}
		Date d = null;
		this.datumverwendung = datumverwendung;

		String tmp = settings.getString(datumverwendung + "datumvon", null);
		if (tmp != null)
		{
			try
			{
				d = new JVDateFormatTTMMJJJJ().parse(tmp);
			}
			catch (ParseException e)
			{
				//
			}
		}

		this.vondatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
		this.vondatum.setTitle("Anfangsdatum");
		this.vondatum.setText("Bitte Anfangsdatum wählen");
		vondatum.addListener(new FilterListener());
		return vondatum;
	}

	public DateInput getBisdatum(String datumverwendung)
	{
		if (bisdatum != null)
		{
			return bisdatum;
		}
		this.datumverwendung = datumverwendung;
		Date d = null;
		String tmp = settings.getString(datumverwendung + "datumbis", null);
		if (tmp != null)
		{
			try
			{
				d = new JVDateFormatTTMMJJJJ().parse(tmp);
			}
			catch (ParseException e)
			{
				//
			}
		}
		this.bisdatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
		this.bisdatum.setTitle("Endedatum");
		this.bisdatum.setText("Bitte Endedatum wählen");
		bisdatum.addListener(new FilterListener());
		return bisdatum;
	}

	public Object[] getCVSExportGrenzen(Mitglied selectedMitglied)
	{
		return new Object[]
		{
				getVondatum(MitgliedskontoControl.DATUM_MITGLIEDSKONTO).getValue(),
				getBisdatum(MitgliedskontoControl.DATUM_MITGLIEDSKONTO).getValue(),
				getDifferenz().getValue(), getCVSExportGrenzeOhneAbbucher(),
				selectedMitglied
		};
	}

	private Boolean getCVSExportGrenzeOhneAbbucher()
	{
		if (null == ohneabbucher)
			return Boolean.FALSE;
		return (Boolean) ohneabbucher.getValue();
	}

	public CheckboxInput getOhneAbbucher()
	{
		if (ohneabbucher != null)
		{
			return ohneabbucher;
		}
		ohneabbucher = new CheckboxInput(false);
		return ohneabbucher;
	}

	public CheckboxInput getSpezialSuche()
	{
		if (spezialsuche != null && !spezialsuche.getControl().isDisposed())
		{
			return spezialsuche;
		}
		spezialsuche = new CheckboxInput(false);
		spezialsuche.setName("Spezial-Suche");
		spezialsuche.addListener(new Listener()
		{

			@Override
			public void handleEvent(Event event)
			{
				try
				{
					refreshMitgliedkonto2();
				}
				catch (RemoteException e)
				{
					Logger.error("Fehler", e);
				}
			}
		});

		return spezialsuche;
	}

	public SelectInput getDifferenz()
	{
		if (differenz != null)
		{
			return differenz;
		}
		DIFFERENZ defaultwert = DIFFERENZ
				.fromString(settings.getString("differenz", DIFFERENZ.EGAL.toString()));
		return getDifferenz(defaultwert);
	}

	public SelectInput getDifferenz(DIFFERENZ defaultvalue)
	{
		differenz = new SelectInput(DIFFERENZ.values(), defaultvalue);
		differenz.setName("Differenz");
		differenz.addListener(new FilterListener());
		return differenz;
	}

	public TextInput getSuchName()
	{
		if (suchname != null && !suchname.getControl().isDisposed())
		{
			return suchname;
		}
		suchname = new TextInput("", 30);
		suchname.setName("Name");
		suchname.addListener(new FilterListener());
		return suchname;
	}

	public TextInput getSuchName2(boolean newcontrol)
	{
		if (!newcontrol && suchname2 != null)
		{
			return suchname2;
		}
		suchname2 = new TextInput("", 30);
		suchname2.setName("Name");
		suchname2.addListener(new FilterListener());
		return suchname2;
	}

	public SelectInput getAusgabeart()
	{
		if (ausgabeart != null)
		{
			return ausgabeart;
		}
		ausgabeart = new SelectInput(Ausgabeart.values(),
				Ausgabeart.valueOf(settings.getString("ausgabeart", "DRUCK")));
		ausgabeart.setName("Ausgabe");
		return ausgabeart;
	}

	public SelectInput getAusgabesortierung()
	{
		if (ausgabesortierung != null)
		{
			return ausgabesortierung;
		}
		ausgabesortierung = new SelectInput(Ausgabesortierung.values(),
				Ausgabesortierung.getByKey(settings.getInt("ausgabesortierung", 1)));
		ausgabesortierung.setName("Sortierung");
		return ausgabesortierung;
	}

	public TextInput getBetreff(String verwendung)
	{
		if (betreff != null)
		{
			return betreff;
		}
		betreff = new TextInput(
				settings.getString(verwendung + ".mail.betreff", ""), 100);
		betreff.setName("Betreff");
		return betreff;
	}

	public TextAreaInput getTxt(String verwendung)
	{
		if (txt != null)
		{
			return txt;
		}
		txt = new TextAreaInput(settings.getString(verwendung + ".mail.text", ""),
				10000);
		txt.setName("Text");
		return txt;
	}

	public void handleStore()
	{
		try
		{
			Mitgliedskonto mkto = getMitgliedskonto();
			mkto.setBetrag((Double) getBetrag().getValue());
			mkto.setDatum((Date) getDatum().getValue());
			Zahlungsweg zw = (Zahlungsweg) getZahlungsweg().getValue();
			mkto.setZahlungsweg(zw.getKey());
			mkto.setZweck1((String) getZweck1().getValue());
			if (getBuchungsart().getValue() != null)
			{
				mkto.setBuchungsart((Buchungsart) getBuchungsart().getValue());
			}
			mkto.store();
			GUI.getStatusBar().setSuccessText("Mitgliedskonto gespeichert");
		}
		catch (ApplicationException e)
		{
			GUI.getStatusBar().setErrorText(e.getMessage());
		}
		catch (RemoteException e)
		{
			String fehler = "Fehler beim speichern";
			Logger.error(fehler, e);
			GUI.getStatusBar().setErrorText(fehler);
		}
	}

	public Part getMitgliedskontoTree(Mitglied mitglied) throws RemoteException
	{
		mitgliedskontoTree = new TreePart(new MitgliedskontoNode(mitglied),
				(Action) null)
		{

			@SuppressWarnings("unchecked")
			@Override
			public void paint(Composite composite) throws RemoteException
			{
				super.paint(composite);
				List<MitgliedskontoNode> items = mitgliedskontoTree.getItems();
				for (MitgliedskontoNode mkn : items)
				{
					GenericIterator<?> items2 = mkn.getChildren();
					while (items2.hasNext())
					{
						MitgliedskontoNode mkn2 = (MitgliedskontoNode) items2.next();
						mitgliedskontoTree.setExpanded(mkn2, false);
					}
				}
			}
		};
		mitgliedskontoTree.addColumn("Name, Vorname", "name");
		mitgliedskontoTree.addColumn("Datum", "datum",
				new DateFormatter(new JVDateFormatTTMMJJJJ()));
		mitgliedskontoTree.addColumn("Zweck1", "zweck1");
		mitgliedskontoTree.addColumn("Zahlungsweg", "zahlungsweg",
				new ZahlungswegFormatter());
		mitgliedskontoTree.addColumn("Soll", "soll",
				new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
		mitgliedskontoTree.addColumn("Ist", "ist",
				new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
		mitgliedskontoTree.addColumn("Differenz", "differenz",
				new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
		mitgliedskontoTree.setContextMenu(new MitgliedskontoMenu());
		mitgliedskontoTree.setRememberColWidths(true);
		mitgliedskontoTree.setRememberOrder(true);
		mitgliedskontoTree.setFormatter(new MitgliedskontoTreeFormatter());
		this.mc = new MitgliedskontoMessageConsumer();
		Application.getMessagingFactory().registerMessageConsumer(this.mc);

		return mitgliedskontoTree;
	}

	public TablePart getMitgliedskontoList(Action action, ContextMenu menu)
			throws RemoteException
	{
		this.action = action;
		GenericIterator<?> mitgliedskonten = getMitgliedskontoIterator();
		settings.setAttribute("differenz", getDifferenz().getValue().toString());
		if (mitgliedskontoList == null)
		{
			mitgliedskontoList = new TablePart(mitgliedskonten, action);
			mitgliedskontoList.addColumn("Datum", "datum",
					new DateFormatter(new JVDateFormatTTMMJJJJ()));
			mitgliedskontoList.addColumn("Abrechnungslauf", "abrechnungslauf");
			mitgliedskontoList.addColumn("Name", "mitglied");
			mitgliedskontoList.addColumn("Zweck", "zweck1");
			mitgliedskontoList.addColumn("Betrag", "betrag",
					new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
			mitgliedskontoList.addColumn("Zahlungseingang", "istsumme",
					new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
			mitgliedskontoList.setContextMenu(menu);
			mitgliedskontoList.setRememberColWidths(true);
			mitgliedskontoList.setRememberOrder(true);
			mitgliedskontoList.setMulti(true);
			mitgliedskontoList.setSummary(true);
		}
		else
		{
			mitgliedskontoList.removeAll();
			while (mitgliedskonten.hasNext())
			{
				mitgliedskontoList.addItem(mitgliedskonten.next());
			}
		}
		return mitgliedskontoList;
	}

	public TablePart getMitgliedskontoList2(Action action, ContextMenu menu)
			throws RemoteException
	{
		this.action = action;
		GenericIterator<Mitglied> mitglieder = getMitgliedIterator();
		if (mitgliedskontoList2 == null)
		{
			mitgliedskontoList2 = new TablePart(mitglieder, action);
			mitgliedskontoList2.addColumn("Name", "name");
			mitgliedskontoList2.addColumn("Vorname", "vorname");
			mitgliedskontoList2.setContextMenu(menu);
			mitgliedskontoList2.setRememberColWidths(true);
			mitgliedskontoList2.setRememberOrder(true);
			mitgliedskontoList2.setMulti(true);
			mitgliedskontoList2.setSummary(true);
		}
		else
		{
			mitgliedskontoList2.removeAll();
			while (mitglieder.hasNext())
			{
				mitgliedskontoList2.addItem(mitglieder.next());
			}
		}
		return mitgliedskontoList2;
	}

	private void refreshMitgliedkonto2() throws RemoteException
	{
		GenericIterator<Mitglied> mitglieder = getMitgliedIterator();
		mitgliedskontoList2.removeAll();
		while (mitglieder.hasNext())
		{
			mitgliedskontoList2.addItem(mitglieder.next());
		}
	}

	private GenericIterator<Mitglied> getMitgliedIterator() throws RemoteException
	{
		DBIterator<Mitglied> mitglieder = Einstellungen.getDBService()
				.createList(Mitglied.class);
		// MitgliedUtils.setMitgliedOderSpender(mitglieder);
		if (suchname2 != null && suchname2.getValue() != null)
		{
			StringBuffer where = new StringBuffer();
			ArrayList<String> object = new ArrayList<>();
			StringTokenizer tok = new StringTokenizer((String) suchname2.getValue(),
					" ,-");
			where.append("(");
			boolean first = true;
			while (tok.hasMoreElements())
			{
				if (!first)
				{
					where.append("or ");
				}
				first = false;
				where.append(
						"upper(name) like upper(?) or upper(vorname) like upper(?) ");
				String o = tok.nextToken();
				if ((Boolean) getSpezialSuche().getValue())
				{
					o = "%" + o + "%";
				}
				object.add(o);
				object.add(o);
			}
			where.append(")");
			if (where.length() > 2)
			{
				mitglieder.addFilter(where.toString(), object.toArray());
			}
		}
		mitglieder.setOrder("order by name, vorname");
		return mitglieder;
	}

	public GenericIterator<?> getMitgliedskontoIterator() throws RemoteException
	{
		DBService service = Einstellungen.getDBService();
		Date d1 = null;
		java.sql.Date vd = null;
		java.sql.Date bd = null;
		if (vondatum != null)
		{
			d1 = (Date) vondatum.getValue();
			if (d1 != null)
			{
				settings.setAttribute(datumverwendung + "datumvon",
						new JVDateFormatTTMMJJJJ().format(d1));
				vd = new java.sql.Date(d1.getTime());
			}
			else
			{
				settings.setAttribute(datumverwendung + "datumvon", "");
			}
		}
		if (bisdatum != null)
		{
			d1 = (Date) bisdatum.getValue();
			if (d1 != null)
			{
				settings.setAttribute(datumverwendung + "datumbis",
						new JVDateFormatTTMMJJJJ().format(d1));
				bd = new java.sql.Date(d1.getTime());
			}
			else
			{
				settings.setAttribute(datumverwendung + "datumbis", "");
			}
		}
		String sql = "select  mitgliedskonto.*, mitglied.name, mitglied.vorname from mitgliedskonto "
				+ "join mitglied on (mitgliedskonto.mitglied = mitglied.id) ";
		String where = "";
		ArrayList<Object> param = new ArrayList<>();
		if (vd != null)
		{
			where += (where.length() > 0 ? "and " : "")
					+ "mitgliedskonto.datum >= ? ";
			param.add(vd);
		}
		if (bd != null)
		{
			where += (where.length() > 0 ? "and " : "")
					+ "mitgliedskonto.datum <= ? ";
			param.add(bd);
		}
		if (where.length() > 0)
		{
			sql += "WHERE " + where;
		}
		sql += "order by mitglied.name, mitglied.vorname, mitgliedskonto.datum desc";
		PseudoIterator mitgliedskonten = (PseudoIterator) service.execute(sql,
				param.toArray(), new ResultSetExtractor()
				{

					@Override
					public Object extract(ResultSet rs)
							throws RemoteException, SQLException
					{
						ArrayList<Mitgliedskonto> ergebnis = new ArrayList<>();

						// In case the text search input is used, we calculate
						// an "equality" score for each Mitgliedskonto (aka
						// Mitgliedskontobuchung) entry. Only the entries with
						// score == maxScore will be shown.
						Integer maxScore = 0;
						while (rs.next())
						{
							Mitgliedskonto mk = (Mitgliedskonto) Einstellungen.getDBService()
									.createObject(Mitgliedskonto.class, rs.getString(1));

							DIFFERENZ diff = DIFFERENZ.EGAL;
							if (differenz != null)
							{
								diff = (DIFFERENZ) differenz.getValue();
							}
							BigDecimal ist = new BigDecimal(mk.getIstSumme());
							ist = ist.setScale(2, RoundingMode.HALF_UP);
							BigDecimal soll = new BigDecimal(mk.getBetrag());
							soll = soll.setScale(2, RoundingMode.HALF_UP);
							if (DIFFERENZ.FEHLBETRAG == diff && ist.compareTo(soll) >= 0)
							{
								continue;
							}
							if (DIFFERENZ.UEBERZAHLUNG == diff && ist.compareTo(soll) <= 0)
							{
								continue;
							}

							if (suchname != null && suchname.getValue() != null)
							{
								StringTokenizer tok = new StringTokenizer(
										(String) suchname.getValue(), " ,-");
								Integer score = 0;
								while (tok.hasMoreElements())
								{
									String nextToken = tok.nextToken();
									if (nextToken.length() > 3)
									{
										score += scoreWord(nextToken, mk.getMitglied().getName());
										score += scoreWord(nextToken,
												mk.getMitglied().getVorname());
										score += scoreWord(nextToken, mk.getZweck1());
									}
								}

								if (maxScore < score)
								{
									maxScore = score;
									// We found a Mitgliedskonto matching with a higher equality
									// score, so we drop all previous matches, because they were
									// less equal.
									ergebnis.clear();
								}
								else if (maxScore > score)
								{
									// This match is worse, so skip it.
									continue;
								}
							}

							ergebnis.add(mk);
						}
						return PseudoIterator.fromArray(
								ergebnis.toArray(new GenericObject[ergebnis.size()]));
					}
				});

		return mitgliedskonten;
	}

	public Integer scoreWord(String word, String in)
	{
		word = reduceWord(word);

		Integer wordScore = 0;
		StringTokenizer tok = new StringTokenizer(in, " ,-");

		while (tok.hasMoreElements())
		{
			String nextToken = tok.nextToken();
			nextToken = reduceWord(nextToken);

			// Full match is twice worth
			if (nextToken.equals(word))
			{
				wordScore += 2;
			}
			else if (nextToken.contains(word))
			{
				wordScore += 1;
			}
		}

		return wordScore;
	}

	public String reduceWord(String word)
	{
		// We replace "ue" -> "u" and "ü" -> "u", because some bank institutions
		// remove the dots "ü" -> "u". So we get "u" == "ü" == "ue".
		return word.toLowerCase().replaceAll("ä", "a").replaceAll("ae", "a")
				.replaceAll("ö", "o").replaceAll("oe", "o").replaceAll("ü", "u")
				.replaceAll("ue", "u").replaceAll("ß", "s").replaceAll("ss", "s");
	}

	public Button getStartRechnungButton(final Object currentObject)
	{
		Button button = new Button("starten", new Action()
		{

			@Override
			public void handleAction(Object context)
			{
				try
				{
					generiereRechnung(currentObject);
				}
				catch (RemoteException e)
				{
					Logger.error("", e);
					GUI.getStatusBar().setErrorText(e.getMessage());
				}
				catch (IOException e)
				{
					Logger.error("", e);
					GUI.getStatusBar().setErrorText(e.getMessage());
				}
			}
		}, null, true, "walking.png");
		return button;
	}

	public Button getStartKontoauszugButton(final Object currentObject,
			final Date von, final Date bis)
	{
		Button button = new Button("starten", new Action()
		{

			@Override
			public void handleAction(Object context)
			{
				try
				{
					new Kontoauszug(currentObject, von, bis);
				}
				catch (Exception e)
				{
					Logger.error("", e);
					GUI.getStatusBar().setErrorText(e.getMessage());
				}
			}
		}, null, true, "walking.png");
		return button;
	}

	private void generiereRechnung(Object currentObject) throws IOException
	{
		Ausgabeart aa = (Ausgabeart) getAusgabeart().getValue();
		settings.setAttribute("ausgabeart", aa.toString());
		Ausgabesortierung as = (Ausgabesortierung) getAusgabesortierung()
				.getValue();
		settings.setAttribute("ausgabesortierung", as.getKey());
		settings.setAttribute(TYP.RECHNUNG.name() + ".mail.betreff",
				(String) getBetreff(TYP.RECHNUNG.name()).getValue());
		settings.setAttribute(TYP.RECHNUNG.name() + ".mail.text",
				(String) getTxt(TYP.RECHNUNG.name()).getValue());
		new Rechnungsausgabe(this);
	}

	public Button getStartMahnungButton(final Object currentObject)
	{
		Button button = new Button("starten", new Action()
		{

			@Override
			public void handleAction(Object context)
			{
				try
				{
					generiereMahnung(currentObject);
				}
				catch (RemoteException e)
				{
					Logger.error("", e);
					GUI.getStatusBar().setErrorText(e.getMessage());
				}
				catch (IOException e)
				{
					Logger.error("", e);
					GUI.getStatusBar().setErrorText(e.getMessage());
				}
			}
		}, null, true, "walking.png");
		return button;
	}

	private void generiereMahnung(Object currentObject) throws IOException
	{
		Ausgabeart aa = (Ausgabeart) getAusgabeart().getValue();
		settings.setAttribute("ausgabeart", aa.toString());
		settings.setAttribute(TYP.MAHNUNG.name() + ".mail.betreff",
				(String) getBetreff(TYP.MAHNUNG.name()).getValue());
		settings.setAttribute(TYP.MAHNUNG.name() + ".mail.text",
				(String) getTxt(TYP.MAHNUNG.name()).getValue());
		new Mahnungsausgabe(this);
	}

	private class FilterListener implements Listener
	{

		@Override
		public void handleEvent(Event event)
		{
			if (event.type == SWT.Selection || event.type != SWT.FocusOut)
			{
				try
				{
					getMitgliedskontoList(action, null);
				}
				catch (RemoteException e)
				{
					Logger.error("Fehler", e);
				}
			}
		}
	}

	public static class MitgliedskontoTreeFormatter implements TreeFormatter
	{

		@Override
		public void format(TreeItem item)
		{
			MitgliedskontoNode mkn = (MitgliedskontoNode) item.getData();
			switch (mkn.getType())
			{
				case MitgliedskontoNode.MITGLIED:
					item.setImage(0, SWTUtil.getImage("user.png"));
					break;
				case MitgliedskontoNode.SOLL:
					item.setImage(0, SWTUtil.getImage("calculator.png"));
					item.setExpanded(false);
					break;
				case MitgliedskontoNode.IST:
					item.setImage(0, SWTUtil.getImage("object-group.png"));
					break;
			}
		}
	}

	/**
	 * Wird benachrichtigt um die Anzeige zu aktualisieren.
	 */
	private class MitgliedskontoMessageConsumer implements MessageConsumer
	{

		/**
		 * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
		 */
		@Override
		public boolean autoRegister()
		{
			return false;
		}

		/**
		 * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
		 */
		@Override
		public Class<?>[] getExpectedMessageTypes()
		{
			return new Class[]
			{
					MitgliedskontoMessage.class
			};
		}

		/**
		 * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
		 */
		@Override
		public void handleMessage(final Message message) throws Exception
		{
			GUI.getDisplay().syncExec(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						if (mitgliedskontoTree == null)
						{
							// Eingabe-Feld existiert nicht. Also abmelden
							Application.getMessagingFactory().unRegisterMessageConsumer(
									MitgliedskontoMessageConsumer.this);
							return;
						}

						MitgliedskontoMessage msg = (MitgliedskontoMessage) message;
						Mitglied mitglied = (Mitglied) msg.getObject();
						mitgliedskontoTree.setRootObject(new MitgliedskontoNode(mitglied));
					}
					catch (Exception e)
					{
						// Wenn hier ein Fehler auftrat, deregistrieren wir uns wieder
						Logger.error("unable to refresh saldo", e);
						Application.getMessagingFactory()
								.unRegisterMessageConsumer(MitgliedskontoMessageConsumer.this);
					}
				}

			});
		}
	}

}
