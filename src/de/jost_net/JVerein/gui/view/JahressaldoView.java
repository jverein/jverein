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
import de.jost_net.JVerein.gui.control.JahressaldoControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class JahressaldoView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Jahressaldo");

    final JahressaldoControl control = new JahressaldoControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Jahr");
    group.addLabelPair("Jahr", control.getSuchJahr());

    ButtonArea buttons = new ButtonArea();
    Button button = new Button("suchen", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        control.getSaldoList();
      }
    }, null, true, "system-search.png");
    buttons.addButton(button);
    buttons.paint(this.getParent());

    LabelGroup group2 = new LabelGroup(getParent(), "Saldo");
    group2.addPart(control.getSaldoList());

    ButtonArea buttons2 = new ButtonArea();
    buttons2.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.JAHRESSALDO, false, "help-browser.png");
    buttons2.addButton(control.getStartAuswertungButton());
    buttons2.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Jahressaldo</span></p>"
        + "</form>";
  }
}
