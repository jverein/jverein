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
 * Revision 1.26  2011-02-12 09:27:50  jost
 * Statische Codeanalyse mit Findbugs
 * Vorbereitung kompakte Abbuchung
 *
 * Revision 1.25  2011-01-15 09:46:49  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.24  2010-10-15 09:58:26  jost
 * Code aufgeräumt
 *
 * Revision 1.23  2010-08-23 13:29:08  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.22  2010-07-25 18:30:07  jost
 * Icon eingefügt
 *
 * Revision 1.21  2010/04/25 13:53:59  jost
 * Vorarbeiten Mitgliedskonto
 *
 * Revision 1.20  2009/02/15 20:01:48  jost
 * Formularwerte speichern und wiederherstellen
 *
 * Revision 1.19  2008/12/22 21:08:19  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.18  2008/11/30 10:44:45  jost
 * Bugfix Abbuchungsmodus
 *
 * Revision 1.17  2008/11/29 13:05:31  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.16  2008/11/16 16:56:03  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.15  2008/08/10 12:34:49  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.14  2008/06/21 08:45:51  jost
 * Bugfix "von-Datum"
 *
 * Revision 1.13  2008/02/09 14:34:50  jost
 * PlausibilitÃ¤tsprÃ¼fung verbessert
 *
 * Revision 1.12  2008/01/31 19:36:05  jost
 * BerÃ¼cksichtigung eines Stichtages fÃ¼r die Abbuchung
 *
 * Revision 1.11  2008/01/07 20:28:55  jost
 * Dateinamensvorgabe fÃ¼r die PDF-Datei
 *
 * Revision 1.10  2008/01/01 13:12:53  jost
 * Neu: Dateinamenmuster
 *
 * Revision 1.9  2007/12/26 18:12:32  jost
 * Lastschriften kÃ¶nnen jetzt als Einzellastschriften oder Sammellastschriften direkt in Hibuscus verbucht werden.
 *
 * Revision 1.8  2007/12/21 13:35:44  jost
 * Ausgabe der DTAUS-Datei im PDF-Format
 *
 * Revision 1.7  2007/12/02 14:14:33  jost
 * ÃœberflÃ¼ssige Plausi entfernt.
 *
 * Revision 1.6  2007/12/02 13:39:10  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.5  2007/08/14 19:19:28  jost
 * Refactoring
 *
 * Revision 1.4  2007/03/27 19:20:16  jost
 * Zusätzliche Plausi
 *
 * Revision 1.3  2007/02/25 19:12:11  jost
 * Neu: Kursteilnehmer
 *
 * Revision 1.2  2007/02/23 20:26:22  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
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
import de.jost_net.JVerein.gui.input.AbbuchungsmodusInput;
import de.jost_net.JVerein.io.Abbuchung;
import de.jost_net.JVerein.io.AbbuchungParam;
import de.jost_net.JVerein.io.Abrechnung;
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

public class AbbuchungControl extends AbstractControl
{
  private CheckboxInput neueAbbuchung;

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

  public AbbuchungControl(AbstractView view)
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
    this.stichtag.setTitle("Stichtag für die Abrechnung");
    this.stichtag.setText("Bitte Stichtag für die Abrechnung wählen");
    this.stichtag.addListener(new Listener()
    {

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
    this.vondatum.setTitle("Anfangsdatum Abrechung");
    this.vondatum.setText("Bitte Anfangsdatum der Abrechnung wählen");
    this.vondatum.setEnabled(false);
    this.vondatum.addListener(new Listener()
    {

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
    String zgrund = settings.getString("zahlungsgrund", "bitte eingeben");

    zahlungsgrund = new TextInput(zgrund, 27);
    return zahlungsgrund;
  }

  public CheckboxInput getNeueAbbuchung()
  {
    if (neueAbbuchung != null)
    {
      return neueAbbuchung;
    }
    neueAbbuchung = new CheckboxInput(false);
    return neueAbbuchung;
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
    Button button = new Button("starten", new Action()
    {

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
    settings.setAttribute("dtausprint", (Boolean) dtausprint.getValue());
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
    Date vondatum = null;
    if (modus != Abrechnungsmodi.KEINBEITRAG)
    {
      vondatum = (Date) getVondatum().getValue();
      if (modus == Abrechnungsmodi.EINGETRETENEMITGLIEDER && vondatum == null)
      {
        throw new ApplicationException("von-Datum fehlt");
      }

      if (stichtag.getValue() == null)
      {
        throw new ApplicationException("Stichtag fehlt");
      }
    }
    Integer ausgabe;
    aa = (Abrechnungsausgabe) this.getAbbuchungsausgabe().getValue();
    ausgabe = aa.getKey();

    if (ausgabe == Abrechnungsausgabe.DTAUS)
    {
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("DTAUS-Ausgabedatei wählen.");

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("abbuchung", Einstellungen.getEinstellung()
          .getDateinamenmuster(), "TXT").get());
      String file = fd.open();

      if (file == null || file.length() == 0)
      {
        throw new ApplicationException("keine Datei ausgewählt!");
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
        throw new ApplicationException(
            "Temporäre Datei für die Abbuchung kann nicht erstellt werden.");
      }
    }

    // PDF-Datei für Dtaus2PDF
    String pdffile = null;
    final Boolean pdfprintb = (Boolean) dtausprint.getValue();
    if (pdfprintb)
    {
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("PDF-Ausgabedatei wählen");

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("abbuchung", Einstellungen.getEinstellung()
          .getDateinamenmuster(), "PDF").get());
      pdffile = fd.open();
    }

    // Wir merken uns noch das Verzeichnis fürs nächste mal
    settings.setAttribute("lastdir", dtausfile.getParent());
    final AbbuchungParam abupar;
    try
    {
      abupar = new AbbuchungParam(this, dtausfile, pdffile);
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
    final boolean nA = (Boolean) neueAbbuchung.getValue();
    BackgroundTask t = new BackgroundTask()
    {

      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          if (nA)
          {
            new Abrechnung(abupar, monitor);
          }
          else
          {
            new Abbuchung(abupar, monitor);
          }
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText(
              "Abrechung durchgeführt., Abbuchungsdatei "
                  + abupar.dtausfile.getAbsolutePath() + " geschrieben.");
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
              "error while reading objects from "
                  + abupar.dtausfile.getAbsolutePath(), e);
          ApplicationException ae = new ApplicationException(
              "Fehler beim erstellen der Abbuchungsdatei: "
                  + abupar.dtausfile.getAbsolutePath(), e);
          monitor.setStatusText(ae.getMessage());
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
