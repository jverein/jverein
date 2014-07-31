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

import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen einer Spendenbescheinigung
 */
public class SpendenbescheinigungDeleteAction implements Action
{

  /**
   * Löschen von einer oder mehreren Spendenbescheinigungen, die in der View
   * markiert sind.
   */
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Spendenbescheinigung[] spbArr = null;
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
    }
    if (context == null)
    {
      throw new ApplicationException("Keine Spendenbescheinigung ausgewählt");
    }
    else if (context instanceof Spendenbescheinigung)
    {
      spbArr = new Spendenbescheinigung[] { (Spendenbescheinigung) context};
    }
    else if (context instanceof Spendenbescheinigung[])
    {
      spbArr = (Spendenbescheinigung[]) context;
    }
    else
    {
      return;
    }
    try
    {
      String mehrzahl = spbArr.length > 1 ? "en" : "";
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Spendenbescheinigung" + mehrzahl + " löschen");
      d.setText("Wollen Sie die Spendenbescheinigung" + mehrzahl
          + " wirklich löschen?");

      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
        {
          return;
        }
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim Löschen der Spendenbescheinigung" + mehrzahl,
            e);
        return;
      }
      for (Spendenbescheinigung spb : spbArr)
      {
        if (spb.isNewObject())
        {
          continue;
        }
        spb.delete();
      }
      GUI.getStatusBar().setSuccessText(
          "Spendenbescheinigung" + mehrzahl + "  gelöscht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Löschen der Spendenbescheinigung";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
