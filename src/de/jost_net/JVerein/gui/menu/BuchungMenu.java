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
import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.gui.action.BuchungBuchungsartZuordnungAction;
import de.jost_net.JVerein.gui.action.BuchungDeleteAction;
import de.jost_net.JVerein.gui.action.BuchungKontoauszugZuordnungAction;
import de.jost_net.JVerein.gui.action.BuchungMitgliedskontoZuordnungAction;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.CheckedSingleContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;

/**
 * Kontext-Menu zu den Buchungen.
 */
public class BuchungMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Buchungen.
   */
  public BuchungMenu()
  {
    this(null);
  }

  public BuchungMenu(BuchungsControl control)
  {
    addItem(new ContextMenuItem(JVereinPlugin.getI18n().tr("neu"),
        new BuchungNeuAction(), "document-new.png"));
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "bearbeiten"), new BuchungAction(), "edit.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Buchungsart zuordnen"),
        new BuchungBuchungsartZuordnungAction(control), "zuordnung.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Mitgliedskonto zuordnen"), new BuchungMitgliedskontoZuordnungAction(
        control), "human_folder_public.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Kontoauszug zuordnen"),
        new BuchungKontoauszugZuordnungAction(control), "zuordnung.png"));
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."), new BuchungDeleteAction(),
        "user-trash.png"));
  }
}
