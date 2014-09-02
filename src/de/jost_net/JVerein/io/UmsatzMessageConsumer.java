package de.jost_net.JVerein.io;

import de.jost_net.JVerein.Einstellungen;
import de.willuhn.datasource.GenericObject;
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
import de.willuhn.jameica.hbci.messaging.ImportMessage;
import de.willuhn.jameica.hbci.messaging.ObjectMessage;
import de.willuhn.jameica.hbci.rmi.Umsatz;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;

/**
 * Klasse damit wir ueber importierte Hibiscus-Umsätze informiert werden.
 */

public class UmsatzMessageConsumer implements MessageConsumer
{
  @Override
  public Class<?>[] getExpectedMessageTypes()
  {
    return new Class<?>[] { ImportMessage.class };
  }

  @Override
  public void handleMessage(Message message) throws Exception
  {
    if (message == null)
    {
      return;
    }
    if (!Einstellungen.getEinstellung().getAutoBuchunguebernahme())
    {
      return;
    }
    final GenericObject o = ((ObjectMessage) message).getObject();

    if (o == null)
    {
      return;
    }
    if (o instanceof Umsatz)
    {
      BuchungsuebernahmeThread but = BuchungsuebernahmeThread.getInstance();
      but.newStart();
    }
  }

  @Override
  public boolean autoRegister()
  {
    return false;
  }
}
