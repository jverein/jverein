/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Michael Trapp
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.7  2009/06/11 21:04:24  jost
 * Vorbereitung I18N
 *
 * Revision 1.6  2009/04/25 05:31:41  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.5  2009/04/19 18:51:02  jost
 * Bugfix
 *
 * Revision 1.4  2009/04/10 09:53:08  jost
 * Umstellung auf aktuelle Methoden.
 *
 * Revision 1.3  2008/12/28 21:26:26  jost
 * Javadoc entfernt.
 *
 * Revision 1.2  2008/12/22 21:21:48  jost
 * Bugfix MySQL-Support
 *
 * Revision 1.1  2008/01/29 18:32:23  jost
 * MySQL-Support von Michael Trapp Ã¼bernommen
 *
 * Revision 1.0  2008/01/14 12:00:00  trapp
 * @N Erster Code fuer Unterstuetzung von MySQL
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Implementierung des Datenbank-Supports fuer MySQL.
 */
public class DBSupportMySqlImpl extends AbstractDBSupportImpl
{
  private static final long serialVersionUID = 3516299482096025540L;

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getJdbcDriver()
   */
  public String getJdbcDriver()
  {
    return JVereinDBService.SETTINGS.getString(
        "database.driver.mysql.jdbcdriver", "com.mysql.jdbc.Driver");
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getJdbcPassword()
   */
  public String getJdbcPassword()
  {
    String key = "database.driver.mysql.password";

    // TODO: Erst moeglich, wenn eine GUI zum Eingeben des Passwortes existiert
    // try
    // {
    // // Das Passwort verschluesseln wir nach Moeglichkeit
    // Wallet wallet = Settings.getWallet();
    // return (String) wallet.get(key);
    // }
    // catch (Exception e)
    // {
    // Logger.error("unable to read jdbc password from wallet, using plaintext
    // fallback",e);
    return JVereinDBService.SETTINGS.getString(key, null);
    // }
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getJdbcUrl()
   */
  public String getJdbcUrl()
  {
    return JVereinDBService.SETTINGS
        .getString(
            "database.driver.mysql.jdbcurl",
            "jdbc:mysql://localhost:3306/jverein?useUnicode=Yes&characterEncoding=ISO8859_1");
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getJdbcUsername()
   */
  public String getJdbcUsername()
  {
    return JVereinDBService.SETTINGS.getString(
        "database.driver.mysql.username", "jverein");
  }

  public void checkConsistency(Connection conn) throws RemoteException,
      ApplicationException
  {
    if (!Application.inClientMode())
    {
      try
      {
        new JVereinUpdateProvider(conn, Application.getCallback()
            .getStartupMonitor());
      }
      catch (Exception e2)
      {
        Logger.error(JVereinPlugin.getI18n().tr(
            "Datenbankupdate kann nicht ausgeführt werden."), e2);
        throw new ApplicationException(e2);
      }
    }
  }

  /**
   * 
   */
  public void execute(Connection conn, File sqlScript) throws RemoteException
  {
    if (sqlScript == null)
      return; // Ignore

    sqlScript = new File(sqlScript.getParent(), sqlScript.getName());
    if (!sqlScript.exists())
    {
      Logger.debug("file " + sqlScript + " does not exist, skipping");
      return;
    }
    super.execute(conn, sqlScript);
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getSQLTimestamp(java.lang.String)
   */
  public String getSQLTimestamp(String content) throws RemoteException
  {
    return MessageFormat.format("(UNIX_TIMESTAMP({0})*1000)",
        new Object[] { content });
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getInsertWithID()
   */
  public boolean getInsertWithID() throws RemoteException
  {
    return false;
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#checkConnection(java.sql.Connection)
   */
  public void checkConnection(Connection conn) throws RemoteException
  {
    Statement s = null;
    ResultSet rs = null;
    try
    {
      s = conn.createStatement();
      rs = s.executeQuery("select 1");
    }
    catch (SQLException e)
    {
      // das Ding liefert in getMessage() den kompletten Stacktrace mit, den
      // brauchen wir
      // nicht (das muellt uns nur das Log voll) Also fangen wir sie und werden
      // eine neue
      // saubere mit kurzem Fehlertext
      String msg = e.getMessage();
      if (msg != null && msg.indexOf("\n") != -1)
        msg = msg.substring(0, msg.indexOf("\n"));
      throw new RemoteException(msg);
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
        if (s != null)
          s.close();
      }
      catch (Exception e)
      {
        throw new RemoteException("unable to close statement/resultset", e);
      }
    }
  }

  public int getTransactionIsolationLevel() throws RemoteException
  {
    // damit sehen wir Datenbank-Updates durch andere
    // ohne vorher ein COMMIT machen zu muessen
    // Insbesondere bei MySQL sinnvoll.
    return Connection.TRANSACTION_READ_COMMITTED;
  }
}