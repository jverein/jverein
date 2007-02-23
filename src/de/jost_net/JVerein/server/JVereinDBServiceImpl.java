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
 * Revision 1.1  2006/09/20 15:39:48  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.datasource.db.EmbeddedDBServiceImpl;
import de.willuhn.jameica.system.Application;

public class JVereinDBServiceImpl extends EmbeddedDBServiceImpl
{
  private static final long serialVersionUID = -8164119243234450453L;

  public JVereinDBServiceImpl() throws RemoteException
  {
    super(Application.getPluginLoader().getPlugin(JVereinPlugin.class)
        .getResources().getWorkPath()
        + "/db/db.conf", "exampleuser", "examplepassword");

    this.setClassFinder(Application.getClassLoader().getClassFinder());
  }
}
