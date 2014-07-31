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

import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Löschen eines Anfangsbestandes
 */
public class AnfangsbestandDeleteAction implements Action
{
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
    }
    if (context == null || !(context instanceof Anfangsbestand))
    {
      throw new ApplicationException("Keinen Anfangsbestand ausgewählt");
    }
    try
    {
      Anfangsbestand a = (Anfangsbestand) context;
      if (a.isNewObject())
      {
        return;
      }
      try
      {
        Jahresabschluss ja = a.getJahresabschluss();
        if (ja != null)
        {
          throw new ApplicationException(
              "Anfangsbestand ist bereits abgeschlossen.");
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(e.getMessage());
      }

      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Anfangsbestand löschen");
      d.setText("Wollen Sie diesen Anfangsbestand wirklich löschen?");

      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim Löschen des Anfangsbestandes: ", e);
        return;
      }
      a.delete();
      GUI.getStatusBar().setSuccessText("Anfangsbestand gelöscht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Löschen des Anfangsbestandes";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
