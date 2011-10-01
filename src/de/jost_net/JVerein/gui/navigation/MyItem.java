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

  public MyItem(NavigationItem item, String navitext, Action action, String icon)
  {
    this.parent = item;
    this.action = action;
    this.navitext = navitext;
    this.icon = icon;
    children = new ArrayList<Item>();
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#getIconClose()
   */
  public Image getIconClose()
  {
    if (action == null)
    {
      return SWTUtil.getImage(icon != null ? icon : "folder.png");
    }
    else
    {
      return SWTUtil.getImage(icon != null ? icon : "page.gif");
    }
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#getIconOpen()
   */
  public Image getIconOpen()
  {
    if (action == null)
    {
      return SWTUtil.getImage(icon != null ? icon : "folder-open.png");
    }
    else
    {
      return SWTUtil.getImage(icon != null ? icon : "page.gif");
    }
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#isExpanded()
   */
  public boolean isExpanded()
  {
    return false;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#addChild(de.willuhn.jameica.gui.Item)
   */
  public void addChild(Item i)
  {
    children.add(i);
  }

  /**
   * @see de.willuhn.jameica.gui.Item#getAction()
   */
  public Action getAction()
  {
    return action;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#getName()
   */
  public String getName()
  {
    return navitext;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#isEnabled()
   */
  public boolean isEnabled()
  {
    return true;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#setEnabled(boolean, boolean)
   */
  public void setEnabled(boolean enabled, boolean recursive)
  {
    // ignore
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getChildren()
   */
  public GenericIterator getChildren() throws RemoteException
  {
    if (children.size() == 0)
    {
      return null;
    }
    else
    {
      return PseudoIterator.fromArray(children.toArray(new MyItem[children
          .size()]));
    }
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getParent()
   */
  public GenericObjectNode getParent()
  {
    return this.parent;
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getPath()
   */
  @SuppressWarnings("unchecked")
  public GenericIterator getPath() throws RemoteException
  {
    List list = PseudoIterator.asList(this.parent.getPath());
    list.add(this);
    return PseudoIterator.fromArray((NavigationItem[]) list
        .toArray(new NavigationItem[list.size()]));
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getPossibleParents()
   */
  public GenericIterator getPossibleParents() throws RemoteException
  {
    throw new RemoteException("not implemented");
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#hasChild(de.willuhn.datasource.GenericObjectNode)
   */
  public boolean hasChild(GenericObjectNode arg0)
  {
    return false;
  }

  /**
   * @see de.willuhn.datasource.GenericObject#equals(de.willuhn.datasource.GenericObject)
   */
  public boolean equals(GenericObject arg0) throws RemoteException
  {
    if (arg0 == null || !(arg0 instanceof MyItem))
      return false;
    return this.getID().equals(arg0.getID());
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttribute(java.lang.String)
   */
  public Object getAttribute(String arg0)
  {
    return getName();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttributeNames()
   */
  public String[] getAttributeNames()
  {
    return new String[] { "name" };
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getID()
   */
  public String getID()
  {
    return getClass().getName() + "." + getName();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
   */
  public String getPrimaryAttribute()
  {
    return "name";
  }

  /**
   * @see de.willuhn.jameica.gui.extension.Extendable#getExtendableID()
   */
  public String getExtendableID()
  {
    return getID();
  }

}

/*******************************************************************************
 * $Log$ Revision 1.7 2010-10-15 09:58:29 jost Code aufger�umt
 * Revision 1.6 2008-12-28 07:55:27 jost Icons an Jameica angepasst
 * 
 * Revision 1.5 2008/12/22 21:16:52 jost Icons ins Menü aufgenommen. Revision
 * 1.4 2008/11/29 13:11:27 jost Refactoring: Warnungen beseitigt.
 * 
 * Revision 1.3 2008/05/22 06:51:47 jost Buchführung Revision 1.2 2007/08/23
 * 19:25:23 jost Header korrigiert.
 * 
 * Revision 1.1 2007/08/22 20:43:54 jost *** empty log message ***
 * 
 ******************************************************************************/
