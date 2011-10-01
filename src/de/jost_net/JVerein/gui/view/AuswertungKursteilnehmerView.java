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
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class AuswertungKursteilnehmerView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(
        JVereinPlugin.getI18n().tr("Auswertung Kursteilnehmer"));

    final KursteilnehmerControl control = new KursteilnehmerControl(this);

    LabelGroup grAbu = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Abbuchungsdatum"));
    grAbu.addLabelPair(JVereinPlugin.getI18n().tr("von"),
        control.getAbbuchungsdatumvon());
    grAbu.addLabelPair(JVereinPlugin.getI18n().tr("bis"),
        control.getAbbuchungsdatumbis());

    ButtonArea buttons = new ButtonArea(getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.AUSWERTUNGKURSTEILNEHMER,
        false, "help-browser.png");
    buttons.addButton(control.getStartAuswertungButton());

  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Auswertung Kursteilnehmer</span></p>"
        + "<p>Die Kursteilnehmer eines vorgegebenen Zeitraums können im PDF-Format ausgegeben werden.</p>"
        + "</form>";
  }
}
