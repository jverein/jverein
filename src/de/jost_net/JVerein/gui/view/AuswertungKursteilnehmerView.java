/**********************************************************************
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

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class AuswertungKursteilnehmerView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Auswertung Kursteilnehmer");

    final KursteilnehmerControl control = new KursteilnehmerControl(this);

    LabelGroup grAbu = new LabelGroup(getParent(), "Abbuchungsdatum");
    grAbu.addLabelPair("von", control.getAbbuchungsdatumvon());
    grAbu.addLabelPair("bis", control.getAbbuchungsdatumbis());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.AUSWERTUNGKURSTEILNEHMER, false, "help-browser.png");
    buttons.addButton(control.getStartAuswertungButton());
    buttons.paint(this.getParent());

  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Auswertung Kursteilnehmer</span></p>"
        + "<p>Die Kursteilnehmer eines vorgegebenen Zeitraums können im PDF-Format ausgegeben werden.</p>"
        + "</form>";
  }
}
