/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 * Revision 1.2  2006/09/25 19:04:27  jost
 * Bugfix Datumvon und Datumbis
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.io.BuchungAuswertungPDF;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
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
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
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

  private Input id;

  private Input umsatzid;

  private Input konto;

  private Input name;

  private DecimalInput betrag;

  private Input zweck;

  private Input zweck2;

  private DateInput datum = null;

  private DecimalInput saldo;

  private Input art;

  private Input kommentar;

  private SelectInput buchungsart;

  private DateInput vondatum = null;

  private DateInput bisdatum = null;

  private Buchung buchung;

  public BuchungsControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Buchung getBuchung()
  {
    if (buchung != null)
    {
      return buchung;
    }
    buchung = (Buchung) getCurrentObject();
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
    umsatzid = new TextInput(getBuchung().getUmsatzid(), 10);
    umsatzid.setEnabled(false);
    return umsatzid;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(getBuchung().getKonto(), 10);
    konto.setEnabled(false);
    return konto;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getBuchung().getName(), 100);
    name.setEnabled(false);
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
    betrag.setEnabled(false);
    return betrag;
  }

  public Input getZweck() throws RemoteException
  {
    if (zweck != null)
    {
      return zweck;
    }
    zweck = new TextInput(getBuchung().getZweck(), 35);
    zweck.setEnabled(false);
    return zweck;
  }

  public Input getZweck2() throws RemoteException
  {
    if (zweck2 != null)
    {
      return zweck2;
    }
    zweck2 = new TextInput(getBuchung().getZweck2(), 35);
    zweck2.setEnabled(false);
    return zweck2;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }
    Date d = getBuchung().getDatum();
    this.datum = new DateInput(d, Einstellungen.DATEFORMAT);
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
    datum.setEnabled(false);
    return datum;
  }

  public DecimalInput getSaldo() throws RemoteException
  {
    if (saldo != null)
    {
      return saldo;
    }
    saldo = new DecimalInput(getBuchung().getSaldo(),
        Einstellungen.DECIMALFORMAT);
    saldo.setEnabled(false);
    return saldo;
  }

  public Input getArt() throws RemoteException
  {
    if (art != null)
    {
      return art;
    }
    art = new TextInput(getBuchung().getArt(), 100);
    art.setEnabled(false);
    return art;
  }

  public Input getKommentar() throws RemoteException
  {
    if (kommentar != null)
    {
      return kommentar;
    }
    kommentar = new TextInput(getBuchung().getKommentar(), 1024);
    kommentar.setEnabled(false);
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
    list.setOrder("ORDER BY nummer");
    buchungsart = new SelectInput(list, getBuchung().getBuchungsart());
    buchungsart.setValue(getBuchung().getBuchungsart());
    buchungsart.setAttribute("bezeichnung");
    buchungsart.setPleaseChoose("Bitte auswählen");
    return buchungsart;
  }

  public DateInput getVondatum() throws RemoteException
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    Date d = null;
    try
    {
      d = Einstellungen.DATEFORMAT.parse(settings.getString("vondatum",
          "01.01.2006"));
    }
    catch (ParseException e)
    {
      //
    }
    this.vondatum = new DateInput(d, Einstellungen.DATEFORMAT);
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
        settings
            .setAttribute("vondatum", Einstellungen.DATEFORMAT.format(date));
      }
    });
    return vondatum;
  }

  public DateInput getBisdatum() throws RemoteException
  {
    if (bisdatum != null)
    {
      return bisdatum;
    }
    Date d = null;
    try
    {
      d = Einstellungen.DATEFORMAT.parse(settings.getString("bisdatum",
          "31.12.2006"));
    }
    catch (ParseException e)
    {
      //
    }
    this.bisdatum = new DateInput(d, Einstellungen.DATEFORMAT);
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
        settings
            .setAttribute("bisdatum", Einstellungen.DATEFORMAT.format(date));
      }
    });
    return bisdatum;
  }

  public Button getStartAuswertungButton()
  {
    Button b = new Button("Start", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        starteAuswertung();
      }
    }, null, true); // "true" defines this button as the default button
    return b;
  }

  public void handleStore()
  {
    try
    {
      Buchung b = getBuchung();

      GenericObject o = (GenericObject) getBuchungsart().getValue();
      b.setBuchungsart(new Integer(o.getID()));
      try
      {
        b.store();
        GUI.getStatusBar().setSuccessText("Buchung gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getView().setErrorText(e.getMessage());
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
    buchungen.addFilter("datum >= ?", new Object[] { vd });
    buchungen.addFilter("datum <= ?", new Object[] { bd });
    buchungen.setOrder("ORDER BY umsatzid DESC");

    if (buchungsList == null)
    {
      buchungsList = new TablePart(buchungen, new BuchungAction());
      buchungsList.addColumn("Nr", "id");
      buchungsList.addColumn("Datum", "datum", new DateFormatter(
          Einstellungen.DATEFORMAT));
      buchungsList.addColumn("Name", "name");
      buchungsList.addColumn("Verwendungszweck", "zweck");
      buchungsList.addColumn("Verwendungszweck 2", "zweck2");
      buchungsList.addColumn("Buchungsart", "buchungsart", new Formatter()
      {
        public String format(Object o)
        {
          Buchungsart ba = (Buchungsart) o;
          if (ba == null)
          {
            return null;
          }
          String bez = null;
          try
          {
            bez = ba.getBezeichnung();
          }
          catch (RemoteException e)
          {
            e.printStackTrace();
          }
          return bez;
        }
      });
      buchungsList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
          Einstellungen.DECIMALFORMAT));
      buchungsList.setRememberColWidths(true);
      buchungsList.setRememberOrder(true);
      buchungsList.setSummary(true);
    }
    else
    {
      buchungsList.removeAll();
      while (buchungen.hasNext())
      {
        buchungsList.addItem((Buchung) buchungen.next());
      }
    }
    return buchungsList;
  }

  private void starteAuswertung()
  {
    DBIterator list;
    Date dVon = null;
    Date dBis = null;
    if (bisdatum.getValue() != null)
    {
      dVon = (Date) vondatum.getValue();
    }
    if (bisdatum.getValue() != null)
    {
      dBis = (Date) bisdatum.getValue();
    }
    try
    {
      list = Einstellungen.getDBService().createList(Buchungsart.class);
      list.setOrder("ORDER BY nummer");

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      Settings settings = new Settings(this.getClass());

      String path = settings.getString("lastdir", System
          .getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        return;
      }

      final File file = new File(s);

      auswertungBuchungPDF(list, file, dVon, dBis);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private void auswertungBuchungPDF(final DBIterator list, final File file,
      final Date dVon, final Date dBis)
  {
    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new BuchungAuswertungPDF(list, file, monitor, dVon, dBis);
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
        catch (RemoteException re)
        {
          monitor.setStatusText(re.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(re.getMessage());
          throw new ApplicationException(re);
        }
      }

      public void interrupt()
      {
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

}
