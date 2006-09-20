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
package de.jost_net.JVerein.gui.view;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.io.Import;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class ImportView extends AbstractView
{
  public void bind() throws Exception
  {

    GUI.getView().setTitle("Import Sparkassen Vereins Daten");

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    Button button = new Button("Import starten", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        doImport();
      }
    }, null, true);
    buttons.addButton(button);
    buttons.addButton("<< Zurück", new BackAction());
  }

  /**
   * Importiert die Daten.
   */
  private void doImport() throws ApplicationException
  {
    YesNoDialog ynd = new YesNoDialog(AbstractDialog.POSITION_CENTER);
    ynd.setText("Achtung! Existierende Daten werden gelöscht. Weiter?");
    ynd.setTitle("Import");
    Boolean choice;
    try
    {
      choice = (Boolean) ynd.open();
      if (!choice.booleanValue())
        return;
    }
    catch (Exception e1)
    {
      e1.printStackTrace();
    }
    Settings settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);

    FileDialog fd = new FileDialog(GUI.getShell(), SWT.OPEN);
    fd.setText("Bitte wählen Sie die Import-Datei aus.");

    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    final String s = fd.open();

    if (s == null || s.length() == 0)
    {
      // close();
      return;
    }

    final File file = new File(s);
    if (!file.exists() || !file.isFile())
    {
      throw new ApplicationException(
          "Datei existiert nicht oder ist nicht lesbar");
    }
    // Wir merken uns noch das Verzeichnis vom letzten mal
    settings.setAttribute("lastdir", file.getParent());

    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new Import(file.getParent(), monitor);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Daten importiert aus " + s);
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
              "Fehler beim Importieren der Daten aus " + s, e);
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

  public void unbind() throws ApplicationException
  {
  }
}
