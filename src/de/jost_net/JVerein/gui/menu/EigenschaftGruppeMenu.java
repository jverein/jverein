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

import de.jost_net.JVerein.gui.action.EigenschaftGruppeDeleteAction;
import de.jost_net.JVerein.gui.action.EigenschaftGruppeDetailAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;

/**
 * Kontext-Menu zu den EigenschaftenGruppen.
 */
public class EigenschaftGruppeMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der EigenschaftenGruppen
   */
  public EigenschaftGruppeMenu()
  {
    addItem(new ContextMenuItem("neu", new EigenschaftGruppeDetailAction(true),
        "document-new.png"));
    addItem(new CheckedContextMenuItem("löschen...",
        new EigenschaftGruppeDeleteAction(), "user-trash.png"));
  }
}
