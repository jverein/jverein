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

import java.util.Date;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class KontoauszugView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Kontoauszug");

    final MitgliedskontoControl control = new MitgliedskontoControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Zeitraum");
    group.addLabelPair("von", control.getVondatum("kontoauszug"));
    group.addLabelPair("bis", control.getBisdatum("kontoauszug"));

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.KONTOAUSZUG, false, "help-browser.png");
    buttons.addButton(control.getStartKontoauszugButton(
        this.getCurrentObject(),
        (Date) control.getVondatum("kontoauszug").getValue(),
        (Date) control.getBisdatum("kontoauszug").getValue()));
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Kontoauszug</span></p>"
        + "<p>Alle Buchungen des Mitgliedskontos werden in einem Kontoauszug im PDF-Format ausgegegen.</p>"
        + "</form>";
  }
}
