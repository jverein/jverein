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
package de.jost_net.JVereinJUnit.messaging;

import de.jost_net.JVereinJUnit.Test;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.messaging.SystemMessage;
import de.willuhn.jameica.system.Application;

/**
 * Wird beim Start von Jameica aufgerufen und startet die Tests.
 */
public class TestMessageConsumer implements MessageConsumer
{
  /**
   * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
   */
  @Override
  public Class<?>[] getExpectedMessageTypes()
  {
    return new Class[] { SystemMessage.class };
  }

  /**
   * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
   */
  @Override
  public void handleMessage(Message message) throws Exception
  {
    SystemMessage m = (SystemMessage) message;
    if (m.getStatusCode() == SystemMessage.SYSTEM_STARTED)
      Test.start();
  }

  /**
   * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
   */
  @Override
  public boolean autoRegister()
  {
    for (String s : Application.getStartupParams().getParams())
    {
      if (s.equalsIgnoreCase("junit"))
      {
        return true;
      }
    }
    return false;
  }

}
