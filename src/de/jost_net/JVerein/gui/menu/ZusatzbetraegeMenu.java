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
 * Revision 1.3  2007/03/30 13:25:11  jost
 * Wiederkehrende Zusatzabbuchungen.
 *
 * Revision 1.2  2007/02/23 20:26:58  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:38:47  jost
 * *** empty log message ***
 *
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
        new ZusatzbetraegeVorherigeFaelligkeitAction(table)));
    addItem(new CheckedContextMenuItem("Nächstes Fälligkeitsdatum",
        new ZusatzbetraegeNaechsteFaelligkeitAction(table)));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new CheckedContextMenuItem("Erneut ausführen",
        new ZusatzbetraegeResetAction(table)));
    addItem(new CheckedContextMenuItem("Löschen...",
        new ZusatzbetraegeDeleteAction(), "user-trash.png"));
  }
}
