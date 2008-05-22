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
 * Revision 1.2  2007/08/23 19:25:23  jost
 * Header korrigiert.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.navigation;

/*******************************************************************************
 * $Revision$ $Date$ $Author$ $Locker$
 * $State$
 * 
 * Copyright (c) by willuhn software & services All rights reserved
 * 
 ******************************************************************************/

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
import de.willuhn.logging.Logger;

/**
 */
public class MyItem implements NavigationItem
{
  private NavigationItem parent = null;

  private Action action;

  private String navitext;

  private ArrayList<Item> children;

  /**
   * ct.
   * 
   * @param item
   */
  public MyItem(NavigationItem item, String navitext, Action action)
  {
    this.parent = item;
    this.action = action;
    this.navitext = navitext;
    children = new ArrayList<Item>();
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#getIconClose()
   */
  public Image getIconClose() throws RemoteException
  {
    if (action == null)
    {
      return SWTUtil.getImage("folder.gif");
    }
    else
    {
      return SWTUtil.getImage("page.gif");
    }
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#getIconOpen()
   */
  public Image getIconOpen() throws RemoteException
  {
    if (action == null)
    {
      return SWTUtil.getImage("folderopen.gif");
    }
    else
    {
      return SWTUtil.getImage("page.gif");
    }
  }

  /**
   * @see de.willuhn.jameica.gui.NavigationItem#isExpanded()
   */
  public boolean isExpanded() throws RemoteException
  {
    return false;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#addChild(de.willuhn.jameica.gui.Item)
   */
  public void addChild(Item i) throws RemoteException
  {
    children.add(i);
  }

  /**
   * @see de.willuhn.jameica.gui.Item#getAction()
   */
  public Action getAction() throws RemoteException
  {
    return action;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#getName()
   */
  public String getName() throws RemoteException
  {
    return navitext;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#isEnabled()
   */
  public boolean isEnabled() throws RemoteException
  {
    return true;
  }

  /**
   * @see de.willuhn.jameica.gui.Item#setEnabled(boolean, boolean)
   */
  public void setEnabled(boolean enabled, boolean recursive)
      throws RemoteException
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
      return PseudoIterator.fromArray((MyItem[]) children
          .toArray(new MyItem[children.size()]));
    }
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getParent()
   */
  public GenericObjectNode getParent() throws RemoteException
  {
    return this.parent;
  }

  /**
   * @see de.willuhn.datasource.GenericObjectNode#getPath()
   */
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
  public boolean hasChild(GenericObjectNode arg0) throws RemoteException
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
  public Object getAttribute(String arg0) throws RemoteException
  {
    return getName();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttributeNames()
   */
  public String[] getAttributeNames() throws RemoteException
  {
    return new String[] { "name" };
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getID()
   */
  public String getID() throws RemoteException
  {
    return getClass().getName() + "." + getName();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
   */
  public String getPrimaryAttribute() throws RemoteException
  {
    return "name";
  }

  /**
   * @see de.willuhn.jameica.gui.extension.Extendable#getExtendableID()
   */
  public String getExtendableID()
  {
    try
    {
      return getID();
    }
    catch (RemoteException re)
    {
      Logger.error("unable to determine id", re);
      return null;
    }
  }

}

/*******************************************************************************
 * $Log$ Revision 1.2 2007/08/23 19:25:23 jost Header
 * korrigiert.
 * 
 * Revision 1.1 2007/08/22 20:43:54 jost *** empty log message ***
 * 
 ******************************************************************************/
