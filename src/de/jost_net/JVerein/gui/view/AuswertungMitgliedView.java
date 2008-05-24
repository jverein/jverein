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
 * $Log$
 * Revision 1.4  2008/01/27 09:42:22  jost
 * Erweiterung der Auswertung um Eigenschaften
 *
 * Revision 1.3  2008/01/01 19:48:12  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.2  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.DBSupportH2Impl;
import de.jost_net.JVerein.server.DBSupportMcKoiImpl;
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

    if (!JVereinDBService.SETTINGS.getString("database.driver",
        DBSupportH2Impl.class.getName()).equals(
        DBSupportMcKoiImpl.class.getName()))
    {
      LabelGroup grEigenschaften = new LabelGroup(getParent(), "Eigenschaften");
      grEigenschaften.addLabelPair("Eigenschaften", control
          .getEigenschaftenAuswahl());
    }

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
    grBeitragsgruppe.addLabelPair("Beitragsgruppe", control
        .getBeitragsgruppeAusw());

    LabelGroup grAusgabe = new LabelGroup(getParent(), "Ausgabe");
    grAusgabe.addLabelPair("Ausgabe", control.getAusgabe());
    grAusgabe.addLabelPair("Sortierung", control.getSortierung());

    ButtonArea buttons = new ButtonArea(getParent(), 3);

    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.AUSWERTUNGMITGLIEDER);
    buttons.addButton(control.getStartAuswertungButton());

  }

  public void unbind() throws ApplicationException
  {
  }
}
