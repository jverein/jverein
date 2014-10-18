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
package de.jost_net.JVerein.gui.menu;

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.action.MailAuswahlDeleteAction;
import de.jost_net.JVerein.gui.control.MailControl;
import de.jost_net.JVerein.gui.dialogs.MailEmpfaengerAuswahlDialog;
import de.jost_net.JVerein.gui.dialogs.MailVorschauDialog;
import de.jost_net.JVerein.gui.dialogs.ShowVariablesDialog;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Kontext-Menu zur MailEmpfänger-Auswahl.
 */
public class MailAuswahlMenu extends ContextMenu
{

  public MailAuswahlMenu(MailControl control)
  {
    final MailControl contr = control;
    addItem(new CheckedContextMenuItem("Variable", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        if (context instanceof MailEmpfaenger)
        {
          MailEmpfaenger m = (MailEmpfaenger) context;
          try
          {
            new ShowVariablesDialog(contr.getVariables(m.getMitglied()));
          }
          catch (RemoteException e)
          {
            Logger.error("Fehler", e);
            throw new ApplicationException(e);
          }
        }
        else
        {
          Logger.error("ShowVariablesDiaglog: Ungültige Klasse: "
              + context.getClass().getCanonicalName());
        }

      }

    }, "variable_view.gif"));
    addItem(new CheckedContextMenuItem("Vorschau", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        if (context != null && context instanceof MailEmpfaenger)
        {
          MailEmpfaenger m = (MailEmpfaenger) context;
          new MailVorschauDialog(contr, m,
              MailEmpfaengerAuswahlDialog.POSITION_CENTER);
        }
        else
        {
          String name = "";
          if (context != null && context.getClass() != null)
          {
            name = context.getClass().getCanonicalName();
          }
          Logger.error("ShowVariablesDiaglog: Ungültige Klasse: " + name);
        }

      }

    }, "edit.png" /* "mail-message-new.png" */));
    addItem(new CheckedContextMenuItem("entfernen",
        new MailAuswahlDeleteAction(control), "user-trash.png"));
  }
}
