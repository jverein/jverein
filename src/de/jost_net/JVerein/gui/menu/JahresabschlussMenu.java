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
 * Revision 1.1  2008/06/28 16:58:24  jost
 * Neu: Jahresabschluss
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.gui.action.JahresabschlussDeleteAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

/**
 * Kontext-Menu zu den Jahresabschlüssen.
 */
public class JahresabschlussMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Jahresabschlüsse
   */
  public JahresabschlussMenu()
  {
    addItem(new CheckedContextMenuItem("Löschen...",
        new JahresabschlussDeleteAction(), "user-trash.png"));
  }
}
