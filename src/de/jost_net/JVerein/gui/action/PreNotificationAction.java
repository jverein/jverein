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

import de.jost_net.JVerein.gui.view.PreNotificationView;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class PreNotificationAction implements Action
{
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null)
    {
      throw new ApplicationException("Keinen Abrechnunglauf ausgewählt!");
    }
    if (!(context instanceof Abrechnungslauf))
    {
      throw new ApplicationException("Programmfehler! Kein Abrechnunglauf!");
    }
    GUI.startView(PreNotificationView.class.getName(), context);
  }
}
