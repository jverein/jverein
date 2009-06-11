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
 * Revision 1.10  2009/01/25 10:58:39  jost
 * Icons aufgenommen.
 *
 * Revision 1.9  2009/01/10 19:27:23  jost
 * Jameica 1.8-Kompatibilität hergestellt.
 *
 * Revision 1.8  2009/01/04 16:27:44  jost
 * Neu: Für mehrere Buchungen gleichzeitig die Buchungsart festlegen.
 *
 * Revision 1.7  2008/12/22 21:12:25  jost
 * Icons ins MenÃ¼ aufgenommen.
 *
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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.gui.action.BuchungBuchungsartZuordnungAction;
import de.jost_net.JVerein.gui.action.BuchungDeleteAction;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.CheckedSingleContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.logging.Logger;

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
    // Work-Around: Jameica unterstütze in Version 1.7 den Konstruktor mit Icon
    // nicht.
    CheckedSingleContextMenuItem mnBearbeiten = new CheckedSingleContextMenuItem(
        JVereinPlugin.getI18n().tr("bearbeiten"), new BuchungAction());
    String icon = "edit.png";
    try
    {
      mnBearbeiten.setImage(SWTUtil.getImage(icon));
    }
    catch (Exception e)
    {
      Logger.warn("icon " + icon + " not found");
    }
    addItem(mnBearbeiten);

    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Buchungsart zuordnen"),
        new BuchungBuchungsartZuordnungAction(control), "zuordnung.png"));
    addItem(new CheckedContextMenuItem(
        JVereinPlugin.getI18n().tr("löschen..."), new BuchungDeleteAction(),
        "user-trash.png"));
  }
}
