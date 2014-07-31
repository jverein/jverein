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

import de.jost_net.JVerein.gui.action.FamilienmitgliedEntfernenAction;
import de.jost_net.JVerein.gui.control.FamilienbeitragNode;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

/**
 * Kontext-Menu zu den Famlienbeiträgen.
 */
public class FamilienbeitragMenu extends ContextMenu
{

  public FamilienbeitragMenu()
  {
    addItem(new AngehoerigerItem("Aus Familienverband entfernen",
        new FamilienmitgliedEntfernenAction(), "accessories-calculator.png"));
  }

  private static class AngehoerigerItem extends CheckedContextMenuItem
  {

    /**
     * @param text
     * @param action
     */
    private AngehoerigerItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    @Override
    public boolean isEnabledFor(Object o)
    {
      if (o instanceof FamilienbeitragNode)
      {
        FamilienbeitragNode fbn = (FamilienbeitragNode) o;
        if (fbn.getType() == FamilienbeitragNode.ANGEHOERIGER)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
      return super.isEnabledFor(o);
    }
  }

}
