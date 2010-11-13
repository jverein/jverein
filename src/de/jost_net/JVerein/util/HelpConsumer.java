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
 * Revision 1.1  2010-10-04 12:18:31  jost
 * Tool zur Ermittlung der Views ohne Hilfetext
 *
 * Revision 1.1  2009-03-26 21:05:56  jost
 * Email-Adress-Checker
 *
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
    return new Class[] { QueryMessage.class};
  }
}