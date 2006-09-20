/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class AuswertungMitgliedView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Auswertung Mitgliedsdaten");

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup grGeburt = new LabelGroup(getParent(), "Geburtsdatum");
    grGeburt.addLabelPair("von", control.getGeburtsdatumvon());
    grGeburt.addLabelPair("bis", control.getGeburtsdatumbis());

    LabelGroup grEintritt = new LabelGroup(getParent(), "Eintrittsdatum");
    grEintritt.addLabelPair("von", control.getEintrittvon());
    grEintritt.addLabelPair("bis", control.getEintrittbis());

    LabelGroup grAustritt = new LabelGroup(getParent(), "Austrittsdatum");
    grAustritt.addLabelPair("von", control.getAustrittvon());
    grAustritt.addLabelPair("bis", control.getAustrittbis());

    LabelGroup grBeitragsgruppe = new LabelGroup(getParent(), "Beitragsgruppe");
    grBeitragsgruppe
        .addLabelPair("Beitragsgruppe", control.getBeitragsgruppe());

    LabelGroup grAusgabe = new LabelGroup(getParent(), "Ausgabe");
    grAusgabe.addLabelPair("Ausgabe", control.getAusgabe());
    grAusgabe.addLabelPair("Sortierung", control.getSortierung());

    ButtonArea buttons = new ButtonArea(getParent(), 4);

    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton(control.getStartAuswertungButton());

  }

  public void unbind() throws ApplicationException
  {
  }
}
