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

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeDeleteAction;
import de.jost_net.JVerein.gui.control.ZusatzbetragControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class ZusatzbetragView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Zusatzbetrag");
    final ZusatzbetragControl control = new ZusatzbetragControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Zusatzbetrag");
    group.addLabelPair("Startdatum", control.getStartdatum(true));
    group.addLabelPair("nächste Fälligkeit", control.getFaelligkeit());
    group.addLabelPair("Intervall", control.getIntervall());
    group.addLabelPair("Endedatum", control.getEndedatum());
    group.addLabelPair("Buchungstext 1", control.getBuchungstext());
    group.addLabelPair("Buchungstext 2", control.getBuchungstext2());
    group.addLabelPair("Betrag", control.getBetrag());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ZUSATZBETRAEGE, false, "help-browser.png");
    buttons.addButton("Mitglied", new MitgliedDetailAction(),
        control.getZusatzbetrag().getMitglied(), false, "system-users.png");
    buttons.addButton("löschen", new ZusatzbetraegeDeleteAction(),
        control.getZusatzbetrag(), false, "user-trash.png");
    buttons.addButton("speichern", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
    buttons.paint(getParent());
  }

  // TODO getHelp()
}
