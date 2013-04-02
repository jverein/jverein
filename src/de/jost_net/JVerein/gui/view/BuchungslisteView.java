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

import de.jost_net.JVerein.gui.action.BuchungImportAction;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class BuchungslisteView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Liste der Buchungen");

    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Suche Buchungen");
    group.addLabelPair("Konto", control.getSuchKonto());
    group.addLabelPair("Buchungsart", control.getSuchBuchungsart());
    group.addLabelPair("Projekt", control.getSuchProjekt());
    group.addLabelPair("von Datum", control.getVondatum());
    group.addLabelPair("bis Datum", control.getBisdatum());
    group.addLabelPair("enthaltener Text", control.getSuchtext());

    control.getBuchungsList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGEN, false, "help-browser.png");
    buttons.addButton("Import", new BuchungImportAction(), null, false,
        "import_obj.gif");
    buttons.addButton(control.getStartCSVAuswertungButton());
    buttons.addButton(control.getStartAuswertungBuchungsjournalButton());
    buttons.addButton(control.getStartAuswertungEinzelbuchungenButton());
    buttons.addButton(control.getStartAuswertungSummenButton());
    buttons.addButton("neu", new BuchungNeuAction(), null, false,
        "document-new.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchungen</span></p>"
        + "<p>Alle Buchungen aus dem vorgegebenen Zeitraum werden angezeigt. Durch einen "
        + "Doppelklick auf eine Buchung kann die Buchungsart zugeordnet werden.</p>"
        + "</form>";
  }
}
