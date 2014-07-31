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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen eines Adresstypen.
 */
public class AdresstypDefaultAction implements Action
{
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    try
    {
      Adresstyp at = (Adresstyp) Einstellungen.getDBService().createObject(
          Adresstyp.class, "1");
      at.setBezeichnung("Mitglied");
      at.setJVereinid(1);
      at.store();
      at = (Adresstyp) Einstellungen.getDBService().createObject(
          Adresstyp.class, "2");
      at.setBezeichnung("Spender/in");
      at.setJVereinid(2);
      at.store();

      GUI.getStatusBar().setSuccessText("Adresstypen eingefügt.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Einfügen von Adresstypen.";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
