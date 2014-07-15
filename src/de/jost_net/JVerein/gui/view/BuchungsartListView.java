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

import de.jost_net.JVerein.gui.action.BuchungsartAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsartControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;

public class BuchungsartListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Buchungsarten");

    BuchungsartControl control = new BuchungsartControl(this);

    control.getBuchungsartList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGSART, false, "help-browser.png");
    buttons.addButton(control.getPDFAusgabeButton());
    buttons.addButton("neu", new BuchungsartAction(), null, false,
        "document-new.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchungsart</span></p>"
        + "<p>Die Nummer und die Bezeichung der Buchungsart sind zu erfassen.</p>"
        + "<p>Bei der Vergabe der Nummern sollten Nummernkreise für Eingaben "
        + "und Ausgaben gebildet werden. Beispielsweise die 1000er Nummern "
        + "für Einnahmen und die 2000er Nummern für Ausgaben. Die Sortierung "
        + "der Buchungsauswertung erfolgt nach diesen Nummern.</p>" + "</form>";
  }
}
