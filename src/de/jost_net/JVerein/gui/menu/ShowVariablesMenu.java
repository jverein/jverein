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

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import de.jost_net.JVerein.gui.dialogs.ShowVariablesDialog.Var;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;

/**
 * Kontext-Menu für einen ShowVariablesDialog. Auswahl "kopieren" kopiert den
 * selektierten Eintrag in den Zwischenspeicher. Mit setPrependCopyText() und
 * setAppendCopyText() kann der zu kopierende Eintrag in anderen Text
 * eingebettet werden. Standard: "${" und "}"
 */
public class ShowVariablesMenu extends ContextMenu
{

  private Action copy;

  /**
   * Liefert Action, die den gewählten Eintrag in den Zwischenspeicher kopiert.
   * 
   * @return Action, die den gewählten Eintrag in den Zwischenspeicher kopiert.
   */
  public Action getCopyToClipboardAction()
  {
    return copy;
  }

  private String prependCopyText = "${";

  /**
   * Setzt Text, der vor den Eintrag in den Zwischen speicher kopiert werden
   * soll.
   * 
   * @param pre
   *        Text, der vor den Eintrag in den Zwischen speicher kopiert werden
   *        soll.
   */
  public void setPrependCopyText(String pre)
  {
    prependCopyText = pre;
  }

  private String appendCopyText = "}";

  /**
   * Setzt Text, der hinter den Eintrag in den Zwischen speicher kopiert werden
   * soll.
   * 
   * @param append
   *        Text, der hinter den Eintrag in den Zwischen speicher kopiert werden
   *        soll.
   */
  public void setAppendCopyText(String append)
  {
    appendCopyText = append;
  }

  /**
   * Erzeugt ein Kontext-Menu
   */
  public ShowVariablesMenu()
  {
    final Clipboard cb = new Clipboard(GUI.getDisplay());

    copy = new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        if (context instanceof Var)
        {
          Var v = (Var) context;
          String textData = (String) v.getAttribute("name");
          if (textData.length() > 0)
          {
            TextTransfer textTransfer = TextTransfer.getInstance();
            cb.setContents(new Object[] { prependCopyText + textData
                + appendCopyText}, new Transfer[] { textTransfer});
          }
        }
      }
    };
    addItem(new CheckedContextMenuItem("kopieren", copy, "copy_edit.png"));
  }
}
