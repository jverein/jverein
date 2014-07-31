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
import de.jost_net.JVerein.gui.view.WiedervorlageView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class WiedervorlageAction implements Action
{

  private Mitglied m;

  public WiedervorlageAction(Mitglied m)
  {
    super();
    this.m = m;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Wiedervorlage w = null;

    if (context != null && (context instanceof Wiedervorlage))
    {
      w = (Wiedervorlage) context;
    }
    else
    {
      try
      {
        w = (Wiedervorlage) Einstellungen.getDBService().createObject(
            Wiedervorlage.class, null);
        if (m != null)
        {
          if (m.getID() == null)
          {
            throw new ApplicationException(
                "Neues Mitglied bitte erst speichern. Dann können Wiedervorlagen aufgenommen werden.");
          }
          w.setMitglied(new Integer(m.getID()).intValue());
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung einer neuen Wiedervorlage", e);
      }
    }
    GUI.startView(WiedervorlageView.class.getName(), w);
  }
}
