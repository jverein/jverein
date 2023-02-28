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
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.QIFImportHeaderMessage;
import de.jost_net.JVerein.gui.input.JVereinKontoInput;
import de.jost_net.JVerein.gui.input.QIFExternKontenInput;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.QIFImportHead;
import de.jost_net.JVerein.rmi.QIFImportPos;
import de.jost_net.JVerein.server.BuchungImpl;
import de.jost_net.JVerein.server.QIFImportPosImpl;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.db.DBServiceImpl;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.formatter.TableFormatter;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Font;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/***
 * Dieses Control verwaltet die Komponenten und Aktionen des Hauptfenster beim
 * Importieren von QIF Dateien in die JVerein.buchungen.
 * 
 * @author Rolf Mamat
 * 
 */
public class QIFBuchungsImportControl extends AbstractControl
{
	private de.willuhn.jameica.system.Settings settings;

	private QIFExternKontenInput auswahlExternesKonto;

	private JVereinKontoInput auswahlJVereinKonto;

	private TextInput beschreibungKonto;

	private TextInput eroffnungsDatum;

	private DecimalInput startSaldo;

	private DecimalInput endSaldo;

	private IntegerInput anzahlBuchungen;

	private TextInput importDatum;

	private TextInput importDatei;

	private TextInput processDatum;

	private TablePart qifImportPosList;

	private QIFImportHeaderMessageConsumer headerMessageConsumer;

	private QIFImportHead headerSelected;

	private Image imageError;

	private Image imageLocked;

	public QIFBuchungsImportControl(AbstractView view)
	{
		super(view);
		init();
	}

	private void init()
	{
		settings = new de.willuhn.jameica.system.Settings(this.getClass());
		settings.setStoreWhenRead(true);
		headerSelected = null;
		imageError = SWTUtil.getImage("stop-circle.png");
		imageLocked = SWTUtil.getImage("locked.png");
	}

	public Input getAuswahlJVereinKonto() throws RemoteException
	{
		if (null == auswahlJVereinKonto)
		{
			auswahlJVereinKonto = new JVereinKontoInput();
			auswahlJVereinKonto.disable();
			auswahlJVereinKonto.addListener(new JVereinKontoAuswahlListener());
		}
		return auswahlJVereinKonto;
	}

	public QIFExternKontenInput getAuswahlExternesKonto() throws RemoteException
	{
		if (null == auswahlExternesKonto)
		{
			auswahlExternesKonto = new QIFExternKontenInput();
			auswahlExternesKonto.addListener(new QIFImportHeaderAuswahlListener());
			this.headerMessageConsumer = new QIFImportHeaderMessageConsumer();
			headerMessageConsumer.registerMe();
		}
		return auswahlExternesKonto;
	}

	public TextInput getInputBeschreibungKonto()
	{
		if (null == beschreibungKonto)
		{
			beschreibungKonto = new TextInput("", 30);
			beschreibungKonto.disable();
		}
		return beschreibungKonto;
	}

	public TextInput getInputEroeffnungsDatum()
	{
		if (null == eroffnungsDatum)
		{
			eroffnungsDatum = new DateAnzeigeInput(" - ", "wurde Konto eröffnet");
		}
		return eroffnungsDatum;
	}

	public DecimalInput getInputStartSaldo()
	{
		if (null == startSaldo)
		{
			startSaldo = new DecimalInput(Einstellungen.DECIMALFORMAT);
			startSaldo.setComment("Euro");
			startSaldo.disable();
		}
		return startSaldo;
	}

	public DecimalInput getInputEndSaldo()
	{
		if (null == endSaldo)
		{
			endSaldo = new DecimalInput(Einstellungen.DECIMALFORMAT);
			endSaldo.setComment("Euro");
			endSaldo.disable();
		}
		return endSaldo;
	}

	public IntegerInput getInputAnzahlBuchungen()
	{
		if (null == anzahlBuchungen)
		{
			anzahlBuchungen = new IntegerInput();
			anzahlBuchungen.disable();
		}
		return anzahlBuchungen;
	}

	public TextInput getInputImportDatum()
	{
		if (null == importDatum)
		{
			importDatum = new DateAnzeigeInput(" - ", "wurde Datei eingelesen.");
		}
		return importDatum;
	}

	public TextInput getInputImportDatei()
	{
		if (null == importDatei)
		{
			importDatei = new TextInput("", 80);
			importDatei.disable();
		}
		return importDatei;
	}

