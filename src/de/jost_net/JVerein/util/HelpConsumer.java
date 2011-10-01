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
package de.jost_net.JVerein.util;

import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.messaging.QueryMessage;
import de.willuhn.logging.Logger;

public class HelpConsumer implements MessageConsumer
{

  public void handleMessage(Message m)
  {
    QueryMessage msg = (QueryMessage) m;
    AbstractView view = (AbstractView) msg.getData();
    Logger.error("help missing for view " + view.getClass());
    GUI.getStatusBar().setErrorText(
        "Hilfe für Ansicht " + view.getClass() + " fehlt");
  }

  public boolean autoRegister()
  {
    return true;
  }

  public Class<?>[] getExpectedMessageTypes()
  {
    return new Class[] { QueryMessage.class };
  }
}