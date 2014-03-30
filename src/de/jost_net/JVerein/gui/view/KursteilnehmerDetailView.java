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

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDeleteAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.jost_net.JVerein.gui.dialogs.BankverbindungDialogButton;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;

public class KursteilnehmerDetailView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Kursteilnehmer");

    final KursteilnehmerControl control = new KursteilnehmerControl(this);

    ScrolledContainer scrolled = new ScrolledContainer(getParent(), 1);

    LabelGroup grGrund = new LabelGroup(scrolled.getComposite(),
        "Daten für die Lastschrift");
    grGrund.getComposite().setSize(290, 190);
    grGrund.addInput(control.getPersonenart());
    grGrund.addInput(control.getAnrede());
    grGrund.addInput(control.getTitel());
    grGrund.addInput(control.getName());
    grGrund.addInput(control.getVorname());
    grGrund.addInput(control.getStrasse());
    grGrund.addInput(control.getAdressierungszusatz());
    grGrund.addInput(control.getPLZ());
    grGrund.addInput(control.getOrt());
    grGrund.addInput(control.getStaat());
    grGrund.addInput(control.getEmail());
    grGrund.addInput(control.getVZweck1());
    grGrund.addInput(control.getMandatDatum());
    grGrund.addInput(control.getIBAN());
    grGrund.addInput(control.getBIC());
    grGrund.addPart(new BankverbindungDialogButton(control.getKursteilnehmer(),
        control.getBlz(), control.getKonto(), control.getBIC(),
        control.getIBAN()));
    // grGrund.addLabelPair("BLZ"),
    // control.getBlz());
    // grGrund.addLabelPair("Konto"),
    // control.getKonto());
    grGrund.addLabelPair("Betrag", control.getBetrag());

    LabelGroup grStatistik = new LabelGroup(scrolled.getComposite(),
        "Statistik");
    grStatistik.getComposite().setSize(290, 190);
    grStatistik.addLabelPair("Geburtsdatum", control.getGeburtsdatum());
    grStatistik.addLabelPair("Geschlecht", control.getGeschlecht());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.KURSTEILNEHMER, false, "help-browser.png");
    buttons.addButton("neu", new KursteilnehmerDetailAction(), null, false,
        "document-new.png");
    buttons.addButton("löschen", new KursteilnehmerDeleteAction(),
        control.getCurrentObject(), false, "user-trash.png");
    buttons.addButton("speichern", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
    buttons.paint(this.getParent());
  }
  // TODO getHelp()

}
