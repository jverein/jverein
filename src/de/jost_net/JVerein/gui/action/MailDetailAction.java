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
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MailVorlageControl;
import de.jost_net.JVerein.gui.dialogs.MailVorlagenAuswahlDialog;
import de.jost_net.JVerein.gui.view.MailDetailView;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailVorlage;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class MailDetailAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Mail m = null;

    if (context != null && (context instanceof Mail))
    {
      m = (Mail) context;
    }
    else
    {
      try
      {
        MailVorlagenAuswahlDialog mvad = new MailVorlagenAuswahlDialog(
            new MailVorlageControl(null),
            MailVorlagenAuswahlDialog.POSITION_CENTER);
        m = (Mail) Einstellungen.getDBService().createObject(Mail.class, null);
        MailVorlage mv = mvad.open();
        if (mv != null)
        {
          m.setBetreff(mv.getBetreff());
          m.setTxt(mv.getTxt());
        }
      }
      catch (Exception e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung der neuen Mail", e);
      }
    }
    GUI.startView(MailDetailView.class.getName(), m);
  }
}
