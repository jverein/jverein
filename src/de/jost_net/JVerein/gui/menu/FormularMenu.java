/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.1  2008/07/18 20:11:17  jost
 * Neu: Formulare
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.gui.action.FormularAnzeigeAction;
import de.jost_net.JVerein.gui.action.FormularDeleteAction;
import de.jost_net.JVerein.gui.action.FormularfelderListeAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
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
  public FormularMenu()
  {
    addItem(new CheckedContextMenuItem("Formularfelder",
        new FormularfelderListeAction()));
    addItem(new CheckedContextMenuItem("Anzeigen", new FormularAnzeigeAction()));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new CheckedContextMenuItem("Löschen...",
        new FormularDeleteAction(), "user-trash.png"));
  }
}
