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
 * Revision 1.26  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.25  2010-10-04 12:18:46  jost
 * Tool zur Ermittlung der Views ohne Hilfetext
 *
 * Revision 1.24  2009-06-11 21:00:26  jost
 * Vorbereitung I18N
 *
 * Revision 1.23  2009/01/19 19:41:13  jost
 * Jameica-Build-Prüfung abgeschaltet.
 *
 * Revision 1.22  2009/01/04 16:25:20  jost
 * Neue Mindest-Build-Date für Jameica festgelegt.
 *
 * Revision 1.21  2008/12/30 21:57:25  jost
 * Anpassung an neue Versionsmimik.
 *
 * Revision 1.20  2008/12/27 15:17:56  jost
 * ÃœberflÃ¼ssiges Import-Statement entfernt
 *
 * Revision 1.19  2008/12/26 17:19:10  jost
 * ÃœberprÃ¼fung der minimalen Jameica-Version.
 *
 * Revision 1.18  2008/12/26 17:17:41  jost
 * ÃœberprÃ¼fung der minimalen Jameica-Version.
 *
 * Revision 1.17  2008/11/11 18:26:21  jost
 * Vorbereitung fÃ¼r kÃ¼nftige Erweiterung
 *
 * Revision 1.16  2008/01/01 12:35:40  jost
 * Javadoc korrigiert
 *
 * Revision 1.15  2007/12/20 20:31:58  jost
 * Anpassung an Jameica-Standard
 *
 * Revision 1.14  2007/12/01 17:45:32  jost
 * Formatierung
 *
 * Revision 1.13  2007/10/18 18:18:33  jost
 * Vorbereitung H2-DB
 *
 * Revision 1.12  2007/08/22 20:42:23  jost
 * Bug #011762
 *
 * Revision 1.11  2007/07/17 16:06:02  jost
 * Release 0.9
 *
 * Revision 1.10  2007/07/06 11:36:02  jost
 * Bugfix Versionsnummer
 *
 * Revision 1.9  2007/05/07 19:23:14  jost
 * Neu: Wiedervorlage
 *
 * Revision 1.8  2007/04/05 18:53:40  jost
 * Vermeidung von ClassNotFoundException
 *
 * Revision 1.7  2007/04/03 16:02:58  jost
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/30 13:18:23  jost
 * Erweiterung für die Version 0.8
 *
 * Revision 1.5  2007/03/28 13:23:01  jost
 * Java 1.5-Kompatibilität
 *
 * Revision 1.4  2007/03/28 12:26:37  jost
 * Überprüfung der Datenbankstruktur beim Startup
 *
 * Revision 1.3  2007/02/23 20:25:16  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/29 07:46:50  jost
 * Updatefunktion für die Datenbank implementiert
 *
 * Revision 1.1  2006/09/20 15:37:43  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Locale;

import de.jost_net.JVerein.gui.navigation.MyExtension;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.JVereinDBServiceImpl;
import de.jost_net.JVerein.util.HelpConsumer;
import de.willuhn.jameica.gui.extension.ExtensionRegistry;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.Version;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * You need to have at least one class wich inherits from
 * <code>AbstractPlugin</code>. If so, Jameica will detect your plugin
 * automatically at startup.
 */
public class JVereinPlugin extends AbstractPlugin
{

  private Settings settings;

  private static I18N i18n;

  /**
   * MessageConsumer, mit dem JVerein über neu eingetroffene Umsätze aus
   * Hibiscus informiert wird.
   */
  // private UmsatzMessageConsumer umc = null;
  /**
   * constructor.
   * 
   */
  public JVereinPlugin()
  {
    super();
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
    i18n = new I18N("lang/jverein_messages", Locale.getDefault(),
        JVereinPlugin.class.getClassLoader());
  }

  /**
   * This method is invoked on every startup. You can make here some stuff to
   * init your plugin. If you get some errors here and you dont want to activate
   * the plugin, simply throw an ApplicationException.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#init()
   */
  @Override
  public void init() throws ApplicationException
  {
    Logger.info("starting init process for jverein");

    call(new ServiceCall()
    {

      public void call(JVereinDBService service) throws ApplicationException,
          RemoteException
      {
        service.checkConsistency();
      }
    });

    Application.getCallback().getStartupMonitor().addPercentComplete(5);
    ExtensionRegistry.register(new MyExtension(), "jverein.main");
    // this.umc = new UmsatzMessageConsumer();
    // Application.getMessagingFactory().registerMessageConsumer(this.umc);
    MessageConsumer mc = new HelpConsumer();
    Application.getMessagingFactory().getMessagingQueue("jameica.help.missing")
        .registerMessageConsumer(mc);

    // Application.getBootLoader().getBootable(ArchiveService.class);

  }

  /**
   * This method is called only the first time, the plugin is loaded (before
   * executing init()). if your installation procedure was not successfull,
   * throw an ApplicationException.
   */
  @Override
  public void install() throws ApplicationException
  {
    call(new ServiceCall()
    {

      public void call(JVereinDBService service) throws RemoteException
      {
        service.install();
      }
    });
  }

  /**
   * This method will be executed on every version change.
   */
  @Override
  public void update(final Version oldVersion) throws ApplicationException
  {
    call(new ServiceCall()
    {

      public void call(JVereinDBService service) throws RemoteException
      {
        service.update(oldVersion, getManifest().getVersion());
      }
    });
  }

  /**
   * Here you can do some cleanup stuff. The method will be called on every
   * clean shutdown of jameica.
   */
  @Override
  public void shutDown()
  {
    try
    {
      getI18n()
          .storeUntranslated(new FileOutputStream("/tmp/untranslated.txt"));
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Prueft, ob die MD5-Checksumme der Datenbank geprueft werden soll.
   * 
   * @return true, wenn die Checksumme geprueft werden soll.
   */
  public boolean getCheckDatabase()
  {
    return settings.getBoolean("checkdatabase", true);
  }

  /**
   * Hilfsmethode zum bequemen Ausfuehren von Aufrufen auf dem Service.
   */
  private interface ServiceCall
  {

    /**
     * @param service
     * @throws ApplicationException
     * @throws RemoteException
     */
    public void call(JVereinDBService service) throws ApplicationException,
        RemoteException;
  }

  /**
   * Hilfsmethode zum bequemen Ausfuehren von Methoden auf dem Service.
   * 
   * @param call
   *          der Call.
   * @throws ApplicationException
   */
  private void call(ServiceCall call) throws ApplicationException
  {
    if (Application.inClientMode())
      return; // als Client muessen wir die DB nicht installieren

    JVereinDBService service = null;
    try
    {
      // Da die Service-Factory zu diesem Zeitpunkt noch nicht da ist, erzeugen
      // wir uns eine lokale Instanz des Services.
      service = new JVereinDBServiceImpl();
      service.start();
      call.call(service);
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      throw new ApplicationException(getI18n().tr(
          "Fehler beim Initialisieren der Datenbank"), e);
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

  public static I18N getI18n()
  {
    return i18n;
  }
}
