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
 * Revision 1.1  2009/11/17 20:58:32  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.EigenschaftDeleteAction;
import de.jost_net.JVerein.gui.action.EigenschaftDetailAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;

/**
 * Kontext-Menu zur Eigenschaft.
 */
public class EigenschaftMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Eigenschaften
   */
  public EigenschaftMenu()
  {
    addItem(new ContextMenuItem(JVereinPlugin.getI18n().tr("neu"),
        new EigenschaftDetailAction(true), "document-new.png"));
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."),
        new EigenschaftDeleteAction(), "user-trash.png"));
  }
}
