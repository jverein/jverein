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
package de.jost_net.JVerein;

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.navigation.MyExtension;
import de.jost_net.JVerein.io.UmsatzMessageConsumer;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.JVereinDBServiceImpl;
import de.jost_net.JVerein.util.HelpConsumer;
import de.jost_net.JVerein.util.MemoryAnalyzer;
import de.willuhn.jameica.gui.extension.ExtensionRegistry;
import de.willuhn.jameica.messaging.LookupService;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.Version;
import de.willuhn.jameica.services.ArchiveService;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * You need to have at least one class wich inherits from
 * <code>AbstractPlugin</code>. If so, Jameica will detect your plugin
 * automatically at startup.
 */
public class JVereinPlugin extends AbstractPlugin
{

  private Settings settings;

  /**
   * MessageConsumer, mit dem JVerein über neu eingetroffene Umsätze aus
   * Hibiscus informiert wird.
   */
  private UmsatzMessageConsumer umc = null;

  /**
   * constructor.
   * 
   */
  public JVereinPlugin()
  {
    super();
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
    if (settings.getBoolean("memlogger", false))
    {
      MemoryAnalyzer mem = new MemoryAnalyzer();
      mem.start();
    }
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

    update();
    // String driver = settings.getString("jdbc.driver", null);
    // String url = settings.getString("jdbc.url", null);
    // String username = settings.getString("jdbc.user", null);
    // String password = settings.getString("jdbc.password", null);
    // String changelog = "liquibase/jverein.xml";
    // try
    // {
    // Class.forName(driver);
    // Connection connection = DriverManager.getConnection(url, username,
    // password);
    // Liquibase liquibase = new Liquibase(changelog,
    // new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
    // liquibase.update("");
    //
    // }
    // catch (Exception e)
    // {
    // throw new ApplicationException(e);
    // }

    changeRepository();

    call(new ServiceCall()
    {

      @Override
      public void call(JVereinDBService service)
          throws ApplicationException, RemoteException
      {
        service.checkConsistency();
      }
    });

    Application.getCallback().getStartupMonitor().addPercentComplete(5);
    ExtensionRegistry.register(new MyExtension(), "jverein.main");
    this.umc = new UmsatzMessageConsumer();
    Application.getMessagingFactory().registerMessageConsumer(this.umc);
    MessageConsumer mc = new HelpConsumer();
    Application.getMessagingFactory().getMessagingQueue("jameica.help.missing")
        .registerMessageConsumer(mc);

    Application.getBootLoader().getBootable(ArchiveService.class);
  }

  /**
   * This method is called only the first time, the plugin is loaded (before
   * executing init()). if your installation procedure was not successfull,
   * throw an ApplicationException.
   */
  @Override
  public void install()
  {
    //
  }

  /**
   * This method will be executed on every version change.
   */
  @Override
  public void update(final Version oldVersion) throws ApplicationException
  {
    call(new ServiceCall()
    {

      @Override
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
    //
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
    public void call(JVereinDBService service)
        throws ApplicationException, RemoteException;
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
      Logger.error("Fehler beim Methodenaufruf", e);
      throw new ApplicationException("Fehler beim Initialisieren der Datenbank",
          e);
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

  public static boolean isArchiveServiceActive() throws RemoteException
  {
    if (!Einstellungen.getEinstellung().getDokumentenspeicherung())
    {
      return false;
    }
    if (Application.getPluginLoader()
        .getPlugin("de.willuhn.jameica.messaging.Plugin") != null)
    {
      Logger.info("Archiv-Plugin ist lokal installiert");
      return true;
    }
    String uri = LookupService
        .lookup("tcp:de.willuhn.jameica.messaging.Plugin.connector.tcp");
    if (uri != null)
    {
      Logger.info("Archiv-Plugin im LAN gefunden: " + uri);
      return true;
    }
    return false;
  }

  private void update() throws ApplicationException
  {
    // Settings s1 = new Settings(JVereinDBService.class);
    // Unter "database.driver" ist die JVerein-Klasse mit den Parametern der
    // Datenbank gespeichert
    // String d1 = s1.getString("database.driver",
    // "de.jost_net.JVerein.server.DBSupportH2Impl");
    try
    {
      // Die Parameterklasse wird geladen
      // Class<?> c = Class.forName(d1);
      // DBSupport dsupp = (DBSupport) c.newInstance();
      // Parameter auslesen
      // String driver = dsupp.getJdbcDriver();
      // String url = dsupp.getJdbcUrl();
      // String username = dsupp.getJdbcUsername();
      // String password = dsupp.getJdbcPassword();
      // Datenbanktreiber laden
      // Class.forName(driver);
      // Connection herstellen
      // Connection connection = DriverManager.getConnection(url, username,
      // password);
      // Versionsnummer aus der Datenbank auslesen
      // Integer version = DBUpdaterTool.getVersion(connection);
      // Wenn die Version null ist, handelt es sich um eine neue leere Datenbank
      // if (version == null)
      // {
      // Weil die Datenbank leer ist, wird sie neu aufgebaut
      // DBUpdaterTool.updateLiquibase(connection);
      // }
      // else if (version < 360)
      // {
      // Wenn die Versionsnummer < 360 muss noch einmal die alte Updatemimik
      // aufgerufen werden.
      call(new ServiceCall()
      {
        @Override
        public void call(JVereinDBService service) throws RemoteException
        {
          service.install();
        }
      });
      // }
      // Ist Liquibase installiert? Das wird über das vorhandensein der Tabelle
      // databaselog geprüft
      // boolean liquibaseinstalliert = DBUpdaterTool
      // .isLiquibaseInstalliert(connection);
      // if (!liquibaseinstalliert)
      // {
      // Liquibase ist nicht installiert. Über die Synchronisation werden die
      // Liquibase-Tabellen mit Inhalt erzeugt.
      // DBUpdaterTool.changelogsyncLiquibase(connection);
      // }
      // Jetzt wird noch geprüft ob ein Update erforderlich ist.
      // DBUpdaterTool.updateLiquibase(connection);
    }
    catch (Exception e)
    {
      throw new ApplicationException(e);
    }
  }

  private void changeRepository()
  {
    Settings repset = new Settings(
        de.willuhn.jameica.services.RepositoryService.class);
    repset.setStoreWhenRead(true);
    for (int i = 0; i < 10; i++)
    {
      String rep = repset.getString("repository.url." + i, "");
      if (rep.equals("https://sourceforge.net/projects/jverein/files/updates/"))
      {
        repset.setAttribute("repository.url." + i,
            "http://www.jverein.de/updates");
      }
    }
  }
}
