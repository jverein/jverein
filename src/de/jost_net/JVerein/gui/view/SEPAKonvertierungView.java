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
import de.jost_net.JVerein.gui.control.SEPAKonvertierungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class SEPAKonvertierungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("SEPA-Konvertierung");

    final SEPAKonvertierungControl control = new SEPAKonvertierungControl(this);

    LabelGroup intgroup = new LabelGroup(getParent(), "Interne Konvertierung");
    ButtonArea intbuttons = new ButtonArea();
    intbuttons.addButton(control.getButtonInterneKonvertierung());
    intbuttons.paint(intgroup.getComposite());

    LabelGroup extgroup = new LabelGroup(getParent(), "Externe Konvertierung");
    ButtonArea extbuttons = new ButtonArea();
    extbuttons.addButton(control.getButtonExtExport());
    extbuttons.addButton(control.getButtonExtImport());
    extbuttons.paint(extgroup.getComposite());

    LabelGroup mandatdatum = new LabelGroup(getParent(),
        "Datum des Mandats setzen");
    mandatdatum.addInput(control.getMandatsdatum());
    ButtonArea mdButton = new ButtonArea();
    mdButton.addButton(control.getButtonMandatdatumSetzen());
    mdButton.paint(mandatdatum.getComposite());

    LabelGroup erggroup = new LabelGroup(getParent(), "Ergebnis", true);
    erggroup.addPart(control.getList());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.IMPORT, false, "help-browser.png");
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">SEPA-Konvertierung</span></p>"
        + "<p></p></form>";
  }
}
