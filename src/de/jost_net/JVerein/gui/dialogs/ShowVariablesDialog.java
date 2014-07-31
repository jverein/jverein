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
package de.jost_net.JVerein.gui.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.menu.ShowVariablesMenu;
import de.willuhn.datasource.GenericObject;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;

/**
 * Dialog, zum Anzeigen von Variablen-Namen und deren Inhalten. Action für
 * Doppelklick auf Eintrag und ContextMenu können mit setDoubleClickAction() und
 * setContextMenu() gesetzt werden. Standard: null und new ShowVariablesMenu()
 */
public class ShowVariablesDialog extends AbstractDialog<Object>
{

  private Map<String, Object> vars;

  private ContextMenu contextMenu;

  private Action doubleClickAction = null;

  public ShowVariablesDialog(Map<String, Object> vars)
  {
    this(vars, true);
  }

  public ShowVariablesDialog(Map<String, Object> vars, boolean open)
  {
    super(AbstractDialog.POSITION_CENTER);
    setTitle("Liste der Variablen");
    setSize(400, 400);
    this.vars = vars;
    // default context menu
    contextMenu = new ShowVariablesMenu();
    if (open)
    {
      try
      {
        this.open();
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
      }
    }
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {

    List<GenericObject> list = new ArrayList<GenericObject>();

    for (Entry<String, Object> entry : vars.entrySet())
    {
      list.add(new Var(entry));
    }
    TablePart tab = new TablePart(list, doubleClickAction);
    tab.addColumn("Name", "name");
    tab.addColumn("Wert", "wert");
    tab.setRememberOrder(true);
    tab.setContextMenu(contextMenu);
    tab.paint(parent);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("OK", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        close();
      }
    });
    buttons.paint(parent);
  }

  /**
   * Setze ContextMenu für Tabelle.
   * 
   * @param newContextMenu
   */
  public void setContextMenu(ContextMenu newContextMenu)
  {
    this.contextMenu = newContextMenu;
  }

  /**
   * Setze Action, die ausgelöst wird, wenn Nutzer doppelt auf Eintrag in
   * Tabelle klickt.
   * 
   * @param newDoubleClickAction
   */
  public void setDoubleClickAction(Action newDoubleClickAction)
  {
    doubleClickAction = newDoubleClickAction;
  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  public Object getData() throws Exception
  {
    return null;
  }

  public class Var implements GenericObject
  {

    private String name;

    private Object wert;

    public Var(Entry<String, Object> entry)
    {
      this.name = entry.getKey();
      this.wert = entry.getValue();
    }

    @Override
    public String[] getAttributeNames()
    {
      return new String[] { "name", "wert"};
    }

    @Override
    public String getID()
    {
      return "name";
    }

    @Override
    public boolean equals(GenericObject arg0)
    {
      return false;
    }

    @Override
    public Object getAttribute(String arg0)
    {
      if (arg0.equals("name"))
      {
        return name;
      }
      else if (arg0.equals("wert"))
      {
        return wert;
      }
      return null;
    }

    @Override
    public String getPrimaryAttribute()
    {
      return "name";
    }

  }
}
