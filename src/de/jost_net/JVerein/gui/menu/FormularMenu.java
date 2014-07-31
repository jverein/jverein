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

import de.jost_net.JVerein.gui.action.FormularAnzeigeAction;
import de.jost_net.JVerein.gui.action.FormularDeleteAction;
import de.jost_net.JVerein.gui.action.FormularDuplizierenAction;
import de.jost_net.JVerein.gui.action.FormularfelderListeAction;
import de.jost_net.JVerein.gui.control.FormularControl;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.CheckedSingleContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;

/**
 * Kontext-Menu zu den Formularen.
 */
public class FormularMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Formulare.
   */
  public FormularMenu(FormularControl control)
  {
    addItem(new CheckedContextMenuItem("Formularfelder",
        new FormularfelderListeAction(), "rechnung.png"));
    addItem(new CheckedContextMenuItem("anzeigen", new FormularAnzeigeAction(),
        "edit.png"));
    addItem(new CheckedSingleContextMenuItem("duplizieren",
        new FormularDuplizierenAction(control), "copy_v2.png"));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new CheckedContextMenuItem("löschen...",
        new FormularDeleteAction(), "user-trash.png"));
  }
}
