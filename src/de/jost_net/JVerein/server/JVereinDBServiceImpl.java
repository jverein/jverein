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
 * Revision 1.6  2008/01/02 10:59:37  jost
 * Standardwert: H2
 *
 * Revision 1.5  2007/12/16 20:27:48  jost
 * Standardwert zurÃ¼ck auf McKoi
 *
 * Revision 1.4  2007/12/01 19:09:16  jost
 * H2-Support
 *
 * Revision 1.3  2007/10/18 18:20:23  jost
 * Vorbereitung H2-DB
 *
 * Revision 1.2  2007/02/23 20:28:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:48  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Locale;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.DBSupport;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.willuhn.datasource.db.DBServiceImpl;
import de.willuhn.jameica.plugin.PluginResources;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;
import de.willuhn.util.ProgressMonitor;

public class JVereinDBServiceImpl extends DBServiceImpl implements
    JVereinDBService
{
  private static final long serialVersionUID = 7221618925760171630L;

  private DBSupport driver = null;

  /**
   * @throws RemoteException
   */
  public JVereinDBServiceImpl() throws RemoteException
  {
    this(SETTINGS.getString("database.driver", DBSupportH2Impl.class.getName()));
  }

  /**
   * Konstruktor mit expliziter Angabe des Treibers.
   * 
   * @param driverClass
   *          der zu verwendende Treiber.
   * @throws RemoteException
   */
  @SuppressWarnings("unchecked")
  protected JVereinDBServiceImpl(String driverClass) throws RemoteException
  {
    super();
    this.setClassloader(Application.getClassLoader());
    this.setClassFinder(Application.getClassLoader().getClassFinder());
    if (driverClass == null)
      throw new RemoteException("no driver given");
    Logger.info("loading database driver: " + driverClass);
    try
    {
      Class c = Application.getClassLoader().load(driverClass);
      this.driver = (DBSupport) c.newInstance();
    }
    catch (Throwable t)
    {
      throw new RemoteException(
          "unable to load database driver " + driverClass, t);
    }
  }

  public String getName() throws RemoteException
  {
    I18N i18n = Application.getPluginLoader().getPlugin(JVereinPlugin.class)
        .getResources().getI18N();
    return i18n.tr("Datenbank-Service für JVerein");
  }

  protected boolean getAutoCommit() throws RemoteException
  {
    return SETTINGS.getBoolean("autocommit", super.getAutoCommit());
  }

  protected String getJdbcDriver() throws RemoteException
  {
    return this.driver.getJdbcDriver();
  }

  protected String getJdbcPassword() throws RemoteException
  {
    return this.driver.getJdbcPassword();
  }

  protected String getJdbcUrl() throws RemoteException
  {
    return this.driver.getJdbcUrl();
  }

  protected String getJdbcUsername() throws RemoteException
  {
    return this.driver.getJdbcUsername();
  }

  public void checkConsistency() throws RemoteException, ApplicationException
  {
    this.driver.checkConsistency(getConnection());
  }

  public void install() throws RemoteException
  {
    I18N i18n = Application.getPluginLoader().getPlugin(JVereinPlugin.class)
        .getResources().getI18N();
    ProgressMonitor monitor = Application.getCallback().getStartupMonitor();
    monitor.setStatusText(i18n.tr("Installiere JVerein"));
    this.driver.install();

    PluginResources res = Application.getPluginLoader().getPlugin(
        JVereinPlugin.class).getResources();
    File file = new File(res.getPath() + File.separator + "sql", "create.sql");
    this.driver.execute(getConnection(), file);
  }

  public void update(double oldVersion, double newVersion)
      throws RemoteException
  {
    Logger.info("starting update process for jverein");

    DecimalFormat df = (DecimalFormat) DecimalFormat
        .getInstance(Locale.ENGLISH); // Punkt als Dezimal-Trenner
    df.setMaximumFractionDigits(1);
    df.setMinimumFractionDigits(1);
    df.setGroupingUsed(false);

    PluginResources res = Application.getPluginLoader().getPlugin(
        JVereinPlugin.class).getResources();

    double target = newVersion;

    try
    {
      // Wir wiederholen die Updates solange, bis wir bei der aktuellen
      // Versionsnummer angekommen sind.
      while (oldVersion < target)
      {
        newVersion = oldVersion + 0.1d;

        File f = new File(res.getPath() + File.separator + "sql", "update_"
            + df.format(oldVersion) + "-" + df.format(newVersion) + ".sql");

        I18N i18n = Application.getPluginLoader()
            .getPlugin(JVereinPlugin.class).getResources().getI18N();
        ProgressMonitor monitor = Application.getCallback().getStartupMonitor();
        monitor.setStatusText(i18n.tr(
            "Führe JVerein-Update durch: von {0} zu {1}", new String[] {
                df.format(oldVersion), df.format(newVersion) }));

        this.driver.execute(getConnection(), f);

        // OK, naechster Durchlauf
        oldVersion = newVersion;
      }

      Logger.info("Update completed");
    }
    catch (RemoteException re)
    {
      throw re;
    }
    catch (Exception e)
    {
      throw new RemoteException("unable to perform database update from "
          + oldVersion + " to " + newVersion, e);
    }
  }

  public String getSQLTimestamp(String content) throws RemoteException
  {
    return this.driver.getSQLTimestamp(content);
  }

  protected boolean getInsertWithID() throws RemoteException
  {
    return this.driver.getInsertWithID();
  }

  protected void checkConnection(Connection conn) throws SQLException
  {
    try
    {
      this.driver.checkConnection(conn);
    }
    catch (RemoteException re)
    {
      throw new SQLException(re.getMessage());
    }
    super.checkConnection(conn);
  }

}
