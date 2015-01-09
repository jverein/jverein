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

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.action.BuchungsartAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsartControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class BuchungsartListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Buchungsarten");

    final BuchungsartControl control = new BuchungsartControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Suche");
    group.addLabelPair("Suche", control.getSuchtext());

    ButtonArea buttons1 = new ButtonArea();
    Button button = new Button("suchen", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          control.getBuchungsartList();
        }
        catch (RemoteException e)
        {
          // TODO Auto-generated catch block
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
      }
    }, null, true, "system-search.png");
    buttons1.addButton(button);
    buttons1.paint(this.getParent());

    LabelGroup group2 = new LabelGroup(getParent(), "Liste", true);
    group2.addPart(control.getBuchungsartList());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGSART, false, "help-browser.png");
    buttons.addButton(control.getPDFAusgabeButton());
    buttons.addButton("neu", new BuchungsartAction(), null, false,
        "document-new.png");
    buttons.paint(this.getParent());
  }
}
