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
 * Revision 1.1  2007/02/25 19:13:34  jost
 * Neu: Kursteilnehmer
 *
 * Revision 1.1  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDeleteAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Kursteilnehmer");

    final KursteilnehmerControl control = new KursteilnehmerControl(this);

    LabelGroup grGrund = new LabelGroup(getParent(), "Daten für die Abbuchung");
    grGrund.getComposite().setSize(290, 190);
    grGrund.addLabelPair("Name", control.getName());
    grGrund.addLabelPair("Verwendungszweck 1", control.getVZweck1());
    grGrund.addLabelPair("Verwendungszweck 2", control.getVZweck2());
    grGrund.addLabelPair("BLZ", control.getBlz());
    grGrund.addLabelPair("Konto", control.getKonto());
    grGrund.addLabelPair("Betrag", control.getBetrag());

    LabelGroup grStatistik = new LabelGroup(getParent(), "Statistik");
    grStatistik.getComposite().setSize(290, 190);
    grStatistik.addLabelPair("Geburtsdatum", control.getGeburtsdatum());
    grStatistik.addLabelPair("Geschlecht", control.getGeschlecht());

    ButtonArea buttons = new ButtonArea(getParent(), 5);

    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.kursteilnehmer);
    buttons.addButton("Neu", new KursteilnehmerDetailAction());
    buttons.addButton("Löschen", new KursteilnehmerDeleteAction(), control
        .getCurrentObject());
    buttons.addButton("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true);
  }

  public void unbind() throws ApplicationException
  {
  }

}
