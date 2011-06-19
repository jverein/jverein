/**********************************************************************
 * $Source$
 *  * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.12  2011-04-07 19:35:47  jost
 * Neue Zurückbutton-Mimik aus Jameica
 *
 * Revision 1.11  2011-01-15 09:46:47  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.10  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.9  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.8  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.7  2009/07/27 15:26:17  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.6  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.5  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.4  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.3  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.2  2008/01/01 19:53:33  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.1  2007/05/07 19:26:35  jost
 * Neu: Wiedervorlage
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.control.WiedervorlageControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class WiedervorlageView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Wiedervorlage"));
    final WiedervorlageControl control = new WiedervorlageControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Wiedervorlage"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Datum"),
        control.getDatum(true));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Vermerk"),
        control.getVermerk());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Erledigung"),
        control.getErledigung());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.WIEDERVORLAGE, false,
        "help-browser.png");
    buttons.addButton("Mitglied", new MitgliedDetailAction(), control
        .getWiedervorlage().getMitglied(), false, "system-users.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
    buttons.paint(getParent());
  }

  // TODO getHelp()

}