	public TextInput getInputProcessDatum()
	{
		if (null == processDatum)
		{
			processDatum = new DateAnzeigeInput("Noch nicht übernommen..",
					"Buchungen wurden übernommen.");
		}
		return processDatum;
	}

	class DateAnzeigeInput extends TextInput
	{
		private DateFormatter dateFormater = new DateFormatter(
				new JVDateFormatTTMMJJJJ());

		private String commentLeer;

		private String commentVoll;

		public DateAnzeigeInput(String commentLeer, String commentVoll)
		{
			super("", 10);
			super.disable();
			super.setComment(commentLeer);
			this.commentLeer = commentLeer;
			this.commentVoll = commentVoll;
		}

		@Override
		public void setValue(Object value)
		{
			super.setValue(dateFormater.format(value));
			if (null != value)
				this.setComment(commentVoll);
			else
				this.setComment(commentLeer);
		}

	}

	public Part getImportKontoPosList(Action action) throws RemoteException
	{
		qifImportPosList = new TablePart(getIterator(), action);
		qifImportPosList.addColumn("ID", QIFImportPos.COL_POSID, null, false,
				Column.ALIGN_RIGHT);
		qifImportPosList.addColumn("Datum", QIFImportPos.COL_DATUM,
				new DateFormatter(new JVDateFormatTTMMJJJJ()));
		qifImportPosList.addColumn("Betrag", QIFImportPos.COL_BETRAG,
				new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
				Column.ALIGN_RIGHT);
		qifImportPosList.addColumn("Stand", QIFImportPos.VIEW_SALDO,
				new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
				Column.ALIGN_RIGHT);
		qifImportPosList.addColumn("Buchungsart", QIFImportPos.COL_QIF_BUCHART);
		qifImportPosList.addColumn("JV Buchart",
				QIFImportPos.VIEW_BUCHART_BEZEICHNER);
		qifImportPosList.addColumn("Beleg", QIFImportPos.COL_BELEG);
		qifImportPosList.addColumn("Name", QIFImportPos.COL_NAME);
		qifImportPosList.addColumn("Zweck", QIFImportPos.COL_ZWECK);
		qifImportPosList.addColumn("Gesperrt", QIFImportPos.COL_SPERRE,
				new PosStatusFormater());
		qifImportPosList.addColumn("Mitglied", QIFImportPos.VIEW_MITGLIEDS_NAME);

		qifImportPosList.setRememberColWidths(true);
		qifImportPosList.setRememberOrder(true);
		qifImportPosList.setSummary(true);
		qifImportPosList.setFormatter(new QIFImportPosListTableFormater());
		return qifImportPosList;

	}

	private void aktuallisierePosListe(QIFImportHead head) throws RemoteException
	{
		DBService service = Einstellungen.getDBService();
		DBIterator<
				QIFImportPos> kontoPosIterator = service.createList(QIFImportPos.class);
		kontoPosIterator.addFilter(QIFImportPos.COL_HEADID + " = " + head.getID());
		kontoPosIterator.setOrder(
				"ORDER BY " + QIFImportPos.COL_DATUM + "," + QIFImportPos.COL_POSID);

		qifImportPosList.removeAll();
		double saldo = head.getStartSaldo();
		int anzahl = 0;
		while (kontoPosIterator.hasNext())
		{
			QIFImportPosImpl importPos = (QIFImportPosImpl) kontoPosIterator.next();
			++anzahl;
			saldo += importPos.getBetrag().doubleValue();
			importPos.setSaldo(saldo);
			qifImportPosList.addItem(importPos);
		}

		anzahlBuchungen.setValue(new Integer(anzahl));
		endSaldo.setValue(Double.valueOf(saldo));
	}

	private GenericIterator<?> getIterator() throws RemoteException
	{
		ArrayList<QIFImportPos> zeile = new ArrayList<>();

		GenericIterator<?> gi = PseudoIterator
				.fromArray(zeile.toArray(new GenericObject[zeile.size()]));
		return gi;
	}

	private void headerDataFelderZeigen(QIFImportHead header)
			throws RemoteException
	{
		if (isSelectedHeaderDifferent(header))
		{
			headerSelected = header;
			beschreibungKonto.setValue(header.getBeschreibung());
			eroffnungsDatum.setValue(header.getStartDate());
			startSaldo.setValue(header.getStartSaldo());
			importDatum.setValue(header.getImportDatum());
			importDatei.setValue(header.getImportFile());
			processDatum.setValue(header.getProcessDate());

			auswahlJVereinKonto.enable();
			auswahlJVereinKonto.setValue(header.getKonto());

			aktuallisierePosListe(header);
		}
	}

