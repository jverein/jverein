/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Queries.BuchungQuery;
import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.gui.action.SplitBuchungDetailAction;
import de.jost_net.JVerein.gui.dialogs.BuchungsjournalSortDialog;
import de.jost_net.JVerein.gui.formatter.BuchungsartFormatter;
import de.jost_net.JVerein.gui.formatter.MitgliedskontoFormatter;
import de.jost_net.JVerein.gui.input.KontoauswahlInput;
import de.jost_net.JVerein.gui.input.MitgliedskontoauswahlInput;
import de.jost_net.JVerein.gui.menu.BuchungMenu;
import de.jost_net.JVerein.gui.parts.BuchungListTablePart;
import de.jost_net.JVerein.io.BuchungAuswertungCSV;
import de.jost_net.JVerein.io.BuchungAuswertungPDFEinzelbuchungen;
import de.jost_net.JVerein.io.BuchungAuswertungPDFSummen;
import de.jost_net.JVerein.io.BuchungsjournalPDF;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungsControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart buchungsList;

  private TablePart splitbuchungsList;

  private Input id;

  private Input umsatzid;

  private DialogInput konto;

  private Input auszugsnummer;

  private Input blattnummer;

  private Input name;

  private DecimalInput betrag;

  private Input zweck;

  private Input zweck2;

  private DateInput datum = null;

  private Input art;

  private DialogInput mitgliedskonto;

  private Input kommentar;

  private SelectInput buchungsart;

  private Input suchkonto;

  private SelectInput suchbuchungsart;

  private DateInput vondatum = null;

  private DateInput bisdatum = null;

  private TextInput suchtext = null;

  private CheckboxInput verzicht;

  private Buchung buchung;

  public static final String BUCHUNGSART = "suchbuchungsart";

  private ArrayList<Buchung> splitbuchungen = null;

  public BuchungsControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Buchung getBuchung() throws RemoteException
  {
    if (buchung != null)
    {
      return buchung;
    }
    buchung = (Buchung) getCurrentObject();
    if (buchung == null)
    {
      buchung = (Buchung) Einstellungen.getDBService().createObject(
          Buchung.class, null);
    }
    return buchung;
  }

  public Input getID() throws RemoteException
  {
    if (id != null)
    {
      return id;
    }
    id = new TextInput(getBuchung().getID(), 10);
    id.setEnabled(false);
    return id;
  }

  public Input getUmsatzid() throws RemoteException
  {
    if (umsatzid != null)
    {
      return umsatzid;
    }
    Integer ui = getBuchung().getUmsatzid();
    if (ui == null)
    {
      ui = Integer.valueOf(0);
    }
    umsatzid = new IntegerInput(ui);
    umsatzid.setEnabled(false);
    return umsatzid;
  }

  public DialogInput getKonto(boolean withFocus) throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    String kontoid = settings.getString("kontoid", "");
    konto = new KontoauswahlInput(getBuchung().getKonto()).getKontoAuswahl(
        false, kontoid);
    if (withFocus)
    {
      konto.focus();
    }
    return konto;
  }

  public Input getAuszugsnummer() throws RemoteException
  {
    if (auszugsnummer != null)
    {
      return auszugsnummer;
    }
    Integer i = getBuchung().getAuszugsnummer();
    if (i == null)
    {
      i = Integer.valueOf(0);
    }
    auszugsnummer = new IntegerInput(i);
    return auszugsnummer;
  }

  public Input getBlattnummer() throws RemoteException
  {
    if (blattnummer != null)
    {
      return blattnummer;
    }
    Integer i = getBuchung().getBlattnummer();
    if (i == null)
    {
      i = Integer.valueOf(0);
    }
    blattnummer = new IntegerInput(i);
    return blattnummer;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getBuchung().getName(), 100);
    return name;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getBuchung().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    return betrag;
  }

  public Input getZweck() throws RemoteException
  {
    if (zweck != null)
    {
      return zweck;
    }
    zweck = new TextInput(getBuchung().getZweck(), 35);
    return zweck;
  }

  public Input getZweck2() throws RemoteException
  {
    if (zweck2 != null)
    {
      return zweck2;
    }
    zweck2 = new TextInput(getBuchung().getZweck2(), 35);
    return zweck2;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }
    Date d = getBuchung().getDatum();
    this.datum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.datum.setTitle("Datum");
    this.datum.setText("Bitte Datum wählen");
    this.datum.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) datum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return datum;
  }

  public TextInput getSuchtext()
  {
    if (suchtext != null)
    {
      return suchtext;
    }
    suchtext = new TextInput(settings.getString("suchtext", ""), 35);
    suchtext.addListener(new FilterListener());
    return suchtext;
  }

  public CheckboxInput getVerzicht() throws RemoteException
  {
    if (verzicht != null)
    {
      return verzicht;
    }

    Boolean vz = buchung.getVerzicht();
    if (vz == null)
    {
      vz = Boolean.FALSE;
    }
    verzicht = new CheckboxInput(vz);
    return verzicht;
  }

  public DialogInput getMitgliedskonto() throws RemoteException
  {
    mitgliedskonto = new MitgliedskontoauswahlInput(getBuchung())
        .getMitgliedskontoAuswahl();
    mitgliedskonto.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        try
        {
          String name = (String) getName().getValue();
          String zweck1 = (String) getZweck().getValue();
          String zweck2 = (String) getZweck2().getValue();
          if (mitgliedskonto.getValue() != null && name.length() == 0
              && zweck1.length() == 0 && zweck2.length() == 0)
          {
            if (mitgliedskonto.getValue() instanceof Mitgliedskonto)
            {
              Mitgliedskonto mk = (Mitgliedskonto) mitgliedskonto.getValue();
              getName().setValue(mk.getMitglied().getNameVorname());
              getBetrag().setValue(mk.getBetrag());
              getZweck().setValue(mk.getZweck1());
              getZweck2().setValue(mk.getZweck2());
              getDatum().setValue(mk.getDatum());
            }
            if (mitgliedskonto.getValue() instanceof Mitglied)
            {
              Mitglied m2 = (Mitglied) mitgliedskonto.getValue();
              getName().setValue(m2.getNameVorname());
              getDatum().setValue(new Date());
            }
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });
    return mitgliedskonto;
  }

  public Input getArt() throws RemoteException
  {
    if (art != null && !art.getControl().isDisposed())
    {
      return art;
    }
    art = new TextInput(getBuchung().getArt(), 100);
    return art;
  }

  public Input getKommentar() throws RemoteException
  {
    if (kommentar != null && !kommentar.getControl().isDisposed())
    {
      return kommentar;
    }
    kommentar = new TextInput(getBuchung().getKommentar(), 1024);
    return kommentar;
  }

  public Input getBuchungsart() throws RemoteException
  {
    if (buchungsart != null && !buchungsart.getControl().isDisposed())
    {
      return buchungsart;
    }
    DBIterator list = Einstellungen.getDBService()
        .createList(Buchungsart.class);
    list.setOrder("ORDER BY bezeichnung");
    buchungsart = new SelectInput(list, getBuchung().getBuchungsart());
    buchungsart.setValue(getBuchung().getBuchungsart());
    buchungsart.setAttribute("bezeichnung");
    buchungsart.setPleaseChoose("Bitte auswählen");
    return buchungsart;
  }

  public Input getSuchKonto() throws RemoteException
  {
    if (suchkonto != null)
    {
      return suchkonto;
    }
    String kontoid = settings.getString("suchkontoid", "");
    suchkonto = new KontoauswahlInput().getKontoAuswahl(true, kontoid);
    suchkonto.addListener(new FilterListener());
    return suchkonto;
  }

  public Input getSuchBuchungsart() throws RemoteException
  {
    if (suchbuchungsart != null)
    {
      return suchbuchungsart;
    }
    DBIterator list = Einstellungen.getDBService()
        .createList(Buchungsart.class);
    list.setOrder("ORDER BY nummer");
    ArrayList<Buchungsart> liste = new ArrayList<Buchungsart>();
    Buchungsart b1 = (Buchungsart) Einstellungen.getDBService().createObject(
        Buchungsart.class, null);
    b1.setNummer(-2);
    b1.setBezeichnung("Alle Buchungsarten");
    b1.setArt(-2);
    liste.add(b1);
    Buchungsart b2 = (Buchungsart) Einstellungen.getDBService().createObject(
        Buchungsart.class, null);
    b2.setNummer(-1);
    b2.setBezeichnung("Ohne Buchungsart");
    b2.setArt(-1);
    liste.add(b2);
    while (list.hasNext())
    {
      liste.add((Buchungsart) list.next());
    }
    int bwert = settings.getInt(BUCHUNGSART, -2);
    Buchungsart b = null;
    for (int i = 0; i < liste.size(); i++)
    {
      if (liste.get(i).getNummer() == bwert)
      {
        b = liste.get(i);
        break;
      }
    }
    suchbuchungsart = new SelectInput(liste, b);
    suchbuchungsart.addListener(new FilterListener());

    suchbuchungsart.setAttribute("bezeichnung");
    return suchbuchungsart;
  }

  public DateInput getVondatum()
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    Date d = null;
    try
    {
      d = new JVDateFormatTTMMJJJJ().parse(settings.getString("vondatum",
          "01.01.2006"));
    }
    catch (ParseException e)
    {
      //
    }
    this.vondatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.vondatum.setTitle("Anfangsdatum");
    this.vondatum.setText("Bitte Anfangsdatum wählen");
    this.vondatum.addListener(new FilterListener());
    this.vondatum.setMandatory(true);
    return vondatum;
  }

  public DateInput getBisdatum()
  {
    if (bisdatum != null)
    {
      return bisdatum;
    }
    Date d = null;
    try
    {
      d = new JVDateFormatTTMMJJJJ().parse(settings.getString("bisdatum",
          "31.12.2006"));
    }
    catch (ParseException e)
    {
      //
    }
    this.bisdatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.bisdatum.setTitle("Anfangsdatum");
    this.bisdatum.setText("Bitte Anfangsdatum wählen");
    this.bisdatum.addListener(new FilterListener());
    this.bisdatum.setMandatory(true);
    return bisdatum;
  }

  public Button getStartAuswertungEinzelbuchungenButton()
  {
    Button b = new Button("PDF Einzelbuchungen", new Action()
    {

      public void handleAction(Object context)
      {
        starteAuswertung(true);
      }
    }, null, true, "pdf.png"); // "true" defines this button as the default
    return b;
  }

  public Button getStartCSVAuswertungButton()
  {
    Button b = new Button("CSV-Export", new Action()
    {

      public void handleAction(Object context)
      {
        starteCSVExport();
      }
    }, null, true, "csv.jpg"); // "true" defines this button as the default
    return b;
  }

  public Button getStartAuswertungSummenButton()
  {
    Button b = new Button("PDF Summen", new Action()
    {

      public void handleAction(Object context)
      {
        starteAuswertung(false);
      }
    }, null, true, "pdf.png"); // "true" defines this button as the default
    return b;
  }

  public Button getStartAuswertungBuchungsjournalButton()
  {
    Button b = new Button("PDF Buchungsjournal", new Action()
    {
      public void handleAction(Object context)
      {
        starteAuswertungBuchungsjournal();
      }
    }, null, true, "pdf.png"); // "true" defines this button as the default
    return b;
  }

  public void handleStore()
  {
    try
    {
      Buchung b = getBuchung();

      GenericObject o = (GenericObject) getBuchungsart().getValue();
      try
      {
        if (o != null)
        {
          b.setBuchungsart(new Integer(o.getID()));
        }
        else
        {
          b.setBuchungsart(null);
        }
        b.setKonto((Konto) getKonto(false).getValue());
        settings.setAttribute("kontoid", b.getKonto().getID());
        b.setAuszugsnummer((Integer) getAuszugsnummer().getValue());
        b.setBlattnummer((Integer) getBlattnummer().getValue());
        b.setName((String) getName().getValue());
        b.setBetrag((Double) getBetrag().getValue());
        b.setZweck((String) getZweck().getValue());
        b.setZweck2((String) getZweck2().getValue());
        b.setDatum((Date) getDatum().getValue());
        b.setArt((String) getArt().getValue());
        b.setVerzicht((Boolean) getVerzicht().getValue());

        if (Einstellungen.getEinstellung().getMitgliedskonto())
        {
          if (mitgliedskonto.getValue() != null)
          {
            if (mitgliedskonto.getValue() instanceof Mitgliedskonto)
            {
              b.setMitgliedskonto((Mitgliedskonto) mitgliedskonto.getValue());
            }
            else if (mitgliedskonto.getValue() instanceof Mitglied)
            {
              Mitglied mitglied = (Mitglied) mitgliedskonto.getValue();
              Mitgliedskonto mk = (Mitgliedskonto) Einstellungen.getDBService()
                  .createObject(Mitgliedskonto.class, null);
              mk.setBetrag(b.getBetrag());
              mk.setDatum(b.getDatum());
              mk.setMitglied(mitglied);
              mk.setZahlungsweg(Zahlungsweg.ÜBERWEISUNG);
              mk.setZweck1(b.getZweck());
              mk.setZweck2(b.getZweck2());
              mk.store();
              b.setMitgliedskonto(mk);
              mitgliedskonto.setValue(mk);
            }
          }
          else
          {
            b.setMitgliedskonto(null);
          }
        }
        b.setKommentar((String) getKommentar().getValue());
        b.store();
        getID().setValue(b.getID());
        GUI.getStatusBar().setSuccessText("Buchung gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Buchung";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getBuchungsList() throws RemoteException
  {
    BuchungQuery query = new BuchungQuery(this);
    if (buchungsList == null)
    {
      // buchungsList = new TablePart(query.get(), new BuchungAction());
      buchungsList = new BuchungListTablePart(query.get(), new BuchungAction());
      buchungsList.addColumn("Nr", "id-int");
      buchungsList.addColumn("Konto", "konto", new Formatter()
      {

        public String format(Object o)
        {
          Konto k = (Konto) o;
          if (k != null)
          {
            try
            {
              return k.getBezeichnung();
            }
            catch (RemoteException e)
            {
              e.printStackTrace();
            }
          }
          return "";
        }
      });
      buchungsList.addColumn("Datum", "datum", new DateFormatter(
          new JVDateFormatTTMMJJJJ()));
      buchungsList.addColumn("Auszug", "auszugsnummer");
      buchungsList.addColumn("Blatt", "blattnummer");
      buchungsList.addColumn("Name", "name");
      buchungsList.addColumn("Verwendungszweck", "zweck");
      buchungsList.addColumn("Verwendungszweck 2", "zweck2");
      buchungsList.addColumn("Buchungsart", "buchungsart",
          new BuchungsartFormatter());
      buchungsList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
          Einstellungen.DECIMALFORMAT));
      if (Einstellungen.getEinstellung().getMitgliedskonto())
      {
        buchungsList.addColumn("Mitglied", "mitgliedskonto",
            new MitgliedskontoFormatter());
      }
      buchungsList.setMulti(true);
      buchungsList.setContextMenu(new BuchungMenu(this));
      buchungsList.setRememberColWidths(true);
      buchungsList.setRememberOrder(true);
      buchungsList.setSummary(true);
    }
    else
    {
      buchungsList.removeAll();

      for (Buchung bu : query.get())
      {
        buchungsList.addItem(bu);
      }
      buchungsList.sort();
    }
    return buchungsList;
  }

  public Part getSplitBuchungsList() throws RemoteException
  {
    if (splitbuchungsList == null)
    {
      splitbuchungen = new ArrayList<Buchung>();
      Buchung b = (Buchung) getCurrentObject();
      // Wenn eine gesplittete Buchung aufgerufen wird, wird die Hauptbuchung
      // gelesen
      if (b.getSplitId() != null)
      {
        b = (Buchung) Einstellungen.getDBService().createObject(Buchung.class,
            b.getSplitId() + "");
      }
      DBIterator it = Einstellungen.getDBService().createList(Buchung.class);
      it.addFilter("id = ? or splitid = ?", b.getID(), b.getID());
      while (it.hasNext())
      {
        splitbuchungen.add((Buchung) it.next());
      }
      it = Einstellungen.getDBService().createList(Buchung.class);
      it.addFilter("(id = ? or splitid = ?)", b.getID(), b.getID());
      it.addFilter("betrag = ?", b.getBetrag() * -1);

      if (!it.hasNext())
      {
        Buchung b2 = (Buchung) Einstellungen.getDBService().createObject(
            Buchung.class, null);
        b2.setArt(b.getArt());
        b2.setAuszugsnummer(b.getAuszugsnummer());
        b2.setBetrag(b.getBetrag() * -1);
        b2.setBlattnummer(b.getBlattnummer());
        b2.setBuchungsart(b.getBuchungsartId());
        b2.setDatum(b.getDatum());
        b2.setKommentar(b.getKommentar());
        b2.setKonto(b.getKonto());
        b2.setMitgliedskonto(b.getMitgliedskonto());
        b2.setName(b.getName());
        b2.setSplitId(Integer.parseInt(b.getID()));
        b2.setUmsatzid(b.getUmsatzid());
        b2.setZweck(b.getZweck());
        b2.setZweck2(b.getZweck2());
        splitbuchungen.add(b2);
      }
      splitbuchungsList = new TablePart(splitbuchungen,
          new SplitBuchungDetailAction(this, this.view));
      splitbuchungsList.addColumn("Nr", "id-int");
      splitbuchungsList.addColumn("Konto", "konto", new Formatter()
      {
        public String format(Object o)
        {
          Konto k = (Konto) o;
          if (k != null)
          {
            try
            {
              return k.getBezeichnung();
            }
            catch (RemoteException e)
            {
              e.printStackTrace();
            }
          }
          return "";
        }
      });
      splitbuchungsList.addColumn("Datum", "datum", new DateFormatter(
          new JVDateFormatTTMMJJJJ()));
      splitbuchungsList.addColumn("Auszug", "auszugsnummer");
      splitbuchungsList.addColumn("Blatt", "blattnummer");
      splitbuchungsList.addColumn("Name", "name");
      splitbuchungsList.addColumn("Verwendungszweck", "zweck");
      splitbuchungsList.addColumn("Verwendungszweck 2", "zweck2");
      splitbuchungsList.addColumn("Buchungsart", "buchungsart",
          new BuchungsartFormatter());
      splitbuchungsList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
          Einstellungen.DECIMALFORMAT));
      if (Einstellungen.getEinstellung().getMitgliedskonto())
      {
        splitbuchungsList.addColumn("Mitglied", "mitgliedskonto",
            new MitgliedskontoFormatter());
      }
      // buchungsList.setContextMenu(new BuchungMenu(this));
      splitbuchungsList.setRememberColWidths(true);
      splitbuchungsList.setSummary(true);
    }
    else
    {
      refreshSplitbuchungen();
    }
    return splitbuchungsList;
  }

  public void refreshSplitbuchungen() throws RemoteException
  {
    splitbuchungsList.removeAll();

    try
    {
      for (Buchung b : splitbuchungen)
      {
        splitbuchungsList.addItem(b);
      }
    }
    catch (RemoteException e)
    {
      throw e;
    }
  }

  public String getDifference(Buchung b2) throws RemoteException
  {
    Double summe = b2.getBetrag();
    for (Buchung b : splitbuchungen)
    {
      summe += b.getBetrag();
    }
    return Einstellungen.DECIMALFORMAT.format(summe);
  }

  private void starteAuswertung(boolean einzelbuchungen)
  {

    try
    {
      DBIterator list;
      BuchungQuery query = new BuchungQuery(this);
      Date dVon = query.getDatumvon();
      Date dBis = query.getDatumbis();
      Konto k = query.getKonto();
      Buchungsart ba = query.getBuchungart();
      list = Einstellungen.getDBService().createList(Buchungsart.class);
      if (ba != null && ba.getArt() != -2)
      {
        list.addFilter("id = ?", new Object[] { ba.getID() });
      }
      list.setOrder("ORDER BY nummer");

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("buchungen", "", Einstellungen
          .getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      auswertungBuchungPDF(list, file, k, ba, dVon, dBis, einzelbuchungen);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private void starteCSVExport()
  {

    try
    {
      BuchungQuery query = new BuchungQuery(this);
      final ArrayList<Buchung> buchungen = query.get();

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("buchungen", "", Einstellungen
          .getEinstellung().getDateinamenmuster(), "CSV").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      BackgroundTask t = new BackgroundTask()
      {

        public void run(ProgressMonitor monitor) throws ApplicationException
        {
          try
          {
            new BuchungAuswertungCSV(buchungen, file, monitor);
            monitor.setPercentComplete(100);
            monitor.setStatus(ProgressMonitor.STATUS_DONE);
            GUI.getStatusBar().setSuccessText("Auswertung gestartet");
            GUI.getCurrentView().reload();
          }
          catch (Exception ae)
          {
            monitor.setStatusText(ae.getMessage());
            monitor.setStatus(ProgressMonitor.STATUS_ERROR);
            GUI.getStatusBar().setErrorText(ae.getMessage());
            ae.printStackTrace();
            throw new ApplicationException(ae);
          }
        }

        public void interrupt()
        {
          //
        }

        public boolean isInterrupted()
        {
          return false;
        }
      };
      Application.getController().start(t);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private void starteAuswertungBuchungsjournal()
  {
    DBIterator list;
    Date dVon = null;
    Date dBis = null;
    Konto k = null;
    if (bisdatum.getValue() != null)
    {
      dVon = (Date) vondatum.getValue();
    }
    if (bisdatum.getValue() != null)
    {
      dBis = (Date) bisdatum.getValue();
    }
    if (suchkonto.getValue() != null)
    {
      k = (Konto) suchkonto.getValue();
    }

    try
    {
      list = Einstellungen.getDBService().createList(Buchung.class);
      if (dVon != null)
      {
        list.addFilter("datum >= ?", new Object[] { dVon });
      }
      if (dBis != null)
      {
        list.addFilter("datum <= ?", new Object[] { dBis });
      }
      if (k != null)
      {
        list.addFilter("konto = ?", new Object[] { k.getID() });
      }

      BuchungsjournalSortDialog djs = new BuchungsjournalSortDialog(
          BuchungsjournalSortDialog.POSITION_CENTER);
      String sort = (String) djs.open();
      if (sort.equals(BuchungsjournalSortDialog.DATUM))
      {
        list.setOrder("ORDER BY datum, auszugsnummer, blattnummer, id");
      }
      else if (sort.equals(BuchungsjournalSortDialog.DATUM_NAME))
      {
        list.setOrder("ORDER BY datum, name, id");
      }
      else
      {
        list.setOrder("ORDER BY id");
      }
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("buchungsjournal", "", Einstellungen
          .getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      auswertungBuchungsjournalPDF(list, file, k, dVon, dBis);
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
    }
  }

  private void auswertungBuchungPDF(final DBIterator list, final File file,
      final Konto konto, final Buchungsart buchungsart, final Date dVon,
      final Date dBis, final boolean einzelbuchungen)
  {
    BackgroundTask t = new BackgroundTask()
    {

      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          if (einzelbuchungen)
          {
            new BuchungAuswertungPDFEinzelbuchungen(list, file, monitor, konto,
                buchungsart, dVon, dBis);
          }
          else
          {
            new BuchungAuswertungPDFSummen(list, file, monitor, konto,
                buchungsart, dVon, dBis);
          }
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
      }

      public void interrupt()
      {
        //
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  public Settings getSettings()
  {
    return settings;
  }

  private void auswertungBuchungsjournalPDF(final DBIterator list,
      final File file, final Konto konto, final Date dVon, final Date dBis)
  {
    BackgroundTask t = new BackgroundTask()
    {

      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new BuchungsjournalPDF(list, file, monitor, konto, dVon, dBis);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
      }

      public void interrupt()
      {
        //
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  public class FilterListener implements Listener
  {

    FilterListener()
    {
    }

    public void handleEvent(Event event)
    {
      if (event.type != SWT.Selection && event.type != SWT.FocusOut)
      {
        return;
      }

      try
      {
        getBuchungsList();
      }
      catch (RemoteException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
  }

}
