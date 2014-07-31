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

import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Nächstes Fälligkeitsdatum eines Zusatzbetrages setzen.
 */
public class ZusatzbetraegeNaechsteFaelligkeitAction implements Action
{

  private TablePart table;

  public ZusatzbetraegeNaechsteFaelligkeitAction(TablePart table)
  {
    this.table = table;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Zusatzbetrag))
    {
      throw new ApplicationException("Kein Zusatzbetrag ausgewählt");
    }
    try
    {
      Zusatzbetrag z = (Zusatzbetrag) context;
      if (z.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Nächste Fälligkeit setzen");
      d.setText("Wollen Sie das nächste Fälligkeitsdatum setzen?");
      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error(
            "Fehler beim Setzen des nächsten Fälligkeitsdatums des Zusatzbetrages",
            e);
        return;
      }
      z.naechsteFaelligkeit();

      int ind = table.removeItem(z);
      z.store();
      table.addItem(z, ind);
      GUI.getStatusBar().setSuccessText("Fälligkeitsdatum gesetzt.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Zurücksetzen des Ausführungsdatums des Zusatzbetrages.";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}