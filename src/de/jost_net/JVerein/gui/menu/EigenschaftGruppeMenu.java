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
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.EigenschaftGruppeDeleteAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

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
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."),
        new EigenschaftGruppeDeleteAction(), "user-trash.png"));
  }
}
