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
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class AuswertungMitgliedView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Auswertung Mitgliedsdaten");

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Filter");

    ColumnLayout cl = new ColumnLayout(group.getComposite(), 3);

    // left
    SimpleContainer left = new SimpleContainer(cl.getComposite());
    Input mitglstat = control.getMitgliedStatus();
    left.addInput(mitglstat);
    left.addInput(control.getEigenschaftenAuswahl());
    left.addInput(control.getBeitragsgruppeAusw());

    if (Einstellungen.getEinstellung().hasZusatzfelder())
    {
      left.addInput(control.getZusatzfelderAuswahl());
    }

    // middle
    SimpleContainer middle = new SimpleContainer(cl.getComposite());
    middle.addInput(control.getMailauswahl());
    middle.addInput(control.getGeburtsdatumvon());
    middle.addInput(control.getGeburtsdatumbis());

    SelectInput inpGeschlecht = control.getGeschlecht();
    inpGeschlecht.setMandatory(false);
    middle.addInput(inpGeschlecht);

    // right
    SimpleContainer right = new SimpleContainer(cl.getComposite());
    right.addInput(control.getEintrittvon());
    right.addInput(control.getEintrittbis());

    right.addInput(control.getAustrittvon());
    right.addInput(control.getAustrittbis());
    right.addInput(control.getStichtag(false));

    if (Einstellungen.getEinstellung().getSterbedatum())
    {
      right.addInput(control.getSterbedatumvon());
      right.addInput(control.getSterbedatumbis());
    }

    // Zweite Gruppe: Ausgabe
    LabelGroup group2 = new LabelGroup(getParent(), "Ausgabe");

    ColumnLayout cl2 = new ColumnLayout(group2.getComposite(), 2);
    SimpleContainer left2 = new SimpleContainer(cl2.getComposite());
    SimpleContainer right2 = new SimpleContainer(cl2.getComposite());

    left2.addInput(control.getSortierung());
    left2.addInput(control.getAuswertungUeberschrift());
    right2.addInput(control.getAusgabe());

    // Button-Bereich
    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.AUSWERTUNGMITGLIEDER, false, "help-browser.png");
    buttons.addButton(control.getVorlagenCsvEditButton());
    buttons.addButton(control.getStartAuswertungButton());
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Auswertung Mitglieder</span></p>"
        + "<p>Der Mitgliederbestand kann nach Geburtsdatum, Eintrittsdatum, "
        + "Austrittsdatum und Beitragsgruppen selektiert werden. Werden "
        + "keine Angaben zum Austrittsdatum gemacht, werden nur Mitglieder "
        + "ausgewertet, die nicht ausgetreten sind.</p>"
        + "<p>Die Sortierung kann nach Name-Vorname, Eintrittsdatum oder "
        + "Austrittsdatum erfolgen.</p>"
        + "<p>Als Ausgabeformate stehen PDF und CSV zur Verfügung.</p>"
        + "</form>";
  }
}