	private boolean isSelectedHeaderDifferent(QIFImportHead header)
			throws RemoteException
	{
		if (headerSelected == null && header == null)
			return false;
		if (headerSelected == null || header == null)
			return true;
		if (headerSelected.getID().equals(header.getID()))
			return false;
		return true;
	}

	private void headerDataFelderLeeren()
	{
		headerSelected = null;
		beschreibungKonto.setValue(null);
		eroffnungsDatum.setValue(null);
		startSaldo.setValue(null);
		endSaldo.setValue(null);
		anzahlBuchungen.setValue(null);
		importDatum.setValue(null);
		importDatei.setValue(null);
		processDatum.setValue(null);
		auswahlJVereinKonto.setValue(null);
		auswahlJVereinKonto.disable();

		qifImportPosList.removeAll();

	}

	private void selectionChanged_JVereinsKonto() throws ApplicationException
	{
		try
		{
			Konto kontoSelected = (Konto) auswahlJVereinKonto.getValue();
			if (null == kontoSelected)
				return;
			if (null == headerSelected)
				return;

			if (kontoSelected.equals(headerSelected.getKonto()))
				return;

			headerSelected.setKonto(kontoSelected);
			headerSelected.store();
		}
		catch (RemoteException ex)
		{
			throw new ApplicationException(
					"JVerein Konto kann nicht zugeordnet werden!", ex);
		}

	}

	public class JVereinKontoAuswahlListener implements Listener
	{
		@Override
		public void handleEvent(Event event)
		{
			try
			{
				if (event.type != SWT.Selection && event.type != SWT.FocusOut)
				{
					return;
				}
				selectionChanged_JVereinsKonto();
			}
			catch (Throwable ex)
			{
				GUI.getStatusBar().setErrorText(ex.getMessage());
			}
		}
	}

	public class QIFImportHeaderAuswahlListener implements Listener
	{

		@Override
		public void handleEvent(Event event)
		{
			if (event.type != SWT.Selection && event.type != SWT.FocusOut)
			{
				return;
			}

			GUI.startSync(new ShowSelectedHeaderData());
		}
	}

