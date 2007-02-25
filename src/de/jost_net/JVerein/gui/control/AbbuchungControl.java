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
 * Revision 1.2  2007/02/23 20:26:22  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Abbuchung;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
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
  private CheckboxInput jahresabbuchung;

  private DateInput vondatum = null;

  private TextInput zahlungsgrund;

  private CheckboxInput zusatzabbuchung;

  private CheckboxInput kursteilnehmer;

  private Settings settings = null;

  public AbbuchungControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public CheckboxInput getJahresabbuchung() throws RemoteException
  {
    if (jahresabbuchung != null)
    {
      return jahresabbuchung;
    }
    jahresabbuchung = new CheckboxInput(false);

    jahresabbuchung.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        boolean b = ((Boolean) jahresabbuchung.getValue()).booleanValue();
        if (b)
        {
          vondatum.setText("");
          vondatum.setEnabled(false);
        }
        else
        {
          vondatum.setEnabled(true);
        }
      }
    });
    return jahresabbuchung;
  }

  public DateInput getVondatum() throws RemoteException
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    Date d = null;
    this.vondatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.vondatum.setTitle("Anfangsdatum Abbuchung");
    this.vondatum.setText("Bitte Anfangsdatum der Verarbeitung wählen");
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

  public TextInput getZahlungsgrund() throws RemoteException
  {
    if (zahlungsgrund != null)
    {
      return zahlungsgrund;
    }
    String zgrund = settings.getString("zahlungsgrund", "bitte eingeben");

    zahlungsgrund = new TextInput(zgrund, 27);
    return zahlungsgrund;
  }

  public CheckboxInput getZusatzabbuchung() throws RemoteException
  {
    if (zusatzabbuchung != null)
    {
      return zusatzabbuchung;
    }
    zusatzabbuchung = new CheckboxInput(false);
    return zusatzabbuchung;
  }

  public CheckboxInput getKursteilnehmer() throws RemoteException
  {
    if (kursteilnehmer != null)
    {
      return kursteilnehmer;
    }
    kursteilnehmer = new CheckboxInput(false);
    return kursteilnehmer;
  }

  public Button getStartButton()
  {
    Button button = new Button("starten", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        doImport();
      }
    }, null, true);
    return button;
  }

  public void doImport() throws ApplicationException
  {
    settings.setAttribute("zahlungsgrund", (String) zahlungsgrund.getValue());

    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");

    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
      fd.setFilterPath(path);

    final String s = fd.open();

    if (s == null || s.length() == 0)
    {
      // close();
      return;
    }

    final File file = new File(s);
    try
    {
      final FileOutputStream fos = new FileOutputStream(file);
      // Wir merken uns noch das Verzeichnis vom letzten mal
      settings.setAttribute("lastdir", file.getParent());

      final Date vond = (Date) vondatum.getValue();
      final Boolean zusatzab = (Boolean) zusatzabbuchung.getValue();
      BackgroundTask t = new BackgroundTask()
      {
        public void run(ProgressMonitor monitor) throws ApplicationException
        {
          try
          {
            new Abbuchung(fos, settings.getString("zahlungsgrund",
                "kein Grund angegeben"), vond, zusatzab, monitor);
            monitor.setPercentComplete(100);
            monitor.setStatus(ProgressMonitor.STATUS_DONE);
            GUI.getStatusBar().setSuccessText(
                "Abbuchungsdatei geschrieben: " + s);
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
            Logger.error("error while reading objects from " + s, e);
            ApplicationException ae = new ApplicationException(
                "Fehler beim erstellen der Abbuchungsdatei: " + s, e);
            monitor.setStatusText(ae.getMessage());
            GUI.getStatusBar().setErrorText(ae.getMessage());
            throw ae;
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
    catch (FileNotFoundException e1)
    {
      throw new ApplicationException(
          "Abbuchungsdatei kann nicht geschrieben werden");
    }
  }
}
