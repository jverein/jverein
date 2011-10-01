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
import de.jost_net.JVerein.gui.action.KursteilnehmerDeleteAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class KursteilnehmerDetailView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Kursteilnehmer"));

    final KursteilnehmerControl control = new KursteilnehmerControl(this);

    LabelGroup grGrund = new LabelGroup(getParent(), JVereinPlugin.getI18n()
        .tr("Daten für die Abbuchung"));
    grGrund.getComposite().setSize(290, 190);
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Name"),
        control.getName(true));
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck 1"),
        control.getVZweck1());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck 2"),
        control.getVZweck2());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("BLZ"), control.getBlz());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Konto"),
        control.getKonto());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"),
        control.getBetrag());

    LabelGroup grStatistik = new LabelGroup(getParent(), JVereinPlugin
        .getI18n().tr("Statistik"));
    grStatistik.getComposite().setSize(290, 190);
    grStatistik.addLabelPair(JVereinPlugin.getI18n().tr("Geburtsdatum"),
        control.getGeburtsdatum());
    grStatistik.addLabelPair(JVereinPlugin.getI18n().tr("Geschlecht"),
        control.getGeschlecht());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.KURSTEILNEHMER, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new KursteilnehmerDetailAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("löschen"),
        new KursteilnehmerDeleteAction(), control.getCurrentObject(), false,
        "user-trash.png");
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
