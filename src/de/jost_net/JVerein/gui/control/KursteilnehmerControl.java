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

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.lowagie.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.gui.menu.KursteilnehmerMenu;
import de.jost_net.JVerein.io.Reporter;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
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
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.messaging.StatusBarMessage;
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

  private GeschlechtInput geschlecht;

  private Kursteilnehmer ktn;

  private TablePart part;

  // Elemente für die Auswertung

  private DateInput eingabedatumvon = null;

  private DateInput eingabedatumbis = null;

  private DateInput abbuchungsdatumvon = null;

  private DateInput abbuchungsdatumbis = null;

  private TextInput suchname = null;

  private Settings settings = null;

  public KursteilnehmerControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
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

  public Input getName(boolean withFocus) throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getKursteilnehmer().getName(), 27);
    name.setMandatory(true);
    if (withFocus)
    {
      name.focus();
    }
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
    this.geburtsdatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
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

  public GeschlechtInput getGeschlecht() throws RemoteException
  {
    if (geschlecht != null)
    {
      return geschlecht;
    }
    geschlecht = new GeschlechtInput(getKursteilnehmer().getGeschlecht());
    geschlecht.setPleaseChoose("Bitte auswählen");
    geschlecht.setMandatory(true);
    return geschlecht;
  }

  public TextInput getSuchname()
  {
    if (suchname != null)
    {
      return suchname;
    }
    String tmp = settings.getString("name", "");
    this.suchname = new TextInput(tmp, 30);
    suchname.addListener(new FilterListener());
    return suchname;
  }

  public DateInput getEingabedatumvon()
  {
    if (eingabedatumvon != null)
    {
      return eingabedatumvon;
    }
    Date d = null;
    String tmp = settings.getString("eingabedatum.von", null);
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
    this.eingabedatumvon = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.eingabedatumvon.setTitle("Eingabedatum");
    this.eingabedatumvon.setText("Beginn des Eingabe-Zeitraumes");
    this.eingabedatumvon.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) eingabedatumvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    eingabedatumvon.addListener(new FilterListener());
    return eingabedatumvon;
  }

  public DateInput getEingabedatumbis()
  {
    if (eingabedatumbis != null)
    {
      return eingabedatumbis;
    }
    Date d = null;
    String tmp = settings.getString("eingabedatum.bis", null);
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
    this.eingabedatumbis = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.eingabedatumbis.setTitle("Eingabedatum");
    this.eingabedatumbis.setText("Ende des Eingabe-Zeitraumes");
    this.eingabedatumbis.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) eingabedatumbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    eingabedatumbis.addListener(new FilterListener());
    return eingabedatumbis;
  }

  public DateInput getAbbuchungsdatumvon()
  {
    if (abbuchungsdatumvon != null)
    {
      return abbuchungsdatumvon;
    }
    Date d = null;
    String tmp = settings.getString("abbuchungsdatum.von", null);
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
    this.abbuchungsdatumvon = new DateInput(d, new JVDateFormatTTMMJJJJ());
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

  public DateInput getAbbuchungsdatumbis()
  {
    if (abbuchungsdatumbis != null)
    {
      return abbuchungsdatumbis;
    }
    Date d = null;
    String tmp = settings.getString("abbuchungsdatum.bis", null);
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
    this.abbuchungsdatumbis = new DateInput(d, new JVDateFormatTTMMJJJJ());
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

  public Part getKursteilnehmerTable() throws RemoteException
  {
    saveDefaults();

    DBIterator kursteilnehmer = getIterator();
    part = new TablePart(kursteilnehmer, new KursteilnehmerDetailAction());

    part.addColumn("Name", "name");
    part.addColumn("VZweck 1", "vzweck1");
    part.addColumn("VZweck 2", "vzweck2");
    part.addColumn("BLZ", "blz");
    part.addColumn("Konto", "konto");
    part.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    part.addColumn("Eingabedatum", "eingabedatum", new DateFormatter(
        new JVDateFormatTTMMJJJJ()));
    part.addColumn("Abbuchungsdatum", "abbudatum", new DateFormatter(
        new JVDateFormatTTMMJJJJ()));
    part.setContextMenu(new KursteilnehmerMenu(part));

    return part;
  }

  private void refresh()
  {

    try
    {
      saveDefaults();
      if (part == null)
      {
        return;
      }
      part.removeAll();
      DBIterator kursteilnehmer = getIterator();
      while (kursteilnehmer.hasNext())
      {
        Kursteilnehmer kt = (Kursteilnehmer) kursteilnehmer.next();
        part.addItem(kt);
      }
    }
    catch (RemoteException e1)
    {
      e1.printStackTrace();
    }
  }

  public Button getStartAuswertungButton()
  {
    Button b = new Button("starten", new Action()
    {

      public void handleAction(Object context)
      {
        starteAuswertung();
      }
    }, null, true, "go.png"); // "true" defines this button as the default
    // button
    return b;
  }

  /**
   * Default-Werte speichern.
   * 
   * @throws RemoteException
   */
  public void saveDefaults()
  {
    if (this.suchname != null)
    {
      settings.setAttribute("name", (String) getSuchname().getValue());
    }

    if (this.eingabedatumvon != null)
    {
      Date tmp = (Date) getEingabedatumvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("eingabedatum.von",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("eingabedatum.von", "");
      }
    }

    if (this.eingabedatumbis != null)
    {
      Date tmp = (Date) getEingabedatumbis().getValue();
      if (tmp != null)
      {
        settings.setAttribute("eingabedatum.bis",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("eingabedatum.bis", "");
      }
    }

    if (this.abbuchungsdatumvon != null)
    {
      Date tmp = (Date) getAbbuchungsdatumvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("abbuchungsdatum.von",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("abbuchungsdatum.von", "");
      }
    }

    if (this.abbuchungsdatumbis != null)
    {
      Date tmp = (Date) getAbbuchungsdatumbis().getValue();
      if (tmp != null)
      {
        settings.setAttribute("abbuchungsdatum.bis",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("abbuchungsdatum.bis", "");
      }
    }

  }

  public void handleStore()
  {
    try
    {
      Kursteilnehmer k = getKursteilnehmer();
      k.setName((String) getName(false).getValue());
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
      GUI.getStatusBar().setErrorText(e.getMessage());
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
      saveDefaults();
      String subtitle = "";
      list = Einstellungen.getDBService().createList(Kursteilnehmer.class);
      if (abbuchungsdatumvon.getValue() != null)
      {
        Date d = (Date) abbuchungsdatumvon.getValue();
        subtitle += "Abbuchungsdatum von "
            + new JVDateFormatTTMMJJJJ().format(d) + "  ";
        list.addFilter("abbudatum >= ?",
            new Object[] { new java.sql.Date(d.getTime()) });
      }
      if (abbuchungsdatumbis.getValue() != null)
      {
        Date d = (Date) abbuchungsdatumbis.getValue();
        subtitle += " bis " + new JVDateFormatTTMMJJJJ().format(d) + "  ";
        list.addFilter("abbudatum <= ?",
            new Object[] { new java.sql.Date(d.getTime()) });
      }
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      Settings settings = new Settings(this.getClass());

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("kursteilnehmer", "", Einstellungen
          .getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        // close();
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());
      final String subtitle2 = subtitle;

      BackgroundTask t = new BackgroundTask()
      {

        public void run(ProgressMonitor monitor) throws ApplicationException
        {
          try
          {
            Reporter rpt = new Reporter(new FileOutputStream(file),
                "Kursteilnehmer", subtitle2, list.size());

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
              rpt.addColumn(kt.getAbbudatum(), Element.ALIGN_LEFT);
              rpt.addColumn(kt.getName(), Element.ALIGN_LEFT);
              rpt.addColumn(kt.getVZweck1() + "\n" + kt.getVZweck2(),
                  Element.ALIGN_LEFT);
              rpt.addColumn(kt.getBetrag());
            }
            rpt.close();
            GUI.getDisplay().asyncExec(new Runnable()
            {

              public void run()
              {
                try
                {
                  new Program().handleAction(file);
                }
                catch (ApplicationException ae)
                {
                  Application.getMessagingFactory().sendMessage(
                      new StatusBarMessage(ae.getLocalizedMessage(),
                          StatusBarMessage.TYPE_ERROR));
                }
              }
            });

          }
          catch (ApplicationException ae)
          {
            Logger.error("Fehler", ae);
            GUI.getStatusBar().setErrorText(ae.getMessage());
            throw ae;
          }
          catch (Exception re)
          {
            Logger.error("Fehler", re);
            GUI.getStatusBar().setErrorText(re.getMessage());
            throw new ApplicationException(re);
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

  private class FilterListener implements Listener
  {

    public void handleEvent(Event event)
    {
      if (event.type != SWT.Selection && event.type != SWT.FocusOut)
      {
        return;
      }
      refresh();
    }
  }

  private DBIterator getIterator() throws RemoteException
  {
    DBIterator kursteilnehmer = Einstellungen.getDBService().createList(
        Kursteilnehmer.class);
    String suchN = (String) getSuchname().getValue();
    if (suchN != null && suchN.length() > 0)
    {
      kursteilnehmer.addFilter("name like ?",
          new Object[] { "%" + suchN + "%" });
    }
    if (getEingabedatumvon().getValue() != null)
    {
      kursteilnehmer.addFilter("eingabedatum >= ?",
          new Object[] { (Date) getEingabedatumvon().getValue() });
    }
    if (getEingabedatumbis().getValue() != null)
    {
      kursteilnehmer.addFilter("eingabedatum <= ?",
          new Object[] { (Date) getEingabedatumbis().getValue() });
    }
    return kursteilnehmer;
  }

}
