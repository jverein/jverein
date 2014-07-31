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

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.ArbeitseinsatzControl;
import de.jost_net.JVerein.gui.input.ArbeitseinsatzUeberpruefungInput;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class ArbeitseinsatzUeberpruefungView extends AbstractView
{
  Button butArbeitseinsaetze = null;

  ArbeitseinsatzUeberpruefungInput aui = null;

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Arbeitseinsätze prüfen");

    final ArbeitseinsatzControl control = new ArbeitseinsatzControl(this);
    butArbeitseinsaetze = control.getArbeitseinsatzAusgabeButton();
    LabelGroup group = new LabelGroup(getParent(), "Filter");
    group.addLabelPair("Jahr", control.getSuchJahr());
    aui = control.getAuswertungSchluessel();
    aui.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        int i = (Integer) aui.getValue();
        butArbeitseinsaetze
            .setEnabled(i == ArbeitseinsatzUeberpruefungInput.MINDERLEISTUNG);
      }

    });
    group.addLabelPair("Auswertung", aui);

    ButtonArea buttons = new ButtonArea();
    Button button = new Button("suchen", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        control.getArbeitseinsatzUeberpruefungList();
      }
    }, null, true, "system-search.png");
    buttons.addButton(button);
    buttons.paint(this.getParent());

    control.getArbeitseinsatzUeberpruefungList().paint(getParent());
    ButtonArea buttons2 = new ButtonArea();
    buttons2.addButton(control.getPDFAusgabeButton());
    buttons2.addButton(control.getCSVAusgabeButton());
    buttons2.addButton(control.getArbeitseinsatzAusgabeButton());
    buttons2.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ARBEITSEINSATZ, false, "help-browser.png");
    buttons2.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Jahressaldo</span></p>"
        + "</form>";
  }
}
