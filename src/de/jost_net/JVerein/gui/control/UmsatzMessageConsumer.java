package de.jost_net.JVerein.gui.control;

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

  public Class[] getExpectedMessageTypes()
  {
    return new Class[] { ImportMessage.class};
  }

  public void handleMessage(Message message) throws Exception
  {
    if (message == null)
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
      Umsatz u = (Umsatz) o;
      System.out.println("UmsatzMessageConsumer:" + u);
    }
    // Checken, ob uns der Transfer-Typ interessiert
    // if (!getObjectType().isAssignableFrom(o.getClass()))
    // { return;}
  }

  public boolean autoRegister()
  {
    return false;
  }
}
