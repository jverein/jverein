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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import de.jost_net.JVerein.rmi.DBSupport;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.willuhn.datasource.db.DBServiceImpl;
import de.willuhn.jameica.plugin.Version;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
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
      Class<?> c = Application.getClassLoader().load(driverClass);
      this.driver = (DBSupport) c.newInstance();
    }
    catch (Throwable t)
    {
      throw new RemoteException(
          "unable to load database driver " + driverClass, t);
    }
  }

  @Override
  public String getName()
  {
    return "Datenbank-Service für JVerein";
  }

  @Override
  protected boolean getAutoCommit() throws RemoteException
  {
    return SETTINGS.getBoolean("autocommit", super.getAutoCommit());
  }

  @Override
  protected String getJdbcDriver()
  {
    return this.driver.getJdbcDriver();
  }

  @Override
  protected String getJdbcPassword()
  {
    return this.driver.getJdbcPassword();
  }

  @Override
  protected String getJdbcUrl()
  {
    return this.driver.getJdbcUrl();
  }

  @Override
  protected String getJdbcUsername()
  {
    return this.driver.getJdbcUsername();
  }

  @Override
  public void checkConsistency() throws RemoteException, ApplicationException
  {
    this.driver.checkConsistency(getConnection());
  }

  @Override
  public void install() throws RemoteException
  {
    ProgressMonitor monitor = Application.getCallback().getStartupMonitor();
    monitor.setStatusText("Installiere JVerein");
    // try
    // {
    // new JVereinUpdateProvider(getConnection(), monitor);
    // }
    // catch (ApplicationException e)
    // {
    // throw new RemoteException(e.getMessage());
    // }
    // this.driver.install();

    // PluginResources res = Application.getPluginLoader().getPlugin(
    // JVereinPlugin.class).getResources();
    // Wir schreiben unseren Prefix davor.
    // String prefix = JVereinDBService.SETTINGS.getString(
    // "database.driver.scriptprefix", "h2-");

    // File file = new File(res.getPath() + File.separator + "sql",
    // prefix + "create.sql");
    // this.driver.execute(getConnection(), file);
  }

  @Override
  public void update(Version oldVersion, Version newVersion)
  {
    //
  }

  // Logger.info("starting update process for jverein");
  //
  // DecimalFormat df = (DecimalFormat) DecimalFormat
  // .getInstance(Locale.ENGLISH); // Punkt als Dezimal-Trenner
  // df.setMaximumFractionDigits(1);
  // df.setMinimumFractionDigits(1);
  // df.setGroupingUsed(false);
  //
  // PluginResources res = Application.getPluginLoader().getPlugin(
  // JVereinPlugin.class).getResources();
  //
  // double target = newVersion;
  //
  // try
  // {
  // // Wir wiederholen die Updates solange, bis wir bei der aktuellen
  // // Versionsnummer angekommen sind.
  // while (oldVersion < target)
  // {
  // newVersion = oldVersion + 0.1d;
  //
  // File f = new File(res.getPath() + File.separator + "sql", "update_"
  // + df.format(oldVersion) + "-" + df.format(newVersion) + ".sql");
  //
  // I18N i18n = Application.getPluginLoader()
  // .getPlugin(JVereinPlugin.class).getResources().getI18N();
  // ProgressMonitor monitor = Application.getCallback().getStartupMonitor();
  // monitor.setStatusText(i18n.tr(
  // "Führe JVerein-Update durch: von {0} zu {1}", new String[] {
  // df.format(oldVersion), df.format(newVersion) }));
  //
  // this.driver.execute(getConnection(), f);
  //
  // // OK, naechster Durchlauf
  // oldVersion = newVersion;
  // }
  //
  // Logger.info("Update completed");
  // }
  // catch (RemoteException re)
  // {
  // throw re;
  // }
  // catch (Exception e)
  // {
  // throw new RemoteException("unable to perform database update from "
  // + oldVersion + " to " + newVersion, e);
  // }
  // }

  @Override
  public String getSQLTimestamp(String content) throws RemoteException
  {
    return this.driver.getSQLTimestamp(content);
  }

  @Override
  protected boolean getInsertWithID() throws RemoteException
  {
    return this.driver.getInsertWithID();
  }

  @Override
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
