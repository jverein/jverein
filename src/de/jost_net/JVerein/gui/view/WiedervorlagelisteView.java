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
 * Revision 1.10  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.9  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.8  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.7  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.6  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.5  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.4  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.3  2008/01/01 19:53:22  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.2  2007/12/20 20:33:30  jost
 * Neu: Wiedervorlage-Ãœbersicht in der Jameica-Startseite
 *
 * Revision 1.1  2007/05/07 19:26:20  jost
 * Neu: Wiedervorlage
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.WiedervorlageListeAction;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.jost_net.JVerein.gui.parts.WiedervorlageList;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class WiedervorlagelisteView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Wiedervorlagen"));
    new WiedervorlageList(new WiedervorlageListeAction()).getWiedervorlageList().paint(
        this.getParent());
    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.WIEDERVORLAGE, false,
        "help-browser.png");

  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Wiedervorlage</span></p>"
        + "<p>In dieser Liste werden die Wiedervorlagen aller Mitglieder angezeigt. "
        + "Durch einen Rechtsklick kann entweder ein Erledigungsvermerk gesetzt werden "
        + "oder der Wiedervorlagetermin wird gelöscht.</p></form>";
  }
}
