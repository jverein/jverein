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
 * Revision 1.15  2011-01-15 09:46:48  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.14  2010-10-15 09:58:25  jost
 * Code aufger�umt
 *
 * Revision 1.13  2010-10-07 19:49:24  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.12  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.11  2009/09/10 18:46:30  jost
 * Hilfe aufgenommen
 *
 * Revision 1.10  2009/09/10 18:18:09  jost
 * neu: Buchungsklassen
 *
 * Revision 1.9  2009/07/24 20:20:48  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.8  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.7  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.6  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.5  2008/05/22 06:52:37  jost
 * Buchführung
 *
 * Revision 1.4  2008/03/16 07:36:29  jost
 * Reaktivierung Buchführung
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
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsartControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class BuchungsartView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchungsart"));

    final BuchungsartControl control = new BuchungsartControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Buchungsart"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Nummer"),
        control.getNummer(true));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung"),
        control.getBezeichnung());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Art"), control.getArt());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsklasse"),
        control.getBuchungsklasse());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Spende"),
        control.getSpende());
    // TODO Jo Dokumentation nachpflegen

    ButtonArea buttons = new ButtonArea(getParent(), 3);

    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGSARTEN, false,
        "help-browser.png");

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
    return "<form><p><span color=\"header\" font=\"header\">Buchungsart</span></p></form>";
  }
}
