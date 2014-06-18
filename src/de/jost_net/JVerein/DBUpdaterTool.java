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
      ResultSet rs = stmt.executeQuery("select id from databasechangelog");
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
    liquibase.changeLogSync("");
    try
    {
      connection.setAutoCommit(true);
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("delete from databasechangelog where orderexecuted > 96");
      connection.setAutoCommit(false);
      stmt.close();
    }
    catch (SQLException e)
    {
      //
    }
  }
}
