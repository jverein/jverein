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
import de.jost_net.JVerein.gui.action.FormularAnzeigeAction;
import de.jost_net.JVerein.gui.action.FormularfeldAction;
import de.jost_net.JVerein.gui.control.FormularfeldControl;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class FormularfelderListeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Formularfelder");

    FormularfeldControl control = new FormularfeldControl(this,
        (Formular) getCurrentObject());
    SimpleContainer labelContainer = new SimpleContainer(getParent(), false, 4);
    labelContainer.addLabelPair("Formulartyp:", control.getFormularTyp());
    labelContainer.addLabelPair("Formularname:", control.getFormularName());

    control.getFormularfeldList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.FORMULARE, false, "help-browser.png");
    buttons.addButton("anzeigen", new FormularAnzeigeAction(),
        getCurrentObject(), false, "edit.png");
    buttons.addButton("neu", new FormularfeldAction(), getCurrentObject(),
        false, "document-new.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Formularfelder</span></p>"
        + "<p>Jedem Formular müssen Formularfelder zugeordnet werden. Mit <i>neu</i> "
        + "wird ein neues Formularfeld aufgenommen. Mit einem Doppelklick öffnet "
        + "sich das Bearbeitungsfenster für ein Formularfeld. Durch einen Rechtsklick "
        + "erscheint ein Kontextmenü. Damit können Formularfelder gelöscht werden.</p>"
        + "</form>";
  }
}
