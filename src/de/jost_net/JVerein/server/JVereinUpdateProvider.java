package de.jost_net.JVerein.server;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.logging.Logger;
import de.willuhn.sql.version.UpdateProvider;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class JVereinUpdateProvider implements UpdateProvider
{
  private Connection conn;

  private ProgressMonitor progressmonitor;

  private String updatedir;

  public JVereinUpdateProvider(Connection conn, String updatedir,
      ProgressMonitor progressmonitor)
  {
    this.conn = conn;
    this.progressmonitor = progressmonitor;
    this.updatedir = updatedir;
  }

  public Connection getConnection() throws ApplicationException
  {
    return conn;
  }

  public int getCurrentVersion() throws ApplicationException
  {
    int ret = 0;
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt
          .executeQuery("SELECT version FROM version WHERE id = 1");
      if (rs.next())
      {
        ret = rs.getInt(1);
      }
      rs.close();
      stmt.close();
    }
    catch (SQLException e)
    {
      return 0;
    }
    return ret;
  }

  public ProgressMonitor getProgressMonitor()
  {
    return progressmonitor;
  }

  public File getUpdatePath() throws ApplicationException
  {
    return new File(updatedir);
  }

  public void setNewVersion(int newVersion) throws ApplicationException
  {
    try
    {
      Statement stmt = conn.createStatement();
      int anzahl = stmt.executeUpdate("UPDATE version SET version = "
          + newVersion + " WHERE id = 1");
      if (anzahl == 0)
      {
        stmt
            .executeUpdate("INSERT INTO version VALUES (1, " + newVersion + ")");
      }
      stmt.close();
      String msg = "JVerein-DB-Update: " + newVersion;
      progressmonitor.setStatusText(msg);
      Logger.info(msg);
    }
    catch (SQLException e)
    {
      Logger.error(JVereinPlugin.getI18n().tr(
          "Versionsnummer kann nicht eingefügt werden."), e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Versionsnummer kann nicht gespeichert werden."));
    }
  }
}
