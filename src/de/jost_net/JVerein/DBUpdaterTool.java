package de.jost_net.JVerein;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class DBUpdaterTool
{

  private static String changelog = "liquibase/jverein.xml";

  public static Integer getVersion(Connection connection)
  {
    try
    {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("select version from version");
      if (rs.next())
      {
        Integer v = rs.getInt(1);
        rs.close();
        stmt.close();
        return v;
      }
      else
      {
        // Darf eigentlich nicht auftreten, dass die Tabelle Version existiert
        // aber leer ist
        return null;
      }
    }
    catch (SQLException e)
    {
      return null; // Tabelle version existiert nicht
    }

  }

  public static boolean isLiquibaseInstalliert(Connection connection)
  {
    try
    {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("select version from databasechangelog");
      return rs.next();
    }
    catch (SQLException e)
    {
      return false;
    }
  }

  public static void updateLiquibase(Connection connection)
      throws LiquibaseException
  {
    Liquibase liquibase = new Liquibase(changelog,
        new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
    liquibase.update("");
  }

  public static void changelogsyncLiquibase(Connection connection)
      throws LiquibaseException
  {
    Liquibase liquibase = new Liquibase(changelog,
        new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
    String contexts = "";
    for (int i = 1; i <= 96; i++)
    {
      if (i > 1)
      {
        contexts += ",";
      }
      contexts += i;
    }
    liquibase.changeLogSync(contexts);
  }
}
