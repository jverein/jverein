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
import de.jost_net.JVerein.gui.action.FormularAction;
import de.jost_net.JVerein.gui.control.FormularControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;

public class FormularListeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Formulare");

    FormularControl control = new FormularControl(this);

    control.getFormularList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.FORMULARE, false, "help-browser.png");
    buttons.addButton("neu", new FormularAction(), null, false,
        "document-new.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Formulare</span></p>"
        + "<p> Alle verfügbaren Formulare werden aufgelistet.</p>"
        + "<p>Durch einen Doppelklick auf ein Formular wird die Detailansicht zur "
        + "Bearbeitung geöffnet.</p>"
        + "<p> Mit einem rechten Mausklick öffnet sich ein Kontext-Menü. Damit können "
        + "die Formularfelder bearbeitet werden. Das Formular kann angezeigt und "
        + "gelöscht werden.</p></form>";
  }
}
