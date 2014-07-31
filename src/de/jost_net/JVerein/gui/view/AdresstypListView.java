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
import de.jost_net.JVerein.gui.action.AdresstypAction;
import de.jost_net.JVerein.gui.action.AdresstypDefaultAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.AdresstypControl;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;

public class AdresstypListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Adresstypen");

    AdresstypControl control = new AdresstypControl(this);

    control.getAdresstypList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ADRESSTYPEN, false, "help-browser.png");
    buttons.addButton("neu", new AdresstypAction(), null, false,
        "document-new.png");

    DBIterator it = Einstellungen.getDBService().createList(Adresstyp.class);
    it.addFilter("jvereinid >= 1 and jvereinid <= 2");
    if (it.size() == 0)
    {
      buttons.addButton("Default-Adresstypen einrichten",
          new AdresstypDefaultAction());
    }
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
