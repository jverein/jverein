package test.de.jost_net.JVerein;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.JVereinDBServiceImpl;
import de.willuhn.logging.Logger;

public class DBTest
{
  private static JVereinDBService service = null;

  public DBTest() throws RemoteException
  {
    try
    { // Da die Service-Factory zu diesem Zeitpunkt noch nicht da ist, erzeugen
      // wir uns eine lokale Instanz des Services.
      service = new JVereinDBServiceImpl();
      service.start();
      // call.call(service);
    }
    finally
    {
      if (service != null)
      {
        try
        {
          service.stop(true);
        }
        catch (Exception e)
        {
          Logger.error("error while closing db service", e);
        }
      }

    }
  }
}
