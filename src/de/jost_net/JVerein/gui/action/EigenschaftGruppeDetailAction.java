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
import de.jost_net.JVerein.gui.view.EigenschaftGruppeDetailView;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class EigenschaftGruppeDetailAction implements Action
{

  private boolean neu;

  public EigenschaftGruppeDetailAction(boolean neu)
  {
    this.neu = neu;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    EigenschaftGruppe eg = null;
    if (neu)
    {
      context = null;
    }
    if (context != null && (context instanceof EigenschaftGruppe))
    {
      eg = (EigenschaftGruppe) context;
    }
    else
    {
      try
      {
        eg = (EigenschaftGruppe) Einstellungen.getDBService().createObject(
            EigenschaftGruppe.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung der neuen EigenschaftGruppe", e);
      }
    }
    GUI.startView(EigenschaftGruppeDetailView.class.getName(), eg);
  }
}
