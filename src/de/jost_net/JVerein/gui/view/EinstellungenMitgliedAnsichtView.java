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
import de.jost_net.JVerein.gui.control.EinstellungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class EinstellungenMitgliedAnsichtView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Einstellungen Mitglied Ansicht");

    final EinstellungControl control = new EinstellungControl(this);

    ScrolledContainer cont = new ScrolledContainer(getParent());

    ColumnLayout colsMitglied = new ColumnLayout(cont.getComposite(), 2);
    SimpleContainer leftMitglied = new SimpleContainer(
        colsMitglied.getComposite());

    leftMitglied.addLabelPair("Anzahl Spalten Stammdaten",
        control.getAnzahlSpaltenStammdatenInput());

    leftMitglied.addLabelPair("Anzahl Spalten Mitgliedschaft",
        control.getAnzahlSpaltenMitgliedschaftInput());

    leftMitglied.addLabelPair("Anzahl Spalten Zahlung",
        control.getAnzahlSpaltenZahlungInput());

    leftMitglied.addLabelPair("Anzahl Spalten Zusatzfelder",
        control.getAnzahlSpaltenZusatzfelderInput());

    if (Einstellungen.getEinstellung().getUseLesefelder())
      leftMitglied.addLabelPair("Anzahl Spalten Lesefelder",
          control.getAnzahlSpaltenLesefelderInput());

    cont.addHeadline("In Tab anzeigen");
    ColumnLayout colsTab = new ColumnLayout(cont.getComposite(), 2);
    SimpleContainer leftTab = new SimpleContainer(colsTab.getComposite());

    leftTab.addLabelPair("Zeige Stammdaten in Tab",
        control.getZeigeStammdatenInTabCheckbox());

    leftTab.addLabelPair("Zeige Mitgliedschaft in Tab",
        control.getZeigeMitgliedschaftInTabCheckbox());

    leftTab.addLabelPair("Zeige Zahlung in Tab",
        control.getZeigeZahlungInTabCheckbox());

    if (Einstellungen.getEinstellung().getZusatzbetrag())
      leftTab.addLabelPair("Zeige Zusatzbeträge in Tab",
          control.getZeigeZusatzbetrageInTabCheckbox());

    leftTab.addLabelPair("Zeige Mitgliedskonto in Tab",
        control.getZeigeMitgliedskontoInTabCheckbox());

    if (Einstellungen.getEinstellung().getVermerke())
      leftTab.addLabelPair("Zeige Vermerke in Tab",
          control.getZeigeVermerkeInTabCheckbox());

    if (Einstellungen.getEinstellung().getWiedervorlage())
      leftTab.addLabelPair("Zeige Wiedervorlage in Tab",
          control.getZeigeWiedervorlageInTabCheckbox());

    leftTab.addLabelPair("Zeige Mails in Tab",
        control.getZeigeMailsInTabCheckbox());

    leftTab.addLabelPair("Zeige Eigenschaften in Tab",
        control.getZeigeEigenschaftenInTabCheckbox());

    leftTab.addLabelPair("Zeige Zusatzfelder in Tab",
        control.getZeigeZusatzfelderInTabCheckbox());

    if (Einstellungen.getEinstellung().getLehrgaenge())
      leftTab.addLabelPair("Zeige Lehrgänge in Tab",
          control.getZeigeLehrgaengeInTabCheckbox());

    if (Einstellungen.getEinstellung().getMitgliedfoto())
      leftTab.addLabelPair("Zeige Foto in Tab",
          control.getZeigeFotoInTabCheckbox());

    if (Einstellungen.getEinstellung().getUseLesefelder())
      leftTab.addLabelPair("Zeige Lesefelder in Tab",
          control.getZeigeLesefelderInTabCheckbox());

    if (Einstellungen.getEinstellung().getArbeitseinsatz())
      leftTab.addLabelPair("Zeige Arbeitseinsatz in Tab",
          control.getZeigeArbeitseinsatzInTabCheckbox());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.EINSTELLUNGEN, false, "help-browser.png");
    buttons.addButton("speichern", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        control.handleStoreMitgliedAnsicht();
      }
    }, null, true, "document-save.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Einstellungen Mitglied Ansicht</span></p>"
        + "</form>";
  }
}
