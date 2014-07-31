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
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoExportAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoExportAction.EXPORT_TYP;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.menu.Mitgliedskonto2Menu;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class MitgliedskontoListeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Mitgliedskonten");

    final MitgliedskontoControl control = new MitgliedskontoControl(this);
    LabelGroup group = new LabelGroup(getParent(), "Filter");
    group.addInput(control.getSuchName());
    group.addLabelPair("von",
        control.getVondatum(MitgliedskontoControl.DATUM_MITGLIEDSKONTO));
    group.addLabelPair("bis",
        control.getBisdatum(MitgliedskontoControl.DATUM_MITGLIEDSKONTO));
    group.addLabelPair("Differenz", control.getDifferenz());

    control.getMitgliedskontoList(new MitgliedDetailAction(),
        new Mitgliedskonto2Menu()).paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(new Button("Export", new MitgliedskontoExportAction(
        EXPORT_TYP.MITGLIEDSKONTO, null), control, false, "document-save.png"));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.MITGLIEDSKONTO_UEBERSICHT, false, "help-browser.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Liste der Mitgliedskonto-Soll-Buchungen</span></p>"
        + "<p>Auflistung aller Mitgliedskonto-Soll-Buchungen. Die Daten können nach Datum und "
        + "Namen (auch Namensfragmente) gefiltert werden.</p></form>";
  }
}
