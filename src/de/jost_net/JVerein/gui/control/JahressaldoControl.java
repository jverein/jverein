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
 * Revision 1.11  2010-10-15 09:58:26  jost
 * Code aufger‰umt
 *
 * Revision 1.10  2010-08-23 13:33:40  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.9  2009/09/03 18:48:00  jost
 * Vermeidung NPE
 *
 * Revision 1.8  2009/03/01 17:39:49  jost
 * - Ausgew‰hltes Jahr wird jetzt korrekt gespeichert und wiederhergestellt
 * - Zus‰tzliche Zeile "‹berschuss/Verlust"
 * - Code bereinigt
 *
 * Revision 1.7  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.6  2008/11/16 16:56:35  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.5  2008/10/13 18:57:14  jost
 * RemoteException ->ApplicationException
 *
 * Revision 1.4  2008/07/11 07:33:02  jost
 * Ausgabeverzeichnis f√ºr den n√§chsten Aufruf merken.
 *
 * Revision 1.3  2008/06/28 16:57:29  jost
 * Vereinheitlichung des Jahressaldos
 *
 * Revision 1.2  2008/05/26 18:58:19  jost
 * Spaltenausrichtung
 * Ber√ºcksichtigung von Er√∂ffnungs- und ggfls. Aufl√∂sungsdatum
 *
 * Revision 1.1  2008/05/25 19:36:13  jost
 * Neu: Jahressaldo
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.parts.JahressaldoList;
import de.jost_net.JVerein.io.JahressaldoPDF;
import de.jost_net.JVerein.io.SaldoZeile;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class JahressaldoControl extends AbstractControl
{

  private JahressaldoList saldoList;

  private SelectInput suchjahr;

  private Settings settings = null;

  public JahressaldoControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public SelectInput getSuchJahr() throws RemoteException, ApplicationException
  {
    if (suchjahr != null)
    {
      return suchjahr;
    }
    DBIterator list = Einstellungen.getDBService().createList(Buchung.class);
    list.setOrder("ORDER BY datum");
    Buchung b = null;
    Calendar von = Calendar.getInstance();
    if (list.hasNext())
    {
      b = (Buchung) list.next();
      von.setTime(b.getDatum());
    }
    else
    {
      throw new ApplicationException("Abbruch! Es existiert noch keine Buchung");
    }
    Calendar bis = Calendar.getInstance();
    ArrayList<Integer> jahre = new ArrayList<Integer>();

    for (int i = von.get(Calendar.YEAR); i <= bis.get(Calendar.YEAR); i++)
    {
      jahre.add(i);
    }

    suchjahr = new SelectInput(jahre, settings.getInt("jahr", jahre.get(0)));
    // suchjahr.setPleaseChoose("Bitte ausw‰hlen");
    suchjahr.setPreselected(settings.getInt("jahr", bis.get(Calendar.YEAR)));
    return suchjahr;
  }

  public Button getStartAuswertungButton()
  {
    Button b = new Button("PDF", new Action()
    {

      public void handleAction(Object context) throws ApplicationException
      {
        starteAuswertung();
      }
    }, null, true, "pdf.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public void handleStore()
  {
    //
  }

  public Part getSaldoList() throws ApplicationException
  {
    try
    {
      settings.setAttribute("jahr", (Integer) getSuchJahr().getValue());

      if (saldoList == null)
      {
        saldoList = new JahressaldoList(null, new Geschaeftsjahr(
            (Integer) getSuchJahr().getValue()));
      }
      else
      {
        saldoList.setGeschaeftsjahr(new Geschaeftsjahr(
            (Integer) getSuchJahr().getValue()));
        ArrayList<SaldoZeile> zeile = saldoList.getInfo();
        saldoList.removeAll();
        for (SaldoZeile sz : zeile)
        {
          saldoList.addItem(sz);
        }
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException("Fehler aufgetreten " + e.getMessage());
    }
    catch (ParseException e)
    {
      throw new ApplicationException("Fehler aufgetreten " + e.getMessage());
    }
    return saldoList.getSaldoList();
  }

  private void starteAuswertung() throws ApplicationException
  {
    try
    {
      ArrayList<SaldoZeile> zeile = saldoList.getInfo();

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei w‰hlen.");
      //
      Settings settings = new Settings(this.getClass());
      //
      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("jahressaldo",
          Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());
      Integer jahr = (Integer) suchjahr.getValue();

      Geschaeftsjahr gj = new Geschaeftsjahr(jahr.intValue());

      auswertungSaldoPDF(zeile, file, gj);
    }
    catch (RemoteException e)
    {
      throw new ApplicationException("Fehler beim Aufbau des Reports: "
          + e.getMessage());
    }
    catch (ParseException e)
    {
      throw new ApplicationException("Fehler beim Aufbau des Reports: "
          + e.getMessage());
    }
  }

  private void auswertungSaldoPDF(final ArrayList<SaldoZeile> zeile,
      final File file, final Geschaeftsjahr gj)
  {
    BackgroundTask t = new BackgroundTask()
    {

      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new JahressaldoPDF(zeile, file, monitor, gj);
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
