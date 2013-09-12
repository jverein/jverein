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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.action.BuchungImportAction;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.control.BuchungsHeaderControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.TabGroup;

public class BuchungslisteView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Buchungen");
    group.addLabelPair("Konto", control.getSuchKonto());

    TabFolder folder = new TabFolder(getParent(), SWT.V_SCROLL | SWT.BORDER);
    folder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    folder.setBackground(Color.BACKGROUND.getSWTColor());
    {
      TabGroup tabAllgemein = new TabGroup(folder, "Suche Buchungen", true, 2);
      tabAllgemein.addLabelPair("Buchungsart", control.getSuchBuchungsart());
      tabAllgemein.addLabelPair("Projekt", control.getSuchProjekt());
      tabAllgemein.addLabelPair("von Datum", control.getVondatum());
      tabAllgemein.addLabelPair("bis Datum", control.getBisdatum());
      tabAllgemein.addLabelPair("enthaltener Text", control.getSuchtext());
    }
    {
      final BuchungsHeaderControl headerControl = new BuchungsHeaderControl(
          this, control);
      TabGroup tabKonto = new TabGroup(folder, "Konto Kenndaten", true, 4);
      tabKonto.addLabelPair("Konto:", headerControl.getKontoNameInput());
      tabKonto.addLabelPair("Vorjahr", new LabelInput(""));

      tabKonto.addLabelPair("Anfangssalto:",
          headerControl.getAktJahrAnfangsSaltoInput());
      tabKonto.addLabelPair("Anfangssalto:",
          headerControl.getVorJahrAnfangsSaltoInput());

      tabKonto.addLabelPair("Einnahmen:",
          headerControl.getAktJahrEinnahmenInput());
      tabKonto.addLabelPair("Einnahmen:",
          headerControl.getVorJahrEinnahmenInput());

      tabKonto.addLabelPair("Ausgaben:",
          headerControl.getAktJahrAusgabenInput());
      tabKonto.addLabelPair("Ausgaben:",
          headerControl.getVorJahrAusgabenInput());

      tabKonto.addLabelPair("Salto:", headerControl.getAktJahrSaltoInput());
      tabKonto.addLabelPair("Salto:", headerControl.getVorJahrSaltoInput());
    }

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
    buttons.addButton("neu", new BuchungNeuAction(), control, false,
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
