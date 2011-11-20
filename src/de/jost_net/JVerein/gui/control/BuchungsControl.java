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
import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.gui.dialogs.BuchungsjournalSortDialog;
import de.jost_net.JVerein.gui.formatter.BuchungsartFormatter;
import de.jost_net.JVerein.gui.formatter.MitgliedskontoFormatter;
import de.jost_net.JVerein.gui.input.KontoauswahlInput;
import de.jost_net.JVerein.gui.input.MitgliedskontoauswahlInput;
import de.jost_net.JVerein.gui.menu.BuchungMenu;
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
import de.willuhn.datasource.rmi.DBService;
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
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungsControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart buchungsList;

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

  private static final String BUCHUNGSART = "buchungsart";

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
    return suchtext;
  }


  public CheckboxInput getVerzicht() throws RemoteException {

    if ( verzicht != null ) {
        return verzicht;
    }

    Boolean vz = buchung.getVerzicht();

    if ( vz == null ) {
        vz = Boolean.FALSE;
    }

    verzicht = new CheckboxInput( vz );

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
            Mitgliedskonto mk = (Mitgliedskonto) mitgliedskonto.getValue();
            getName().setValue(mk.getMitglied().getNameVorname());
            getBetrag().setValue(mk.getBetrag());
            getZweck().setValue(mk.getZweck1());
            getZweck2().setValue(mk.getZweck2());
            getDatum().setValue(mk.getDatum());
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
    if (art != null)
    {
      return art;
    }
    art = new TextInput(getBuchung().getArt(), 100);
    return art;
  }

  public Input getKommentar() throws RemoteException
  {
    if (kommentar != null)
    {
      return kommentar;
    }
    kommentar = new TextInput(getBuchung().getKommentar(), 1024);
    return kommentar;
  }

  public Input getBuchungsart() throws RemoteException
  {
    if (buchungsart != null)
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
    Buchungsart b = (Buchungsart) Einstellungen.getDBService().createObject(
        Buchungsart.class, null);
    b.setArt(-2);
    b.setBezeichnung("---");
    b.setNummer(-2);
    liste.add(b);
    b = (Buchungsart) Einstellungen.getDBService().createObject(
        Buchungsart.class, null);
    b.setArt(-1);
    b.setBezeichnung("Ohne Buchungsart");
    b.setNummer(-1);
    liste.add(b);
    while (list.hasNext())
    {
      liste.add((Buchungsart) list.next());
    }
    int bwert = settings.getInt(BUCHUNGSART, -2);
    for (int i = 0; i < liste.size(); i++)
    {
      if (liste.get(i).getArt() == bwert)
      {
        b = liste.get(i);
        break;
      }
    }
    suchbuchungsart = new SelectInput(liste, b);
    suchbuchungsart.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Buchungsart ba = (Buchungsart) suchbuchungsart.getValue();
        try
        {
          settings.setAttribute(BUCHUNGSART, ba.getArt());
        }
        catch (RemoteException e)
        {
          // e.printStackTrace();
        }
      }
    });

    suchbuchungsart.setAttribute("bezeichnung");
    // suchbuchungsart.setPleaseChoose("Ohne Buchungsart");
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
    this.vondatum.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) vondatum.getValue();
        if (date == null)
        {
          return;
        }
        settings.setAttribute("vondatum",
            new JVDateFormatTTMMJJJJ().format(date));
      }
    });
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
    this.bisdatum.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) bisdatum.getValue();
        if (date == null)
        {
          return;
        }
        settings.setAttribute("bisdatum",
            new JVDateFormatTTMMJJJJ().format(date));
      }
    });
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
    // button
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
    // button
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
    // button
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
        b.setVerzicht( (Boolean) getVerzicht().getValue() );

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
    DBService service = Einstellungen.getDBService();
    DBIterator buchungen = service.createList(Buchung.class);
    Date d1 = (Date) vondatum.getValue();
    java.sql.Date vd = new java.sql.Date(d1.getTime());
    d1 = (Date) bisdatum.getValue();
    java.sql.Date bd = new java.sql.Date(d1.getTime());

    Konto k = null;
    if (suchkonto.getValue() != null)
    {
      k = (Konto) suchkonto.getValue();
      settings.setAttribute("suchkontoid", k.getID());
    }
    else
    {
      settings.setAttribute("suchkontoid", "");
    }

    Buchungsart b = null;
    if (suchbuchungsart != null)
    {
      b = (Buchungsart) suchbuchungsart.getValue();
    }
    buchungen.addFilter("datum >= ?", new Object[] { vd });
    buchungen.addFilter("datum <= ?", new Object[] { bd });
    if (k != null)
    {
      buchungen.addFilter("konto = ?", new Object[] { k.getID() });
    }
    if (b != null)
    {
      if (b.getArt() == -1)
      {
        buchungen.addFilter("buchungsart is null");
      }
      else if (b.getArt() >= 0)
      {
        buchungen.addFilter("buchungsart = ?", new Object[] { b.getID() });
      }
    }
    String sute = (String) getSuchtext().getValue();
    if (sute.length() > 0)
    {
      settings.setAttribute("suchtext", sute);
      sute = sute.toUpperCase();
      sute = "%" + sute + "%";
      buchungen
          .addFilter(
              "(upper(name) like ? or upper(zweck) like ? or upper(zweck2) like ?)",
              new Object[] { sute, sute, sute });
    }
    else
    {
      settings.setAttribute("suchtext", "");

    }
    buchungen.setOrder("ORDER BY umsatzid DESC");

    if (buchungsList == null)
    {
      buchungsList = new TablePart(buchungen, new BuchungAction());
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
      while (buchungen.hasNext())
      {
        buchungsList.addItem(buchungen.next());
      }
      buchungsList.sort();
    }
    return buchungsList;
  }

  private void starteAuswertung(boolean einzelbuchungen)
  {
    DBIterator list;
    Date dVon = null;
    Date dBis = null;
    Konto k = null;
    Buchungsart ba = null;
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
    if (suchbuchungsart.getValue() != null)
    {
      ba = (Buchungsart) suchbuchungsart.getValue();
    }

    try
    {
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
      fd.setFileName(new Dateiname("buchungen", Einstellungen.getEinstellung()
          .getDateinamenmuster(), "PDF").get());

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
      fd.setFileName(new Dateiname("buchungsjournal", Einstellungen
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

}
