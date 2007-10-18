/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Kopie aus Hibiscus
 * Copyright (c) by willuhn software & services
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 **********************************************************************/

package de.jost_net.JVerein.server;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.datasource.db.EmbeddedDatabase;
import de.willuhn.jameica.plugin.PluginResources;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.sql.CheckSum;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;
import de.willuhn.util.ProgressMonitor;

/**
 * Implementierung des Datenbank-Supports fuer McKoi.
 */
public class DBSupportMcKoiImpl extends AbstractDBSupportImpl
{
  private static final long serialVersionUID = -1928366492576556400L;

  // Mapper von Datenbank-Hash zu Versionsnummer
  private static HashMap<String, Double> DBMAPPING = new HashMap<String, Double>();

  static
  {
    DBMAPPING.put("p9XzkIUJkzcvEgnLD+YeIA==", new Double(0.7));
    DBMAPPING.put("OaONZJuDOABopEgRYGo3fA==", new Double(0.8));
    DBMAPPING.put("OaONZJuDOABopEgRYGo3fA==", new Double(0.9));
  }

  public String getJdbcDriver()
  {
    return "com.mckoi.JDBCDriver";
  }

  public String getJdbcPassword()
  {
    return "examplepassword";
  }

  public String getJdbcUrl()
  {
    return ":jdbc:mckoi:local://"
        + Application.getPluginLoader().getPlugin(JVereinPlugin.class)
            .getResources().getWorkPath() + "/db/db.conf";
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getJdbcUsername()
   */
  public String getJdbcUsername()
  {
    return "exampleuser";
  }

  public void checkConsistency(Connection conn) throws RemoteException,
      ApplicationException
  {
    // //////////////////////////////////////////////////////////////////////////
    // Damit wir die Updates nicht immer haendisch nachziehen muessen, rufen wir
    // das letzte Update-Script ggf. nochmal auf.
    if (!Application.inClientMode())
    {
      try
      {
        PluginResources res = Application.getPluginLoader().getPlugin(
            JVereinPlugin.class).getResources();
        de.willuhn.jameica.system.Settings s = res.getSettings();
        double size = s.getDouble("sql-update-size", -1);

        DecimalFormat df = (DecimalFormat) DecimalFormat
            .getInstance(Locale.ENGLISH); // Punkt als Dezimal-Trenner
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        df.setGroupingUsed(false);

        double version = Application.getPluginLoader().getManifest(
            JVereinPlugin.class).getVersion();
        double oldVersion = version - 0.1d;

        File f = new File(res.getPath() + File.separator + "sql", "update_"
            + df.format(oldVersion) + "-" + df.format(version) + ".sql");

        if (f.exists())
        {
          long length = f.length();
          if (length != size)
          {
            s.setAttribute("sql-update-size", (double) f.length());
            execute(conn, f);
          }
          else
          {
            Logger.info("database up to date");
          }
        }
      }
      catch (Exception e2)
      {
        Logger.error("unable to execute sql update script", e2);
      }
    }
    // //////////////////////////////////////////////////////////////////////////

    if (!Einstellungen.getCheckDatabase())
    {
      return;
    }
    I18N i18n = Application.getPluginLoader().getPlugin(JVereinPlugin.class)
        .getResources().getI18N();

    try
    {
      ProgressMonitor monitor = Application.getCallback().getStartupMonitor();
      monitor.setStatusText(i18n.tr("Prüfe Datenbank-Integrität"));

      String checkSum = CheckSum.md5(conn, null, "APP");
      if (DBMAPPING.get(checkSum) == null)
      {
        throw new ApplicationException(
            i18n
                .tr(
                    "Datenbank-Checksumme ungültig: {0}. Datenbank-Version nicht kompatibel zur JVerein-Version?",
                    checkSum));
      }
      monitor.setStatusText(i18n.tr("Datenbank-Checksumme korrekt"));
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      throw new RemoteException(i18n.tr("Fehler beim Prüfen der Datenbank"), e);
    }
  }

  public void install() throws RemoteException
  {
    try
    {
      EmbeddedDatabase db = new EmbeddedDatabase(Application.getPluginLoader()
          .getPlugin(JVereinPlugin.class).getResources().getWorkPath()
          + "/db", getJdbcUsername(), getJdbcPassword());
      if (!db.exists())
      {
        I18N i18n = Application.getPluginLoader()
            .getPlugin(JVereinPlugin.class).getResources().getI18N();
        ProgressMonitor monitor = Application.getCallback().getStartupMonitor();
        monitor.setStatusText(i18n.tr("Erstelle JVerein-Datenbank"));
        db.create();
      }
    }
    catch (Exception e)
    {
      throw new RemoteException("unable to create embedded database", e);
    }
  }

  public String getSQLTimestamp(String content) throws RemoteException
  {
    return MessageFormat.format("tonumber({0})", new Object[] { content });
  }

  public boolean getInsertWithID() throws RemoteException
  {
    return true;
  }

  public void checkConnection(Connection conn) throws RemoteException
  {
    // brauchen wir bei McKoi nicht
  }
}
