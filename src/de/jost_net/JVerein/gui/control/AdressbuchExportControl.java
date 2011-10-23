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
import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Adressbuch.Txt;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class AdressbuchExportControl extends AbstractControl
{

  private CheckboxInput nurEmail;

  private SelectInput encoding;

  private TextInput trennzeichen;

  private Settings settings = null;

  public AdressbuchExportControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public CheckboxInput getNurEmail()
  {
    if (nurEmail != null)
    {
      return nurEmail;
    }
    nurEmail = new CheckboxInput(settings.getBoolean("nurEmail", true));
    return nurEmail;
  }

  public SelectInput getEncoding()
  {
    if (encoding != null)
    {
      return encoding;
    }
    encoding = new SelectInput(new Object[] { "Cp1250", "ISO8859_15_FDIS",
        "UTF-8" }, settings.getString("encoding", "ISO8859_15_FDIS"));
    return encoding;
  }

  public TextInput getTrennzeichen()
  {
    if (trennzeichen != null)
    {
      return trennzeichen;
    }
    trennzeichen = new TextInput(settings.getString("trennzeichen", ","), 1);
    return trennzeichen;
  }

  public Button getStartButton()
  {
    Button button = new Button("starten", new Action()
    {

      public void handleAction(Object context)
      {

        try
        {
          doExport();
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

  private void doExport() throws ApplicationException, RemoteException
  {
    // settings.setAttribute("zahlungsgrund", (String)
    // zahlungsgrund.getValue());
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Export-Datei auswählen.");

    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("adressbuchexport", Einstellungen
        .getEinstellung().getDateinamenmuster(), "CSV").get());
    final String file = fd.open();

    if (file == null || file.length() == 0)
    {
      throw new ApplicationException("keine Datei ausgewählt!");
    }
    File exportdatei = new File(file);

    // Wir merken uns noch das Verzeichnis fürs nächste mal
    settings.setAttribute("lastdir", exportdatei.getParent());
    final boolean isNurEmail = (Boolean) getNurEmail().getValue();
    settings.setAttribute("nurEmail", isNurEmail);
    final String trennzeichen = (String) getTrennzeichen().getValue();
    settings.setAttribute("trennzeichen", trennzeichen);
    final String encoding = (String) getEncoding().getValue();
    settings.setAttribute("encoding", encoding);
    BackgroundTask t = new BackgroundTask()
    {

      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          DBIterator it = Einstellungen.getDBService().createList(
              Mitglied.class);
          MitgliedUtils.setNurAktive(it);
          MitgliedUtils.setMitglied(it);
          if (isNurEmail)
          {
            it.addFilter("email is not null and length(email)>0");
          }
          Txt txt = new Txt(file, encoding, trennzeichen);
          while (it.hasNext())
          {
            txt.add((Mitglied) it.next());
          }
          txt.close();
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Exportdateii geschrieben ");
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
          ApplicationException ae = new ApplicationException(
              "Fehler beim erstellen der Exportdatei " + e.getMessage(), e);
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
