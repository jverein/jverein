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
 * Revision 1.8  2011-04-07 19:35:47  jost
 * Neue Zurückbutton-Mimik aus Jameica
 *
 * Revision 1.7  2011-01-15 09:46:48  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.6  2010-10-15 09:58:24  jost
 * Code aufgeräumt
 *
 * Revision 1.5  2010-10-07 19:49:23  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.4  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.3  2010-08-16 20:17:47  jost
 * Neu: Mahnung
 *
 * Revision 1.2  2010-08-15 19:01:42  jost
 * Rechnungen auch für einen vorgegebenen Zeitraum ausgeben.
 *
 * Revision 1.1  2010-08-04 10:41:27  jost
 * Prerelease Rechnung
 *
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
