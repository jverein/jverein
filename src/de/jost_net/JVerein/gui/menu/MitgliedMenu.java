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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.FreiesFormularAction;
import de.jost_net.JVerein.gui.action.KontoauszugAction;
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
   * Erzeugt ein Kontext-Menu für die Liste der Mitglieder.
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
        "löschen..."), new MitgliedDeleteAction(), "user-trash.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Mail senden ..."), new MitgliedMailSendenAction(),
        "mail-message-new.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Eigenschaften"), new MitgliedEigenschaftZuordnungAction(),
        "settings.gif"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n()
        .tr("Kontoauszug"), new KontoauszugAction(), "rechnung.png"));
    addItem(new CheckedSingleContextMenuItem(JVereinPlugin.getI18n().tr(
        "Spendenbescheinigung"), new SpendenbescheinigungAction(),
        "rechnung.png"));
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
        "Personalbogen"), new PersonalbogenAction(), "rechnung.png"));
    DBIterator it = Einstellungen.getDBService().createList(Formular.class);
    it.addFilter("art = ?", new Object[] { Formularart.FREIESFORMULAR });
    while (it.hasNext())
    {
      Formular f = (Formular) it.next();
      addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr(
          f.getBezeichnung()), new FreiesFormularAction(f.getID()),
          "rechnung.png"));
    }
  }
}
