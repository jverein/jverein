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
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.input.AbbuchungsmodusInput;
import de.jost_net.JVerein.io.AbrechnungSEPA;
import de.jost_net.JVerein.io.AbrechnungSEPAParam;
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
import de.jost_net.JVerein.keys.Abrechnungsmodi;
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

  private DateInput vondatum = null;

  private TextInput zahlungsgrund;

  private CheckboxInput zusatzbetrag;

  private CheckboxInput kursteilnehmer;

  private CheckboxInput kompakteabbuchung;

  private CheckboxInput dtausprint;

  private SelectInput ausgabe;

  private Settings settings = null;

  public AbrechnungSEPAControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
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
    Date d = null;
    this.stichtag = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.stichtag.setTitle(JVereinPlugin.getI18n().tr(
        "Stichtag für die Abrechnung"));
    this.stichtag.setText(JVereinPlugin.getI18n().tr(
        "Bitte Stichtag für die Abrechnung wählen"));
    this.stichtag.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) stichtag.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    this.stichtag.setComment("*)");
    return stichtag;
  }

  public DateInput getVondatum()
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    Date d = null;
    this.vondatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.vondatum
        .setTitle(JVereinPlugin.getI18n().tr("Anfangsdatum Abrechung"));
    this.vondatum.setText(JVereinPlugin.getI18n().tr(
        "Bitte Anfangsdatum der Abrechnung wählen"));
    this.vondatum.setEnabled(false);
    this.vondatum.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) vondatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return vondatum;
  }

  public TextInput getZahlungsgrund()
  {
    if (zahlungsgrund != null)
    {
      return zahlungsgrund;
    }
    String zgrund = settings.getString("zahlungsgrund", JVereinPlugin.getI18n()
        .tr("bitte eingeben"));

    zahlungsgrund = new TextInput(zgrund, 27);
    return zahlungsgrund;
  }

  public CheckboxInput getZusatzbetrag()
  {
    if (zusatzbetrag != null)
    {
      return zusatzbetrag;
    }
    zusatzbetrag = new CheckboxInput(settings.getBoolean("zusatzbetraege",
        false));
    return zusatzbetrag;
  }

  public CheckboxInput getKursteilnehmer()
  {
    if (kursteilnehmer != null)
    {
      return kursteilnehmer;
    }
    kursteilnehmer = new CheckboxInput(settings.getBoolean("kursteilnehmer",
        false));
    return kursteilnehmer;
  }

  public CheckboxInput getKompakteAbbuchung()
  {
    if (kompakteabbuchung != null)
    {
      return kompakteabbuchung;
    }
    kompakteabbuchung = new CheckboxInput(settings.getBoolean(
        "kompakteabbuchung", false));
    return kompakteabbuchung;
  }

  public CheckboxInput getDtausPrint()
  {
    if (dtausprint != null)
    {
      return dtausprint;
    }
    dtausprint = new CheckboxInput(settings.getBoolean("dtausprint", false));
    return dtausprint;
  }

  public SelectInput getAbbuchungsausgabe()
  {
    if (ausgabe != null)
    {
      return ausgabe;
    }
    ausgabe = new SelectInput(Abrechnungsausgabe.getArray(),
        new Abrechnungsausgabe(settings.getInt("abrechnungsausgabe",
            Abrechnungsausgabe.DTAUS)));
    return ausgabe;
  }

  public Button getStartButton()
  {
    Button button = new Button(JVereinPlugin.getI18n().tr("starten"),
        new Action()
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
        }, null, true, "go.png");
    return button;
  }

  private void doAbrechnung() throws ApplicationException, RemoteException
  {
    File dtausfile;
    settings.setAttribute("zahlungsgrund", (String) zahlungsgrund.getValue());
    settings.setAttribute("zusatzbetraege", (Boolean) zusatzbetrag.getValue());
    settings
        .setAttribute("kursteilnehmer", (Boolean) kursteilnehmer.getValue());
    settings.setAttribute("kompakteabbuchung",
        (Boolean) kompakteabbuchung.getValue());
    // settings.setAttribute("dtausprint", (Boolean) dtausprint.getValue());
    Abrechnungsausgabe aa = (Abrechnungsausgabe) ausgabe.getValue();
    settings.setAttribute("abrechnungsausgabe", aa.getKey());

    Integer modus = null;
    try
    {
      modus = (Integer) getAbbuchungsmodus().getValue();
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Interner Fehler - kann Abrechnungsmodus nicht auslesen"));
    }
    Date vondatum = null;
    if (stichtag.getValue() == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Stichtag fehlt"));
    }
    if (modus != Abrechnungsmodi.KEINBEITRAG)
    {
      vondatum = (Date) getVondatum().getValue();
      if (modus == Abrechnungsmodi.EINGETRETENEMITGLIEDER && vondatum == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "von-Datum fehlt"));
      }
    }
    Integer ausgabe;
    aa = (Abrechnungsausgabe) this.getAbbuchungsausgabe().getValue();
    ausgabe = aa.getKey();

    if (ausgabe == Abrechnungsausgabe.DTAUS)
    {
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText(JVereinPlugin.getI18n().tr("DTAUS-Ausgabedatei wählen."));

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("abbuchung", "", Einstellungen
          .getEinstellung().getDateinamenmuster(), "TXT").get());
      String file = fd.open();

      if (file == null || file.length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "keine Datei ausgewählt!"));
      }
      dtausfile = new File(file);
    }
    else
    {
      try
      {
        dtausfile = File.createTempFile("dtaus", null);
      }
      catch (IOException e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Temporäre Datei für die Abbuchung kann nicht erstellt werden."));
      }
    }

    // PDF-Datei für Dtaus2PDF
    String pdffile = null;
    // final Boolean pdfprintb = (Boolean) dtausprint.getValue();
    // if (pdfprintb)
    // {
    // FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    // fd.setText(JVereinPlugin.getI18n().tr("PDF-Ausgabedatei wählen"));
    //
    // String path = settings.getString("lastdir",
    // System.getProperty("user.home"));
    // if (path != null && path.length() > 0)
    // {
    // fd.setFilterPath(path);
    // }
    // fd.setFileName(new Dateiname("abbuchung", "", Einstellungen
    // .getEinstellung().getDateinamenmuster(), "PDF").get());
    // pdffile = fd.open();
    // }

    // Wir merken uns noch das Verzeichnis fürs nächste mal
    settings.setAttribute("lastdir", dtausfile.getParent());
    final AbrechnungSEPAParam abupar;
    try
    {
      abupar = new AbrechnungSEPAParam(this, dtausfile, pdffile);
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
          new AbrechnungSEPA(abupar, monitor);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText(
              JVereinPlugin.getI18n().tr(
                  "Abrechung durchgeführt., Abbuchungsdatei {0} geschrieben.",
                  abupar.dtausfile.getAbsolutePath()));
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (Exception e)
        {
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          Logger.error(
              JVereinPlugin.getI18n().tr(
                  "error while reading objects from {0}",
                  abupar.dtausfile.getAbsolutePath()), e);
          ApplicationException ae = new ApplicationException(JVereinPlugin
              .getI18n().tr("Fehler beim erstellen der Abbuchungsdatei: {0}",
                  abupar.dtausfile.getAbsolutePath()), e);
          monitor.setStatusText(ae.getMessage());
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
