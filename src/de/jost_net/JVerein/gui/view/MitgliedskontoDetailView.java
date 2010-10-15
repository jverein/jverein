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
 * Revision 1.3  2010-10-07 19:49:23  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.2  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.1  2010-07-25 18:43:40  jost
 * Neu: Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.gui.internal.buttons.Back;
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
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Mitgliedskonto-Buchung"));

    final MitgliedskontoControl control = new MitgliedskontoControl(this);
    LabelGroup grBuchung = new LabelGroup(getParent(),
        JVereinPlugin.getI18n().tr(
            (typ == MitgliedskontoNode.SOLL ? "Soll" : "Ist") + "-Buchung"));
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

    ButtonArea buttons = new ButtonArea(getParent(), 5);

    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.KURSTEILNEHMER, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  // TODO getHelp()

}
