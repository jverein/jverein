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
 * Revision 1.1  2007/02/25 19:12:44  jost
 * Neu: Kursteilnehmer
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.gui.action.KursteilnehmerAbuResetAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDeleteAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.TablePart;

/**
 * Kontext-Menu zu den Kursteilnehmer.
 */
public class KursteilnehmerMenu extends ContextMenu
{
  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Kursteilnehmer.
   */
  public KursteilnehmerMenu(TablePart table)
  {
    addItem(new CheckedContextMenuItem("Abbuchungsdatum löschen...",
        new KursteilnehmerAbuResetAction(table)));
    addItem(new CheckedContextMenuItem("Löschen...",
        new KursteilnehmerDeleteAction()));
  }
}
