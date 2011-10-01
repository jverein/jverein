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
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentDeleteAction;
import de.jost_net.JVerein.gui.action.DokumentShowAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;

/**
 * Kontext-Menu zu den Dokumenten.
 */
public class DokumentMenu extends ContextMenu
{

  public DokumentMenu()
  {
    new ContextMenuItem();
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr("anzeigen"),
        new DokumentShowAction(), "show.png"));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."), new DokumentDeleteAction(),
        "user-trash.png"));
  }
}