	private class ShowSelectedHeaderData implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				QIFImportHead header = (QIFImportHead) auswahlExternesKonto.getValue();
				if (null == header || header.getID() == null)
					headerDataFelderLeeren();
				else
					headerDataFelderZeigen(header);
			}
			catch (RemoteException e)
			{
				GUI.getStatusBar().setErrorText(e.getMessage());
			}
		}
	}

	/**
	 * Dieser Formater zeichnet in der Spalte Buchungsart ein rotes Symbol, wenn
	 * dieses leer ist
	 * 
	 */
	class QIFImportPosListTableFormater implements TableFormatter
	{
		@Override
		public void format(TableItem item)
		{
			if (null == item)
				return;

			try
			{
				QIFImportPos pos = (QIFImportPos) item.getData();
				if (pos.getGesperrt().booleanValue())
				{
					item.setImage(0, imageLocked);
				}
				else
				{
					Buchungsart buchungsArt = pos.getBuchungsart();
					if (buchungsArt == null)
					{
						item.setImage(4, imageError);
						item.setFont(4, Font.ITALIC.getSWTFont());
						item.setText(4, "Buchungsart zuordnen!!");
					}
					if (pos.getMitgliedZuordenbar().booleanValue() == false)
					{
						item.setFont(10, Font.ITALIC.getSWTFont());
						item.setText(10, "ohne Mitgliedsreferenz");
					}
				}

			}
			catch (Throwable ex)
			{
				Logger.error("Fehler:", ex);
			}
		}
	}

	private class QIFImportHeaderMessageConsumer implements MessageConsumer
	{
		@Override
		public Class<?>[] getExpectedMessageTypes()
		{
			return new Class[]
			{
					QIFImportHeaderMessage.class
			};
		}

		@Override
		public void handleMessage(Message message) throws Exception
		{
			GUI.getDisplay().syncExec(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						if (null == auswahlExternesKonto)
						{
							unregisterMe();
							return;
						}
						auswahlExternesKonto.refresh();
					}
					catch (Exception e)
					{
						Logger.error("Unable to refresh Select ExternesKonto", e);
					}
				}
			});
		}

		public void unregisterMe()
		{
			Application.getMessagingFactory()
					.unRegisterMessageConsumer(QIFImportHeaderMessageConsumer.this);
		}

		public void registerMe()
		{
			Application.getMessagingFactory()
					.registerMessageConsumer(QIFImportHeaderMessageConsumer.this);
		}

		@Override
		public boolean autoRegister()
		{
			return false;
		}
	}

	public static class PosStatusFormater implements Formatter
	{
		@Override
		public String format(Object o)
		{
			if (QIFImportPos.SPERRE_JA.equals(o))
				return "Gesperrt";
			return "";
		}
	}

	public Action getPIFPosBuchenAction()
	{
		return new Action()
		{
			@Override
			public void handleAction(Object context)
			{
				GUI.startSync(new ExterneBuchungenImportieren());
			}
		};
	}

	class ExterneBuchungenImportieren extends DlgExecutor
	{
		private int buchungen;

		private Date datumStart;

		private Date datumEnde;

		private ArrayList<QIFImportHead> importHeadList;

		private double aktSaldo;

		private int saldoJahr;

		private ArrayList<SaldoJahr> jahresListe = new ArrayList<>();

		private Calendar calenderItem = Calendar.getInstance();

		@Override
		public void prozess() throws ApplicationException
		{
			try
			{
				GUI.getStatusBar().startProgress();
				init();
				pruefenDaten();
				frageBenutzer();
				buchungenUebernehmen();
				speichernJahresabschluss();
				headerAktuallisieren();
			}
			catch (RemoteException ex)
			{
				throw new ApplicationException(
						"Fehler beim Übernehmen der externen Buchungen!!", ex);
			}
			finally
			{
				GUI.getStatusBar().stopProgress();
			}
		}

		private void speichernJahresabschluss()
				throws RemoteException, ApplicationException
		{
			int aktJahr = getAktJahr();
			for (SaldoJahr jahr : jahresListe)
			{
				int saldoJahr = jahr.getJahr();
				if (saldoJahr >= aktJahr)
					break;
				speichernJahresabschluss(saldoJahr);
			}
		}

		private void speichernJahresabschluss(int jahr)
				throws RemoteException, ApplicationException
		{
			Jahresabschluss abschluss = (Jahresabschluss) Einstellungen.getDBService()
					.createObject(Jahresabschluss.class, null);
			abschluss.setDatum(new Date());
			abschluss.setVon(getJahresStart(jahr));
			abschluss.setBis(getJahresEnde(jahr));
			abschluss.setName("Importierte Buchungen");
			abschluss.store();
		}

		private Date getJahresEnde(int jahr)
		{
			calenderItem.clear();
			calenderItem.set(Calendar.YEAR, jahr);
			calenderItem.set(Calendar.DAY_OF_MONTH, 31);
			calenderItem.set(Calendar.MONTH, Calendar.DECEMBER);
			return calenderItem.getTime();
		}

		private Date getJahresStart(int jahr)
		{
			calenderItem.clear();
			calenderItem.set(Calendar.YEAR, jahr);
			calenderItem.set(Calendar.DAY_OF_YEAR, 1);
			return calenderItem.getTime();
		}

		private int getAktJahr()
		{
			Calendar datum = Calendar.getInstance();
			return datum.get(Calendar.YEAR);
		}

		private void buchungenUebernehmen()
				throws RemoteException, ApplicationException
		{
			GUI.getStatusBar().startProgress();
			for (QIFImportHead importHead : importHeadList)
			{
				buchungenUebernehmen(importHead);
			}
			zeigeMeldung("Alle Buchungen erfolgreich übernommen!");
		}

		private void buchungenUebernehmen(QIFImportHead importHead)
				throws RemoteException, ApplicationException
		{
			Konto konto = importHead.getKonto();
			zeigeMeldung(
					"Buchungen werden übernommen für Konto : " + konto.getBezeichnung());
			DBIterator<
					QIFImportPos> iteratorQIFImportPos = loadExterneBuchungen(importHead);
			speichernKontoeroeffnung(konto, importHead);
			speichernAnfangsbestand(importHead.getStartSaldo(), konto,
					iteratorQIFImportPos.next());

			iteratorQIFImportPos.begin();
			while (iteratorQIFImportPos.hasNext())
			{
				QIFImportPos importPos =  iteratorQIFImportPos.next();
				if (pruefeImportPos(importPos))
				{
					neuerAnfangsBestandWennNotwendig(importPos, konto);
					Buchung buchung = buchungUebernehmen(importPos, konto);
					mitgliedsKontoSpeichern(importPos, buchung);
				}
			}

			importHead.setProcessDate(new Date());
			importHead.store();
		}

		private void speichernKontoeroeffnung(Konto konto, QIFImportHead importHead)
				throws RemoteException, ApplicationException
		{
			if (konto.getEroeffnung() == null)
			{
				konto.setEroeffnung(importHead.getStartDate());
				konto.store();
			}
		}

		private void neuerAnfangsBestandWennNotwendig(QIFImportPos importPos,
				Konto konto) throws RemoteException, ApplicationException
		{
			int buchJahr = getJahr(importPos.getDatum());
			if (buchJahr != saldoJahr)
				speichernAnfangsbestand(aktSaldo, konto, importPos);
		}

		private void mitgliedsKontoSpeichern(QIFImportPos importPos,
				Buchung buchung) throws RemoteException, ApplicationException
		{
			Mitglied mitglied = importPos.getMitglied();
			if (null == mitglied)
				return;

			Mitgliedskonto mk = (Mitgliedskonto) Einstellungen.getDBService()
					.createObject(Mitgliedskonto.class, null);
			mk.setBetrag(buchung.getBetrag());
			mk.setDatum(buchung.getDatum());
			mk.setMitglied(mitglied);
			mk.setZahlungsweg(Zahlungsweg.ÜBERWEISUNG);
			mk.setZweck1(buchung.getZweck());
			mk.store();

			buchung.setMitgliedskonto(mk);
			buchung.store();
		}

		private int getJahr(Date datum)
		{
			calenderItem.setTime(datum);
			return calenderItem.get(Calendar.YEAR);
		}

		private void speichernAnfangsbestand(double anfangWert, Konto konto,
				QIFImportPos importPos) throws RemoteException, ApplicationException
		{
			Date datum = gibDatumAnfangsSaldo(importPos);
			aktSaldo = anfangWert;
			saldoJahr = getJahr(datum);
			merkenSaldoJahr(saldoJahr);

			Anfangsbestand anfangsBestand = (Anfangsbestand) Einstellungen
					.getDBService().createObject(Anfangsbestand.class, null);
			anfangsBestand.setKonto(konto);
			anfangsBestand.setBetrag(anfangWert);
			anfangsBestand.setDatum(datum);
			anfangsBestand.store();
		}

		private void merkenSaldoJahr(int jahr)
		{
			SaldoJahr saldoJahr = new SaldoJahr(jahr);
			if (jahresListe.contains(saldoJahr) == false)
				jahresListe.add(saldoJahr);
		}

		private Date gibDatumAnfangsSaldo(QIFImportPos importPos)
				throws RemoteException
		{
			calenderItem.setTime(importPos.getDatum());
			calenderItem.set(Calendar.DAY_OF_MONTH, 1);
			calenderItem.set(Calendar.MONTH, Calendar.JANUARY);
			return calenderItem.getTime();
		}

		private Buchung buchungUebernehmen(QIFImportPos importPos, Konto konto)
				throws RemoteException, ApplicationException
		{
			Buchung buchung = BuchungNoCheck.getNewInstanze();
			buchung.setArt(importPos.getBeleg());
			buchung.setBetrag(importPos.getBetrag().doubleValue());
			buchung.setBuchungsart(importPos.getBuchungsartId());
			buchung.setDatum(importPos.getDatum());
			buchung.setKommentar("Importiert von externen Programm");
			buchung.setKonto(konto);
			buchung.setName(importPos.getName());
			buchung.setZweck(importPos.getZweck());
			buchung.store();

			aktSaldo += buchung.getBetrag();

			return buchung;
		}

		private boolean pruefeImportPos(QIFImportPos importPos)
				throws RemoteException
		{
			if (importPos.getGesperrt().booleanValue())
				return false;
			Double betrag = importPos.getBetrag();
			if (null == betrag)
				return false;
			if (betrag.doubleValue() == 0d)
				return false;
			return true;
		}

		private DBIterator<QIFImportPos>
				loadExterneBuchungen(QIFImportHead importHead) throws RemoteException
		{
			DBIterator<QIFImportPos> iterator = Einstellungen.getDBService()
					.createList(QIFImportPos.class);
			iterator.addFilter(QIFImportPos.COL_HEADID + "= ?", importHead.getID());
			iterator.setOrder(
					"order by " + QIFImportPos.COL_DATUM + "," + QIFImportPos.COL_POSID);
			return iterator;
		}

		private void frageBenutzer() throws ApplicationException
		{
			super.frageBenutzer("Externe Buchungen importieren",
					"Sollen die externen Buchungen nach JVerein übernommen werden?\n"
							+ "Anzahl Konten : " + importHeadList.size() + "\n"
							+ "Anzahl Buchungen : " + buchungen + "\n\n"
							+ "Sie sollten alle QIF Dateien importiert haben bevor diese Funktion gestartet wird,\n"
							+ "weil für alte Buchungsjahre ein Jahresabschluss gemacht wird.");
		}

		private void init()
		{
			buchungen = 0;
			datumStart = null;
			datumEnde = null;
			importHeadList = new ArrayList<>();
		}

		private void pruefenDaten() throws RemoteException, ApplicationException
		{
			pruefenHeadDaten();
			pruefenPosDaten();

		}

		private void zeigeMeldung(String meldetext)
		{
			GUI.getStatusBar().setSuccessText(meldetext);
		}

		private void pruefenPosDaten() throws RemoteException, ApplicationException
		{
			zeigeMeldung("Prüfe externe Buchungspositionen ..");

			DBIterator<QIFImportPos> posIterator = Einstellungen.getDBService()
					.createList(QIFImportPos.class);
			while (posIterator.hasNext())
			{
				QIFImportPos importPos = posIterator.next();
				pruefenPosDaten(importPos);
			}
			if (buchungen == 0)
				throw new ApplicationException(
						"Es gibt keine externen Buchungen zum Übernehmen!");
		}

		private void pruefenPosDaten(QIFImportPos importPos)
				throws RemoteException, ApplicationException
		{
			if (importPos.getGesperrt().booleanValue())
				return;
			if (importPos.getBuchungsart() == null)
				throw new ApplicationException(
						"Es gibt externe Buchungen ohne zugewiesene JVereins Buchungsart, z.B. Buchung mit ID "
								+ importPos.getID());

			++buchungen;
			Date buchungsDatum = importPos.getDatum();
			if (null == datumStart || buchungsDatum.before(datumStart))
				datumStart = buchungsDatum;
			if (null == datumEnde || buchungsDatum.after(datumEnde))
				datumEnde = buchungsDatum;
		}

		private void pruefenHeadDaten() throws RemoteException, ApplicationException
		{
			zeigeMeldung("Prüfe Externe Daten ..");
			DBIterator<QIFImportHead> headIterator = Einstellungen.getDBService()
					.createList(QIFImportHead.class);
			while (headIterator.hasNext())
			{
				QIFImportHead importHead = headIterator.next();
				pruefeHeadDaten(importHead);
				importHeadList.add(importHead);
			}
		}

		private void pruefeHeadDaten(QIFImportHead importHead)
				throws RemoteException, ApplicationException
		{
			if (importHead.getProcessDate() != null)
				throw new ApplicationException("Das externe Konto "
						+ importHead.getName() + " wurde bereits importiert!");

			Konto konto = importHead.getKonto();
			if (null == konto)
				throw new ApplicationException("Dem externen Konto "
						+ importHead.getName() + " wurde kein JVereinskonto zugeordnet!");

			pruefeKontoIstLeer(konto);
			pruefeKontoHatAnfangsbestand(konto);
		}

		private void pruefeKontoHatAnfangsbestand(Konto konto)
				throws RemoteException, ApplicationException
		{
			zeigeMeldung("Prüfe JVereins Konto Anfangsbestand ..");
			DBIterator<QIFImportHead> iteratorBuchungsListe = Einstellungen
					.getDBService().createList(Anfangsbestand.class);
			iteratorBuchungsListe.addFilter("KONTO = ?", konto.getID());
			int anzahl = iteratorBuchungsListe.size();
			if (anzahl > 0)
				throw new ApplicationException(
						"Import nicht möglich!! Das JVereins Konto "
								+ konto.getBezeichnung() + " hat bereits Anfangsbestand");
		}

		private void pruefeKontoIstLeer(Konto konto)
				throws RemoteException, ApplicationException
		{
			zeigeMeldung("Prüfe JVereins Konten ..");
			DBIterator<Buchung> iteratorBuchungsListe = Einstellungen.getDBService()
					.createList(Buchung.class);
			iteratorBuchungsListe.addFilter("KONTO = ?", konto.getID());
			int anzahl = iteratorBuchungsListe.size();
			if (anzahl > 0)
				throw new ApplicationException(
						"Import nicht möglich!! Das JVereins Konto "
								+ konto.getBezeichnung() + " hat bereits " + anzahl
								+ " Buchungen.");
		}

	}

	static class SaldoJahr
	{
		private int jahr;

		public SaldoJahr(int jahr)
		{
			this.jahr = jahr;
		}

		public int getJahr()
		{
			return jahr;
		}

		@Override
		public int hashCode()
		{
			return jahr;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SaldoJahr other = (SaldoJahr) obj;
			if (jahr != other.jahr)
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "Jahr : " + jahr;
		}
	}

	/***
	 * Wir verwenden zum Speichern eine eigen Buchung.class weil wir Buchungen
	 * importieren wollen die auch älter als 10 Jahre sind aber das Original
	 * Objekt nicht ändern werden.
	 * 
	 * @author Rolf Mamat
	 * 
	 */
	private static class BuchungNoCheck extends BuchungImpl
	{
		private static final long serialVersionUID = -4605550517137564571L;

		public BuchungNoCheck()
				throws RemoteException
		{
			super();
		}

		public static Buchung getNewInstanze() throws RemoteException
		{
			try
			{
				BuchungNoCheck buchung = new BuchungNoCheck();
				buchung.setService((DBServiceImpl) Einstellungen.getDBService());
				buchung.init();
				return buchung;
			}
			catch (Throwable ex)
			{
				throw new RemoteException(
						"Neue Instanz von Buchung kann nicht erzeugt werden.");
			}
		}

		@Override
		public void plausi() throws RemoteException, ApplicationException
		{
			if (getKonto() == null)
			{
				throw new ApplicationException("Bitte Konto eingeben");
			}
			if (getDatum() == null)
			{
				throw new ApplicationException("Bitte Datum eingeben");
			}
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(getDatum());
			Calendar cal2 = Calendar.getInstance();
			if (cal1.after(cal2))
			{
				throw new ApplicationException("Buchungsdatum liegt in der Zukunft");
			}

			Jahresabschluss ja = getJahresabschluss();
			if (ja != null)
			{
				throw new ApplicationException(
						"Buchung kann nicht gespeichert werden. Zeitraum ist bereits abgeschlossen!");
			}
			if (getBetrag() == 0.0d)
			{
				throw new ApplicationException("Betrag fehlt!");
			}
			if (!getSpeicherung() && getBuchungsart() == null)
			{
				throw new ApplicationException("Buchungsart fehlt bei Splitbuchung!");
			}
		}

	}

	public Action getAktuellenImportLoeschenAction()
	{
		return new Action()
		{
			@Override
			public void handleAction(Object context)
			{
				GUI.startSync(new AktuellenImportLoeschen());
			}
		};
	}

	public Action getAlleImportsLoeschenAction()
	{
		return new Action()
		{
			@Override
			public void handleAction(Object context)
			{
				GUI.startSync(new AlleImportsLoeschen());
			}
		};
	}

	private class AlleImportsLoeschen extends DlgExecutor
	{
		@Override
		void prozess() throws RemoteException, ApplicationException
		{
			DBIterator<QIFImportHead> itHeader = checkHeader();
			frageBenutzer(itHeader);
			loeschen(itHeader);
			headerAktuallisieren();
		}

		private void loeschen(DBIterator<QIFImportHead> itHeader)
				throws RemoteException, ApplicationException
		{
			loeschenPositionen();
			loeschenHeader(itHeader);
		}

		private void loeschenHeader(DBIterator<QIFImportHead> itHeader)
				throws RemoteException, ApplicationException
		{
			while (itHeader.hasNext())
			{
				QIFImportHead head = itHeader.next();
				head.delete();
			}
		}

		private void loeschenPositionen()
				throws RemoteException, ApplicationException
		{
			DBIterator<QIFImportPos> iterator = Einstellungen.getDBService()
					.createList(QIFImportPos.class);
			while (iterator.hasNext())
			{
				QIFImportPos pos = iterator.next();
				pos.delete();
			}
		}

		private void frageBenutzer(DBIterator<QIFImportHead> itHeader)
				throws RemoteException, ApplicationException
		{
			super.frageBenutzer("Alle Imports löschen",
					"Sollen alle " + itHeader.size() + " Imports gelöscht werden?");
		}

		private DBIterator<QIFImportHead> checkHeader()
				throws RemoteException, ApplicationException
		{
			DBIterator<QIFImportHead> iterator = Einstellungen.getDBService()
					.createList(QIFImportHead.class);
			if (iterator.size() == 0)
				throw new ApplicationException(
						"Es gibt keine Imports die gelöscht werden könnten.");
			return iterator;
		}
	}

	private class AktuellenImportLoeschen extends DlgExecutor
	{
		@Override
		void prozess() throws RemoteException, ApplicationException
		{
			checkAuswahl();
			frageBenutzer();
			loeschen();
			headerAktuallisieren();
		}

		private void loeschen() throws RemoteException, ApplicationException
		{
			DBIterator<QIFImportPos> iterator = Einstellungen.getDBService()
					.createList(QIFImportPos.class);
			iterator.addFilter(QIFImportPos.COL_HEADID + " = ?",
					headerSelected.getID());
			while (iterator.hasNext())
			{
				QIFImportPos pos = iterator.next();
				pos.delete();
			}
			headerSelected.delete();
		}

		private void frageBenutzer() throws RemoteException, ApplicationException
		{
			super.frageBenutzer("Externe Buchungen löschen",
					"Sollen die importierten Daten des Kontos " + headerSelected.getName()
							+ " gelöscht werden?");
		}

		private void checkAuswahl() throws ApplicationException
		{
			if (null == headerSelected)
				throw new ApplicationException(
						"Kein externes Konto zum Löschen ausgewählt!");
		}
	}

	/***
	 * Diese Klasse bietet einen Rahmen in dem Aktionen in einer Datenbank
	 * Transaktion ablaufen. Im Fehlerfall wird alles zurück gerollt im Gutfall
	 * werden die Änderungen commited
	 * 
	 * @author Rolf Mamat
	 * 
	 */
	private abstract class DlgExecutor implements Runnable
	{
		private AbstractDBObject transactionObject;

		@Override
		public void run()
		{
			try
			{
				transactionStarten();

				prozess();

				transactionCommit();
			}
			catch (RemoteException ex)
			{
				transactionRollback();
				GUI.getStatusBar().setErrorText(ex.getLocalizedMessage());
			}
			catch (ApplicationException ex)
			{
				transactionRollback();
				GUI.getStatusBar().setErrorText(ex.getLocalizedMessage());
			}
		}

		private void transactionRollback()
		{
			try
			{
				if (null != transactionObject)
					transactionObject.transactionRollback();
				transactionObject = null;
			}
			catch (RemoteException ex)
			{
				final String meldung = "Transaction kann nicht zurück gerollt werden!!";
				Logger.error(meldung, ex);
				GUI.getStatusBar().setErrorText(meldung);
			}
		}

		private void transactionCommit() throws RemoteException
		{
			if (null != transactionObject)
				transactionObject.transactionCommit();
			transactionObject = null;
		}

		private void transactionStarten() throws RemoteException
		{
			transactionObject = (AbstractDBObject) Einstellungen.getDBService()
					.createObject(Buchung.class, null);
			transactionObject.transactionBegin();
		}

		protected void frageBenutzer(final String titel, final String frage)
				throws ApplicationException
		{
			try
			{
				GUI.getStatusBar().stopProgress();

				YesNoDialog dialog = new YesNoDialog(YesNoDialog.POSITION_CENTER);
				dialog.setTitle(titel);
				dialog.setText(frage);
				Boolean antwort = (Boolean) dialog.open();
				if (antwort.booleanValue() == false)
					throw new ApplicationException(
							"Funktion abgebrochen durch Benutzer!!");
			}
			catch (ApplicationException ex)
			{
				throw ex;
			}
			catch (Exception ex)
			{
				throw new ApplicationException(
						"Benutzerdialog '" + titel + "' kann nicht gezeigt werden.", ex);
			}
		}

		protected void headerAktuallisieren()
		{
			Application.getMessagingFactory()
					.sendMessage(new QIFImportHeaderMessage());
		}

		abstract void prozess() throws RemoteException, ApplicationException;

	}

}
