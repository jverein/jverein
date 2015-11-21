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
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeDeleteAction;
import de.jost_net.JVerein.gui.control.ZusatzbetragControl;
import de.jost_net.JVerein.gui.dialogs.ZusatzbetragVorlageDialog;
import de.jost_net.JVerein.gui.parts.ZusatzbetragPart;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.ZusatzbetragVorlage;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ZusatzbetragView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Zusatzbetrag");
    final ZusatzbetragControl control = new ZusatzbetragControl(this);

    final ZusatzbetragPart part = control.getZusatzbetragPart();
    part.paint(getParent());

    LabelGroup group2 = new LabelGroup(getParent(), "Vorlagen");
    group2.addLabelPair("Als Vorlage speichern", control.getVorlage());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ZUSATZBETRAEGE, false, "help-browser.png");
    buttons.addButton("Vorlagen", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          ZusatzbetragVorlageDialog zbvd = new ZusatzbetragVorlageDialog();
          ZusatzbetragVorlage zbv = zbvd.open();
          part.getBetrag().setValue(zbv.getBetrag());
          part.getBuchungstext().setValue(zbv.getBuchungstext());
          part.getEndedatum().setValue(zbv.getEndedatum());
          part.getFaelligkeit().setValue(zbv.getFaelligkeit());
          part.getIntervall().setValue(zbv.getIntervall());
          for (Object obj : part.getIntervall().getList())
          {
            IntervallZusatzzahlung ivz = (IntervallZusatzzahlung) obj;
            if (zbv.getIntervall() == ivz.getKey())
            {
              part.getIntervall().setPreselected(ivz);
              break;
            }
          }
          part.getStartdatum(false).setValue(zbv.getStartdatum());
        }
        catch (OperationCanceledException e)
        {
          // Nothing to do
        }
        catch (Exception e)
        {
          Logger.error("ZusatzbetragVorlageDialog kann nicht geöffnet werden",
              e);
        }
      }

    });
    buttons.addButton("Mitglied", new MitgliedDetailAction(), control
        .getZusatzbetrag().getMitglied(), false, "system-users.png");
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
}
