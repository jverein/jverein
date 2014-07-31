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
import de.jost_net.JVerein.gui.action.HibiscusKontenImportAction;
import de.jost_net.JVerein.gui.action.KontoAction;
import de.jost_net.JVerein.gui.control.KontoControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;

public class KontoListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Konten");

    KontoControl control = new KontoControl(this);

    control.getKontenList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.KONTEN, false, "help-browser.png");
    buttons.addButton("Hibiscus-Konten-Import", new HibiscusKontenImportAction(
        control), null, false, "go.png");
    buttons.addButton("neu", new KontoAction(), null, false, "document-new.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Konten</span></p>"
        + "<p>Konten können entweder von Hibuscus übernommen werden oder durch einen "
        + "Klick auf neu aufgenommen werden.</p>"
        + "<p>Durch einen Rechtsklick auf ein Konto kann entweder der Anfangsbestand des "
        + "Kontos zu einem Zeitpunkt eingegeben werden oder das Konto kann gelöscht werden, "
        + "sofern keine Buchungen für das Konto existieren.</p></form>";
  }
}
