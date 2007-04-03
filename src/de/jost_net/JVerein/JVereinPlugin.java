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

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;

import de.willuhn.datasource.db.EmbeddedDatabase;
import de.willuhn.jameica.hbci.Settings;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.PluginResources;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * You need to have at least one class wich inherits from
 * <code>AbstractPlugin</code>. If so, Jameica will detect your plugin
 * automatically at startup.
 */
public class JVereinPlugin extends AbstractPlugin
{
  private EmbeddedDatabase db = null;

  // Mapper von Datenbank-Hash zu Versionsnummer
  private static HashMap<String, Double> DBMAPPING = new HashMap<String, Double>();

  /**
   * constructor.
   * 
   * @param file
   *          the plugin file (your jar or the plugin directory).
   */
  public JVereinPlugin(File file)
  {
    super(file);
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
    Logger.info("starting init process for hibiscus");
    DBMAPPING.put("p9XzkIUJkzcvEgnLD+YeIA==", new Double(0.7));
    DBMAPPING.put("/L+dtSgsG/njGQb4wM49lA==", new Double(0.8));
    try
    {
      Application.getCallback().getStartupMonitor().setStatusText(
          "jverein: checking database integrity");

      // Damit wir die Updates nicht immer haendisch nachziehen muessen, rufen
      // wir bei einem Fehler das letzte Update-Script nochmal auf.
      if (!Application.inClientMode())
      {
        try
        {
          de.willuhn.jameica.system.Settings s = getResources().getSettings();
          double size = s.getDouble("sql-update-size", -1);

          File f = new File(getResources().getPath()
              + "/sql/update_0.6-0.7.sql");

          if (f.exists())
          {
            long length = f.length();
            if (length != size)
            {
              s.setAttribute("sql-update-size", (double) f.length());
              getDatabase().executeSQLScript(f);
            }
          }
        }
        catch (Exception e2)
        {
          Logger.error("unable to execute sql update script", e2);
        }
      }
      checkConsistency();
    }
    catch (Exception e)
    {
      throw new ApplicationException(
          "Fehler beim Prüfung der Datenbank-Integrität, "
              + "Plugin wird aus Sicherheitsgründen deaktiviert", e);
    }

    Application.getCallback().getStartupMonitor().addPercentComplete(5);
  }

  /**
   * This method is called only the first time, the plugin is loaded (before
   * executing init()). if your installation procedure was not successfull,
   * throw an ApplicationException.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#install()
   */
  public void install() throws ApplicationException
  {
    // If we are running in client/server mode and this instance
    // is the client, we do not need to create a database.
    // Instead of this we will get our objects via RMI from
    // the server
    if (Application.inClientMode())
      return;
    try
    {

      // Let's create an embedded Database
      PluginResources res = Application.getPluginLoader().getPlugin(
          JVereinPlugin.class).getResources();
      db = getDatabase();

      // create the sql tables.
      db.executeSQLScript(new File(res.getPath() + "/sql/create.sql"));

      // That's all. Database installed and tables created ;)
    }
    catch (Exception e)
    {
      throw new ApplicationException("error while installing plugin", e);
    }
  }

  /**
   * Prueft, ob sich die Datenbank der Anwendung im erwarteten Zustand befindet
   * (via MD5-Checksum). Entlarvt Manipulationen des DB-Schemas durch Dritte.
   * 
   * @throws Exception
   */
  private void checkConsistency() throws Exception
  {
    if (Application.inClientMode() || !Settings.getCheckDatabase())
    {
      // Wenn wir als Client laufen, muessen wir uns
      // nicht um die Datenbank kuemmern. Das macht
      // der Server schon
      return;
    }
    String checkSum = getDatabase().getMD5Sum();
    if (DBMAPPING.get(checkSum) == null)
      throw new Exception(
          "database checksum does not match any known version: " + checkSum);
  }

  /**
   * This method will be executed on every version change.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#update(double)
   */
  public void update(double oldVersion) throws ApplicationException
  {
    if (Application.inClientMode())
    {
      return;
    }
    Logger.info("starting update process for jverein");

    DecimalFormat df = (DecimalFormat) DecimalFormat
        .getInstance(Locale.ENGLISH);
    df.setMaximumFractionDigits(1);
    df.setMinimumFractionDigits(1);
    df.setGroupingUsed(false);

    double newVersion = oldVersion + 0.1d;

    try
    {
      File f = new File(getResources().getPath() + "/sql/update_"
          + df.format(oldVersion) + "-" + df.format(newVersion) + ".sql");

      Logger.info("checking sql file " + f.getAbsolutePath());
      while (f.exists())
      {
        Logger.info("  file exists, executing");
        getDatabase().executeSQLScript(f);
        oldVersion = newVersion;
        newVersion = oldVersion + 0.1d;
        f = new File(getResources().getPath() + "/sql/update_"
            + df.format(oldVersion) + "-" + df.format(newVersion) + ".sql");
      }
      Logger.info("Update completed");
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      throw new ApplicationException(getResources().getI18N().tr(
          "Fehler beim Update der Datenbank"), e);
    }
  }

  /**
   * Here you can do some cleanup stuff. The method will be called on every
   * clean shutdown of jameica.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#shutDown()
   */
  public void shutDown()
  {
  }

  /**
   * Liefert die Datenbank des Plugins. Lauft die Anwendung im Client-Mode, wird
   * immer <code>null</code> zurueckgegeben.
   * 
   * @return die Embedded Datenbank.
   * @throws Exception
   */
  private EmbeddedDatabase getDatabase() throws Exception
  {
    if (Application.inClientMode())
      return null;
    if (db != null)
      return db;
    PluginResources res = Application.getPluginLoader().getPlugin(
        JVereinPlugin.class).getResources();
    db = new EmbeddedDatabase(res.getWorkPath() + "/db", "exampleuser",
        "examplepassword");
    return db;
  }
}
