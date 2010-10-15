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
 * Revision 1.4  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.3  2010-09-14 06:19:22  jost
 * Hilfe f. Mitgliedskonto
 *
 * Revision 1.2  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.1  2010-08-16 20:17:37  jost
 * Neu: Mahnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.jost_net.JVerein.keys.Formularart;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class MitgliedskontoMahnungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Mahnung"));

    final MitgliedskontoControl control = new MitgliedskontoControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Parameter"));
    if (this.getCurrentObject() == null)
    {
      group.addLabelPair(JVereinPlugin.getI18n().tr("von Datum"),
          control.getVondatum(MitgliedskontoControl.DATUM_MAHNUNG));
      group.addLabelPair(JVereinPlugin.getI18n().tr("bis Datum"),
          control.getBisdatum(MitgliedskontoControl.DATUM_MAHNUNG));
    }
    group.addLabelPair(JVereinPlugin.getI18n().tr("Formular"),
        control.getFormular(Formularart.MAHNUNG));
    control.getDifferenz("Fehlbetrag");

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.MAHNUNG, false,
        "help-browser.png");
    buttons.addButton(control.getStartMahnungButton(this.getCurrentObject()));
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Mahnungen ausgeben</span></p>"
        + "<p>Für den vorgegebenen Zeitraum werden die Mahnungen für die noch nicht bezahlten Beträge ausgegeben.</p>"
        + "</form>";
  }
}
