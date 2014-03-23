package de.jost_net.JVerein.io;

import de.jost_net.JVerein.Einstellungen;
import de.willuhn.datasource.GenericObject;
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
