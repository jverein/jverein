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
 * Revision 1.1  2007/12/21 14:48:48  jost
 * PDF-Dokumentation -> Wiki
 *
 * Revision 1.1  2007/07/20 20:15:19  jost
 * Neu
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.io.File;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

public class DokumentationAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    final Object cont = context;
    GUI.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        try
        {
          if (cont instanceof String)
          {
            new Program().handleAction(new File((String) cont));
          }
          else
          {
            new Program().handleAction(new File(
                "http://www.jverein.de/index.php5?title=Dokumentation"));
          }
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
