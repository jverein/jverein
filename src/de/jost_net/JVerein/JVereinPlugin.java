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

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.navigation.MyExtension;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.JVereinDBServiceImpl;
import de.willuhn.jameica.gui.extension.ExtensionRegistry;
import de.willuhn.jameica.plugin.AbstractPlugin;
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
   * constructor.
   * 
   */
  public JVereinPlugin()
  {
    super();
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  /**
   * This method is invoked on every startup. You can make here some stuff to
   * init your plugin. If you get some errors here and you dont want to activate
   * the plugin, simply throw an ApplicationException.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#init()
   */
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

  }

  /**
   * This method is called only the first time, the plugin is loaded (before
   * executing init()). if your installation procedure was not successfull,
   * throw an ApplicationException.
   */
  public void install() throws ApplicationException
  {
    call(new ServiceCall()
    {
      public void call(JVereinDBService service) throws ApplicationException,
          RemoteException
      {
        service.install();
      }
    });
  }

  /**
   * This method will be executed on every version change.
   */
  public void update(final double oldVersion) throws ApplicationException
  {
    call(new ServiceCall()
    {
      public void call(JVereinDBService service) throws ApplicationException,
          RemoteException
      {
        service.update(oldVersion, getManifest().getVersion());
      }
    });
  }

  /**
   * Here you can do some cleanup stuff. The method will be called on every
   * clean shutdown of jameica.
   */
  public void shutDown()
  {
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
      throw new ApplicationException(getResources().getI18N().tr(
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

}
