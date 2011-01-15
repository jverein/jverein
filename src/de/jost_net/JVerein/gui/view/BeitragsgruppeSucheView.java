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
 * Revision 1.12  2010-10-15 09:58:23  jost
 * Code aufgeräumt
 *
 * Revision 1.11  2010-10-07 19:49:23  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.10  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.9  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.8  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.7  2009/01/20 19:14:56  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.6  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.5  2008/01/01 19:49:05  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.4  2007/12/28 15:55:00  jost
 * Button-Leiste Ã¼berarbeitet.
 *
 * Revision 1.3  2007/08/30 19:49:18  jost
 * LÃ¶schung Ã¼ber Knopf
 *
 * Revision 1.2  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BeitragsgruppeDeleteAction;
import de.jost_net.JVerein.gui.action.BeitragsgruppeDetailAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BeitragsgruppeControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class BeitragsgruppeSucheView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Beitragsgruppen"));

    BeitragsgruppeControl control = new BeitragsgruppeControl(this);

    control.getBeitragsgruppeTable().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BEITRAGSGRUPPEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("löschen"),
        new BeitragsgruppeDeleteAction(), control.getBeitragsgruppeTable(),
        false, "user-trash.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new BeitragsgruppeDetailAction(), null, false, "document-new.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Beitragsgruppe</span></p>"
        + "<p>Alle Beitragsgruppen werden angezeigt. Durch einen Doppelklick kann eine "
        + "Beitragsgruppe zur Bearbeitung ausgewählt werden.</p></form>";
  }
}
