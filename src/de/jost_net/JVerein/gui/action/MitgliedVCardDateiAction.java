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
package de.jost_net.JVerein.gui.action;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.FileViewer;
import de.jost_net.JVerein.io.VCardTool;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;
import ezvcard.Ezvcard;
import ezvcard.VCardVersion;

public class MitgliedVCardDateiAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    try
    {
      if (context != null
          && (context instanceof Mitglied || context instanceof Mitglied[]))
      {
        ArrayList<Mitglied> mitgl = new ArrayList<Mitglied>();
        if (context instanceof Mitglied)
        {
          mitgl.add((Mitglied) context);
        }
        else if (context instanceof Mitglied[])
        {
          for (Mitglied mitglied : (Mitglied[]) context)
          {
            mitgl.add(mitglied);
          }
        }
        FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
        fd.setText("Ausgabedatei wählen.");

        Settings settings = new de.willuhn.jameica.system.Settings(
            this.getClass());
        String path = settings.getString("lastdir",
            System.getProperty("user.home"));
        if (path != null && path.length() > 0)
        {
          fd.setFilterPath(path);
        }
        fd.setFileName(new Dateiname("vCards", "", Einstellungen
            .getEinstellung().getDateinamenmuster(), "VCF").get());
        fd.setFilterExtensions(new String[] { "*.VCF" });

        String s = fd.open();
        if (s == null || s.length() == 0)
        {
          return;
        }
        if (!s.endsWith(".VCF"))
        {
          s = s + ".VCF";
        }
        final File file = new File(s);
        final ArrayList<Mitglied> mitglieder = mitgl;
        settings.setAttribute("lastdir", file.getParent());
        BackgroundTask t = new BackgroundTask()
        {

          @Override
          public void run(ProgressMonitor monitor) throws ApplicationException
          {
            try
            {
              Ezvcard.write(VCardTool.getVCards(mitglieder)).version(VCardVersion.V3_0).go(file);
              FileViewer.show(file);
            }
            catch (Exception re)
            {
              Logger.error("Fehler", re);
              GUI.getStatusBar().setErrorText(re.getMessage());
              throw new ApplicationException(re);
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
      else
      {
        throw new ApplicationException("Kein Mitglied ausgewählt");
      }
    }
    catch (Exception e)
    {
      throw new ApplicationException("Fehler: " + e.getLocalizedMessage());
    }
  }
}
