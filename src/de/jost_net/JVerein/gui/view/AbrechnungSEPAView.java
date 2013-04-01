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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.AbrechnunslaufListAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.AbrechnungSEPAControl;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class AbrechnungSEPAView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    if (Einstellungen.getEinstellung().getName() == null
        || Einstellungen.getEinstellung().getName().length() == 0
        || Einstellungen.getEinstellung().getIban() == null
        || Einstellungen.getEinstellung().getIban().length() == 0)
    {
      throw new ApplicationException(
          "Name des Vereins oder Bankverbindung fehlt.Bitte unter Administration|Einstellungen erfassen.");
    }
    if (Einstellungen.getEinstellung().getGlaeubigerID() == null
        || Einstellungen.getEinstellung().getGlaeubigerID().length() == 0)
    {
      throw new ApplicationException(
          "Gläubiger-ID fehlt. Gfls. unter https://extranet.bundesbank.de/scp/ oder http://www.oenb.at/idakilz/cid?lang=de beantragen und unter Administration|Einstellungen|Allgemein eintragen.\n"
              + "Zu Testzwecken kann DE98ZZZ09999999999 eingesetzt werden.");
    }

    DBIterator it = Einstellungen.getDBService().createList(Konto.class);
    it.addFilter("nummer = ?", Einstellungen.getEinstellung().getKonto());
    if (it.size() != 1)
    {
      throw new ApplicationException(
          JVereinPlugin
              .getI18n()
              .tr("Konto {0} ist in der Buchführung nicht eingerichtet. Menu: Buchführung | Konten",
                  Einstellungen.getEinstellung().getKonto()));
    }

    GUI.getView().setTitle("Abrechnung SEPA");

    final AbrechnungSEPAControl control = new AbrechnungSEPAControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Parameter"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Modus"),
        control.getAbbuchungsmodus());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Stichtag"),
        control.getStichtag());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Von Eingabedatum"),
        control.getVondatum());
    group.addLabelPair(
        JVereinPlugin.getI18n().tr("Zahlungsgrund für Beiträge"),
        control.getZahlungsgrund());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Zusatzbeträge"),
        control.getZusatzbetrag());
    if (!Einstellungen.getEinstellung().getZusatzbetrag())
    {
      control.getZusatzbetrag().setEnabled(false);
    }
    group.addLabelPair(JVereinPlugin.getI18n().tr("Kursteilnehmer"),
        control.getKursteilnehmer());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Kompakte Abbuchung"),
        control.getKompakteAbbuchung());
    // group.addLabelPair(JVereinPlugin.getI18n().tr("Dtaus-Datei drucken"),
    // control.getDtausPrint());

    if (!Einstellungen.getEinstellung().getKursteilnehmer())
    {
      control.getKursteilnehmer().setEnabled(false);
    }
    group.addLabelPair(JVereinPlugin.getI18n().tr("Abbuchungsausgabe"),
        control.getAbbuchungsausgabe());
    group.addSeparator();
    group
        .addText(
            JVereinPlugin
                .getI18n()
                .tr("*) für die Berechnung, ob ein Mitglied bereits eingetreten oder ausgetreten ist. "),
            true);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Rückgängig", new AbrechnunslaufListAction(), null,
        false, "edit-undo.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.ABRECHNUNG, false,
        "help-browser.png");
    buttons.addButton(control.getStartButton());
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return JVereinPlugin
        .getI18n()
        .tr("<form><p><span color=\"header\" font=\"header\">Abbuchung</span> </p>"
            + "<p>Zunächst ist der Modus auszuwählen. Die Auswahlmöglichkeiten "
            + "richten sich nach dem ausgewählten Beitragsmodell (siehe Einstellungen).</p>"
            + "<p>Der Stichtag wird  zur Prüfung herangezogen, ob die Mitgliedschaft schon/noch besteht "
            + "und damit eine Abrechnung generiert muss. Liegt das Eintrittsdatum vor dem Stichtag und "
            + "das Austrittsdatum nach dem Stichtag, wird das Mitglied berücksichtigt.</p>"
            + "<p>Der angegebene Verwendungszweck wird bei allen Mitgliedsbeitrags-Buchungen "
            + "eingetragen. </p>"
            + "<p>  Es kann angegeben werden, ob Zusatzabbuchungen und Kursteilnehmer berücksichtigt "
            + " werden sollen.</p></form>");
  }
}
