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
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.keys.Formularart;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class MitgliedskontoRechnungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Rechnung"));

    final MitgliedskontoControl control = new MitgliedskontoControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Parameter"));
    if (this.getCurrentObject() == null)
    {
      group.addLabelPair(JVereinPlugin.getI18n().tr("von Datum"),
          control.getVondatum(MitgliedskontoControl.DATUM_RECHNUNG));
      group.addLabelPair(JVereinPlugin.getI18n().tr("bis Datum"),
          control.getBisdatum(MitgliedskontoControl.DATUM_RECHNUNG));
      group.addLabelPair(JVereinPlugin.getI18n().tr("ohne Abbucher"),
          control.getOhneAbbucher());
    }

    group.addLabelPair(JVereinPlugin.getI18n().tr("Formular"),
        control.getFormular(Formularart.RECHNUNG));

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.RECHNUNG, false,
        "help-browser.png");
    buttons.addButton(control.getStartRechnungButton(this.getCurrentObject()));
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Rechnungen ausgeben</span></p>"
        + "<p>Für den vorgegebenen Zeitraum werden die Rechnungen ausgegeben.</p>"
        + "</form>";
  }
}
