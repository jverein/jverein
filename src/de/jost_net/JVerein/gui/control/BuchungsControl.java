/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.DBTools.DBTransaction;
import de.jost_net.JVerein.Messaging.BuchungMessage;
import de.jost_net.JVerein.Queries.BuchungQuery;
import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.gui.dialogs.BuchungsjournalSortDialog;
import de.jost_net.JVerein.gui.dialogs.SammelueberweisungAuswahlDialog;
import de.jost_net.JVerein.gui.formatter.BuchungsartFormatter;
import de.jost_net.JVerein.gui.formatter.MitgliedskontoFormatter;
import de.jost_net.JVerein.gui.formatter.ProjektFormatter;
import de.jost_net.JVerein.gui.input.BuchungsartInput;
import de.jost_net.JVerein.gui.input.KontoauswahlInput;
import de.jost_net.JVerein.gui.input.MitgliedskontoauswahlInput;
import de.jost_net.JVerein.gui.menu.BuchungMenu;
import de.jost_net.JVerein.gui.menu.SplitBuchungMenu;
import de.jost_net.JVerein.gui.parts.BuchungListTablePart;
import de.jost_net.JVerein.gui.parts.SplitbuchungListTablePart;
import de.jost_net.JVerein.io.BuchungAuswertungCSV;
import de.jost_net.JVerein.io.BuchungAuswertungPDF;
import de.jost_net.JVerein.io.BuchungsjournalPDF;
import de.jost_net.JVerein.io.SplitbuchungsContainer;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.BuchungsartSort;
import de.jost_net.JVerein.keys.SplitbuchungTyp;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.Projekt;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericObject;
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
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.formatter.TableFormatter;
import de.willuhn.jameica.gui.input.AbstractInput;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.parts.table.FeatureSummary;
import de.willuhn.jameica.hbci.rmi.SepaSammelUeberweisung;
import de.willuhn.jameica.hbci.rmi.SepaSammelUeberweisungBuchung;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungsControl extends AbstractControl {

  private de.willuhn.jameica.system.Settings settings;

  private TablePart buchungsList;

  /* Split-Buchnungen */
  private TablePart splitbuchungsList;

  /* Controls */
  private Input id;

  private IntegerInput belegnummer;

  private Input umsatzid;

  private DialogInput konto;

  private IntegerInput auszugsnummer;

  private IntegerInput blattnummer;

  private Input name;

  private DecimalInput betrag;

  private TextAreaInput zweck;

  private DateInput datum = null;

  private Input art;

  private DialogInput mitgliedskonto;

  private TextAreaInput kommentar;

  // Definition für beide Auswahlvarianten (SelectInput und
  // BuchungsartSearchInput)
  private AbstractInput buchungsart;

  private SelectInput projekt;

  private DialogInput suchkonto;

  private SelectInput suchbuchungsart;

  private SelectInput suchprojekt;

  private SelectInput hasmitglied;

  private DateInput vondatum = null;

  private DateInput bisdatum = null;

  private TextInput suchtext = null;

  private TextInput suchbetrag = null;

  private CheckboxInput verzicht;

  private Buchung buchung;

  private Button sammelueberweisungButton;

  private BuchungQuery query;

  public static final String BUCHUNGSART = "suchbuchungsart";

  public static final String PROJEKT = "suchprojekt";

  public static final String MITGLIEDZUGEORDNET = "suchmitgliedzugeordnet";

  private Vector<Listener> changeKontoListener = new Vector<>();

  private static GregorianCalendar LastBeginnGeschaeftsjahr;
  private static GregorianCalendar LastEndeGeschaeftsjahr;
  private static String LastKontoId;
  private static Integer LastBelegnummer;

  public BuchungsControl(AbstractView view) {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public static Integer getLastBelegnummer(Date NewDate_, String NewKontoId)
      throws RemoteException {
    if (Einstellungen.getEinstellung().getVerwendeBelegnummer()) {
      Boolean getNewLastBelegnummer = false;

      // Falls kein neues Datum bisher angegeben wurde, wird implizit angenommen, dass heute als
      // aktuelles Datum eingetragen wird
      GregorianCalendar NewDate = new GregorianCalendar();
      if (NewDate_ != null) {
        NewDate.setTime(NewDate_);
      }

      // Falls noch nichts initialisiert wurde, dann entsprechend alles setzen
      if (LastBeginnGeschaeftsjahr == null || LastEndeGeschaeftsjahr == null
          || LastKontoId == null) {
        LastBeginnGeschaeftsjahr = Einstellungen.getBeginnGeschaeftsjahr(NewDate);
        LastEndeGeschaeftsjahr = Einstellungen.getEndeGeschaeftsjahr(NewDate);
        LastKontoId = NewKontoId;
        getNewLastBelegnummer = true;
      }

      // Falls neues Datum nicht innerhalb des aktuellen Geschäftsjahres liegt, Belegnummer neu
      // auslesen
      if (!(NewDate.compareTo(LastBeginnGeschaeftsjahr) >= 0
          && NewDate.compareTo(LastEndeGeschaeftsjahr) <= 0)
          && Einstellungen.getEinstellung().getBelegnummerProJahr()) {
        LastBeginnGeschaeftsjahr = Einstellungen.getBeginnGeschaeftsjahr(NewDate);
        LastEndeGeschaeftsjahr = Einstellungen.getEndeGeschaeftsjahr(NewDate);
        getNewLastBelegnummer = true;
      }

      // Falls KontoId anders ist, Belegnummer neu auslesen
      if (LastKontoId != NewKontoId && Einstellungen.getEinstellung().getBelegnummerProKonto()) {
        LastKontoId = NewKontoId;
        getNewLastBelegnummer = true;
      }

      // neue max. Belegnummer auslesen
      if (getNewLastBelegnummer) {
        // SQL Befehl zusammensetzen (je nachdem welche Einstellungen gesetzt sind)
        List<Object> arg_list = new ArrayList<Object>();
        String sql = "SELECT max(belegnummer) FROM buchung";
        if (Einstellungen.getEinstellung().getBelegnummerProJahr()
            && Einstellungen.getEinstellung().getBelegnummerProKonto()) {
          sql += " WHERE ";
        }
        if (Einstellungen.getEinstellung().getBelegnummerProJahr()) {
          sql += "datum >= ? AND datum <= ?";
          arg_list.add(LastBeginnGeschaeftsjahr.getTime());
          arg_list.add(LastEndeGeschaeftsjahr.getTime());
          if (Einstellungen.getEinstellung().getBelegnummerProKonto()) {
            sql += " AND ";
          }
        }
        if (Einstellungen.getEinstellung().getBelegnummerProKonto()) {
          sql += "konto = " + LastKontoId;
        }
        DBService service = Einstellungen.getDBService();
        LastBelegnummer =
            (Integer) service.execute(sql, arg_list.toArray(), new ResultSetExtractor() {
              @Override
              public Object extract(ResultSet rs) throws SQLException {
                rs.next();
                return rs.getInt(1);
              }
            });
      }
      return LastBelegnummer;
    } else {
      return -1;
    }
  }

  public static void setNewLastBelegnummer(Integer NewLastBelegnummer, Date NewDate_,
      String NewKontoId) throws RemoteException {
    getLastBelegnummer(NewDate_, NewKontoId);
    LastBelegnummer = NewLastBelegnummer;
  }

  public Buchung getBuchung() throws RemoteException {
    if (buchung != null) {
      return buchung;
    }
    buchung = (Buchung) getCurrentObject();
    if (buchung == null) {
      buchung = (Buchung) Einstellungen.getDBService().createObject(Buchung.class, null);
    }
    return buchung;
  }

  public Input getID() throws RemoteException {
    if (id != null) {
      return id;
    }
    id = new TextInput(getBuchung().getID(), 10);
    id.setEnabled(false);
    return id;
  }

  public IntegerInput getBelegnummer() throws RemoteException {
    if (belegnummer != null) {
      return belegnummer;
    }
    belegnummer = new IntegerInput(getBuchung().getBelegnummer());
    return belegnummer;
  }

  public Input getUmsatzid() throws RemoteException {
    if (umsatzid != null) {
      return umsatzid;
    }
    Integer ui = getBuchung().getUmsatzid();
    if (ui == null) {
      ui = Integer.valueOf(0);
    }
    umsatzid = new IntegerInput(ui);
    umsatzid.setEnabled(false);
    return umsatzid;
  }

  public DialogInput getKonto(boolean withFocus) throws RemoteException {
    if (konto != null) {
      return konto;
    }
    String kontoid = getVorauswahlKontoId();
    konto = new KontoauswahlInput(getBuchung().getKonto()).getKontoAuswahl(false, kontoid, false,
        false);
    if (withFocus) {
      konto.focus();
    }
    return konto;
  }

  private String getVorauswahlKontoId() throws RemoteException {
    Buchung buchung = getBuchung();
    if (null != buchung) {
      Konto konto = buchung.getKonto();
      if (null != konto)
        return konto.getID();
    }
    return settings.getString("kontoid", "");
  }

  public Input getAuszugsnummer() {
    if (auszugsnummer != null) {
      return auszugsnummer;
    }
    Integer intAuszugsnummer;
    try {
      intAuszugsnummer = getBuchung().getAuszugsnummer();
    } catch (RemoteException e) {
      intAuszugsnummer = null;
    }
    auszugsnummer = new IntegerInput(intAuszugsnummer != null ? intAuszugsnummer : -1);
    return auszugsnummer;
  }

  public Input getBlattnummer() {
    if (blattnummer != null) {
      return blattnummer;
    }
    Integer intBlattnummer;
    try {
      intBlattnummer = getBuchung().getBlattnummer();
    } catch (RemoteException e) {
      intBlattnummer = null;
    }
    blattnummer = new IntegerInput(intBlattnummer != null ? intBlattnummer : -1);
    return blattnummer;
  }

  public Input getName() throws RemoteException {
    if (name != null) {
      return name;
    }
    name = new TextInput(getBuchung().getName(), 100);
    return name;
  }

  public DecimalInput getBetrag() throws RemoteException {
    if (betrag != null) {
      return betrag;
    }

    if (getBuchung().isNewObject() && getBuchung().getBetrag() == 0d) {
      betrag = new DecimalInput(Einstellungen.DECIMALFORMAT);
    } else {
      betrag = new DecimalInput(getBuchung().getBetrag(), Einstellungen.DECIMALFORMAT);
    }
    return betrag;
  }

  public Input getZweck() throws RemoteException {
    if (zweck != null) {
      return zweck;
    }
    zweck = new TextAreaInput(getBuchung().getZweck(), 500);
    zweck.setHeight(50);
    return zweck;
  }

  public DateInput getDatum() throws RemoteException {
    if (datum != null) {
      return datum;
    }
    Date d = getBuchung().getDatum();
    this.datum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.datum.setTitle("Datum");
    this.datum.setText("Bitte Datum wählen");
    return datum;
  }

  public TextInput getSuchtext() {
    if (suchtext != null) {
      return suchtext;
    }
    suchtext = new TextInput(settings.getString("suchtext", ""), 35);
    suchtext.addListener(new FilterListener());
    return suchtext;
  }

  public TextInput getSuchBetrag() throws RemoteException {
    if (suchbetrag != null) {
      return suchbetrag;
    }
    suchbetrag = new TextInput(settings.getString("suchbetrag", ""));
    suchbetrag.addListener(new FilterListener());
    return suchbetrag;
  }

  public CheckboxInput getVerzicht() throws RemoteException {
    if (verzicht != null) {
      return verzicht;
    }

    Boolean vz = buchung.getVerzicht();
    if (vz == null) {
      vz = Boolean.FALSE;
    }
    verzicht = new CheckboxInput(vz);
    return verzicht;
  }

  public DialogInput getMitgliedskonto() throws RemoteException {
    mitgliedskonto = new MitgliedskontoauswahlInput(getBuchung()).getMitgliedskontoAuswahl();
    mitgliedskonto.addListener(new Listener() {

      @Override
      public void handleEvent(Event event) {
        try {
          String name = (String) getName().getValue();
          String zweck1 = (String) getZweck().getValue();
          if (mitgliedskonto.getValue() != null && name.length() == 0 && zweck1.length() == 0) {
            if (mitgliedskonto.getValue() instanceof Mitgliedskonto) {
              Mitgliedskonto mk = (Mitgliedskonto) mitgliedskonto.getValue();
              getName().setValue(Adressaufbereitung.getNameVorname(mk.getMitglied()));
              getBetrag().setValue(mk.getBetrag());
              getZweck().setValue(mk.getZweck1());
              getDatum().setValue(mk.getDatum());
            }
            if (mitgliedskonto.getValue() instanceof Mitglied) {
              Mitglied m2 = (Mitglied) mitgliedskonto.getValue();
              getName().setValue(Adressaufbereitung.getNameVorname(m2));
              getDatum().setValue(new Date());
            }
          }
          if (mitgliedskonto.getValue() instanceof Mitgliedskonto) {
            Mitgliedskonto mk = (Mitgliedskonto) mitgliedskonto.getValue();
            if (getBuchungsart().getValue() == null) {
              getBuchungsart().setValue(mk.getBuchungsart());
            }
          }
        } catch (RemoteException e) {
          Logger.error("Fehler", e);
        }
      }
    });
    return mitgliedskonto;
  }

  public Input getArt() throws RemoteException {
    if (art != null && !art.getControl().isDisposed()) {
      return art;
    }
    art = new TextInput(getBuchung().getArt(), 100);
    return art;
  }

  public Input getKommentar() throws RemoteException {
    if (kommentar != null && !kommentar.getControl().isDisposed()) {
      return kommentar;
    }
    kommentar = new TextAreaInput(getBuchung().getKommentar(), 1024);
    kommentar.setHeight(50);
    return kommentar;
  }

  public Input getBuchungsart() throws RemoteException {
    if (buchungsart != null && !buchungsart.getControl().isDisposed()) {
      return buchungsart;
    }
    buchungsart =
        new BuchungsartInput().getBuchungsartInput(buchungsart, getBuchung().getBuchungsart());
    return buchungsart;
  }

  public Input getProjekt() throws RemoteException {
    if (projekt != null && !projekt.getControl().isDisposed()) {
      return projekt;
    }
    DBIterator<Projekt> list = Einstellungen.getDBService().createList(Projekt.class);
    Date buchungsDatum = getBuchung().getDatum() == null ? new Date() : getBuchung().getDatum();
    list.addFilter(
        "((startdatum is null or startdatum <= ?) and (endedatum is null or endedatum >= ?))",
        new Object[] {buchungsDatum, buchungsDatum});
    list.setOrder("ORDER BY bezeichnung");
    projekt = new SelectInput(list, getBuchung().getProjekt());
    projekt.setValue(getBuchung().getProjekt());
    projekt.setAttribute("bezeichnung");
    projekt.setPleaseChoose("Bitte auswählen");
    return projekt;
  }

  public DialogInput getSuchKonto() throws RemoteException {
    if (suchkonto != null) {
      return suchkonto;
    }
    String kontoid = settings.getString("suchkontoid", "");
    suchkonto = new KontoauswahlInput().getKontoAuswahl(true, kontoid, false, false);
    suchkonto.addListener(new FilterListener());
    return suchkonto;
  }

  public Button getSammelueberweisungButton() {
    sammelueberweisungButton = new Button("Sammelüberweisung", new Action() {

      @Override
      public void handleAction(Object context) {
        Buchung master = (Buchung) getCurrentObject();
        SammelueberweisungAuswahlDialog suad = new SammelueberweisungAuswahlDialog(master);
        try {
          SepaSammelUeberweisung su = suad.open();
          if (su != null) {
            for (SepaSammelUeberweisungBuchung ssub : su.getBuchungen()) {
              Buchung b = (Buchung) Einstellungen.getDBService().createObject(Buchung.class, null);
              b.setAuszugsnummer(master.getAuszugsnummer());
              b.setBetrag(ssub.getBetrag() * -1);
              b.setBlattnummer(master.getBlattnummer());
              b.setBuchungsart(master.getBuchungsartId());
              b.setDatum(su.getAusfuehrungsdatum());
              b.setKonto(master.getKonto());
              b.setName(ssub.getGegenkontoName());
              b.setSpeicherung(true);
              b.setSplitId(master.getSplitId());
              b.setSplitTyp(SplitbuchungTyp.SPLIT);
              b.setZweck(ssub.getZweck());
              SplitbuchungsContainer.add(b);
            }
            refreshSplitbuchungen();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }, null, false, "stock_navigator-shift-right.png");
    return sammelueberweisungButton;
  }

  public Input getSuchProjekt() throws RemoteException {
    if (suchprojekt != null) {
      return suchprojekt;
    }
    ArrayList<Projekt> projektliste = new ArrayList<>();
    Projekt p1 = (Projekt) Einstellungen.getDBService().createObject(Projekt.class, null);
    p1.setBezeichnung("Ohne Projekt");
    projektliste.add(p1);

    DBIterator<Projekt> list = Einstellungen.getDBService().createList(Projekt.class);
    list.setOrder("ORDER BY bezeichnung");
    while (list.hasNext()) {
      projektliste.add(list.next());
    }

    suchprojekt = new SelectInput(projektliste, null);
    suchprojekt.addListener(new FilterListener());
    suchprojekt.setAttribute("bezeichnung");
    suchprojekt.setPleaseChoose("keine Einschränkung");
    return suchprojekt;
  }

  public Input getSuchBuchungsart() throws RemoteException {
    if (suchbuchungsart != null) {
      return suchbuchungsart;
    }
    DBIterator<Buchungsart> list = Einstellungen.getDBService().createList(Buchungsart.class);
    if (Einstellungen.getEinstellung().getBuchungsartSort() == BuchungsartSort.NACH_NUMMER) {
      list.setOrder("ORDER BY nummer");
    } else {
      list.setOrder("ORDER BY bezeichnung");
    }
    ArrayList<Buchungsart> liste = new ArrayList<>();
    Buchungsart b1 =
        (Buchungsart) Einstellungen.getDBService().createObject(Buchungsart.class, null);
    b1.setNummer(-2);
    b1.setBezeichnung("Alle Buchungsarten");
    b1.setArt(-2);
    liste.add(b1);
    Buchungsart b2 =
        (Buchungsart) Einstellungen.getDBService().createObject(Buchungsart.class, null);
    b2.setNummer(-1);
    b2.setBezeichnung("Ohne Buchungsart");
    b2.setArt(-1);
    liste.add(b2);
    while (list.hasNext()) {
      liste.add(list.next());
    }
    int bwert = settings.getInt(BUCHUNGSART, -2);
    Buchungsart b = null;
    for (int i = 0; i < liste.size(); i++) {
      if (liste.get(i).getNummer() == bwert) {
        b = liste.get(i);
        break;
      }
    }
    suchbuchungsart = new SelectInput(liste, b);
    suchbuchungsart.addListener(new FilterListener());

    switch (Einstellungen.getEinstellung().getBuchungsartSort()) {
      case BuchungsartSort.NACH_NUMMER:
        suchbuchungsart.setAttribute("nrbezeichnung");
        break;
      case BuchungsartSort.NACH_BEZEICHNUNG_NR:
        suchbuchungsart.setAttribute("bezeichnungnr");
        break;
      default:
        suchbuchungsart.setAttribute("bezeichnung");
        break;
    }

    return suchbuchungsart;
  }

  public DateInput getVondatum() {
    if (vondatum != null) {
      return vondatum;
    }
    Date d = null;
    try {
      d = new JVDateFormatTTMMJJJJ().parse(settings.getString("vondatum", "01.01.2006"));
    } catch (ParseException e) {
      //
    }
    this.vondatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.vondatum.setTitle("Anfangsdatum");
    this.vondatum.setText("Bitte Anfangsdatum wählen");
    this.vondatum.addListener(new FilterListener());
    this.vondatum.setMandatory(true);
    return vondatum;
  }

  public DateInput getBisdatum() {
    if (bisdatum != null) {
      return bisdatum;
    }
    Date d = null;
    try {
      d = new JVDateFormatTTMMJJJJ().parse(settings.getString("bisdatum", "31.12.2006"));
    } catch (ParseException e) {
      //
    }
    this.bisdatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.bisdatum.setTitle("Anfangsdatum");
    this.bisdatum.setText("Bitte Anfangsdatum wählen");
    this.bisdatum.addListener(new FilterListener());
    this.bisdatum.setMandatory(true);
    return bisdatum;
  }

  public Button getStartAuswertungEinzelbuchungenButton() {
    Button b = new Button("PDF Einzelbuchungen", new Action() {

      @Override
      public void handleAction(Object context) {
        starteAuswertung(true);
      }
    }, null, true, "file-pdf.png"); // "true" defines this button as the default
    return b;
  }

  public Button getStartCSVAuswertungButton() {
    Button b = new Button("CSV-Export", new Action() {

      @Override
      public void handleAction(Object context) {
        starteCSVExport();
      }
    }, null, true, "code.png"); // "true" defines this button as the default
    return b;
  }

  public Button getStartAuswertungSummenButton() {
    Button b = new Button("PDF Summen", new Action() {

      @Override
      public void handleAction(Object context) {
        starteAuswertung(false);
      }
    }, null, true, "file-pdf.png"); // "true" defines this button as the default
    return b;
  }

  public Button getStartAuswertungBuchungsjournalButton() {
    Button b = new Button("PDF Buchungsjournal", new Action() {

      @Override
      public void handleAction(Object context) {
        starteAuswertungBuchungsjournal();
      }
    }, null, true, "file-pdf.png"); // "true" defines this button as the default
    return b;
  }

  private void handleStore() throws ApplicationException {
    try {
      Buchung b = getBuchung();

      b.setBuchungsart(getSelectedBuchungsArtId());
      b.setProjektID(getSelectedProjektId());
      b.setKonto(getSelectedKonto());
      b.setAuszugsnummer(getAuszugsnummerWert());
      b.setBlattnummer(getBlattnummerWert());
      b.setName((String) getName().getValue());
      if (getBetrag().getValue() != null) {
        b.setBetrag((Double) getBetrag().getValue());
      }
      b.setZweck((String) getZweck().getValue());
      b.setDatum((Date) getDatum().getValue());
      b.setArt((String) getArt().getValue());
      b.setVerzicht((Boolean) getVerzicht().getValue());
      b.setMitgliedskonto(getSelectedMitgliedsKonto(b));
      b.setKommentar((String) getKommentar().getValue());
      b.setBelegnummer((Integer) getBelegnummer().getValue());

      if (b.getSpeicherung()) {
        b.store();
        getID().setValue(b.getID());
        setNewLastBelegnummer(b.getBelegnummer(), b.getDatum(), b.getKonto().getID());
        GUI.getStatusBar().setSuccessText("Buchung gespeichert");
      } else {
        SplitbuchungsContainer.add(b);
        refreshSplitbuchungen();
        GUI.getStatusBar().setSuccessText("Buchung übernommen");
      }
    } catch (RemoteException ex) {
      final String meldung = "Fehler beim Speichern der Buchung.";
      Logger.error(meldung, ex);
      throw new ApplicationException(meldung, ex);
    }
  }

  private Mitgliedskonto getSelectedMitgliedsKonto(Buchung b) throws ApplicationException {
    try {
      Object auswahl = mitgliedskonto.getValue();
      if (null == auswahl)
        return null;

      if (auswahl instanceof Mitgliedskonto)
        return (Mitgliedskonto) auswahl;

      if (auswahl instanceof Mitglied) {
        Mitglied mitglied = (Mitglied) auswahl;
        Mitgliedskonto mk =
            (Mitgliedskonto) Einstellungen.getDBService().createObject(Mitgliedskonto.class, null);
        mk.setBetrag(b.getBetrag());
        mk.setDatum(b.getDatum());
        mk.setMitglied(mitglied);
        mk.setZahlungsweg(Zahlungsweg.ÜBERWEISUNG);
        mk.setZweck1(b.getZweck());
        mk.store();
        mitgliedskonto.setValue(mk);

        return mk;
      }
      return null;
    } catch (RemoteException ex) {
      final String meldung = "Fehler beim Buchen des Mitgliedskontos.";
      Logger.error(meldung, ex);
      throw new ApplicationException(meldung, ex);
    }
  }

  private Integer getBlattnummerWert() throws ApplicationException {
    Integer intBlatt = (Integer) getBlattnummer().getValue();
    if (intBlatt != null && intBlatt <= 0) {
      final String meldung =
          "Blattnummer kann nicht gespeichert werden. Muss leer oder eine positive Zahl sein.";
      Logger.error(meldung);
      throw new ApplicationException(meldung);
    }
    return intBlatt;
  }

  private Integer getAuszugsnummerWert() throws ApplicationException {
    Integer intAuszugsnummer = (Integer) auszugsnummer.getValue();
    if (intAuszugsnummer != null && intAuszugsnummer <= 0) {
      final String meldung =
          "Auszugsnummer kann nicht gespeichert werden. Muss leer oder eine positive Zahl sein.";
      Logger.error(meldung);
      throw new ApplicationException(meldung);
    }
    return intAuszugsnummer;
  }

  private Konto getSelectedKonto() throws ApplicationException {
    try {
      Konto konto = (Konto) getKonto(false).getValue();
      settings.setAttribute("kontoid", konto.getID());
      return konto;
    } catch (RemoteException ex) {
      final String meldung = "Konto der Buchung kann nicht ermittelt werden";
      Logger.error(meldung, ex);
      throw new ApplicationException(meldung, ex);
    }
  }

  private Long getSelectedProjektId() throws ApplicationException {
    try {
      Projekt projekt = (Projekt) getProjekt().getValue();
      if (null == projekt)
        return null;
      Long id = new Long(projekt.getID());
      return id;
    } catch (RemoteException ex) {
      final String meldung = "Gewähltes Projekt kann nicht ermittelt werden";
      Logger.error(meldung, ex);
      throw new ApplicationException(meldung, ex);
    }
  }

  private Long getSelectedBuchungsArtId() throws ApplicationException {
    try {
      Buchungsart buchungsArt = (Buchungsart) getBuchungsart().getValue();
      if (null == buchungsArt)
        return null;
      Long id = new Long(buchungsArt.getID());
      return id;
    } catch (RemoteException ex) {
      final String meldung = "Gewählte Buchungsart kann nicht ermittelt werden";
      Logger.error(meldung, ex);
      throw new ApplicationException(meldung, ex);
    }
  }

  public Part getBuchungsList() throws RemoteException {
    // Werte speichern
    Date dv = (Date) getVondatum().getValue();
    if (dv == null) {
      throw new RemoteException("von-Datum fehlt!");
    }
    settings.setAttribute("vondatum", new JVDateFormatTTMMJJJJ().format(dv));
    Date db = (Date) getBisdatum().getValue();
    if (db == null) {
      throw new RemoteException("bis-Datum fehlt!");
    }
    settings.setAttribute("bisdatum", new JVDateFormatTTMMJJJJ().format(db));
    Konto k = null;
    if (getSuchKonto().getValue() != null) {
      k = (Konto) getSuchKonto().getValue();
      settings.setAttribute("suchkontoid", k.getID());
    } else {
      settings.setAttribute("suchkontoid", "");
    }
    MitgliedZustand m = (MitgliedZustand) getSuchMitgliedZugeordnet().getValue();
    if (m != null) {
      settings.setAttribute(MITGLIEDZUGEORDNET, m.getText());
    }
    Buchungsart b = (Buchungsart) getSuchBuchungsart().getValue();
    if (b != null && b.getNummer() != 0) {
      b = (Buchungsart) getSuchBuchungsart().getValue();
      settings.setAttribute(BuchungsControl.BUCHUNGSART, b.getNummer());
    }
    Projekt p = (Projekt) getSuchProjekt().getValue();
    if (p != null) {
      p = (Projekt) getSuchProjekt().getValue();
      settings.setAttribute(BuchungsControl.PROJEKT, p.getID());
    } else {
      settings.setAttribute(BuchungsControl.PROJEKT, -2);
    }
    settings.setAttribute("suchtext", (String) getSuchtext().getValue());
    settings.setAttribute("suchbetrag", (String) getSuchBetrag().getValue());

    query = new BuchungQuery(dv, db, k, b, p, (String) getSuchtext().getValue(),
        (String) getSuchBetrag().getValue(), m.getValue());
    if (buchungsList == null) {
      buchungsList = new BuchungListTablePart(query.get(), new BuchungAction(false));
      if (!Einstellungen.getEinstellung().getVerwendeBelegnummer()) {
        buchungsList.addColumn("Nr", "id-int");
      } else {
        buchungsList.addColumn("Nr", "belegnummer");
      }

      buchungsList.addColumn("S", "splitid", new Formatter() {
        @Override
        public String format(Object o) {
          return (o != null ? "S" : " ");
        }
      });
      buchungsList.addColumn("Konto", "konto", new Formatter() {

        @Override
        public String format(Object o) {
          Konto k = (Konto) o;
          if (k != null) {
            try {
              return k.getBezeichnung();
            } catch (RemoteException e) {
              Logger.error("Fehler", e);
            }
          }
          return "";
        }
      });
      buchungsList.addColumn("Datum", "datum", new DateFormatter(new JVDateFormatTTMMJJJJ()));
      // buchungsList.addColumn(new Column("auszugsnummer", "Auszug",
      // new Formatter()
      // {
      // @Override
      // public String format(Object o)
      // {
      // return o.toString();
      // }
      // }, false, Column.ALIGN_AUTO, Column.SORT_BY_DISPLAY));
      // buchungsList.addColumn(new Column("blattnummer", "Blatt", new
      // Formatter()
      // {
      // @Override
      // public String format(Object o)
      // {
      // return o.toString();
      // }
      // }, false, Column.ALIGN_AUTO, Column.SORT_BY_DISPLAY));
      buchungsList.addColumn("Auszugsnummer", "auszugsnummer");
      buchungsList.addColumn("Blatt", "blattnummer");

      buchungsList.addColumn("Name", "name");
      buchungsList.addColumn("Verwendungszweck", "zweck", new Formatter() {
        @Override
        public String format(Object value) {
          if (value == null) {
            return null;
          }
          String s = value.toString();
          s = s.replaceAll("\r\n", " ");
          s = s.replaceAll("\r", " ");
          s = s.replaceAll("\n", " ");
          return s;
        }
      });
      buchungsList.addColumn("Buchungsart", "buchungsart", new BuchungsartFormatter());
      buchungsList.addColumn("Betrag", "betrag",
          new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
      buchungsList.addColumn(new Column("mitgliedskonto", "Mitglied", new MitgliedskontoFormatter(),
          false, Column.ALIGN_AUTO, Column.SORT_BY_DISPLAY));
      buchungsList.addColumn("Projekt", "projekt", new ProjektFormatter());
      buchungsList.setMulti(true);
      buchungsList.setContextMenu(new BuchungMenu(this));
      buchungsList.setRememberColWidths(true);
      buchungsList.setRememberOrder(true);
      buchungsList.setRememberState(true);
      buchungsList.addFeature(new FeatureSummary());
      Application.getMessagingFactory().registerMessageConsumer(new BuchungMessageConsumer());
    } else {
      buchungsList.removeAll();

      for (Buchung bu : query.get()) {
        buchungsList.addItem(bu);
      }
      buchungsList.sort();
    }

    informKontoChangeListener();

    return buchungsList;
  }

  public Part getSplitBuchungsList() throws RemoteException {
    if (splitbuchungsList == null) {
      splitbuchungsList =
          new SplitbuchungListTablePart(SplitbuchungsContainer.get(), new BuchungAction(true));
      if (!Einstellungen.getEinstellung().getVerwendeBelegnummer()) {
        splitbuchungsList.addColumn("Nr", "id-int");
      } else {
        splitbuchungsList.addColumn("Nr", "belegnummer");
      }

      splitbuchungsList.addColumn("Konto", "konto", new Formatter() {
        @Override
        public String format(Object o) {
          Konto k = (Konto) o;
          if (k != null) {
            try {
              return k.getBezeichnung();
            } catch (RemoteException e) {
              Logger.error("Fehler", e);
            }
          }
          return "";
        }
      });
      splitbuchungsList.addColumn("Typ", "splittyp", new Formatter() {
        @Override
        public String format(Object o) {
          Integer typ = (Integer) o;
          return SplitbuchungTyp.get(typ);
        }
      });
      splitbuchungsList.addColumn("Datum", "datum", new DateFormatter(new JVDateFormatTTMMJJJJ()));
      splitbuchungsList.addColumn("Auszug", "auszugsnummer");
      splitbuchungsList.addColumn("Blatt", "blattnummer");
      splitbuchungsList.addColumn("Name", "name");
      splitbuchungsList.addColumn("Verwendungszweck", "zweck");
      splitbuchungsList.addColumn("Buchungsart", "buchungsart", new BuchungsartFormatter());
      splitbuchungsList.addColumn("Betrag", "betrag",
          new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
      splitbuchungsList.addColumn("Mitglied", "mitgliedskonto", new MitgliedskontoFormatter());
      splitbuchungsList.addColumn("Projekt", "projekt", new ProjektFormatter());
      splitbuchungsList.setContextMenu(new SplitBuchungMenu(this));
      splitbuchungsList.setRememberColWidths(true);
      splitbuchungsList.addFeature(new FeatureSummary());
      Application.getMessagingFactory().registerMessageConsumer(new SplitBuchungMessageConsumer());
      splitbuchungsList.setFormatter(new TableFormatter() {
        /**
         * @see de.willuhn.jameica.gui.formatter.TableFormatter#format(org.eclipse.swt.widgets.TableItem)
         */
        @Override
        public void format(TableItem item) {
          if (item == null) {
            return;
          }
          try {
            Buchung b = (Buchung) item.getData();
            if (b.isToDelete()) {
              item.setForeground(new Color(null, new RGB(255, 0, 0)));
            }
          } catch (Exception e) {
            Logger.error("unable to format line", e);
          }
        }
      });
    } else {
      refreshSplitbuchungen();
    }
    return splitbuchungsList;
  }

  public void refreshBuchungen() throws RemoteException {
    if (buchungsList == null) {
      return;
    }
    buchungsList.removeAll();

    for (Buchung b : query.get()) {
      buchungsList.addItem(b);
    }
  }

  public void refreshSplitbuchungen() throws RemoteException {
    if (splitbuchungsList == null) {
      return;
    }
    splitbuchungsList.removeAll();

    for (Buchung b : SplitbuchungsContainer.get()) {
      splitbuchungsList.addItem(b);
    }
  }

  private void starteAuswertung(boolean einzelbuchungen) {

    try {
      DBIterator<Buchungsart> list = Einstellungen.getDBService().createList(Buchungsart.class);
      if (query.getBuchungsart() != null && query.getBuchungsart().getArt() != -2) {
        list.addFilter("id = ?", new Object[] {query.getBuchungsart().getID()});
      }
      if (query.getBuchungsart() != null && query.getBuchungsart().getArt() == -1) {
        list.addFilter("id = ?", -1);
      }
      list.setOrder("ORDER BY nummer");
      ArrayList<Buchungsart> buchungsarten = new ArrayList<>();
      while (list.hasNext()) {
        buchungsarten.add(list.next());
      }
      if (buchungsarten.size() > 1) {
        Buchungsart ohnezuordnung =
            (Buchungsart) Einstellungen.getDBService().createObject(Buchungsart.class, null);
        ohnezuordnung.setBezeichnung("Ohne Zuordnung");
        ohnezuordnung.setArt(-1);
        buchungsarten.add(ohnezuordnung);
      }

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir", System.getProperty("user.home"));
      if (path != null && path.length() > 0) {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("buchungen", "",
          Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0) {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      auswertungBuchungPDF(buchungsarten, file, einzelbuchungen);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private void starteCSVExport() {

    try {
      final List<Buchung> buchungen = query.get();

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir", System.getProperty("user.home"));
      if (path != null && path.length() > 0) {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("buchungen", "",
          Einstellungen.getEinstellung().getDateinamenmuster(), "CSV").get());

      final String s = fd.open();

      if (s == null || s.length() == 0) {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      BackgroundTask t = new BackgroundTask() {

        @SuppressWarnings("unused")
        @Override
        public void run(ProgressMonitor monitor) throws ApplicationException {
          try {
            new BuchungAuswertungCSV(buchungen, file, monitor);
            GUI.getCurrentView().reload();
          } catch (Exception ae) {
            Logger.error("Fehler", ae);
            GUI.getStatusBar().setErrorText(ae.getMessage());
            throw new ApplicationException(ae);
          }
        }

        @Override
        public void interrupt() {
          //
        }

        @Override
        public boolean isInterrupted() {
          return false;
        }
      };
      Application.getController().start(t);
    } catch (RemoteException e) {
      Logger.error("Fehler", e);
    }
  }

  private void starteAuswertungBuchungsjournal() {

    try {

      BuchungsjournalSortDialog djs =
          new BuchungsjournalSortDialog(BuchungsjournalSortDialog.POSITION_CENTER);
      String sort = djs.open();
      if (sort.equals(BuchungsjournalSortDialog.DATUM)) {
        query.setOrderDatumAuszugsnummerBlattnummer();
      } else if (sort.equals(BuchungsjournalSortDialog.DATUM_NAME)) {
        query.setOrderDatumName();
      } else {
        query.setOrderID();
      }
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir", System.getProperty("user.home"));
      if (path != null && path.length() > 0) {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("buchungsjournal", "",
          Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0) {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      auswertungBuchungsjournalPDF(query, file);
    } catch (Exception e) {
      Logger.error("Fehler", e);
    }
  }

  private void auswertungBuchungPDF(final ArrayList<Buchungsart> buchungsarten, final File file,
      final boolean einzelbuchungen) {
    BackgroundTask t = new BackgroundTask() {

      @SuppressWarnings("unused")
      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException {
        try {
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          new BuchungAuswertungPDF(buchungsarten, file, query, einzelbuchungen);
        } catch (ApplicationException ae) {
          Logger.error("Fehler", ae);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
      }

      @Override
      public void interrupt() {
        //
      }

      @Override
      public boolean isInterrupted() {
        return false;
      }
    };
    Application.getController().start(t);
  }

  public Settings getSettings() {
    return settings;
  }

  private void auswertungBuchungsjournalPDF(final BuchungQuery query, final File file) {
    BackgroundTask t = new BackgroundTask() {

      @SuppressWarnings("unused")
      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException {
        try {
          new BuchungsjournalPDF(query, file);
          GUI.getCurrentView().reload();
        } catch (ApplicationException ae) {
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
      }

      @Override
      public void interrupt() {
        //
      }

      @Override
      public boolean isInterrupted() {
        return false;
      }
    };
    Application.getController().start(t);
  }

  public class FilterListener implements Listener {

    FilterListener() {
    }

    @Override
    public void handleEvent(Event event) {
      if (event.type != SWT.Selection && event.type != SWT.FocusOut) {
        return;
      }

      try {
        getBuchungsList();
      } catch (RemoteException e) {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }

  }

  private void informKontoChangeListener() throws RemoteException {
    Konto k = (Konto) getSuchKonto().getValue();
    Event event = new Event();
    event.data = k;
    for (Listener listener : changeKontoListener) {
      listener.handleEvent(event);
    }
  }

  public void addKontoChangeListener(Listener listener) {
    this.changeKontoListener.add(listener);
  }

  public String getTitleBuchungsView() throws RemoteException {
    if (getBuchung().getSpeicherung())
      return "Buchung";
    return "Splitbuchung";
  }

  public boolean isBuchungAbgeschlossen() throws ApplicationException {
    try {
      if (!getBuchung().isNewObject()) {
        Jahresabschluss ja = getBuchung().getJahresabschluss();
        if (ja != null) {
          GUI.getStatusBar()
              .setErrorText(String.format("Buchung wurde bereits am %s von %s abgeschlossen.",
                  new JVDateFormatTTMMJJJJ().format(ja.getDatum()), ja.getName()));
          return true;
        }
      }
    } catch (RemoteException e) {
      throw new ApplicationException("Status der aktuellen Buchung kann nicht geprüft werden.", e);
    }
    return false;
  }

  public Action getBuchungSpeichernAction() {
    return new Action() {
      @Override
      public void handleAction(Object context) {
        buchungSpeichern();
      }
    };
  }

  private void buchungSpeichern() {
    try {
      DBTransaction.starten();
      handleStore();
      DBTransaction.commit();
      refreshSplitbuchungen();
    } catch (RemoteException e) {
      Logger.error("Fehler", e);
    } catch (ApplicationException e) {
      DBTransaction.rollback();
      GUI.getStatusBar().setErrorText(e.getLocalizedMessage());
    }
  }

  /**
   * Wird benachrichtigt um die Anzeige zu aktualisieren.
   */
  private class BuchungMessageConsumer implements MessageConsumer {

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
     */
    @Override
    public boolean autoRegister() {
      return false;
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
     */
    @Override
    public Class<?>[] getExpectedMessageTypes() {
      return new Class[] {BuchungMessage.class};
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
     */
    @Override
    public void handleMessage(final Message message) throws Exception {
      GUI.getDisplay().syncExec(new Runnable() {

        @Override
        public void run() {
          try {
            if (buchungsList == null) {
              // Eingabe-Feld existiert nicht. Also abmelden
              Application.getMessagingFactory()
                  .unRegisterMessageConsumer(BuchungMessageConsumer.this);
              return;
            }
            refreshBuchungen();
          } catch (Exception e) {
            // Wenn hier ein Fehler auftrat, deregistrieren wir uns
            // wieder
            Logger.error("unable to refresh Splitbuchungen", e);
            Application.getMessagingFactory()
                .unRegisterMessageConsumer(BuchungMessageConsumer.this);
          }
        }
      });
    }
  }

  /**
   * Wird benachrichtigt um die Anzeige zu aktualisieren.
   */
  private class SplitBuchungMessageConsumer implements MessageConsumer {

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
     */
    @Override
    public boolean autoRegister() {
      return false;
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
     */
    @Override
    public Class<?>[] getExpectedMessageTypes() {
      return new Class[] {BuchungMessage.class};
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
     */
    @Override
    public void handleMessage(final Message message) throws Exception {
      GUI.getDisplay().syncExec(new Runnable() {

        @Override
        public void run() {
          try {
            if (splitbuchungsList == null) {
              // Eingabe-Feld existiert nicht. Also abmelden
              Application.getMessagingFactory()
                  .unRegisterMessageConsumer(SplitBuchungMessageConsumer.this);
              return;
            }
            refreshSplitbuchungen();
          } catch (Exception e) {
            // Wenn hier ein Fehler auftrat, deregistrieren wir uns
            // wieder
            Logger.error("unable to refresh Splitbuchungen", e);
            Application.getMessagingFactory()
                .unRegisterMessageConsumer(SplitBuchungMessageConsumer.this);
          }
        }
      });
    }
  }

  public Input getSuchMitgliedZugeordnet() {
    if (hasmitglied != null) {
      return hasmitglied;
    }

    ArrayList<MitgliedZustand> liste = new ArrayList<>();

    MitgliedZustand ja = new MitgliedZustand(true, "Ja");
    liste.add(ja);

    MitgliedZustand nein = new MitgliedZustand(false, "Nein");
    liste.add(nein);

    MitgliedZustand beide = new MitgliedZustand(null, "Beide");
    liste.add(beide);

    String bwert = settings.getString(MITGLIEDZUGEORDNET, "Beide");
    MitgliedZustand b = ja;
    for (int i = 0; i < liste.size(); i++) {
      if (liste.get(i).getText().equals(bwert)) {
        b = liste.get(i);
        break;
      }
    }

    hasmitglied = new SelectInput(liste, b);
    hasmitglied.addListener(new FilterListener());

    return hasmitglied;
  }

  /**
   * Hilfsklasse zur Anzeige der Importer.
   */
  private class MitgliedZustand implements GenericObject, Comparable<MitgliedZustand> {

    private Boolean value = null;

    private String text = null;

    private MitgliedZustand(Boolean value, String text) {
      this.value = value;
      this.text = text;
    }

    public Boolean getValue() {
      return value;
    }

    public void setValue(Boolean value) {
      this.value = value;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String arg0) {
      return getText();
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getAttributeNames()
     */
    @Override
    public String[] getAttributeNames() {
      return new String[] {"name"};
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getID()
     */
    @Override
    public String getID() {
      String repr = "null";
      if (getValue() != null)
        Boolean.toString(getValue());

      return getText() + "#" + repr;
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
     */
    @Override
    public String getPrimaryAttribute() {
      return "name";
    }

    /**
     * @see de.willuhn.datasource.GenericObject#equals(de.willuhn.datasource.GenericObject)
     */
    @Override
    public boolean equals(GenericObject arg0) throws RemoteException {
      if (arg0 == null)
        return false;
      return this.getID().equals(arg0.getID());
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(MitgliedZustand o) {
      if (o == null) {
        return -1;
      }
      try {
        return this.getText().compareTo((o).getText());
      } catch (Exception e) {
        // Tss, dann halt nicht
      }
      return 0;
    }

  }
}
