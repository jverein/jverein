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
 * Revision 1.6  2008/12/06 10:50:07  jost
 * Bearbeiten aufgenommen
 *
 * Revision 1.5  2008/05/22 06:50:44  jost
 * BuchfÃ¼hrung
 *
 * Revision 1.4  2008/03/16 07:36:10  jost
 * Reaktivierung BuchfÃ¼hrung
 *
 * Revision 1.2  2007/02/23 20:26:58  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/10/14 16:11:34  jost
 * Buchungen löschen eingeführt
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.gui.action.BuchungDeleteAction;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
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
    addItem(new ContextMenuItem("Neu", new BuchungNeuAction(),
        "document-new.png"));
    addItem(new CheckedContextMenuItem("Bearbeiten", new BuchungAction(),
        "edit.png"));
    addItem(new CheckedContextMenuItem("Löschen...", new BuchungDeleteAction(),
        "user-trash.png"));
  }
}
