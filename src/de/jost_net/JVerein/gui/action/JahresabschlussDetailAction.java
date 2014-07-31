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
import de.jost_net.JVerein.gui.view.JahresabschlussView;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class JahresabschlussDetailAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Jahresabschluss ja = null;

    if (context != null && (context instanceof Jahresabschluss))
    {
      ja = (Jahresabschluss) context;
    }
    else
    {
      try
      {
        ja = (Jahresabschluss) Einstellungen.getDBService().createObject(
            Jahresabschluss.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung eines neuen Jahresabschlusses", e);
      }
    }
    GUI.startView(JahresabschlussView.class.getName(), ja);
  }
}
