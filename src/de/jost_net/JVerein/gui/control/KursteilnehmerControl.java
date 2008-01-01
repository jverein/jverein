/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.5  2007/12/01 10:05:34  jost
 * Ã„nderung wg. neuem Classloader in Jameica
 *
 * Revision 1.4  2007/05/26 16:26:09  jost
 * Neu: Auswertung Kursteilnehmer
 *
 * Revision 1.3  2007/03/21 12:11:22  jost
 * Neu: Abbuchungsdatum beim Kursteilnehmer kann zurÃ¼ckgesetzt werden.
 *
 * Revision 1.2  2007/03/10 13:41:08  jost
 * Redaktionelle Ã„nderung
 *
 * Revision 1.1  2007/02/25 19:12:29  jost
 * Neu: Kursteilnehmer
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.lowagie.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.menu.KursteilnehmerMenu;
import de.jost_net.JVerein.io.Reporter;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
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

public class KursteilnehmerControl extends AbstractControl
{

  private Input name;

  private DecimalInput betrag;

  private Input vzweck1;

  private Input vzweck2;

  private Input blz;

  private Input konto;

  private DateInput geburtsdatum = null;

  private SelectInput geschlecht;

  private Kursteilnehmer ktn;

  // Elemente für die Auswertung

  private DateInput abbuchungsdatumvon = null;

  private DateInput abbuchungsdatumbis = null;

  public KursteilnehmerControl(AbstractView view)
  {
    super(view);
  }

