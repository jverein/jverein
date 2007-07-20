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
package de.jost_net.JVerein.gui.action;

import java.io.File;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

public class HandbuchAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    GUI.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        try
        {
          AbstractPlugin p = Application.getPluginLoader().getPlugin(
              JVereinPlugin.class);
          new Program().handleAction(new File(p.getResources().getPath()
              + "/doc/handbuch.pdf"));
        }
        catch (ApplicationException ae)
        {
          Application.getMessagingFactory().sendMessage(
              new StatusBarMessage(ae.getLocalizedMessage(),
                  StatusBarMessage.TYPE_ERROR));
        }
      }
    });
  }
}
