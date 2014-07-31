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

import de.jost_net.JVerein.gui.action.MitgliedskontoMahnungAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoRechnungAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

/**
 * Kontext-Menu zu den Mitgliedskonten.
 */
public class Mitgliedskonto2Menu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer Mitgliedskonten.
   */
  public Mitgliedskonto2Menu()
  {
    addItem(new CheckedContextMenuItem("Rechnung...",
        new MitgliedskontoRechnungAction(), "rechnung.png"));
    addItem(new CheckedContextMenuItem("Mahnung...",
        new MitgliedskontoMahnungAction(), "rechnung.png"));
  }
}
