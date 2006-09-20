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
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import de.jost_net.JVerein.io.Buchungsuebernahme;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungsuebernahmeControl extends AbstractControl
{
  private Settings settings = null;

  public BuchungsuebernahmeControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Button getStartButton()
  {
    Button button = new Button("starten", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        doUebernahme();
      }
    }, null, true);
    return button;
  }

  public void doUebernahme() throws ApplicationException
  {
    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          monitor.setPercentComplete(100);
          new Buchungsuebernahme(monitor);
          monitor.setStatus(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Daten übernommen");
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
          Logger.error("error while reading objects from ", e);
          ApplicationException ae = new ApplicationException(
              "Fehler beim der Übernahme: ", e);
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
}
