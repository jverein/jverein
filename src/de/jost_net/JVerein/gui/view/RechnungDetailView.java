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
 * Revision 1.8  2010-10-07 19:49:23  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.7  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.6  2009/10/25 07:35:18  jost
 * Icon auf Button
 *
 * Revision 1.5  2009/07/24 20:22:41  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.4  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.3  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.2  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.1  2008/09/16 18:52:18  jost
 * Neu: Rechnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.RechnungListeAction;
import de.jost_net.JVerein.gui.control.RechnungControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class RechnungDetailView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Rechnungsinformationen");

    final RechnungControl control = new RechnungControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Beitrag"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Zweck1"),
        control.getZweck1(true));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Zweck2"),
        control.getZweck2());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Datum"), control.getDatum());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"),
        control.getBetrag());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.RECHNUNG, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("s&uche"),
        new RechnungListeAction(), null, false, "system-search.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  // TODO getHelp()

}
