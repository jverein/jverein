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
package de.jost_net.JVerein.util;

import de.jost_net.JVerein.JVereinPlugin;
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
        JVereinPlugin.getI18n().tr("Hilfe für Ansicht {0} fehlt",
            view.getClass().getName()));
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