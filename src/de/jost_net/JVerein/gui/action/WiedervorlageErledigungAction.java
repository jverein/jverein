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
import java.util.Date;

import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Erledigungsdatum einer Wiedervorlage setzen/zurücksetzen.
 */
public class WiedervorlageErledigungAction implements Action
{

  private TablePart table;

  public WiedervorlageErledigungAction(TablePart table)
  {
    this.table = table;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Wiedervorlage))
    {
      throw new ApplicationException("Keine Wiedervorlage ausgewählt");
    }
    try
    {
      Wiedervorlage w = (Wiedervorlage) context;
      if (w.isNewObject())
      {
        return;
      }
      if (w.getErledigung() == null)
      {
        w.setErledigung(new Date());
      }
      else
      {
        w.setErledigung(null);
      }
      int ind = table.removeItem(w);
      w.store();
      table.addItem(w, ind);
      GUI.getStatusBar().setSuccessText("Erledigungsdatum bearbeitet.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Verändern des Erledigungsdatums.";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
