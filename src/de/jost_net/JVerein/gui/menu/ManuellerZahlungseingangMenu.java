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
 * Revision 1.5  2009/06/11 21:03:02  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2008/12/30 10:53:15  jost
 * Fehlende Icons ergÃ¤nzt.
 *
 * Revision 1.3  2008/12/22 21:14:45  jost
 * Icons ins MenÃ¼ aufgenommen.
 *
 * Revision 1.2  2007/03/20 07:56:54  jost
 * Probleme beim Encoding.
 *
 * Revision 1.1  2007/03/13 19:57:01  jost
 * Neu: Manueller Zahlungseingang.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.ManuellerZahlungseingangDatumLoeschenAction;
import de.jost_net.JVerein.gui.action.ManuellerZahlungseingangDatumSetzenAction;
import de.jost_net.JVerein.gui.action.ManuellerZahlungseingangDeleteAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.TablePart;

/**
 * Kontext-Menu zu den ManuellenZahlungseingängen.
 * @deprecated In Version 1.5 entfernen
 */
public class ManuellerZahlungseingangMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der ManuellenZahlungseingänge.
   */
  public ManuellerZahlungseingangMenu(TablePart table)
  {
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Zahlungseingangsdatum setzen ..."),
        new ManuellerZahlungseingangDatumSetzenAction(table),
        "office-calendar.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Zahlungseingangsdatum löschen ..."),
        new ManuellerZahlungseingangDatumLoeschenAction(table),
        "user-trash.png"));
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."),
        new ManuellerZahlungseingangDeleteAction(table), "user-trash.png"));
  }
}
