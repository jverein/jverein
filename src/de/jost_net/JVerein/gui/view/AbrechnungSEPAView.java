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
import de.jost_net.JVerein.gui.control.AbrechnungSEPAControl;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class AbrechnungSEPAView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Abrechnung");

    final AbrechnungSEPAControl control = new AbrechnungSEPAControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Parameter");
    group.addLabelPair("Modus", control.getAbbuchungsmodus());
    group.addLabelPair("Fälligkeit SEPA (Erst-/Einzel-Lastschrift)",
        control.getFaelligkeit1());
    group.addLabelPair("Fälligkeit SEPA (Folge-/Letzte-Lastschrift)",
        control.getFaelligkeit2());
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.FLEXIBEL)
    {
      group.addLabelPair("Abrechnungsmonat", control.getAbrechnungsmonat());
    }
    group.addLabelPair("Stichtag", control.getStichtag());
    group.addLabelPair("Von Eingabedatum", control.getVondatum());
    group
        .addLabelPair("Zahlungsgrund für Beiträge", control.getZahlungsgrund());
    group.addLabelPair("Zusatzbeträge", control.getZusatzbetrag());
    if (!Einstellungen.getEinstellung().getZusatzbetrag())
    {
      control.getZusatzbetrag().setEnabled(false);
    }
    group.addLabelPair("Kursteilnehmer", control.getKursteilnehmer());
    group.addLabelPair("Kompakte Abbuchung", control.getKompakteAbbuchung());
    group.addLabelPair("SEPA-Datei drucken", control.getSEPAPrint());

    if (!Einstellungen.getEinstellung().getKursteilnehmer())
    {
      control.getKursteilnehmer().setEnabled(false);
    }
    group.addLabelPair("Abbuchungsausgabe", control.getAbbuchungsausgabe());
    group.addSeparator();
    group
        .addText(
            "*) für die Berechnung, ob ein Mitglied bereits eingetreten oder ausgetreten ist. ",
            true);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ABRECHNUNG, false, "help-browser.png");
    buttons.addButton("Fehler/Warnungen/Hinweise", new Action()
    {

      @Override
      public void handleAction(Object context) 
      {
        GUI.startView(SEPABugsView.class.getName(), null);
      }
    }, null, false, "debug_exc.gif");
    buttons.addButton(control.getStartButton());
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Abbuchung</span> </p>"
        + "<p>Zunächst ist der Modus auszuwählen. Die Auswahlmöglichkeiten "
        + "richten sich nach dem ausgewählten Beitragsmodell (siehe Einstellungen).</p>"
        + "<p>Der Stichtag wird  zur Prüfung herangezogen, ob die Mitgliedschaft schon/noch besteht "
        + "und damit eine Abrechnung generiert muss. Liegt das Eintrittsdatum vor dem Stichtag und "
        + "das Austrittsdatum nach dem Stichtag, wird das Mitglied berücksichtigt.</p>"
        + "<p>Der angegebene Verwendungszweck wird bei allen Mitgliedsbeitrags-Buchungen "
        + "eingetragen. </p>"
        + "<p>  Es kann angegeben werden, ob Zusatzabbuchungen und Kursteilnehmer berücksichtigt "
        + " werden sollen.</p></form>";
  }
}
