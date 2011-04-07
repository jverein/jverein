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
 * Revision 1.13  2011-01-15 09:46:47  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.12  2010-10-15 09:58:23  jost
 * Code aufgeräumt
 *
 * Revision 1.11  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.10  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.9  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.8  2009/06/01 08:32:39  jost
 * Icon aufgenommen.
 *
 * Revision 1.7  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.6  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.5  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.4  2008/01/01 19:51:20  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.3  2007/02/23 20:27:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/23 19:08:54  jost
 * Import optimiert
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
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
  @Override
  public void bind() throws Exception
  {

    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Daten-Import"));

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.IMPORT, false,
        "help-browser.png");
    Button button = new Button(JVereinPlugin.getI18n().tr("importieren"),
        new Action()
        {
          public void handleAction(Object context) throws ApplicationException
          {
            doImport();
          }
        }, null, true, "go.png");
    buttons.addButton(button);
  }

  /**
   * Importiert die Daten.
   */
  private void doImport() throws ApplicationException
  {
    YesNoDialog ynd = new YesNoDialog(AbstractDialog.POSITION_CENTER);
    ynd.setText(JVereinPlugin.getI18n().tr(
        "Achtung! Existierende Daten werden gelöscht. Weiter?"));
    ynd.setTitle(JVereinPlugin.getI18n().tr("Import"));
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
    fd.setText(JVereinPlugin.getI18n().tr(
        "Bitte wählen Sie die Import-Datei aus."));

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
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Datei existiert nicht oder ist nicht lesbar"));
    }
    // Wir merken uns noch das Verzeichnis vom letzten mal
    settings.setAttribute("lastdir", file.getParent());

    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new Import(file.getParent(), file.getName(), monitor);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText(
              JVereinPlugin.getI18n().tr("Daten importiert aus {0}", s));
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
          ApplicationException ae = new ApplicationException(JVereinPlugin
              .getI18n().tr("Fehler beim Importieren der Daten aus {0}, {1}",
                  new String[] { s, e.getMessage() }));
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

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Import</span></p>"
        + "<p>Import der Daten aus einer CSV-Datei. Der Aufbau der Datei ist in der Hilfe beschrieben.  </p>"
        + "<p>Achtung! Existierende Daten werden gelöscht.</p></form>";
  }
}
