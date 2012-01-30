/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsuebernahmeControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class BuchungsUebernahmeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(
        JVereinPlugin.getI18n().tr("Buchungen aus Hibiscus übernehmen"));

    final BuchungsuebernahmeControl control = new BuchungsuebernahmeControl(
        this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Auswahl"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Konto"), control.getKonto());
    ButtonArea suchButton = new ButtonArea(group.getComposite(), 1);
    suchButton.addButton(control.getSuchButton());

    if (control.getKonto().getValue() != null)
    {
      control.getBuchungsList().paint(this.getParent());
    }
    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGENAUSHIBISCUS,
        false, "help-browser.png");
    buttons.addButton(control.getUebernahmeButton());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchungen aus Hibiscus übernehmen</span></p>"
        + "<p>Neue Buchungen (Umsätze) aus Hibiscus können übernommen werden.</p></form>";
  }
}