  private Kursteilnehmer getKursteilnehmer()
  {
    if (ktn != null)
    {
      return ktn;
    }
    ktn = (Kursteilnehmer) getCurrentObject();
    return ktn;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getKursteilnehmer().getName(), 27);
    name.setMandatory(true);
    return name;
  }

  public Input getVZweck1() throws RemoteException
  {
    if (vzweck1 != null)
    {
      return vzweck1;
    }
    vzweck1 = new TextInput(getKursteilnehmer().getVZweck1(), 27);
    vzweck1.setMandatory(true);
    return vzweck1;
  }

  public Input getVZweck2() throws RemoteException
  {
    if (vzweck2 != null)
    {
      return vzweck2;
    }
    vzweck2 = new TextInput(getKursteilnehmer().getVZweck2(), 27);
    return vzweck2;
  }

  public Input getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(getKursteilnehmer().getBlz(), 8);
    blz.setMandatory(true);
    BLZListener l = new BLZListener();
    blz.addListener(l);
    l.handleEvent(null); // Einmal initial ausfuehren
    return blz;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(getKursteilnehmer().getKonto(), 10);
    konto.setMandatory(true);
    return konto;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getKursteilnehmer().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    betrag.setMandatory(true);
    return betrag;
  }

  public DateInput getGeburtsdatum() throws RemoteException
  {
    if (geburtsdatum != null)
    {
      return geburtsdatum;
    }
    Date d = getKursteilnehmer().getGeburtsdatum();
    this.geburtsdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatum.setTitle("Geburtsdatum");
    this.geburtsdatum.setText("Bitte Geburtsdatum wählen");
    this.geburtsdatum.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    this.geburtsdatum.setMandatory(true);
    return geburtsdatum;
  }

  public SelectInput getGeschlecht() throws RemoteException
  {
    if (geschlecht != null)
    {
      return geschlecht;
    }
    geschlecht = new SelectInput(new String[] { "m", "w" }, getKursteilnehmer()
        .getGeschlecht());
    geschlecht.setPleaseChoose("Bitte auswählen");
    geschlecht.setMandatory(true);
    return geschlecht;
  }

  public DateInput getAbbuchungsdatumvon() throws RemoteException
  {
    if (abbuchungsdatumvon != null)
    {
      return abbuchungsdatumvon;
    }
    Date d = null;

    this.abbuchungsdatumvon = new DateInput(d, Einstellungen.DATEFORMAT);
    this.abbuchungsdatumvon.setTitle("Abbuchungsdatum");
    this.abbuchungsdatumvon.setText("Beginn des Abbuchungszeitraumes");
    this.abbuchungsdatumvon.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) abbuchungsdatumvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return abbuchungsdatumvon;
  }

  public DateInput getAbbuchungsdatumbis() throws RemoteException
  {
    if (abbuchungsdatumbis != null)
    {
      return abbuchungsdatumbis;
    }
    Date d = null;

    this.abbuchungsdatumbis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.abbuchungsdatumbis.setTitle("Abbuchungsdatum");
    this.abbuchungsdatumbis.setText("Ende des Abbuchungszeitraumes");
    this.abbuchungsdatumbis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) abbuchungsdatumbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return abbuchungsdatumbis;
  }

  public Part getKursteilnehmerTable(TablePart part) throws RemoteException
  {
    if (part != null)
    {
      return part;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator kursteilnehmer = service.createList(Kursteilnehmer.class);
    part = new TablePart(kursteilnehmer, new KursteilnehmerDetailAction());

    part.addColumn("Name", "name");
    part.addColumn("VZweck 1", "vzweck1");
    part.addColumn("BLZ", "blz");
    part.addColumn("Konto", "konto");
    part.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    part.addColumn("Eingabedatum", "eingabedatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    part.addColumn("Abbuchungsdatum", "abbudatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    part.setContextMenu(new KursteilnehmerMenu(part));

    return part;
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
      Kursteilnehmer k = getKursteilnehmer();
      k.setName((String) getName().getValue());
      k.setVZweck1((String) getVZweck1().getValue());
      k.setVZweck2((String) getVZweck2().getValue());
      k.setBlz((String) getBlz().getValue());
      k.setKonto((String) getKonto().getValue());
      k.setBetrag((Double) getBetrag().getValue());
      k.setGeburtsdatum((Date) getGeburtsdatum().getValue());
      if (k.getID() == null)
      {
        k.setEingabedatum();
      }
      k.store();
      GUI.getStatusBar().setSuccessText("Kursteilnehmer gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei Speichern des Kursteilnehmers";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  /**
   * Sucht das Geldinstitut zur eingegebenen BLZ und zeigt es als Kommentar
   * hinter dem BLZ-Feld an.
   */
  private class BLZListener implements Listener
  {
    public void handleEvent(Event event)
    {
      try
      {
        String blz = (String) getBlz().getValue();
        getBlz().setComment(Einstellungen.getNameForBLZ(blz));
      }
      catch (RemoteException e)
      {
        Logger.error("error while updating blz comment", e);
      }
    }
  }

  private void starteAuswertung()
  {
    // Alle Kursteilnehmer lesen
    final DBIterator list;
    try
    {
      String subtitle = "";
      list = Einstellungen.getDBService().createList(Kursteilnehmer.class);
      if (abbuchungsdatumvon.getValue() != null)
      {
        Date d = (Date) abbuchungsdatumvon.getValue();
        subtitle += "Abbuchungsdatum von " + Einstellungen.DATEFORMAT.format(d)
            + "  ";
        list.addFilter("abbudatum >= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      if (abbuchungsdatumbis.getValue() != null)
      {
        Date d = (Date) abbuchungsdatumbis.getValue();
        subtitle += " bis " + Einstellungen.DATEFORMAT.format(d) + "  ";
        list.addFilter("abbudatum <= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      Settings settings = new Settings(this.getClass());

      String path = settings.getString("lastdir", System
          .getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("kursteilnehmer", Einstellungen
          .getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        // close();
        return;
      }

      final File file = new File(s);
      final String subtitle2 = subtitle;

      BackgroundTask t = new BackgroundTask()
      {
        public void run(ProgressMonitor monitor) throws ApplicationException
        {
          try
          {
            Reporter rpt = new Reporter(new FileOutputStream(file), monitor,
                "Kursteilnehmer", subtitle2, list.size());

            monitor.setPercentComplete(100);
            monitor.setStatus(ProgressMonitor.STATUS_DONE);
            GUI.getStatusBar().setSuccessText("Auswertung gestartet");
            GUI.getCurrentView().reload();

            rpt.addHeaderColumn("Datum", Element.ALIGN_LEFT, 50,
                Color.LIGHT_GRAY);
            rpt.addHeaderColumn("Name", Element.ALIGN_LEFT, 80,
                Color.LIGHT_GRAY);
            rpt.addHeaderColumn("Verwendungszweck", Element.ALIGN_LEFT, 80,
                Color.LIGHT_GRAY);
            rpt.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 40,
                Color.LIGHT_GRAY);
            rpt.createHeader();
            while (list.hasNext())
            {
              Kursteilnehmer kt = (Kursteilnehmer) list.next();
              rpt.addColumn(rpt.getDetailCell(kt.getAbbudatum(),
                  Element.ALIGN_LEFT));
              rpt
                  .addColumn(rpt
                      .getDetailCell(kt.getName(), Element.ALIGN_LEFT));
              rpt.addColumn(rpt.getDetailCell(kt.getVZweck1() + "\n"
                  + kt.getVZweck2(), Element.ALIGN_LEFT));
              rpt.addColumn(rpt.getDetailCell(kt.getBetrag()));
              rpt.setNextRecord();
            }
            rpt.close();

          }
          catch (ApplicationException ae)
          {
            monitor.setStatusText(ae.getMessage());
            monitor.setStatus(ProgressMonitor.STATUS_ERROR);
            GUI.getStatusBar().setErrorText(ae.getMessage());
            throw ae;
          }
          catch (Exception re)
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
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
}
