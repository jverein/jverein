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
import de.jost_net.JVerein.gui.control.DokumentControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;

public class DokumentView extends AbstractView
{

  private String verzeichnis;

  public DokumentView(String verzeichnis)
  {
    this.verzeichnis = verzeichnis;
  }

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Dokument");

    final DokumentControl control = new DokumentControl(this, verzeichnis, true);

    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    LabelGroup grDokument = new LabelGroup(scrolled.getComposite(), "Dokument");
    grDokument.addLabelPair("Datei", control.getDatei());
    grDokument.addLabelPair("Datum", control.getDatum());
    grDokument.addLabelPair("Bemerkung", control.getBemerkung());
    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGEN, false, "help-browser.png");
    buttons.addButton(control.getSpeichernButton(verzeichnis + "."));
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Dokument</span></p>";
  }
}
