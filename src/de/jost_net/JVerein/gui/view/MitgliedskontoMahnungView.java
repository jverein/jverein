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

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.keys.Formularart;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class MitgliedskontoMahnungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Mahnung");

    final MitgliedskontoControl control = new MitgliedskontoControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Parameter");
    if (this.getCurrentObject() == null)
    {
      group.addLabelPair("von Datum",
          control.getVondatum(MitgliedskontoControl.DATUM_MAHNUNG));
      group.addLabelPair("bis Datum",
          control.getBisdatum(MitgliedskontoControl.DATUM_MAHNUNG));
    }
    group.addLabelPair("Formular", control.getFormular(Formularart.MAHNUNG));
    control.getDifferenz("Fehlbetrag");

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.MAHNUNG, false, "help-browser.png");
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
