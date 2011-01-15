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
 * Revision 1.15  2010-11-17 04:50:41  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 * Revision 1.14  2010-10-15 09:58:24  jost
 * Code aufgeräumt
 *
 * Revision 1.13  2010-10-07 19:49:24  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.12  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.11  2009/11/05 20:06:03  jost
 * Image auf Button
 *
 * Revision 1.10  2009/07/24 20:20:30  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.9  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.8  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.7  2009/01/20 19:14:45  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.6  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.5  2008/01/01 19:48:33  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.4  2007/03/25 17:00:12  jost
 * Beitragsart aufgenommen.
 *
 * Revision 1.3  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/29 07:49:11  jost
 * Redaktionelle Änderung
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BeitragsgruppeSucheAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BeitragsgruppeControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class BeitragsgruppeDetailView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Beitragsgruppe"));

    final BeitragsgruppeControl control = new BeitragsgruppeControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Beitrag"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung"), control
        .getBezeichnung(true));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"), control
        .getBetrag());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Beitragsart"), control
        .getBeitragsArt());

    if (Einstellungen.getEinstellung().getArbeitseinsatz())
    {
      LabelGroup groupAe = new LabelGroup(getParent(), JVereinPlugin.getI18n()
          .tr("Arbeitseinsatz"));
      groupAe.addLabelPair(JVereinPlugin.getI18n().tr("Stunden"), control
          .getArbeitseinsatzStunden());
      groupAe.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"), control
          .getArbeitseinsatzBetrag());
    }

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BEITRAGSGRUPPEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("suche"),
        new BeitragsgruppeSucheAction(), null, false, "system-search.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Beitragsgruppe</span></p>"
        + "<p>Die Bezeichung und die Höhe des Beitrages sind zu erfassen.</p>"
        + "<p>Durch die Kennzeichnung als Familientarif können dem Mitglied die Mitglieder "
        + "zugeordnet, die zur Famlie gehören.</p>" + "</form>";
  }

}
