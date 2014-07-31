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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class AuswertungAdresseView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Auswertung Adressen");

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Filter");

    ColumnLayout cl = new ColumnLayout(group.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cl.getComposite());

    left.addInput(control.getAdresstyp());
    left.addInput(control.getEigenschaftenAuswahl());

    if (Einstellungen.getEinstellung().hasZusatzfelder())
    {
      left.addInput(control.getZusatzfelderAuswahl());
    }
    left.addInput(control.getGeburtsdatumvon());
    left.addInput(control.getGeburtsdatumbis());

    left.addInput(control.getMailauswahl());

    SimpleContainer right = new SimpleContainer(cl.getComposite());

    right.addInput(control.getAusgabe());
    right.addInput(control.getAuswertungUeberschrift());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.AUSWERTUNGMITGLIEDER, false, "help-browser.png");
    buttons.addButton(control.getStartAdressAuswertungButton());
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Auswertung Adressen</span></p>"
        + "<p>Der Adressbestand kann nach verschiedenen Kriterien ausgewertet werden. </p>"
        + "<p>Als Ausgabeformate stehen PDF und CSV zur Verfügung.</p>"
        + "</form>";
  }
}
