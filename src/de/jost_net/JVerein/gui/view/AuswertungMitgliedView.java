/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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
    GUI.getView().setTitle(
        JVereinPlugin.getI18n().tr("Auswertung Mitgliedsdaten"));

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Filter"));

    ColumnLayout cl = new ColumnLayout(group.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cl.getComposite());

    Input mitglstat = control.getMitgliedStatus();
    left.addInput(mitglstat);

    left.addInput(control.getEigenschaftenAuswahl());

    if (Einstellungen.getEinstellung().hasZusatzfelder())
    {
      left.addInput(control.getZusatzfelderAuswahl());
    }
    left.addInput(control.getGeburtsdatumvon());
    left.addInput(control.getGeburtsdatumbis());

    if (Einstellungen.getEinstellung().getSterbedatum())
    {
      left.addInput(control.getSterbedatumvon());
      left.addInput(control.getSterbedatumbis());
    }

    SelectInput inpGeschlecht = control.getGeschlecht();
    inpGeschlecht.setMandatory(false);
    left.addInput(inpGeschlecht);

    left.addInput(control.getOhneMail());

    SimpleContainer right = new SimpleContainer(cl.getComposite());
    right.addInput(control.getEintrittvon());
    right.addInput(control.getEintrittbis());

    right.addInput(control.getAustrittvon());
    right.addInput(control.getAustrittbis());

    right.addInput(control.getBeitragsgruppeAusw());

    right.addInput(control.getAusgabe());
    right.addInput(control.getSortierung());
    right.addInput(control.getAuswertungUeberschrift());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.AUSWERTUNGMITGLIEDER,
        false, "help-browser.png");
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
