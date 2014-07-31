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
import de.jost_net.JVerein.gui.view.LehrgangsartDetailView;
import de.jost_net.JVerein.rmi.Lehrgangsart;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class LehrgangsartAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Lehrgangsart l = null;

    if (context != null && (context instanceof Lehrgangsart))
    {
      l = (Lehrgangsart) context;
    }
    else
    {
      try
      {
        l = (Lehrgangsart) Einstellungen.getDBService().createObject(
            Lehrgangsart.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung einer neuen Lehrgangsart", e);
      }
    }
    GUI.startView(LehrgangsartDetailView.class.getName(), l);
  }
}
