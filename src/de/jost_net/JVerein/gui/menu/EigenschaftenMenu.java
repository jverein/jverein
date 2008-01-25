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

import de.jost_net.JVerein.gui.action.EigenschaftenDeleteAction;
import de.jost_net.JVerein.gui.action.EigenschaftenNewAction;
import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;

/**
 * Kontext-Menu zu den Eigenschaften.
 */
public class EigenschaftenMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Zusatzabbuchungen.
   */
  public EigenschaftenMenu(EigenschaftenControl control, Mitglied m)
  {
    addItem(new ContextMenuItem("Neu ...", new EigenschaftenNewAction(control,
        m)));
    addItem(new CheckedContextMenuItem("Löschen", new EigenschaftenDeleteAction(
        control)));
  }
}
