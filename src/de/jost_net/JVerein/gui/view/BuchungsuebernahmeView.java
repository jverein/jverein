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
import de.jost_net.JVerein.gui.control.BuchungsuebernahmeControl;
import de.jost_net.JVerein.io.Buchungsuebernahme;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;

public class BuchungsuebernahmeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Hibiscus-Buchungen übernehmen");

    final BuchungsuebernahmeControl control = new BuchungsuebernahmeControl(
        this);
    
    control.getKontenList().paint(this.getParent());


    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGSUEBERNAHME, false, "help-browser.png");
    buttons.addButton("Übernahme", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        new Buchungsuebernahme();
      }
    }, null, true, "document-save.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Adresstypen</span></p>"
        + "<p>JVerein gibt die Adresstypen Mitglied und Spender automatisch vor. Weitere Adresstypen "
        + "(Beispiele: Lieferanten, Trainer) können eingerichtet werden.</p>"
        + "</form>";
  }
}
