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

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Ausführungsdatum der Abbuchung eines Kursteilnehmers entfernen.
 */
public class KursteilnehmerAbuResetAction implements Action
{

  private TablePart table;

  public KursteilnehmerAbuResetAction(TablePart table)
  {
    this.table = table;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Kursteilnehmer))
    {
      throw new ApplicationException("Kein Kursteilnehmer ausgewählt");
    }
    try
    {
      Kursteilnehmer kt = (Kursteilnehmer) context;
      if (kt.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Abbuchungsdatum zurücksetzen");
      d.setText("Wollen Sie das Ausführungsdatum der Abbuchung wirklich zurücksetzen?");
      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error(
            "Fehler beim Reset des Abbuchungsdatums des Kursteilnehmers", e);
        return;
      }
      int ind = table.removeItem(kt);
      kt.resetAbbudatum();
      kt.store();
      table.addItem(kt, ind);

      GUI.getStatusBar().setSuccessText("Abbuchungsdatum zurückgesetzt.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Zurücksetzen des Abbuchungsdatum des Kursteilnehmers.";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
