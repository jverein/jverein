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
package de.jost_net.JVerein.gui.navigation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.GenericObjectNode;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Item;
import de.willuhn.jameica.gui.NavigationItem;
import de.willuhn.jameica.gui.util.SWTUtil;

/**
 */
public class MyItem implements NavigationItem
{

  private NavigationItem parent = null;

  private Action action;

  private String navitext;

  private ArrayList<Item> children;

  private String icon;

  public MyItem(NavigationItem item, String navitext, Action action)
  {
    this(item, navitext, action, null);
  }

  public MyItem(NavigationItem item, String navitext, Action action,
      String icon)
  {
    this.parent = item;
    this.action = action;
    this.navitext = navitext;
    this.icon = icon;
    children = new ArrayList<>();
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#getIconClose()
   */
  @Override
  public Image getIconClose()
  {
    if (action == null)
    {
      return SWTUtil.getImage(icon != null ? icon : "folder1.png");
    }
    else
    {
      return SWTUtil.getImage(icon != null ? icon : "page.gif");
    }
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#getIconOpen()
   */
  @Override
  public Image getIconOpen()
  {
    if (action == null)
    {
      return SWTUtil.getImage(icon != null ? icon : "folder1-open.png");
    }
    else
    {
      return SWTUtil.getImage(icon != null ? icon : "page.gif");
    }
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#isExpanded()
   */
  @Override
  public boolean isExpanded()
  {
    return false;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#addChild(de.willuhn.jameica.gui.Item)
   */
  @Override
  public void addChild(Item i)
  {
    children.add(i);
  }

  /**
   * @see de.willuhn.jameica.gui.Item#getAction()
   */
  @Override
  public Action getAction()
  {
    return action;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#getName()
   */
  @Override
  public String getName()
  {
    return navitext;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#isEnabled()
   */
  @Override
  public boolean isEnabled()
  {
    return true;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#setEnabled(boolean, boolean)
   */
  @Override
  public void setEnabled(boolean enabled, boolean recursive)
  {
    // ignore
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getChildren()
   */
  @Override
  public GenericIterator<?> getChildren() throws RemoteException
  {
    // if (children.size() == 0)
    // {
    // return null;
    // }
    // else
    // {
    return PseudoIterator
        .fromArray(children.toArray(new MyItem[children.size()]));
    // }
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getParent()
   */
  @Override
  public GenericObjectNode getParent()
  {
    return this.parent;
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getPath()
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public GenericIterator getPath() throws RemoteException
  {
    List list = PseudoIterator.asList(this.parent.getPath());
    list.add(this);
    return PseudoIterator.fromArray(
        (NavigationItem[]) list.toArray(new NavigationItem[list.size()]));
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getPossibleParents()
   */
  @Override
  public GenericIterator<?> getPossibleParents() throws RemoteException
  {
    throw new RemoteException("not implemented");
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#hasChild(de.willuhn.datasource.GenericObjectNode)
   */
  @Override
  public boolean hasChild(GenericObjectNode arg0)
  {
    return false;
  }

  /**
   * @see de.willuhn.datasource.GenericObject#equals(de.willuhn.datasource.GenericObject)
   */
  @Override
  public boolean equals(GenericObject arg0) throws RemoteException
  {
    if (arg0 == null || !(arg0 instanceof MyItem))
      return false;
    return this.getID().equals(arg0.getID());
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttribute(java.lang.String)
   */
  @Override
  public Object getAttribute(String arg0)
  {
    return getName();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttributeNames()
   */
  @Override
  public String[] getAttributeNames()
  {
    return new String[] { "name" };
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getID()
   */
  @Override
  public String getID()
  {
    return getClass().getName() + "." + getName();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
   */
  @Override
  public String getPrimaryAttribute()
  {
    return "name";
  }

  /**
   * @see de.willuhn.jameica.gui.extension.Extendable#getExtendableID()
   */
  @Override
  public String getExtendableID()
  {
    return getID();
  }

}
