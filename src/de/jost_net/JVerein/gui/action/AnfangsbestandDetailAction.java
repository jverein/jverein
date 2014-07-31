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
import de.jost_net.JVerein.gui.view.AnfangsbestandView;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class AnfangsbestandDetailAction implements Action
{
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Anfangsbestand a = null;

    if (context != null && (context instanceof Anfangsbestand))
    {
      a = (Anfangsbestand) context;
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
    }
    else
    {
      try
      {
        a = (Anfangsbestand) Einstellungen.getDBService().createObject(
            Anfangsbestand.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung eines neuen Anfangsbestandes: ", e);
      }
    }
    GUI.startView(AnfangsbestandView.class.getName(), a);
  }
}
