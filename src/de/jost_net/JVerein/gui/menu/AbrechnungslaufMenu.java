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
package de.jost_net.JVerein.gui.menu;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.AbrechnungslaufAbschliessenAction;
import de.jost_net.JVerein.gui.action.AbrechnungslaufDeleteAction;
import de.jost_net.JVerein.gui.action.AbrechnungslaufDetailAction;
import de.jost_net.JVerein.gui.action.PreNotificationAction;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.logging.Logger;

/**
 * Kontext-Menu zu den Abrechnungsläufen
 */
public class AbrechnungslaufMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Abrechnungläufe
   */
  public AbrechnungslaufMenu()
  {
    addItem(new ContextMenuItem("Bearbeiten", new AbrechnungslaufDetailAction(),
        "edit.png"));
    addItem(new AbgeschlossenDisabledItem("Pre-Notification",
        new PreNotificationAction(), "file.png"));
    addItem(new AbgeschlossenDisabledItem("löschen...",
        new AbrechnungslaufDeleteAction(), "trash-alt.png"));
    try
    {
      if (Einstellungen.getEinstellung().getAbrlAbschliessen())
      {
        addItem(ContextMenuItem.SEPARATOR);
        addItem(new AbgeschlossenDisabledItem("abschließen...",
            new AbrechnungslaufAbschliessenAction(), "lock.png"));
      }
    }
    catch (RemoteException e)
    {
      Logger.error("unable to extend context menu");
    }
  }

  private static class AbgeschlossenDisabledItem extends CheckedContextMenuItem
  {

    private AbgeschlossenDisabledItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    @Override
    public boolean isEnabledFor(Object o)
    {
      if (o instanceof Abrechnungslauf)
      {
        Abrechnungslauf abrl = (Abrechnungslauf) o;
        try
        {
          if (abrl.getAbgeschlossen())
          {
            return false;
          }
          else
          {
            return true;
          }
        }
        catch (RemoteException e)
        {
          return false;
        }
      }
      return super.isEnabledFor(o);
    }
  }

}
