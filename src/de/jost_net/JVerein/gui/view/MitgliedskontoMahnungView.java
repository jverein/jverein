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
import de.jost_net.JVerein.gui.action.MitgliedskontoExportAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoExportAction.EXPORT_TYP;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl.DIFFERENZ;
import de.jost_net.JVerein.keys.FormularArt;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class MitgliedskontoMahnungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Mahnung");

    final MitgliedskontoControl control = new MitgliedskontoControl(this);

    SimpleContainer cont = new SimpleContainer(getParent(), true);
    cont.addHeadline("Parameter");
    if (this.getCurrentObject() == null)
    {
      cont.addLabelPair("von Datum",
          control.getVondatum(MitgliedskontoControl.TYP.MAHNUNG.name()));
      cont.addLabelPair("bis Datum",
          control.getBisdatum(MitgliedskontoControl.TYP.MAHNUNG.name()));
    }
    cont.addLabelPair("Formular", control.getFormular(FormularArt.MAHNUNG));
    control.getDifferenz(DIFFERENZ.FEHLBETRAG);

    cont.addInput(control.getAusgabeart());

    cont.addHeadline("Mail");
    cont.addInput(control.getBetreff(MitgliedskontoControl.TYP.MAHNUNG.name()));
    cont.addLabelPair("Text",
        control.getTxt(MitgliedskontoControl.TYP.MAHNUNG.name()));

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.MAHNUNG, false, "help-browser.png");
    buttons.addButton(new Button("Export", new MitgliedskontoExportAction(
        EXPORT_TYP.MAHNUNGEN, getCurrentObject()), control, false,
        "document-save.png"));
    buttons.addButton(control.getStartMahnungButton(this.getCurrentObject()));
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Mahnungen ausgeben</span></p>"
        + "<p>Für den vorgegebenen Zeitraum werden die Mahnungen für die noch nicht bezahlten Beträge ausgegeben.</p>"
        + "</form>";
  }
}
