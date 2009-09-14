package de.jost_net.JVerein.gui.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.input.UpdateIntervalInput;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.willuhn.datasource.rmi.ObjectNotFoundException;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.messaging.SystemMessage;
import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;

/**
 * Klasse damit wir ueber importierte Hibiscus-Umsätze informiert werden.
 */

public class UpdateCheckMessageConsumer implements MessageConsumer
{

  @SuppressWarnings("unchecked")
  public Class[] getExpectedMessageTypes()
  {
    return new Class[] { SystemMessage.class };
  }

  public void handleMessage(Message message) throws Exception
  {
    if (message == null || !(message instanceof SystemMessage))
      return;

    if (((SystemMessage) message).getStatusCode() != SystemMessage.SYSTEM_STARTED)
    {
      return;
    }
    int updateinterval = Einstellungen.getEinstellung().getUpdateInterval();
    if (updateinterval == UpdateIntervalInput.MANUELL)
    {
      return;
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.add(Calendar.DAY_OF_YEAR, updateinterval * -1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(Einstellungen.getEinstellung().getUpdateLastCheck());
    if (cal1.before(cal2))
    {
      return;
    }

    Manifest manifest = null;
    try
    {
      manifest = Application.getPluginLoader().getManifest(JVereinPlugin.class);
    }
    catch (Exception e)
    {
      Logger.error("unable to read info.xml from plugin jverein", e);
    }
    String diag = "";

    if (Einstellungen.getEinstellung().getUpdateDiagInfos())
    {
      Stammdaten stamm = null;
      try
      {
        stamm = (Stammdaten) Einstellungen.getDBService().createObject(
            Stammdaten.class, "0");
      }
      catch (ObjectNotFoundException e)
      {
        stamm = (Stammdaten) Einstellungen.getDBService().createObject(
            Stammdaten.class, "1");
      }
      diag = "&name=" + URLEncoder.encode(stamm.getName(), "utf8");
    }
    URL url = new URL("http://www.jverein.de/versionscheck.php?version="
        + manifest.getVersion() + "&build=" + manifest.getBuildnumber() + diag);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setUseCaches(false);
    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
    {
      BufferedReader in = new BufferedReader(new InputStreamReader(conn
          .getInputStream()));

      String inputLine = in.readLine();
      in.close();
      if (inputLine != null && inputLine.startsWith("neue"))
      {
        Application.getCallback().notifyUser(inputLine);
      }
      Einstellungen.getEinstellung().setUpdateLastCheck(new Date());
      Einstellungen.getEinstellung().store();
    }
  }

  public boolean autoRegister()
  {
    return true;
  }
}
