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

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.DBTools.DBTransaction;
import de.jost_net.JVerein.gui.input.AbbuchungsmodusInput;
import de.jost_net.JVerein.io.AbrechnungSEPA;
import de.jost_net.JVerein.io.AbrechnungSEPAParam;
import de.jost_net.JVerein.io.Bankarbeitstage;
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
import de.jost_net.JVerein.keys.Abrechnungsmodi;
import de.jost_net.JVerein.keys.Monat;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class AbrechnungSEPAControl extends AbstractControl
{

  private AbbuchungsmodusInput modus;

  private DateInput stichtag = null;

  private SelectInput abrechnungsmonat;

  private DateInput faelligkeit = null;

  private DateInput vondatum = null;

  private DateInput bisdatum = null;

  private TextInput zahlungsgrund;

  private CheckboxInput zusatzbetrag;

  private CheckboxInput kursteilnehmer;

  private CheckboxInput kompakteabbuchung;

  private CheckboxInput sepaprint;

  private SelectInput ausgabe;

  private Settings settings = null;

  public AbrechnungSEPAControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public SelectInput getAbrechnungsmonat()
  {
    if (abrechnungsmonat != null)
    {
      return abrechnungsmonat;
    }
    abrechnungsmonat = new SelectInput(Monat.values(), Monat.JANUAR);
    return abrechnungsmonat;
  }

  public AbbuchungsmodusInput getAbbuchungsmodus() throws RemoteException
  {
    if (modus != null)
    {
      return modus;
    }
    modus = new AbbuchungsmodusInput(Abrechnungsmodi.KEINBEITRAG);
    modus.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        Integer m = ((Integer) modus.getValue());
        if (m.intValue() != Abrechnungsmodi.EINGETRETENEMITGLIEDER)
        {
          vondatum.setValue(null);
          vondatum.setEnabled(false);
        }
        else
        {
          vondatum.setEnabled(true);
          vondatum.setValue(new Date());
        }
        if (m.intValue() != Abrechnungsmodi.ABGEMELDETEMITGLIEDER)
        {
          bisdatum.setValue(null);
          bisdatum.setEnabled(false);
        }
        else
        {
          bisdatum.setEnabled(true);
          bisdatum.setValue(new Date());
        }
      }
    });
    return modus;
  }

  public DateInput getStichtag()
  {
    if (stichtag != null)
    {
      return stichtag;
    }
    this.stichtag = new DateInput(null, new JVDateFormatTTMMJJJJ());
    this.stichtag.setTitle("Stichtag für die Abrechnung");
    this.stichtag.setText("Bitte Stichtag für die Abrechnung wählen");
    this.stichtag.setComment("*)");
    return stichtag;
  }

  public DateInput getFaelligkeit() throws RemoteException
  {
    if (faelligkeit != null)
    {
      return faelligkeit;
    }
    Calendar cal = Calendar.getInstance();
    Bankarbeitstage bat = new Bankarbeitstage();
    cal = bat.getCalendar(cal,
        1 + Einstellungen.getEinstellung().getSEPADatumOffset());
    this.faelligkeit = new DateInput(cal.getTime(),
        new JVDateFormatTTMMJJJJ());
    this.faelligkeit.setTitle("Fälligkeit SEPA-Lastschrift");
    this.faelligkeit.setText(
        "Bitte Fälligkeitsdatum der SEPA-Lastschrift wählen");
    return faelligkeit;
  }

  public DateInput getVondatum()
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    this.vondatum = new DateInput(null, new JVDateFormatTTMMJJJJ());
    this.vondatum.setTitle("Anfangsdatum Abrechnung");
    this.vondatum.setText("Bitte Anfangsdatum der Abrechnung wählen");
    this.vondatum.setEnabled(false);
    return vondatum;
  }

  public DateInput getBisdatum()
  {
    if (bisdatum != null)
    {
      return bisdatum;
    }
    this.bisdatum = new DateInput(null, new JVDateFormatTTMMJJJJ());
    this.bisdatum.setTitle("Enddatum Abrechnung");
    this.bisdatum
        .setText("Bitte maximales Austrittsdatum für die Abrechnung wählen");
    this.bisdatum.setEnabled(false);
    return bisdatum;
  }

  public TextInput getZahlungsgrund()
  {
    if (zahlungsgrund != null)
    {
      return zahlungsgrund;
    }
    String zgrund = settings.getString("zahlungsgrund", "bitte eingeben");

    zahlungsgrund = new TextInput(zgrund, 50);
    return zahlungsgrund;
  }

  public CheckboxInput getZusatzbetrag()
  {
    if (zusatzbetrag != null)
    {
      return zusatzbetrag;
    }
    zusatzbetrag = new CheckboxInput(
        settings.getBoolean("zusatzbetraege", false));
    return zusatzbetrag;
  }

  public CheckboxInput getKursteilnehmer()
  {
    if (kursteilnehmer != null)
    {
      return kursteilnehmer;
    }
    kursteilnehmer = new CheckboxInput(
        settings.getBoolean("kursteilnehmer", false));
    return kursteilnehmer;
  }

  public CheckboxInput getKompakteAbbuchung()
  {
    if (kompakteabbuchung != null)
    {
      return kompakteabbuchung;
    }
    kompakteabbuchung = new CheckboxInput(
        settings.getBoolean("kompakteabbuchung", false));
    return kompakteabbuchung;
  }

  public CheckboxInput getSEPAPrint()
  {
    if (sepaprint != null)
    {
      return sepaprint;
    }
    sepaprint = new CheckboxInput(settings.getBoolean("sepaprint", false));
    return sepaprint;
  }

  public SelectInput getAbbuchungsausgabe()
  {
    if (ausgabe != null)
    {
      return ausgabe;
    }
    Abrechnungsausgabe aus = Abrechnungsausgabe.getByKey(settings
        .getInt("abrechnungsausgabe", Abrechnungsausgabe.SEPA_DATEI.getKey()));
    if (aus != Abrechnungsausgabe.SEPA_DATEI
        && aus != Abrechnungsausgabe.HIBISCUS)
    {
      aus = Abrechnungsausgabe.HIBISCUS;
    }
    ausgabe = new SelectInput(Abrechnungsausgabe.values(), aus);
    return ausgabe;
  }

  public Button getStartButton()
  {
    Button button = new Button("starten", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          doAbrechnung();
        }
        catch (ApplicationException e)
        {
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
        catch (RemoteException e)
        {
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
      }
    }, null, true, "walking.png");
    return button;
  }

  private void doAbrechnung() throws ApplicationException, RemoteException
  {
    File sepafilercur;
    settings.setAttribute("zahlungsgrund", (String) zahlungsgrund.getValue());
    settings.setAttribute("zusatzbetraege", (Boolean) zusatzbetrag.getValue());
    settings.setAttribute("kursteilnehmer",
        (Boolean) kursteilnehmer.getValue());
    settings.setAttribute("kompakteabbuchung",
        (Boolean) kompakteabbuchung.getValue());
    settings.setAttribute("sepaprint", (Boolean) sepaprint.getValue());
    Abrechnungsausgabe aa = (Abrechnungsausgabe) ausgabe.getValue();
    settings.setAttribute("abrechnungsausgabe", aa.getKey());
    Integer modus = null;
    try
    {
      modus = (Integer) getAbbuchungsmodus().getValue();
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(
          "Interner Fehler - kann Abrechnungsmodus nicht auslesen");
    }
    if (faelligkeit.getValue() == null)
    {
      throw new ApplicationException("Fälligkeitsdatum fehlt");
    }
    Date f = (Date) faelligkeit.getValue();
    if (f.before(new Date()))
    {
      throw new ApplicationException(
          "Fälligkeit muss in der Zukunft liegen");
    }
    Date vondatum = null;
    if (stichtag.getValue() == null)
    {
      throw new ApplicationException("Stichtag fehlt");
    }
    if (modus != Abrechnungsmodi.KEINBEITRAG)
    {
      vondatum = (Date) getVondatum().getValue();
      if (modus == Abrechnungsmodi.EINGETRETENEMITGLIEDER && vondatum == null)
      {
        throw new ApplicationException("von-Datum fehlt");
      }
      Date bisdatum = (Date) getBisdatum().getValue();
      if (modus == Abrechnungsmodi.ABGEMELDETEMITGLIEDER && bisdatum == null)
      {
        throw new ApplicationException("bis-Datum fehlt");
      }
    }
    aa = (Abrechnungsausgabe) this.getAbbuchungsausgabe().getValue();

    if (aa == Abrechnungsausgabe.SEPA_DATEI)
    {
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("SEPA-Ausgabedatei wählen.");
      String path = settings.getString("lastdir.sepa",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("abbuchungRCUR", "",
          Einstellungen.getEinstellung().getDateinamenmuster(), "XML").get());
      String file = fd.open();
      if (file == null || file.length() == 0)
      {
        throw new ApplicationException("keine Datei ausgewählt!");
      }
      sepafilercur = new File(file);
      // Wir merken uns noch das Verzeichnis fürs nächste mal
      settings.setAttribute("lastdir.sepa", sepafilercur.getParent());
    }
    else
    {
      try
      {
        sepafilercur = File.createTempFile("separcur", null);
      }
      catch (IOException e)
      {
        throw new ApplicationException(
            "Temporäre Datei für die Abbuchung kann nicht erstellt werden.");
      }
    }

    // PDF-Datei für Basislastschrift2PDF
    String pdffileRCUR = null;
    final Boolean pdfprintb = (Boolean) sepaprint.getValue();
    if (pdfprintb)
    {
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("PDF-Ausgabedatei wählen");

      String path = settings.getString("lastdir.pdf",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("abbuchungRCUR", "",
          Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());
      pdffileRCUR = fd.open();
      File file = new File(pdffileRCUR);
      // Wir merken uns noch das Verzeichnis fürs nächste mal
      settings.setAttribute("lastdir.pdf", file.getParent());
    }

    {
      final AbrechnungSEPAParam abupar;
      try
      {
        abupar = new AbrechnungSEPAParam(this, sepafilercur, pdffileRCUR);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(e);
      }
      BackgroundTask t = new BackgroundTask()
      {
        @Override
        public void run(ProgressMonitor monitor) throws ApplicationException
        {
          try
          {

            DBTransaction.starten();
            new AbrechnungSEPA(abupar, monitor);
            DBTransaction.commit();

            monitor.setPercentComplete(100);
            monitor.setStatus(ProgressMonitor.STATUS_DONE);
            GUI.getStatusBar().setSuccessText(String.format(
                "Abrechnung durchgeführt., SEPA-Datei %s geschrieben.",
                abupar.sepafileRCUR.getAbsolutePath()));
            GUI.getCurrentView().reload();
          }
          catch (ApplicationException ae)
          {
            DBTransaction.rollback();
            GUI.getStatusBar().setErrorText(ae.getMessage());
            throw ae;
          }
          catch (Exception e)
          {
            DBTransaction.rollback();
            Logger.error(String.format("error while creating %s",
                abupar.sepafileRCUR.getAbsolutePath()), e);
            ApplicationException ae = new ApplicationException(
                String.format("Fehler beim erstellen der Abbuchungsdatei: %s",
                    abupar.sepafileRCUR.getAbsolutePath()),
                e);
            GUI.getStatusBar().setErrorText(ae.getMessage());
            throw ae;
          }
        }

        @Override
        public void interrupt()
        {
          //
        }

        @Override
        public boolean isInterrupted()
        {
          return false;
        }
      };
      Application.getController().start(t);
    }
  }
}
