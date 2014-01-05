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
import de.jost_net.JVerein.gui.action.StatistikJahrgaengeExportAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;

public class StatistikJahrgaengeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Statistik Jahrgänge");

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Parameter");
    group.addLabelPair("Jahr", control.getJubeljahr());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.JUBILAEEN, false, "help-browser.png");
    Button btnStart = new Button("Start",
        new StatistikJahrgaengeExportAction(), control.getJubeljahr(), true,
        "go.png");
    if (!Einstellungen.getEinstellung().getGeburtsdatumPflicht())
    {
      btnStart.setEnabled(false);
      Application
          .getMessagingFactory()
          .sendMessage(
              new StatusBarMessage(
                  "Einstellungen->Anzeige->Geburtsdatum: keine Pflicht. Die Statistik kann nicht erstellt werden.",
                  StatusBarMessage.TYPE_ERROR));

    }
    buttons.addButton(btnStart);

    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Statistik Jahrgänge</span></p>"
        + "<p>Ausgabe von Jahrgangszahlen z. B. für die DOSB-Statistik.</p></form>";
  }
}
