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
 * Revision 1.4  2010-07-28 07:26:58  jost
 * deprecated
 *
 * Revision 1.3  2009/06/11 21:03:02  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2008/12/22 21:15:08  jost
 * Icons ins MenÃ¼ aufgenommen.
 *
 * Revision 1.1  2008/09/16 18:51:44  jost
 * Neu: Rechnung
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.RechnungDeleteAction;
import de.jost_net.JVerein.gui.action.RechnungAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

/**
 * Kontext-Menu zu den Rechnungen.
 * @deprecated In Version 1.5 ausmustern
 */
@Deprecated
public class RechungMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Rechnungen.
   */
  public RechungMenu()
  {
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n()
        .tr("Rechnung..."), new RechnungAction(), "rechnung.png"));
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."), new RechnungDeleteAction(),
        "user-trash.png"));
  }
}
