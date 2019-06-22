/**********************************************************************
 * JVerein - Mitgliederverwaltung und einfache Buchhaltung für Vereine
 * Copyright (c) by Heiner Jostkleigrewe
 * Copyright (c) 2014 by Thomas Hooge
 * Main Project: heiner@jverein.dem  http://www.jverein.de/
 * Module Author: thomas@hoogi.de, http://www.hoogi.de/
 *
 * This file is part of JVerein.
 *
 * JVerein is free software: you can redistribute it and/or modify 
 * it under the terms of the  GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JVerein is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.dialogs.ExportDialog;
import de.jost_net.JVerein.gui.view.DokumentationUtil;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FormularfelderExportAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Formular f = null;

    if (context != null && (context instanceof Formular))
    {
      f = (Formular) context;
    }
    else
    {
      throw new ApplicationException("Kein Formular ausgewählt");
    }

    try
    {
      DBService service = Einstellungen.getDBService();
      DBIterator<Formularfeld> formularfelder = service
          .createList(Formularfeld.class);
      formularfelder.addFilter("formular = ?", new Object[] { f.getID() });
      ArrayList<Formularfeld> fflist = new ArrayList<>();
      while (formularfelder.hasNext())
      {
        Formularfeld ff = formularfelder.next();
        fflist.add(ff);
      }

      ExportDialog d = new ExportDialog((Object[]) fflist.toArray(),
          Formularfeld.class, DokumentationUtil.FORMULARE);
      d.open();
    }
    catch (OperationCanceledException oce)
    {
      Logger.info(oce.getMessage());
      return;
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar()
          .setErrorText("Fehler beim Exportieren der Formularfelder");
    }
  }
}
