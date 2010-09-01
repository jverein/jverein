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
 * Revision 1.6  2010/02/01 20:59:57  jost
 * Neu: Einfache Mailfunktion
 *
 * Revision 1.5  2009/06/11 21:03:02  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2008/12/22 21:14:57  jost
 * Icons ins Menü aufgenommen.
 *
 * Revision 1.3  2008/07/18 20:11:31  jost
 * Neu: Spendenbescheinigung
 *
 * Revision 1.2  2007/08/31 05:36:00  jost
 * Jetzt auch bearbeiten über das Context-Menü
 *
 * Revision 1.1  2007/08/30 19:48:53  jost
 * Neues Kontext-Menü
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.MitgliedMailSendenAction;
import de.jost_net.JVerein.gui.action.PersonalbogenAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungAction;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.CheckedSingleContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

/**
 * Kontext-Menu zu den Beitragsgruppen.
 */
public class MitgliedMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu f�r die Liste der Mitglieder.
   */
  public MitgliedMenu()
  {
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "bearbeiten"), new MitgliedDetailAction(), "edit.png"));
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "l�schen..."), new MitgliedDeleteAction(), "user-trash.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Mail senden ..."), new MitgliedMailSendenAction(),
        "mail-message-new.png"));
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "Spendenbescheinigung"), new SpendenbescheinigungAction(),
        "rechnung.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Personalbogen"), new PersonalbogenAction(), "rechnung.png"));
  }
}
