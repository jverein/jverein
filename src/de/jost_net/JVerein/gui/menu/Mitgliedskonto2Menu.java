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
import de.jost_net.JVerein.gui.action.MitgliedskontoMahnungAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoRechnungAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

/**
 * Kontext-Menu zu den Mitgliedskonten.
 */
public class Mitgliedskonto2Menu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer Mitgliedskonten.
   */
  public Mitgliedskonto2Menu()
  {
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n()
        .tr("Rechnung..."), new MitgliedskontoRechnungAction(), "rechnung.png"));
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("Mahnung..."),
        new MitgliedskontoMahnungAction(), "rechnung.png"));
  }
}
