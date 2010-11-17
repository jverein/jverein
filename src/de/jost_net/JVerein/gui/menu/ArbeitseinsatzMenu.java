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
import de.jost_net.JVerein.gui.action.ArbeitseinsatzDeleteAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.TablePart;

/**
 * Kontext-Menu zu den Arbeitseinsätzen.
 */
public class ArbeitseinsatzMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Arbeitseinsätze.
   */
  public ArbeitseinsatzMenu()
  {
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."),
        new ArbeitseinsatzDeleteAction(), "user-trash.png"));
  }
}
