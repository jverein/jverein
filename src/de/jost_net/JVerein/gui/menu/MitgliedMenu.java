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
 * Revision 1.10  2011-06-06 19:17:27  jost
 * Neu: Funktion zur gleichzeitigen Zuordnung einer Eigenschaft an viele Mitglieder
 *
 * Revision 1.9  2011-04-23 06:56:19  jost
 * Neu: Freie Formulare
 *
 * Revision 1.8  2011-02-03 22:02:08  jost
 * Bugfix Kontextmenu
 *
 * Revision 1.7  2010-09-01 05:57:20  jost
 * neu: Personalbogen
 *
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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.FreiesFormularAction;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedEigenschaftZuordnungAction;
import de.jost_net.JVerein.gui.action.MitgliedKopierenAction;
import de.jost_net.JVerein.gui.action.MitgliedMailSendenAction;
import de.jost_net.JVerein.gui.action.PersonalbogenAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungAction;
import de.jost_net.JVerein.keys.Formularart;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
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
   * 
   * @throws RemoteException
   */
  public MitgliedMenu(Action detailaction) throws RemoteException
  {
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "bearbeiten"), detailaction, "edit.png"));
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "kopieren"), new MitgliedKopierenAction(), "copy_edit.gif"));
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "l�schen..."), new MitgliedDeleteAction(), "user-trash.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Mail senden ..."), new MitgliedMailSendenAction(),
        "mail-message-new.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Eigenschaften"), new MitgliedEigenschaftZuordnungAction(),
        "settings.gif"));
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "Spendenbescheinigung"), new SpendenbescheinigungAction(),
        "rechnung.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Personalbogen"), new PersonalbogenAction(), "rechnung.png"));
    DBIterator it = Einstellungen.getDBService().createList(Formular.class);
    it.addFilter("art = ?", new Object[] { Formularart.FREIESFORMULAR});
    while (it.hasNext())
    {
      Formular f = (Formular) it.next();
      addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
          f.getBezeichnung()), new FreiesFormularAction(f.getID()),
          "rechnung.png"));
    }
  }
}
