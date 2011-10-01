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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class MitgliedskontoDetailView extends AbstractView
{

  private int typ;

  public MitgliedskontoDetailView(int typ)
  {
    this.typ = typ;
  }

  @Override
  public void bind() throws Exception
  {
    GUI.getView()
        .setTitle(JVereinPlugin.getI18n().tr("Mitgliedskonto-Buchung"));

    final MitgliedskontoControl control = new MitgliedskontoControl(this);
    LabelGroup grBuchung = new LabelGroup(getParent(), JVereinPlugin.getI18n()
        .tr((typ == MitgliedskontoNode.SOLL ? "Soll" : "Ist") + "-Buchung"));
    grBuchung.addLabelPair(JVereinPlugin.getI18n().tr("Datum"),
        control.getDatum());
    grBuchung.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck 1"),
        control.getZweck1());
    grBuchung.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck 2"),
        control.getZweck2());
    grBuchung.addLabelPair(JVereinPlugin.getI18n().tr("Zahlungsweg"),
        control.getZahlungsweg());
    control.getBetrag().setMandatory(true);
    grBuchung.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"),
        control.getBetrag());

    ButtonArea buttons = new ButtonArea(getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.KURSTEILNEHMER, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  // TODO getHelp()

}
