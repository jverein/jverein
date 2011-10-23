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
package de.jost_net.JVerein.gui.menu;

import java.rmi.RemoteException;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.dialogs.ShowVariablesDialog.Var;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.util.ApplicationException;

/**
 * Kontext-Menu
 */
public class ShowVariablesMenu extends ContextMenu
{

  /**
   * Erzeugt ein Kontext-Menu
   */
  public ShowVariablesMenu(Composite parent)
  {
    final Clipboard cb = new Clipboard(parent.getDisplay());

    Action copy = new Action()
    {

      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          if (context instanceof Var)
          {
            Var v = (Var) context;
            String textData = (String) v.getAttribute("name");
            if (textData.length() > 0)
            {
              TextTransfer textTransfer = TextTransfer.getInstance();
              cb.setContents(new Object[] { "${" + textData + "}" },
                  new Transfer[] { textTransfer });
            }
          }
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e);
        }
      }
    };
    addItem(new CheckedContextMenuItem(JVereinPlugin.getI18n().tr("kopieren"),
        copy, "copy_edit.png"));
  }
}
