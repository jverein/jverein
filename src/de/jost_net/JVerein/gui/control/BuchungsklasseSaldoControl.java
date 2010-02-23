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
 * Revision 1.3  2009/09/15 19:21:35  jost
 * TODO-Tag entfernt.
 *
 * Revision 1.2  2009/09/12 19:02:46  jost
 * neu: Buchungsjournal
 *
 * Revision 1.1  2009/09/10 18:17:23  jost
 * neu: Buchungsklassen
 *
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.parts.BuchungsklasseSaldoList;
import de.jost_net.JVerein.io.BuchungsklasseSaldoZeile;
import de.jost_net.JVerein.io.BuchungsklassesaldoPDF;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungsklasseSaldoControl extends AbstractControl
{
  private BuchungsklasseSaldoList saldoList;

  private DateInput datumvon;

  private DateInput datumbis;

  private Settings settings = null;

  public BuchungsklasseSaldoControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public DateInput getDatumvon()
  {
    if (datumvon != null)
    {
      return datumvon;
    }
    Calendar cal = Calendar.getInstance();
    Date d = new Date();
    try
    {
      d = Einstellungen.DATEFORMAT.parse(settings.getString("von", "01.01"
          + cal.get(Calendar.YEAR)));
    }
    catch (ParseException e)
    {
      //
    }
    datumvon = new DateInput(d, Einstellungen.DATEFORMAT);
    return datumvon;
  }

  public DateInput getDatumbis()
  {
    if (datumbis != null)
    {
      return datumbis;
    }
    Calendar cal = Calendar.getInstance();
    Date d = new Date();
    try
    {
      d = Einstellungen.DATEFORMAT.parse(settings.getString("bis", "31.12."
          + cal.get(Calendar.YEAR)));
    }
    catch (ParseException e)
    {
      //
    }
    datumbis = new DateInput(d, Einstellungen.DATEFORMAT);
    return datumbis;
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
  }

  public Part getSaldoList() throws ApplicationException
  {
    try
    {
      if (getDatumvon().getValue() != null)
      {
        settings.setAttribute("von", Einstellungen.DATEFORMAT
            .format((Date) getDatumvon().getValue()));
      }
      if (getDatumvon().getValue() != null)
      {
        settings.setAttribute("bis", Einstellungen.DATEFORMAT
            .format((Date) getDatumbis().getValue()));
      }

      if (saldoList == null)
      {
        saldoList = new BuchungsklasseSaldoList(null, (Date) datumvon
            .getValue(), (Date) datumbis.getValue());
      }
      else
      {
        settings.setAttribute("von", Einstellungen.DATEFORMAT
            .format((Date) getDatumvon().getValue()));

        saldoList.setDatumvon((Date) datumvon.getValue());
        saldoList.setDatumbis((Date) datumbis.getValue());
        ArrayList<BuchungsklasseSaldoZeile> zeile = saldoList.getInfo();
        saldoList.removeAll();
        for (BuchungsklasseSaldoZeile sz : zeile)
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
      ArrayList<BuchungsklasseSaldoZeile> zeile = saldoList.getInfo();

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");
      //
      Settings settings = new Settings(this.getClass());
      //
      String path = settings.getString("lastdir", System
          .getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("buchungsklassensaldo", Einstellungen
          .getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());
      auswertungSaldoPDF(zeile, file, (Date) getDatumvon().getValue(),
          (Date) getDatumbis().getValue());
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

  private void auswertungSaldoPDF(
      final ArrayList<BuchungsklasseSaldoZeile> zeile, final File file,
      final Date datumvon, final Date datumbis)
  {
    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new BuchungsklassesaldoPDF(zeile, file, monitor, datumvon, datumbis);
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
