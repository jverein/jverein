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

import de.jost_net.JVerein.gui.action.ZusatzbetraegeDeleteAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeNaechsteFaelligkeitAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeResetAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeVorherigeFaelligkeitAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.jameica.gui.parts.TablePart;

/**
 * Kontext-Menu zu den Zusatzbeträgen.
 */
public class ZusatzbetraegeMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Zusatzbeträge.
   */
  public ZusatzbetraegeMenu(TablePart table)
  {
    addItem(new CheckedContextMenuItem("Vorheriges Fälligkeitsdatum",
        new ZusatzbetraegeVorherigeFaelligkeitAction(table),
        "office-calendar.png"));
    addItem(new CheckedContextMenuItem("Nächstes Fälligkeitsdatum",
        new ZusatzbetraegeNaechsteFaelligkeitAction(table),
        "office-calendar.png"));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new CheckedContextMenuItem("erneut ausführen",
        new ZusatzbetraegeResetAction(table), "view-refresh.png"));
    addItem(new CheckedContextMenuItem("löschen...",
        new ZusatzbetraegeDeleteAction(), "user-trash.png"));
  }
}
