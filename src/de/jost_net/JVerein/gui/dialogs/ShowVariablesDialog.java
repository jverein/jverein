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
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.menu.ShowVariablesMenu;
import de.willuhn.datasource.GenericObject;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.logging.Logger;

/**
 * Dialog zur Zuordnung einer Buchungsart.
 */
public class ShowVariablesDialog extends AbstractDialog
{
  private Map<String, Object> vars;

  /**
   * @param position
   */
  public ShowVariablesDialog(Map<String, Object> vars)
  {
    super(AbstractDialog.POSITION_CENTER);
    setTitle(JVereinPlugin.getI18n().tr("Liste der Variablen"));
    setSize(400, 400);
    this.vars = vars;
    try
    {
      this.open();
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
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
    TablePart tab = new TablePart(list, null);
    tab.addColumn("Name", "name");
    tab.addColumn("Wert", "wert");
    tab.setRememberOrder(true);
    tab.setContextMenu(new ShowVariablesMenu(parent));
    tab.paint(parent);
    ButtonArea buttons = new ButtonArea(parent, 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("OK"), new Action()
    {

      public void handleAction(Object context)
      {
        close();
      }
    });

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

    public String[] getAttributeNames() throws RemoteException
    {
      return new String[] { "name", "wert" };
    }

    public String getID() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      return false;
    }

    public Object getAttribute(String arg0) throws RemoteException
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

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

  }
}
