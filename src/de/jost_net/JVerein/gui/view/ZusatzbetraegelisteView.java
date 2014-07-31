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
import de.jost_net.JVerein.gui.control.ZusatzbetragControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class ZusatzbetraegelisteView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Liste der Zusatzbeträge");

    final ZusatzbetragControl control = new ZusatzbetragControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Ausführungstag");
    group.addLabelPair("Ausführungstag", control.getAusfuehrungSuch());

    control.getZusatzbetraegeList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(control.getPDFAusgabeButton());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ZUSATZBETRAEGE, false, "help-browser.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Liste der Zusatzbeträge</span></p>"
        + "<p>Anzeige der Zusatzbeträge. Filterung auf alle, aktive, noch nicht ausgeführte und zu einem "
        + "bestimmten Datum ausgeführte Zusatzbeträge. Die angezeigte Liste kann als PDF-Dokument "
        + "ausgegeben werden.</p></form>";
  }
}
