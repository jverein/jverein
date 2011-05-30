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
