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

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jost_net.JVerein.Variable.MitgliedVar;
import de.jost_net.JVerein.Variable.MitgliedskontoVar;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.DDLTool.AbstractDDLUpdate;
import de.willuhn.jameica.security.Wallet;
import de.willuhn.logging.Logger;
import de.willuhn.sql.ScriptExecutor;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class JVereinUpdateProvider
{
  private ProgressMonitor progressmonitor;

  private StringBuilder sb;

  private String driver;

  public static final String MYSQL = DBSupportMySqlImpl.class.getName();

  public static final String H2 = DBSupportH2Impl.class.getName();

  public JVereinUpdateProvider(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    this.progressmonitor = progressmonitor;
    driver = JVereinDBService.SETTINGS.getString("database.driver", H2);

    int cv = getCurrentVersion(conn);
    if (cv == 0)
    {
      install(conn);
    }
    cv = getCurrentVersion(conn);
    cv++;
    for (int i = cv; i <= 360; i++)
    {
      callMethod(conn, i);
    }
    // Zwischenzeitlich war Liquibase in den Versionen 2.8.0 und 2.8.1 im
    // Einsatz. Wenn durch Liquibase die Datenbankstruktur angepasst wurde, muss
    // die interne Datenbankversion angepasst werden.
    cv = getCurrentVersion(conn);
    if (cv <= 360)
    {
      cv = getLiquibaseVersion(conn);
      if (cv > 0)
      {
        setNewVersion(conn, cv);
      }
      else
      {
        cv = 360;
      }
    }
    cv++;
    try
    {
      while (callMethod2(driver, progressmonitor, conn, cv))
      {
        cv++;
      }
    }
    catch (Exception e)
    {
      throw new ApplicationException(e.getMessage());
    }
  }

  public int getCurrentVersion(Connection conn)
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

  // In den Versionen 2.8.0 und 2.8.1 war Liquibase im Einsatz. Wegen Problemen
  // mit MySQL wurde Liquibase wieder ausgebaut.
  public static Integer getLiquibaseVersion(Connection connection)
  {
    try
    {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt
          .executeQuery("select * from DATABASECHANGELOG where id = '117'");
      if (rs.next())
      {
        // Wenn es den Datensatz mit der id 117 gibt, ist JVerein 2.8.1
        // installiert.
        return 385;
      }

      rs = stmt
          .executeQuery("select * from DATABASECHANGELOG where id = '113'");
      if (rs.next())
      {
        // Wenn es den Datensatz mit der id 113 gibt, ist mindestens JVerein
        // 2.8.0 installiert.
        return 381;
      }

      rs = stmt
          .executeQuery("select * from DATABASECHANGELOG where id = '106'");
      if (rs.next())
      {
        // Wenn es den Datensatz mit der id 106 gibt, ist mindestens JVerein
        // 2.7.0-devel 480 installiert.
        return 372;
      }
      rs = stmt.executeQuery("select * from DATABASECHANGELOG where id = '98'");
      if (rs.next())
      {
        return 362;
      }
      else
      {
        return 360;
      }
    }
    catch (SQLException e)
    {
      return 0; // Tabelle version existiert nicht
    }

  }

  public ProgressMonitor getProgressMonitor()
  {
    return progressmonitor;
  }

  private void setNewVersion(Connection conn, int newVersion)
      throws ApplicationException
  {
    try
    {
      String msg = "JVerein-DB-Update: " + newVersion;
      progressmonitor.setStatusText(msg);
      Logger.info(msg);
      Statement stmt = conn.createStatement();
      int anzahl = stmt.executeUpdate("UPDATE version SET version = "
          + newVersion + " WHERE id = 1");
      if (anzahl == 0)
      {
        stmt.executeUpdate("INSERT INTO version VALUES (1, " + newVersion + ")");
      }
      stmt.close();
    }
    catch (SQLException e)
    {
      Logger.error("Versionsnummer kann nicht eingefügt werden.", e);
      throw new ApplicationException(
          "Versionsnummer kann nicht gespeichert werden.");
    }
  }

  private boolean callMethod(Connection conn, int currentversion)
  {
    Method method = null;

    try
    {
      DecimalFormat df = new DecimalFormat("0000");
      String methodname = "update" + df.format(currentversion);
      method = this.getClass().getDeclaredMethod(methodname, Connection.class);
      // Object[] args = new Object[] { conn };
      method.invoke(this, conn);
    }
    catch (Exception e)
    {
      Logger.error("Fehler beim Methodenaufruf", e);
      return false;
    }
    return true;
  }

  private boolean callMethod2(String driver, ProgressMonitor monitor,
      Connection conn, int currentversion) throws Exception
  {
    try
    {
      DecimalFormat df = new DecimalFormat("0000");
      String clazzname = "de.jost_net.JVerein.server.DDLTool.Updates.Update"
          + df.format(currentversion);
      Class<?> clazz = Class.forName(clazzname);
      Constructor<?> ctor = clazz.getConstructor(String.class,
          ProgressMonitor.class, Connection.class);
      AbstractDDLUpdate object = (AbstractDDLUpdate) ctor.newInstance(driver,
          monitor, conn);
      Method method = object.getClass().getMethod("run", new Class[] {});
      Object[] args = new Object[] {};
      method.invoke(object, args);
    }
    catch (ClassNotFoundException e)
    {
      return false;
    }
    return true;
  }

  public void execute(Connection conn, Map<String, String[]> statements,
      int version, boolean dummy) throws ApplicationException
  {
    for (String driver : statements.keySet())
    {
      String[] sqls = statements.get(driver);
      for (String sql : sqls)
      {
        Map<String, String> stmt = new HashMap<String, String>();
        stmt.put(driver, sql);
        execute(conn, stmt, version);
      }
    }
  }

  public void execute(Connection conn, Map<String, String> statements,
      int version) throws ApplicationException
  {
    String driver = JVereinDBService.SETTINGS.getString("database.driver",
        DBSupportH2Impl.class.getName());
    String sql = statements.get(driver);
    if (sql != null)
    {
      try
      {
        Logger.debug(sql);
        ScriptExecutor.execute(new StringReader(sql), conn, null);
        setNewVersion(conn, version);
      }
      catch (Exception e)
      {
        Logger.error("unable to execute update", e);
        throw new ApplicationException("Fehler beim Ausführen des Updates", e);
      }
    }
  }

  private void install(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitglied (");
    sb.append("id IDENTITY(1), ");
    sb.append("externemitgliedsnummer INTEGER,");
    sb.append("anrede VARCHAR(10),");
    sb.append("titel VARCHAR(20),");
    sb.append("name VARCHAR(40) NOT NULL, ");
    sb.append("vorname VARCHAR(40) NOT NULL,");
    sb.append("strasse VARCHAR(40) NOT NULL, ");
    sb.append("plz VARCHAR(10)  NOT NULL, ");
    sb.append("ort VARCHAR(40) NOT NULL, ");
    sb.append("zahlungsweg INTEGER,");
    sb.append("zahlungsrhytmus INTEGER,");
    sb.append("blz VARCHAR(8),");
    sb.append("konto VARCHAR(10),");
    sb.append("kontoinhaber VARCHAR(27),");
    sb.append("geburtsdatum DATE,");
    sb.append("geschlecht CHAR(1),");
    sb.append("telefonprivat VARCHAR(15),");
    sb.append("telefondienstlich VARCHAR(15),");
    sb.append("handy VARCHAR(15),");
    sb.append("email VARCHAR(50),");
    sb.append("eintritt DATE,");
    sb.append("beitragsgruppe INTEGER,");
    sb.append("zahlerid INTEGER,");
    sb.append("austritt DATE,");
    sb.append("kuendigung DATE,");
    sb.append("vermerk1 VARCHAR(255),");
    sb.append("vermerk2 VARCHAR(255),");
    sb.append("eingabedatum DATE,");
    sb.append("UNIQUE (id), ");
    sb.append("UNIQUE (externemitgliedsnummer),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE beitragsgruppe(");
    sb.append("id IDENTITY(1),");
    sb.append("bezeichnung VARCHAR(30) NOT NULL,");
    sb.append("betrag DOUBLE,");
    sb.append("beitragsart INTEGER DEFAULT 0,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE zusatzabbuchung(");
    sb.append("id IDENTITY(1),");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("faelligkeit DATE NOT NULL,");
    sb.append("buchungstext VARCHAR(27) NOT NULL,");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("startdatum DATE,");
    sb.append("intervall INTEGER,");
    sb.append("endedatum DATE,");
    sb.append("ausfuehrung DATE,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE stammdaten(");
    sb.append("id IDENTITY(1),");
    sb.append("name VARCHAR(30) NOT NULL,");
    sb.append("blz VARCHAR(8)  NOT NULL,");
    sb.append("konto VARCHAR(10) NOT NULL,");
    sb.append("altersgruppen VARCHAR(50),");
    sb.append("jubilaeen VARCHAR(50),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE kursteilnehmer (");
    sb.append("id IDENTITY(1),");
    sb.append("name VARCHAR(27) NOT NULL,");
    sb.append("vzweck1 VARCHAR(27) NOT NULL,");
    sb.append("vzweck2 VARCHAR(27),");
    sb.append("blz VARCHAR(8)  NOT NULL,");
    sb.append("konto VARCHAR(10) NOT NULL,");
    sb.append("betrag DOUBLE       NOT NULL,");
    sb.append("geburtsdatum DATE,");
    sb.append("geschlecht VARCHAR(1),");
    sb.append("eingabedatum  DATE NOT NULL,");
    sb.append("abbudatum DATE,");
    sb.append("UNIQUE (id), ");
    sb.append("PRIMARY KEY (id));");
    sb.append("CREATE TABLE manuellerzahlungseingang (");
    sb.append("id IDENTITY(1), ");
    sb.append("name VARCHAR(27) NOT NULL,");
    sb.append("vzweck1 VARCHAR(27) NOT NULL,");
    sb.append("vzweck2 VARCHAR(27),");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("eingabedatum DATE NOT NULL,");
    sb.append("eingangsdatum DATE,");
    sb.append("UNIQUE (id), ");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE wiedervorlage(");
    sb.append("id IDENTITY(1),");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("datum DATE NOT NULL,");
    sb.append("vermerk VARCHAR(50) NOT NULL,");
    sb.append("erledigung DATE,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE eigenschaften(");
    sb.append("id IDENTITY(1),");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("eigenschaft VARCHAR(50) NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);\n");
    sb.append("CREATE TABLE `version` (");
    sb.append("`id` IDENTITY(1), ");
    sb.append("`version` INTEGER,");
    sb.append("UNIQUE (`id`), ");
    sb.append("PRIMARY KEY (`id`));\n");
    sb.append("create table felddefinition(");
    sb.append("id IDENTITY(1),");
    sb.append("name VARCHAR(50) NOT NULL,");
    sb.append("label VARCHAR(50) NOT NULL,");
    sb.append("laenge integer NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("create table zusatzfelder(");
    sb.append("id IDENTITY(1),");
    sb.append("mitglied integer NOT NULL,");
    sb.append("felddefinition integer NOT NULL,");
    sb.append("feld varchar(1000),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE DEFERRABLE;\n");
    sb.append("CREATE TABLE konto (");
    sb.append("id IDENTITY(1),");
    sb.append("nummer VARCHAR(10),");
    sb.append("bezeichnung VARCHAR(30),");
    sb.append("eroeffnung DATE,");
    sb.append("aufloesung DATE,");
    sb.append("hibiscusid INTEGER,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (nummer),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE buchungsart (");
    sb.append("id IDENTITY(1),");
    sb.append("nummer INTEGER,");
    sb.append("bezeichnung VARCHAR(30),");
    sb.append("art INTEGER,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (nummer),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE buchung (");
    sb.append("id IDENTITY(1),");
    sb.append("umsatzid INTEGER,");
    sb.append("konto INTEGER  NOT NULL,");
    sb.append("name VARCHAR(100),");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("zweck VARCHAR(35),");
    sb.append("zweck2 VARCHAR(35),");
    sb.append("datum DATE NOT NULL,");
    sb.append("art VARCHAR(100),");
    sb.append("kommentar TEXT,");
    sb.append("buchungsart INTEGER,");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id) DEFERRABLE;\n");
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE anfangsbestand (");
    sb.append("id IDENTITY(1),");
    sb.append("konto INTEGER,");
    sb.append("datum DATE,");
    sb.append("betrag DOUBLE,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (konto, datum),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE jahresabschluss (");
    sb.append("id IDENTITY(1),");
    sb.append("von DATE,");
    sb.append("bis DATE,");
    sb.append("datum DATE,");
    sb.append("name VARCHAR(50),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("INSERT INTO version VALUES (1,15);\n");
    sb.append("COMMIT;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitglied (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("externemitgliedsnummer int(10),");
    sb.append("anrede VARCHAR(10),");
    sb.append("titel VARCHAR(20),");
    sb.append("name VARCHAR(40) NOT NULL,");
    sb.append("vorname VARCHAR(40) NOT NULL,");
    sb.append("strasse VARCHAR(40) NOT NULL,");
    sb.append("plz VARCHAR(10)  NOT NULL,");
    sb.append("ort VARCHAR(40) NOT NULL,");
    sb.append("zahlungsweg INTEGER,");
    sb.append("zahlungsrhytmus INTEGER,");
    sb.append("blz VARCHAR(8),");
    sb.append("konto VARCHAR(10),");
    sb.append("kontoinhaber VARCHAR(27),");
    sb.append("geburtsdatum DATE,");
    sb.append("geschlecht CHAR(1),");
    sb.append("telefonprivat VARCHAR(15),");
    sb.append("telefondienstlich VARCHAR(15),");
    sb.append("handy VARCHAR(15),");
    sb.append("email VARCHAR(50),");
    sb.append("eintritt DATE,");
    sb.append("beitragsgruppe INTEGER,");
    sb.append("zahlerid INTEGER,");
    sb.append("austritt DATE,");
    sb.append("kuendigung DATE,");
    sb.append("vermerk1 VARCHAR(255),");
    sb.append("vermerk2 VARCHAR(255),");
    sb.append("eingabedatum DATE,");
    sb.append("UNIQUE (id), ");
    sb.append("UNIQUE (externemitgliedsnummer),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("CREATE TABLE beitragsgruppe (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("bezeichnung VARCHAR(30) NOT NULL,");
    sb.append("betrag DOUBLE,");
    sb.append("beitragsart INTEGER DEFAULT 0,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id) ;\n");
    sb.append("CREATE TABLE zusatzabbuchung (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("faelligkeit DATE NOT NULL,");
    sb.append("buchungstext VARCHAR(27) NOT NULL,");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("startdatum DATE,");
    sb.append("intervall INTEGER,");
    sb.append("endedatum DATE,");
    sb.append("ausfuehrung DATE,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ;\n");
    sb.append("CREATE TABLE stammdaten (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("name VARCHAR(30) NOT NULL,");
    sb.append("blz VARCHAR(8)  NOT NULL,");
    sb.append("konto VARCHAR(10) NOT NULL,");
    sb.append("altersgruppen VARCHAR(50),");
    sb.append("jubilaeen VARCHAR(50),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("CREATE TABLE kursteilnehmer (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("name VARCHAR(27) NOT NULL,");
    sb.append("vzweck1 VARCHAR(27) NOT NULL,");
    sb.append("vzweck2 VARCHAR(27),");
    sb.append("blz VARCHAR(8)  NOT NULL,");
    sb.append("konto VARCHAR(10) NOT NULL,");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("geburtsdatum DATE,");
    sb.append("geschlecht VARCHAR(1),");
    sb.append("eingabedatum DATE NOT NULL,");
    sb.append("abbudatum DATE,");
    sb.append("UNIQUE (id), ");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("CREATE TABLE manuellerzahlungseingang (");
    sb.append("id int(10) AUTO_INCREMENT, ");
    sb.append("name VARCHAR(27) NOT NULL,");
    sb.append("vzweck1 VARCHAR(27) NOT NULL,");
    sb.append("vzweck2 VARCHAR(27),");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("eingabedatum DATE NOT NULL,");
    sb.append("eingangsdatum DATE,");
    sb.append("UNIQUE (id), ");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("CREATE TABLE wiedervorlage (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("datum DATE NOT NULL,");
    sb.append("vermerk VARCHAR(50) NOT NULL,");
    sb.append("erledigung DATE,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ;\n");
    sb.append("CREATE TABLE eigenschaften (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("eigenschaft VARCHAR(50) NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);\n");
    sb.append("CREATE TABLE `version` (");
    sb.append("`id` INTEGER AUTO_INCREMENT,");
    sb.append("`version` INTEGER,");
    sb.append("UNIQUE (`id`),");
    sb.append("PRIMARY KEY (`id`)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("create table felddefinition (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append(" name VARCHAR(50) NOT NULL,");
    sb.append("label VARCHAR(50) NOT NULL,");
    sb.append("laenge integer NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("create table zusatzfelder (");
    sb.append("id integer auto_increment,");
    sb.append("mitglied integer NOT NULL,");
    sb.append("felddefinition integer NOT NULL,");
    sb.append("feld varchar(1000),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n");
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE;\n");
    sb.append("CREATE TABLE konto (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("nummer VARCHAR(10),");
    sb.append("bezeichnung VARCHAR(30),");
    sb.append("eroeffnung DATE,");
    sb.append("aufloesung DATE,");
    sb.append("hibiscusid INTEGER,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (nummer),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("CREATE TABLE buchungsart (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("nummer INTEGER,");
    sb.append("bezeichnung VARCHAR(30),");
    sb.append("art INTEGER,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (nummer),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("CREATE TABLE buchung (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("umsatzid INTEGER,");
    sb.append("konto INTEGER  NOT NULL,");
    sb.append("name VARCHAR(100),");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("zweck VARCHAR(35),");
    sb.append("zweck2 VARCHAR(35),");
    sb.append("datum DATE NOT NULL,");
    sb.append("art VARCHAR(100),");
    sb.append("kommentar TEXT,");
    sb.append("buchungsart   INTEGER,");
    sb.append("PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id);\n");
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id);\n");
    sb.append("CREATE TABLE anfangsbestand (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("konto INTEGER,");
    sb.append("datum DATE,");
    sb.append("BETRAG DOUBLE,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (konto, datum),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id);\n");
    sb.append("CREATE TABLE jahresabschluss (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("von DATE,");
    sb.append("bis DATE,");
    sb.append("datum DATE,");
    sb.append("name VARCHAR(50),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("INSERT INTO version VALUES (1,15);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 15);
  }

  @SuppressWarnings("unused")
  private void update0016(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE formular (");
    sb.append("  id IDENTITY(1),");
    sb.append(" inhalt BLOB,");
    sb.append(" art integer,");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE formular (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" inhalt BLOB,");
    sb.append(" art integer,");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 16);
  }

  @SuppressWarnings("unused")
  private void update0017(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE formularfeld(");
    sb.append(" id IDENTITY(1),");
    sb.append(" formular integer,");
    sb.append(" name VARCHAR(20),");
    sb.append(" x double,");
    sb.append(" y double,");
    sb.append(" font VARCHAR(20),");
    sb.append(" fontsize integer,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE formularfeld (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" formular integer,");
    sb.append(" name VARCHAR(20),");
    sb.append(" x double,");
    sb.append(" y double,");
    sb.append(" font VARCHAR(20),");
    sb.append(" fontsize integer,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 17);
  }

  @SuppressWarnings("unused")
  private void update0018(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung alter column  kommentar varchar(1000);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung modify column  kommentar varchar(1000);\n");

    execute(conn, statements, 18);
  }

  @SuppressWarnings("unused")
  private void update0019(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table formularfeld add fontstyle integer;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table formularfeld add fontstyle integer;\n");

    execute(conn, statements, 19);
  }

  @SuppressWarnings("unused")
  private void update0020(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE spendenbescheinigung (");
    sb.append(" id IDENTITY(1),");
    sb.append(" zeile1 VARCHAR(40),");
    sb.append(" zeile2 VARCHAR(40),");
    sb.append(" zeile3 VARCHAR(40),");
    sb.append(" zeile4 VARCHAR(40),");
    sb.append(" zeile5 VARCHAR(40),");
    sb.append(" zeile6 VARCHAR(40),");
    sb.append(" zeile7 VARCHAR(40),");
    sb.append(" spendedatum DATE,");
    sb.append(" bescheinigungsdatum DATE,");
    sb.append(" betrag DOUBLE,");
    sb.append(" formular INTEGER,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE RESTRICT;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE spendenbescheinigung (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" zeile1 VARCHAR(40),");
    sb.append(" zeile2 VARCHAR(40),");
    sb.append(" zeile3 VARCHAR(40),");
    sb.append(" zeile4 VARCHAR(40),");
    sb.append(" zeile5 VARCHAR(40),");
    sb.append(" zeile6 VARCHAR(40),");
    sb.append(" zeile7 VARCHAR(40),");
    sb.append(" spendedatum DATE,");
    sb.append(" bescheinigungsdatum DATE,");
    sb.append(" betrag DOUBLE,");
    sb.append(" formular INTEGER,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE RESTRICT;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 20);
  }

  @SuppressWarnings("unused")
  private void update0021(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE UNIQUE INDEX ixFormular1 ON formular(bezeichnung);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE UNIQUE INDEX ixFormular1 ON formular(bezeichnung);\n");

    execute(conn, statements, 21);
  }

  @SuppressWarnings("unused")
  private void update0022(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE abrechnung (");
    sb.append(" id IDENTITY(1), ");
    sb.append(" mitglied INTEGER,");
    sb.append(" datum DATE,");
    sb.append(" zweck1 VARCHAR(27),");
    sb.append(" zweck2 VARCHAR(27),");
    sb.append(" betrag DOUBLE,");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE abrechnung (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" mitglied INTEGER,");
    sb.append(" datum DATE,");
    sb.append(" zweck1 VARCHAR(27),");
    sb.append(" zweck2 VARCHAR(27),");
    sb.append(" betrag DOUBLE,");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 22);
  }

  @SuppressWarnings("unused")
  private void update0023(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE INDEX ixAbrechnung1 ON abrechnung(mitglied);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE INDEX ixAbrechnung1 ON abrechnung(mitglied);\n");

    execute(conn, statements, 23);
  }

  @SuppressWarnings("unused")
  private void update0024(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE abrechnung ADD CONSTRAINT fkAbrechnung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE RESTRICT;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE abrechnung ADD CONSTRAINT fkAbrechnung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE RESTRICT;\n");

    execute(conn, statements, 24);
  }

  @SuppressWarnings("unused")
  private void update0025(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE stammdaten ADD altersjubilaeen varchar(50);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE stammdaten ADD altersjubilaeen varchar(50);\n");

    execute(conn, statements, 25);
  }

  @SuppressWarnings("unused")
  private void update0026(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "ALTER TABLE mitglied ADD adressierungszusatz varchar(40) before strasse;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE mitglied ADD adressierungszusatz varchar(40) after vorname;\n");

    execute(conn, statements, 26);
  }

  @SuppressWarnings("unused")
  private void update0027(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "UPDATE mitglied SET adressierungszusatz = '' WHERE adressierungszusatz is null;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "UPDATE mitglied SET adressierungszusatz = '' WHERE adressierungszusatz is null;\n");

    execute(conn, statements, 27);
  }

  @SuppressWarnings("unused")
  private void update0028(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE einstellung (");
    sb.append(" id IDENTITY(1), ");
    sb.append(" geburtsdatumpflicht CHAR(5),");
    sb.append(" eintrittsdatumpflicht CHAR(5),");
    sb.append(" kommunikationsdaten CHAR(5),");
    sb.append(" zusatzabbuchung CHAR(5),");
    sb.append(" vermerke CHAR(5),");
    sb.append(" wiedervorlage CHAR(5),");
    sb.append(" kursteilnehmer CHAR(5),");
    sb.append(" externemitgliedsnummer CHAR(5),");
    sb.append(" beitragsmodel INTEGER,");
    sb.append(" dateinamenmuster VARCHAR(50),");
    sb.append(" beginngeschaeftsjahr CHAR(6),");
    sb.append(" rechnungfuerabbuchung CHAR(5),");
    sb.append(" rechnungfuerueberweisung CHAR(5),");
    sb.append(" rechnungfuerbarzahlung CHAR(5),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE einstellung (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" geburtsdatumpflicht CHAR(5),");
    sb.append(" eintrittsdatumpflicht CHAR(5),");
    sb.append(" kommunikationsdaten CHAR(5),");
    sb.append(" zusatzabbuchung CHAR(5),");
    sb.append(" vermerke CHAR(5),");
    sb.append(" wiedervorlage CHAR(5),");
    sb.append(" kursteilnehmer CHAR(5),");
    sb.append(" externemitgliedsnummer CHAR(5),");
    sb.append(" beitragsmodel INTEGER,");
    sb.append(" dateinamenmuster VARCHAR(50),");
    sb.append(" beginngeschaeftsjahr CHAR(6),");
    sb.append(" rechnungfuerabbuchung CHAR(5),");
    sb.append(" rechnungfuerueberweisung CHAR(5),");
    sb.append(" rechnungfuerbarzahlung CHAR(5),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 28);
  }

  @SuppressWarnings("unused")
  private void update0029(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE buchung ADD auszugsnummer integer before name;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE buchung ADD auszugsnummer integer after konto;\n");

    execute(conn, statements, 29);
  }

  @SuppressWarnings("unused")
  private void update0030(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE buchung ADD blattnummer integer before name;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE buchung ADD blattnummer integer after auszugsnummer;\n");

    execute(conn, statements, 30);
  }

  @SuppressWarnings("unused")
  private void update0031(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN telefonprivat varchar(20);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN telefonprivat varchar(20);\n");

    execute(conn, statements, 31);
  }

  @SuppressWarnings("unused")
  private void update0032(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN telefondienstlich varchar(20);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN telefondienstlich varchar(20);\n");

    execute(conn, statements, 32);
  }

  @SuppressWarnings("unused")
  private void update0033(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN handy varchar(20);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN handy varchar(20);\n");

    execute(conn, statements, 33);
  }

  @SuppressWarnings("unused")
  private void update0034(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE spendenbescheinigung ADD ersatzaufwendungen char(5);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE spendenbescheinigung ADD ersatzaufwendungen char(5);\n");

    execute(conn, statements, 34);
  }

  @SuppressWarnings("unused")
  private void update0035(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE report");
    sb.append("(");
    sb.append(" id IDENTITY(1),");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" art INTEGER,");
    sb.append(" quelle BLOB,");
    sb.append(" kompilat BLOB,");
    sb.append(" aenderung TIMESTAMP,");
    sb.append(" nutzung  TIMESTAMP,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE report");
    sb.append(" (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" art INTEGER,");
    sb.append(" quelle BLOB,");
    sb.append(" kompilat BLOB,");
    sb.append(" aenderung TIMESTAMP,");
    sb.append(" nutzung TIMESTAMP,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 35);
  }

  @SuppressWarnings("unused")
  private void update0036(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("drop table report;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("drop table report;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 36);
  }

  @SuppressWarnings("unused")
  private void update0037(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table lehrgangsart (");
    sb.append(" id IDENTITY(1),");
    sb.append(" bezeichnung VARCHAR(50) NOT NULL,");
    sb.append(" von DATE,");
    sb.append(" bis DATE,");
    sb.append(" veranstalter VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(");\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("create table lehrgangsart (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" bezeichnung VARCHAR(50) NOT NULL,");
    sb.append(" von DATE,");
    sb.append(" bis DATE,");
    sb.append(" veranstalter VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 37);
  }

  @SuppressWarnings("unused")
  private void update0038(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD lehrgaenge char(5) before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD lehrgaenge char(5) after kursteilnehmer;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 38);
  }

  @SuppressWarnings("unused")
  private void update0039(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table lehrgang (");
    sb.append(" id IDENTITY(1),");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" lehrgangsart INTEGER NOT NULL,");
    sb.append(" von DATE,");
    sb.append(" bis DATE,");
    sb.append(" veranstalter VARCHAR(50),");
    sb.append(" ergebnis VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(");\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("create table lehrgang (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" lehrgangsart INTEGER NOT NULL,");
    sb.append(" von DATE,");
    sb.append(" bis DATE,");
    sb.append(" veranstalter VARCHAR(50),");
    sb.append(" ergebnis VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 39);
  }

  @SuppressWarnings("unused")
  private void update0040(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 40);
  }

  @SuppressWarnings("unused")
  private void update0041(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 41);
  }

  @SuppressWarnings("unused")
  private void update0042(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang DROP CONSTRAINT fkLehrgang1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang DROP FOREIGN KEY fkLehrgang1;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 42);
  }

  @SuppressWarnings("unused")
  private void update0043(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang DROP CONSTRAINT fkLehrgang2;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang DROP FOREIGN KEY fkLehrgang2;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 43);
  }

  @SuppressWarnings("unused")
  private void update0044(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 44);
  }

  @SuppressWarnings("unused")
  private void update0045(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 45);
  }

  @SuppressWarnings("unused")
  private void update0046(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE formular ALTER COLUMN inhalt LONGBLOB;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE formular MODIFY COLUMN inhalt LONGBLOB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 46);
  }

  @SuppressWarnings("unused")
  private void update0047(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD juristischepersonen char(5) before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD juristischepersonen char(5) after lehrgaenge;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 47);
  }

  @SuppressWarnings("unused")
  private void update0048(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD personenart char(1) before anrede;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD personenart char(1) after externemitgliedsnummer;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 48);
  }

  @SuppressWarnings("unused")
  private void update0049(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("update mitglied set personenart = 'n';\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("update mitglied set personenart = 'n';\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 49);
  }

  @SuppressWarnings("unused")
  private void update0050(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("alter table mitglied alter column vorname varchar(40) null;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("alter table mitglied modify column vorname varchar(40) null;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 50);
  }

  @SuppressWarnings("unused")
  private void update0051(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder DROP CONSTRAINT fkzusatzfelder1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder DROP FOREIGN KEY fkZusatzfelder1;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 51);
  }

  @SuppressWarnings("unused")
  private void update0052(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkzusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkzusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 52);
  }

  @SuppressWarnings("unused")
  private void update0053(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD aktuellegeburtstagevorher integer default 3 before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD aktuellegeburtstagevorher integer default 3 after juristischepersonen;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 53);
  }

  @SuppressWarnings("unused")
  private void update0054(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD aktuellegeburtstagenachher integer default 7 before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD aktuellegeburtstagenachher integer default 7 after aktuellegeburtstagevorher;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 54);
  }

  @SuppressWarnings("unused")
  private void update0055(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE buchungsklasse (");
    sb.append(" id IDENTITY(1),");
    sb.append(" nummer INTEGER,");
    sb.append(" bezeichnung VARCHAR(30),");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (nummer),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE buchungsklasse (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" nummer INTEGER,");
    sb.append(" bezeichnung VARCHAR(30),");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (nummer),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 55);
  }

  @SuppressWarnings("unused")
  private void update0056(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE `buchungsart` add COLUMN buchungsklasse INTEGER;\n");
    sb.append("CREATE INDEX buchungsart_2 on buchungsart(buchungsklasse);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE `buchungsart` add COLUMN buchungsklasse INTEGER;\n");
    sb.append("CREATE INDEX buchungsart_2 on buchungsart(buchungsklasse);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 56);
  }

  @SuppressWarnings("unused")
  private void update0057(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchungsart ADD CONSTRAINT fkBuchungsart2 FOREIGN KEY (buchungsklasse) REFERENCES buchungsklasse (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchungsart ADD CONSTRAINT fkBuchungsart2 FOREIGN KEY (buchungsklasse) REFERENCES buchungsklasse (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 57);
  }

  @SuppressWarnings("unused")
  private void update0058(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD updateinterval integer default 30;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD updateinterval integer default 30;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 58);
  }

  @SuppressWarnings("unused")
  private void update0059(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD updatediaginfos char(5)default 'true';\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD updatediaginfos char(5)default 'true';\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 59);
  }

  @SuppressWarnings("unused")
  private void update0060(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD updatelastcheck date;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD updatelastcheck date;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 60);
  }

  @SuppressWarnings("unused")
  private void update0061(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("-- nothing to do\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("alter table `anfangsbestand` change BETRAG `betrag` double default NULL;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 61);
  }

  @SuppressWarnings("unused")
  private void update0062(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_server varchar(50);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_server varchar(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 62);
  }

  @SuppressWarnings("unused")
  private void update0063(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_port char(5);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_port char(5);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 63);
  }

  @SuppressWarnings("unused")
  private void update0064(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_auth_user varchar(50);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_auth_user varchar(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 64);
  }

  @SuppressWarnings("unused")
  private void update0065(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_auth_pwd varchar(50);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_auth_pwd varchar(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 65);
  }

  @SuppressWarnings("unused")
  private void update0066(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_from_address varchar(50);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_from_address varchar(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 66);
  }

  @SuppressWarnings("unused")
  private void update0067(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_ssl char(5);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_ssl char(5);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 67);
  }

  @SuppressWarnings("unused")
  private void update0068(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE eigenschaftgruppe (");
    sb.append(" id IDENTITY(1),");
    sb.append(" bezeichnung VARCHAR(30) NOT NULL,");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE eigenschaftgruppe (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" bezeichnung VARCHAR(30) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 68);
  }

  @SuppressWarnings("unused")
  private void update0069(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE eigenschaft (");
    sb.append("id                IDENTITY(1),");
    sb.append("eigenschaftgruppe     INTEGER,");
    sb.append("bezeichnung     VARCHAR(30) NOT NULL,");
    sb.append("UNIQUE        (id),");
    sb.append("PRIMARY KEY   (id));\n");
    sb.append("ALTER TABLE eigenschaft ADD CONSTRAINT fkEigenschaft1 FOREIGN KEY (eigenschaftgruppe) REFERENCES eigenschaftgruppe (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE eigenschaft (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append("eigenschaftgruppe     INTEGER,");
    sb.append(" bezeichnung VARCHAR(30) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE eigenschaft ADD CONSTRAINT fkEigenschaft1 FOREIGN KEY (eigenschaftgruppe) REFERENCES eigenschaftgruppe (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 69);
  }

  @SuppressWarnings("unused")
  private void update0070(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN anrede varchar(40);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN anrede varchar(40);\n");

    execute(conn, statements, 70);
  }

  @SuppressWarnings("unused")
  private void update0071(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN titel varchar(40);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN titel varchar(40);\n");

    execute(conn, statements, 71);
  }

  @SuppressWarnings("unused")
  private void update0072(Connection conn) throws ApplicationException
  {
    try
    {
      List<String> eigenschaften = new ArrayList<String>();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt
          .executeQuery("select eigenschaft from eigenschaften group by eigenschaft order by eigenschaft");
      while (rs.next())
      {
        eigenschaften.add(rs.getString(1));
      }
      rs.close();
      stmt.close();
      PreparedStatement pstmt = conn
          .prepareStatement("INSERT INTO eigenschaft (bezeichnung) values (?)");
      for (String eig : eigenschaften)
      {
        pstmt.setString(1, eig);
        pstmt.executeUpdate();
      }
      pstmt.close();
    }
    catch (Exception e)
    {
      Logger.error("kann Liste der Eigenschaften nicht aufbauen", e);
    }
    setNewVersion(conn, 72);
  }

  @SuppressWarnings("unused")
  private void update0073(Connection conn) throws ApplicationException
  {
    try
    {
      Map<String, String> eigenschaften = new HashMap<String, String>();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt
          .executeQuery("select id, bezeichnung from eigenschaft");
      while (rs.next())
      {
        eigenschaften.put(rs.getString(1), rs.getString(2));
      }
      rs.close();
      stmt.close();
      PreparedStatement pstmt = conn
          .prepareStatement("UPDATE eigenschaften SET eigenschaft = ? WHERE eigenschaft = ?");
      for (String eig : eigenschaften.keySet())
      {
        pstmt.setString(1, eig);
        pstmt.setString(2, eigenschaften.get(eig));
        pstmt.executeUpdate();
      }
      pstmt.close();
    }
    catch (Exception e)
    {
      Logger.error("kann Liste der Eigenschaften nicht aufbauen", e);
    }
    setNewVersion(conn, 73);
  }

  @SuppressWarnings("unused")
  private void update0074(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table eigenschaften alter column  eigenschaft integer not null;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table eigenschaften modify column  eigenschaft integer not null;\n");

    execute(conn, statements, 74);
  }

  @SuppressWarnings("unused")
  private void update0075(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "INSERT INTO eigenschaftgruppe (id, bezeichnung) values ('1', 'keine Zuordnung');";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 75);
  }

  @SuppressWarnings("unused")
  private void update0076(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "UPDATE eigenschaft SET eigenschaftgruppe = '1' WHERE eigenschaftgruppe IS NULL;";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 76);
  }

  @SuppressWarnings("unused")
  private void update0077(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung);\n");

    execute(conn, statements, 77);
  }

  @SuppressWarnings("unused")
  private void update0078(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "CREATE UNIQUE INDEX ixEigenschaftGruppe1 ON eigenschaftgruppe(bezeichnung);\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "CREATE UNIQUE INDEX ixEigenschaftGruppe1 ON eigenschaftgruppe(bezeichnung);\n");

    execute(conn, statements, 78);
  }

  @SuppressWarnings("unused")
  private void update0079(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN updateinterval;\n"
            + "ALTER TABLE einstellung DROP COLUMN updatediaginfos;\n"
            + "ALTER TABLE einstellung DROP COLUMN updatelastcheck;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN updateinterval, "
            + "DROP COLUMN updatediaginfos, DROP COLUMN updatelastcheck;\n");

    execute(conn, statements, 79);
  }

  @SuppressWarnings("unused")
  private void update0080(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE felddefinition ADD datentyp integer default 1 before laenge;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE felddefinition ADD datentyp integer default 1 after label;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 80);
  }

  @SuppressWarnings("unused")
  private void update0081(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD felddatum DATE NULL;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD felddatum DATE NULL;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 81);
  }

  @SuppressWarnings("unused")
  private void update0082(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldganzzahl integer NULL;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldganzzahl integer NULL;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 82);
  }

  @SuppressWarnings("unused")
  private void update0083(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldgleitkommazahl DOUBLE null;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldgleitkommazahl DOUBLE null;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 83);
  }

  @SuppressWarnings("unused")
  private void update0084(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldwaehrung DECIMAL(15,2);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldwaehrung DECIMAL(15,2);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 84);
  }

  @SuppressWarnings("unused")
  private void update0085(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldjanein CHAR(5) NULL;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzfelder ADD feldjanein CHAR(5) NULL;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 85);
  }

  @SuppressWarnings("unused")
  private void update0086(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zahlungsweg INT DEFAULT 1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zahlungsweg INT DEFAULT 1;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 86);
  }

  @SuppressWarnings("unused")
  private void update0087(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zahlungsrhytmus INT DEFAULT 12;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zahlungsrhytmus INT DEFAULT 12;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 87);
  }

  @SuppressWarnings("unused")
  private void update0088(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailvorlage (");
    sb.append(" id IDENTITY(1), ");
    sb.append(" betreff VARCHAR(100) NOT NULL,");
    sb.append(" txt VARCHAR(1000) NOT NULL,");
    sb.append(" UNIQUE (id), ");
    sb.append(" UNIQUE (betreff),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailvorlage (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" betreff VARCHAR(100) NOT NULL,");
    sb.append(" txt VARCHAR(1000),");
    sb.append(" UNIQUE (id), ");
    sb.append(" UNIQUE (betreff),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 88);
  }

  @SuppressWarnings("unused")
  private void update0089(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mail (");
    sb.append(" id IDENTITY(1), ");
    sb.append(" betreff VARCHAR(100) NOT NULL,");
    sb.append(" txt VARCHAR(1000) NOT NULL,");
    sb.append(" bearbeitung TIMESTAMP NOT NULL, ");
    sb.append(" versand TIMESTAMP, ");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mail (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" betreff VARCHAR(100) NOT NULL,");
    sb.append(" txt VARCHAR(1000),");
    sb.append(" bearbeitung TIMESTAMP NOT NULL, ");
    sb.append(" versand TIMESTAMP, ");
    sb.append(" UNIQUE (id), ");
    sb.append("UNIQUE (betreff),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 89);
  }

  @SuppressWarnings("unused")
  private void update0090(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailempfaenger (");
    sb.append(" id IDENTITY(1), ");
    sb.append(" mail INTEGER NOT NULL, ");
    sb.append(" mitglied INTEGER,");
    sb.append(" adresse VARCHAR(50),");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailempfaenger (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" mail INTEGER NOT NULL, ");
    sb.append(" mitglied INTEGER, ");
    sb.append(" adresse VARCHAR(50),");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 90);
  }

  @SuppressWarnings("unused")
  private void update0091(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailEmpfaenger1 FOREIGN KEY (mail) REFERENCES mail (id)  ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailEmpfaenger1 FOREIGN KEY (mail) REFERENCES mail (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 91);
  }

  @SuppressWarnings("unused")
  private void update0092(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailEmpfaenger2 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailEmpfaenger2 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 92);
  }

  @SuppressWarnings("unused")
  private void update0093(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailanhang (");
    sb.append("  id IDENTITY(1), ");
    sb.append(" mail INTEGER NOT NULL, ");
    sb.append(" anhang BLOB,");
    sb.append(" dateiname VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailanhang (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" mail INTEGER NOT NULL, ");
    sb.append(" anhang BLOB,");
    sb.append(" dateiname VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 93);
  }

  @SuppressWarnings("unused")
  private void update0094(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailanhang ADD CONSTRAINT fkMailAnhang1 FOREIGN KEY (mail) REFERENCES mail (id)  ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailanhang ADD CONSTRAINT fkMailAnhang1 FOREIGN KEY (mail) REFERENCES mail (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 94);
  }

  @SuppressWarnings("unused")
  private void update0095(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("DELETE FROM eigenschaften WHERE eigenschaften.id IN (select e.id from eigenschaften as e left join mitglied as m on e.mitglied = m.id where m.id is null  );\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("DELETE eigenschaften from eigenschaften left join mitglied on mitglied.id = eigenschaften.mitglied;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 95);

    statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE  DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) on delete cascade;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 95);
  }

  @SuppressWarnings("unused")
  private void update0096(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE abrechnungslauf (");
    sb.append(" id IDENTITY(1), ");
    sb.append(" datum DATE NOT NULL,");
    sb.append(" modus INTEGER NOT NULL,");
    sb.append(" stichtag DATE, ");
    sb.append(" eingabedatum DATE, ");
    sb.append(" zahlungsgrund VARCHAR(27),");
    sb.append(" zusatzbetraege CHAR(5), ");
    sb.append(" kursteilnehmer CHAR(5), ");
    sb.append(" dtausdruck CHAR(5), ");
    sb.append(" abbuchungsausgabe INTEGER, ");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE abrechnungslauf (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" datum DATE NOT NULL,");
    sb.append(" modus INTEGER NOT NULL,");
    sb.append(" stichtag DATE, ");
    sb.append(" eingabedatum DATE, ");
    sb.append(" zahlungsgrund VARCHAR(27),");
    sb.append(" zusatzbetraege CHAR(5), ");
    sb.append(" kursteilnehmer CHAR(5), ");
    sb.append(" dtausdruck CHAR(5), ");
    sb.append(" abbuchungsausgabe INTEGER, ");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 96);
  }

  @SuppressWarnings("unused")
  private void update0097(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedskonto (");
    sb.append(" id IDENTITY(1), ");
    sb.append(" abrechnungslauf INTEGER,");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" datum DATE NOT NULL,");
    sb.append(" zweck1 VARCHAR(27),");
    sb.append(" zweck2 VARCHAR(27),");
    sb.append(" zahlungsweg INTEGER, ");
    sb.append(" betrag DOUBLE,");
    sb.append(" UNIQUE (id), ");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedskonto (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" abrechnungslauf INTEGER,");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" datum DATE NOT NULL,");
    sb.append(" zweck1 VARCHAR(27),");
    sb.append(" zweck2 VARCHAR(27),");
    sb.append(" zahlungsweg INTEGER, ");
    sb.append(" betrag DOUBLE,");
    sb.append(" UNIQUE (id), ");
    sb.append(" INDEX(abrechnungslauf), ");
    sb.append(" INDEX(mitglied), ");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 97);
  }

  @SuppressWarnings("unused")
  private void update0098(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitgliedskonto ADD CONSTRAINT fkMitgliedskonto1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE  DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitgliedskonto ADD CONSTRAINT fkMitgliedskonto1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) on delete cascade;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 98);
  }

  @SuppressWarnings("unused")
  private void update0099(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitgliedskonto ADD CONSTRAINT fkMitgliedskonto2 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE  DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitgliedskonto ADD CONSTRAINT fkMitgliedskonto2 FOREIGN KEY (mitglied) REFERENCES mitglied (id) on delete cascade;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 99);
  }

  @SuppressWarnings("unused")
  private void update0100(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD mitgliedskonto char(5) before aktuellegeburtstagevorher;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD mitgliedskonto char(5) after juristischepersonen;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 100);
  }

  @SuppressWarnings("unused")
  private void update0101(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung add mitgliedskonto integer;\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung add mitgliedskonto integer;\n");

    execute(conn, statements, 101);
  }

  @SuppressWarnings("unused")
  private void update0102(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung3 FOREIGN KEY (mitgliedskonto) REFERENCES mitgliedskonto (id)  ON DELETE CASCADE  DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung3 FOREIGN KEY (mitgliedskonto) REFERENCES mitgliedskonto (id)  ON DELETE CASCADE  ;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 102);
  }

  @SuppressWarnings("unused")
  private void update0103(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung add abrechnungslauf integer;\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung add abrechnungslauf integer;\n");

    execute(conn, statements, 103);
  }

  @SuppressWarnings("unused")
  private void update0104(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung4 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE  DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung4 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 104);
  }

  @SuppressWarnings("unused")
  private void update0105(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD manuellezahlungen char(5) before aktuellegeburtstagevorher;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD manuellezahlungen char(5) after mitgliedskonto;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 105);
  }

  @SuppressWarnings("unused")
  private void update0106(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungen13 char(5) before aktuellegeburtstagevorher;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungen13 char(5) after manuellezahlungen;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 106);
  }

  @SuppressWarnings("unused")
  private void update0107(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungtextabbuchung varchar(100) before aktuellegeburtstagevorher;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungtextabbuchung varchar(100) after rechnungen13;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 107);
  }

  @SuppressWarnings("unused")
  private void update0108(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungtextueberweisung varchar(100) before aktuellegeburtstagevorher;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungtextueberweisung varchar(100) after rechnungtextabbuchung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 108);
  }

  @SuppressWarnings("unused")
  private void update0109(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungtextbar varchar(100) before aktuellegeburtstagevorher;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD rechnungtextbar varchar(100) after rechnungtextueberweisung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 109);
  }

  @SuppressWarnings("unused")
  private void update0110(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung DROP CONSTRAINT fkBuchung3;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung3 FOREIGN KEY (mitgliedskonto) REFERENCES mitgliedskonto (id);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung DROP FOREIGN KEY fkBuchung3;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung3 FOREIGN KEY (mitgliedskonto) REFERENCES mitgliedskonto (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 110);
  }

  @SuppressWarnings("unused")
  private void update0111(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchungsart alter column  bezeichnung varchar(50);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchungsart modify column  bezeichnung varchar(50);\n");

    execute(conn, statements, 111);
  }

  @SuppressWarnings("unused")
  private void update0112(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedfoto (");
    sb.append(" id IDENTITY(1),");
    sb.append(" mitglied INTEGER NOT NULL, ");
    sb.append(" foto BLOB,");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (mitglied), ");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedfoto (");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" mitglied INTEGER NOT NULL, ");
    sb.append(" foto BLOB,");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (mitglied), ");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 112);
  }

  @SuppressWarnings("unused")
  private void update0113(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitgliedfoto ADD CONSTRAINT fkMitgliedfoto1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitgliedfoto ADD CONSTRAINT fkMitgliedfoto1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 113);
  }

  @SuppressWarnings("unused")
  private void update0114(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD mitgliedfoto char(5) before manuellezahlungen;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD mitgliedfoto char(5) after mitgliedskonto;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 114);
  }

  @SuppressWarnings("unused")
  private void update0115(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaftgruppe ADD pflicht char(5);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaftgruppe ADD pflicht char(5);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 115);
  }

  @SuppressWarnings("unused")
  private void update0116(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mail alter column  txt varchar(10000);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mail modify column  txt varchar(10000);\n");

    execute(conn, statements, 116);
  }

  @SuppressWarnings("unused")
  private void update0117(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mailvorlage alter column  txt varchar(10000);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mailvorlage modify column  txt varchar(10000);\n");

    execute(conn, statements, 117);
  }

  @SuppressWarnings("unused")
  private void update0118(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table konto alter column  bezeichnung varchar(255);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table konto modify column  bezeichnung varchar(255);\n");

    execute(conn, statements, 118);
  }

  @SuppressWarnings("unused")
  private void update0119(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD auslandsadressen char(5) before manuellezahlungen;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD auslandsadressen char(5) after mitgliedfoto;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 119);
  }

  @SuppressWarnings("unused")
  private void update0120(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD staat varchar(50) before zahlungsweg;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD staat varchar(50) after ort;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 120);
  }

  @SuppressWarnings("unused")
  private void update0121(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD sterbetag date before vermerk1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD sterbetag date after kuendigung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 121);
  }

  @SuppressWarnings("unused")
  private void update0122(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungen13;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungen13;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 122);
  }

  @SuppressWarnings("unused")
  private void update0123(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP manuellezahlungen;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP manuellezahlungen;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 123);
  }

  @SuppressWarnings("unused")
  private void update0124(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungfuerabbuchung;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungfuerabbuchung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 124);
  }

  @SuppressWarnings("unused")
  private void update0125(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungfuerueberweisung;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungfuerueberweisung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 125);
  }

  @SuppressWarnings("unused")
  private void update0126(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungfuerbarzahlung;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP rechnungfuerbarzahlung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 126);
  }

  @SuppressWarnings("unused")
  private void update0127(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("DROP TABLE abrechnung;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("DROP TABLE abrechnung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 127);
  }

  @SuppressWarnings("unused")
  private void update0128(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("DROP TABLE manuellerzahlungseingang;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("DROP TABLE manuellerzahlungseingang;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 128);
  }

  @SuppressWarnings("unused")
  private void update0129(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD arbeitseinsatz char(5) before rechnungtextabbuchung;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD arbeitseinsatz char(5) after auslandsadressen;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 129);
  }

  @SuppressWarnings("unused")
  private void update0130(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE beitragsgruppe ADD arbeitseinsatzstunden double;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE beitragsgruppe ADD arbeitseinsatzstunden double;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 130);
  }

  @SuppressWarnings("unused")
  private void update0131(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE beitragsgruppe ADD arbeitseinsatzbetrag double;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE beitragsgruppe ADD arbeitseinsatzbetrag double;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 131);
  }

  @SuppressWarnings("unused")
  private void update0132(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table arbeitseinsatz (");
    sb.append(" id IDENTITY(1),");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" datum DATE NOT NULL, ");
    sb.append(" stunden DOUBLE NOT NULL,");
    sb.append(" bemerkung VARCHAR(50), ");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(");\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("create table arbeitseinsatz (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" datum DATE NOT NULL,");
    sb.append(" stunden DOUBLE NOT NULL,");
    sb.append(" bemerkung VARCHAR(50), ");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 132);
  }

  @SuppressWarnings("unused")
  private void update0133(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table formularfeld alter column  name varchar(30);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table formularfeld modify column  name varchar(30);\n");

    execute(conn, statements, 133);
  }

  @SuppressWarnings("unused")
  private void update0134(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaftgruppe ADD max1 char(5);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaftgruppe ADD max1 char(5);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 134);
  }

  @SuppressWarnings("unused")
  private void update0135(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table buchungdokument (");
    sb.append(" id IDENTITY(1),");
    sb.append(" buchung INTEGER NOT NULL,");
    sb.append(" uuid VARCHAR(50) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(");\n");
    sb.append("ALTER TABLE buchungdokument ADD CONSTRAINT fkBuchungDokument1 FOREIGN KEY (buchung) REFERENCES buchung (id);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("create table buchungdokument (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" buchung INTEGER NOT NULL,");
    sb.append(" uuid VARCHAR(50) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE buchungdokument ADD CONSTRAINT fkBuchungDokument1 FOREIGN KEY (buchung) REFERENCES buchung (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 135);
  }

  @SuppressWarnings("unused")
  private void update0136(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("drop table buchungdokument;");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("drop table buchungdokument;");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 136);
  }

  @SuppressWarnings("unused")
  private void update0137(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table buchungdokument (");
    sb.append(" id IDENTITY(1),");
    sb.append(" referenz INTEGER NOT NULL,");
    sb.append(" datum DATE NOT NULL, ");
    sb.append("  bemerkung VARCHAR(50), ");
    sb.append(" uuid VARCHAR(50) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(");\n");
    sb.append("ALTER TABLE buchungdokument ADD CONSTRAINT fkBuchungDokument1 FOREIGN KEY (referenz) REFERENCES buchung (id);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("create table buchungdokument (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" referenz INTEGER NOT NULL,");
    sb.append("  datum DATE NOT NULL, ");
    sb.append("  bemerkung VARCHAR(50), ");
    sb.append(" uuid VARCHAR(50) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE buchungdokument ADD CONSTRAINT fkBuchungDokument1 FOREIGN KEY (referenz) REFERENCES buchung (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 137);
  }

  @SuppressWarnings("unused")
  private void update0138(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table mitglieddokument (");
    sb.append(" id IDENTITY(1),");
    sb.append(" referenz INTEGER NOT NULL,");
    sb.append(" datum DATE NOT NULL, ");
    sb.append("  bemerkung VARCHAR(50), ");
    sb.append(" uuid VARCHAR(50) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(");\n");
    sb.append("ALTER TABLE mitglieddokument ADD CONSTRAINT fkMitgliedDokument1 FOREIGN KEY (referenz) REFERENCES mitglied (id);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("create table mitglieddokument (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" referenz INTEGER NOT NULL,");
    sb.append("  datum DATE NOT NULL, ");
    sb.append("  bemerkung VARCHAR(50), ");
    sb.append(" uuid VARCHAR(50) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE mitglieddokument ADD CONSTRAINT fkMitgliedDokument1 FOREIGN KEY (referenz) REFERENCES mitglied (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 138);
  }

  @SuppressWarnings("unused")
  private void update0139(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung add splitid integer;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung add splitid integer;\n");

    execute(conn, statements, 139);
  }

  @SuppressWarnings("unused")
  private void update0140(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD dokumentenspeicherung char(5) before rechnungtextabbuchung;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD dokumentenspeicherung char(5) after arbeitseinsatz;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 140);
  }

  @SuppressWarnings("unused")
  private void update0141(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD name varchar(30) before geburtsdatumpflicht;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD name varchar(30) first;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 141);
  }

  @SuppressWarnings("unused")
  private void update0142(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD blz varchar(8) before geburtsdatumpflicht;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD blz varchar(8) after name;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 142);
  }

  @SuppressWarnings("unused")
  private void update0143(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD konto varchar(10) before geburtsdatumpflicht;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD konto varchar(10) after blz;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 143);
  }

  @SuppressWarnings("unused")
  private void update0144(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD altersgruppen varchar(50);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD altersgruppen varchar(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 144);
  }

  @SuppressWarnings("unused")
  private void update0145(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD jubilaeen varchar(50);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD jubilaeen varchar(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 145);
  }

  @SuppressWarnings("unused")
  private void update0146(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD altersjubilaeen varchar(50);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD altersjubilaeen varchar(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 146);
  }

  @SuppressWarnings("unused")
  private void update0147(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET name = (SELECT name from stammdaten);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET name = (SELECT name from stammdaten);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 147);
  }

  @SuppressWarnings("unused")
  private void update0148(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET blz = (SELECT blz from stammdaten);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET blz = (SELECT blz from stammdaten);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 148);
  }

  @SuppressWarnings("unused")
  private void update0149(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET konto = (SELECT konto from stammdaten);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET konto = (SELECT konto from stammdaten);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 149);
  }

  @SuppressWarnings("unused")
  private void update0150(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET altersgruppen = (SELECT altersgruppen from stammdaten);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET altersgruppen = (SELECT altersgruppen from stammdaten);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 150);
  }

  @SuppressWarnings("unused")
  private void update0151(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET jubilaeen = (SELECT jubilaeen from stammdaten);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET jubilaeen = (SELECT jubilaeen from stammdaten);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 151);
  }

  @SuppressWarnings("unused")
  private void update0152(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET altersjubilaeen = (SELECT altersjubilaeen from stammdaten);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("UPDATE einstellung SET altersjubilaeen = (SELECT altersjubilaeen from stammdaten);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 152);
  }

  @SuppressWarnings("unused")
  private void update0153(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE adresstyp (");
    sb.append("  id IDENTITY(1),");
    sb.append(" bezeichnung varchar(30),");
    sb.append(" jvereinid integer,");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (bezeichnung),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE adresstyp (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" bezeichnung varchar(30),");
    sb.append(" jvereinid integer,");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (bezeichnung),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 153);
  }

  @SuppressWarnings("unused")
  private void update0154(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    sb = new StringBuilder();
    sb.append("INSERT into adresstyp VALUES (1, 'Mitglied', 1);\n");
    sb.append("INSERT into adresstyp VALUES (2, 'Spender/in', 2);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 154);
  }

  @SuppressWarnings("unused")
  private void update0155(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD adresstyp integer default 1 not null before personenart;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD adresstyp integer default 1 not null after externemitgliedsnummer;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 155);
  }

  @SuppressWarnings("unused")
  private void update0156(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    sb = new StringBuilder();
    sb.append("UPDATE mitglied set adresstyp = 1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 156);
  }

  @SuppressWarnings("unused")
  private void update0157(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE INDEX ixMitglied_1 ON mitglied(adresstyp);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE INDEX ixMitglied_1 ON mitglied(adresstyp);\n");

    execute(conn, statements, 157);
  }

  @SuppressWarnings("unused")
  private void update0158(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD CONSTRAINT fkMitglied2 FOREIGN KEY (adresstyp) REFERENCES adresstyp (id);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD CONSTRAINT fkMitglied2 FOREIGN KEY (adresstyp) REFERENCES adresstyp (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 158);
  }

  @SuppressWarnings("unused")
  private void update0159(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mitglied alter column  adresstyp integer not null;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mitglied modify column  adresstyp integer not null;\n");

    execute(conn, statements, 159);
  }

  @SuppressWarnings("unused")
  private void update0160(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD delaytime integer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD delaytime integer;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 160);
  }

  @SuppressWarnings("unused")
  private void update0161(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zusatzadressen char(5);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zusatzadressen char(5);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 161);
  }

  @SuppressWarnings("unused")
  private void update0162(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD letzteaenderung date;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 162);
  }

  @SuppressWarnings("unused")
  private void update0163(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mailempfaenger DROP COLUMN adresse;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mailempfaenger DROP COLUMN adresse;\n");

    execute(conn, statements, 163);
  }

  @SuppressWarnings("unused")
  private void update0164(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("drop table stammdaten;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("drop table stammdaten;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 164);
  }

  @SuppressWarnings("unused")
  private void update0165(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "alter table buchungsart add spende char(5) ;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 165);
  }

  @SuppressWarnings("unused")
  private void update0166(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "alter table buchung add spendenbescheinigung integer ;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 166);
  }

  @SuppressWarnings("unused")
  private void update0167(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "CREATE INDEX ixBuchung1 ON buchung(spendenbescheinigung);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements, 167);
  }

  @SuppressWarnings("unused")
  private void update0168(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "ALTER TABLE buchung ADD CONSTRAINT fkBuchung5 FOREIGN KEY (spendenbescheinigung) REFERENCES spendenbescheinigung (id);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 168);
  }

  @SuppressWarnings("unused")
  private void update0169(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "alter table spendenbescheinigung add mitglied integer ;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 169);
  }

  @SuppressWarnings("unused")
  private void update0170(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "CREATE INDEX ixSpendenbescheinigung2 ON spendenbescheinigung(mitglied);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements, 170);
  }

  @SuppressWarnings("unused")
  private void update0171(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung2 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 171);
  }

  @SuppressWarnings("unused")
  private void update0172(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD strasse char(30) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD strasse char(30) after name;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 172);
  }

  @SuppressWarnings("unused")
  private void update0173(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD plz char(5) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD plz char(5) after strasse;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 173);
  }

  @SuppressWarnings("unused")
  private void update0174(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD ort char(30) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD ort char(30) after plz;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 174);
  }

  @SuppressWarnings("unused")
  private void update0175(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD finanzamt char(30) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD finanzamt char(30) after ort;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 175);
  }

  @SuppressWarnings("unused")
  private void update0176(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD steuernummer char(30) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD steuernummer char(30) after finanzamt;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 176);
  }

  @SuppressWarnings("unused")
  private void update0177(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD bescheiddatum date before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD bescheiddatum date after steuernummer;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 177);
  }

  @SuppressWarnings("unused")
  private void update0178(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD vorlaeufig char(5) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD vorlaeufig char(5) after bescheiddatum;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 178);
  }

  @SuppressWarnings("unused")
  private void update0179(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD beguenstigterzweck char(30) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD beguenstigterzweck char(30) after vorlaeufig;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 179);
  }

  @SuppressWarnings("unused")
  private void update0180(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD mitgliedsbeitraege char(5) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD mitgliedsbeitraege char(5) after beguenstigterzweck;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 180);
  }

  @SuppressWarnings("unused")
  private void update0181(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD vorlaeufigab date before beguenstigterzweck;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD vorlaeufigab date after vorlaeufig;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 181);
  }

  @SuppressWarnings("unused")
  private void update0182(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE spendenbescheinigung ADD spendenart int default 1 not null before zeile1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE spendenbescheinigung ADD spendenart int default 1 not null after id;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 182);
  }

  @SuppressWarnings("unused")
  private void update0183(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE spendenbescheinigung ADD bezeichnungsachzuwendung varchar(100);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements, 183);
  }

  @SuppressWarnings("unused")
  private void update0184(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE spendenbescheinigung ADD herkunftspende int;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements, 184);
  }

  @SuppressWarnings("unused")
  private void update0185(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE spendenbescheinigung ADD unterlagenwertermittlung char(5);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements, 185);
  }

  @SuppressWarnings("unused")
  private void update0186(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung DROP COLUMN aktuellegeburtstagevorher;\n"
                + "ALTER TABLE einstellung DROP COLUMN aktuellegeburtstagenachher;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN aktuellegeburtstagevorher, "
            + "DROP COLUMN aktuellegeburtstagenachher;\n");

    execute(conn, statements, 186);
  }

  @SuppressWarnings("unused")
  private void update0187(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), "-- nothing to do;\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE `mail`  DROP INDEX `betreff`;\n");

    execute(conn, statements, 187);
  }

  @SuppressWarnings("unused")
  private void update0188(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE beitragsgruppe ADD buchungsart integer;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements, 188);
  }

  @SuppressWarnings("unused")
  private void update0189(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "ALTER TABLE beitragsgruppe ADD CONSTRAINT fkBeitragsgruppe1 FOREIGN KEY (buchungsart) REFERENCES buchungsart(id);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 189);
  }

  @SuppressWarnings("unused")
  private void update0190(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_starttls char(5);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_starttls char(5);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 190);
  }

  @SuppressWarnings("unused")
  private void update0191(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE formularfeld ALTER COLUMN name varchar(60);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE formularfeld MODIFY COLUMN name varchar(60);\n");

    execute(conn, statements, 191);
  }

  @SuppressWarnings("unused")
  private void update0192(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "UPDATE formularfeld set name = replace(name,'.','_');\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 192);
  }

  @SuppressWarnings("unused")
  private void update0193(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE auswertung (");
    sb.append(" id IDENTITY(1),");
    sb.append(" bezeichnung VARCHAR(30) NOT NULL,");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE auswertung (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" bezeichnung VARCHAR(30) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 193);
  }

  @SuppressWarnings("unused")
  private void update0194(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE auswertungpos (");
    sb.append(" id IDENTITY(1),");
    sb.append(" auswertung integer not null, ");
    sb.append(" feld VARCHAR(50) NOT NULL, ");
    sb.append(" zeichenfolge VARCHAR(100) ,");
    sb.append(" datum DATE, ");
    sb.append(" ganzzahl integer, ");
    sb.append(" janein char(5), ");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE auswertungpos (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" auswertung integer not null, ");
    sb.append(" feld VARCHAR(50) NOT NULL, ");
    sb.append(" zeichenfolge VARCHAR(100) ,");
    sb.append(" datum DATE, ");
    sb.append(" ganzzahl integer, ");
    sb.append(" janein char(5), ");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 194);
  }

  @SuppressWarnings("unused")
  private void update0195(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE auswertungpos ADD CONSTRAINT fkAuswertungpos1 FOREIGN KEY (auswertung) REFERENCES auswertung (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE auswertungpos ADD CONSTRAINT fkAuswertungpos1 FOREIGN KEY (auswertung) REFERENCES auswertung (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 195);
  }

  @SuppressWarnings("unused")
  private void update0196(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();

    String sql = "drop table auswertungpos\n;" + "drop table auswertung;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 196);
  }

  @SuppressWarnings("unused")
  private void update0197(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD individuellebeitraege char(5) before rechnungtextabbuchung;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD individuellebeitraege char(5) after dokumentenspeicherung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 197);
  }

  @SuppressWarnings("unused")
  private void update0198(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "ALTER TABLE mitglied ADD individuellerbeitrag DOUBLE before zahlerid;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE mitglied ADD individuellerbeitrag DOUBLE after beitragsgruppe;\n");

    execute(conn, statements, 198);
  }

  @SuppressWarnings("unused")
  private void update0199(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "ALTER TABLE zusatzabbuchung ADD buchungstext2 VARCHAR(27) before betrag;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE zusatzabbuchung ADD buchungstext2 VARCHAR(27) after buchungstext;\n");

    execute(conn, statements, 199);
  }

  @SuppressWarnings("unused")
  private void update0200(Connection conn) throws ApplicationException
  {
    final String ID = "ID";

    final String EXTERNEMITGLIEDSNUMMER = "externe Mitgliedsnummer";

    final String ANREDE = "Anrede";
    final String TITEL = "Titel";

    final String NAME = "Name";

    final String VORNAME = "Vorname";

    final String ADRESSIERUNGSZUSATZ = "Adressierungszusatz";
    final String STRASSE = "Strasse";

    final String PLZ = "PLZ";

    final String ORT = "Ort";

    final String STAAT = "Staat";

    final String ZAHLUNGSRHYTMUS = "Zahlungsrhytmus";

    final String BLZ = "Bankleitzahl";

    final String KONTO = "Konto";

    final String KONTOINHABER = "Kontoinhaber";

    final String GEBURTSDATUM = "Geburtsdatum";

    final String GESCHLECHT = "Geschlecht";

    final String TELEFONPRIVAT = "Telefon privat";

    final String TELEFONDIENSTLICH = "Telefon dienstlich";

    final String HANDY = "Handy";

    final String EMAIL = "Email";

    final String EINTRITT = "Eintritt";

    final String BEITRAGSGRUPPE = "Beitragsgruppe";

    final String AUSTRITT = "Austritt";

    final String ZAHLUNGSGRUND = "Zahlungsgrund";

    final String ZAHLUNGSGRUND1 = "Zahlungsgrund 1";

    final String ZAHLUNGSGRUND2 = "Zahlungsgrund 2";

    final String BUCHUNGSDATUM = "Buchungsdatum";

    final String ZAHLUNGSWEG = "Zahlungsweg";

    final String BETRAG = "Betrag";

    final String KUENDIGUNG = "Kündigung";
    final String sql1 = "UPDATE formularfeld SET name = '";
    final String sql2 = "' WHERE name = '";
    final String sql3 = "';\n";
    @SuppressWarnings("deprecation")
    String sql = sql1
        + MitgliedVar.ID.getName()
        + sql2
        + ID
        + sql3 //
        + sql1
        + MitgliedVar.EXTERNE_MITGLIEDSNUMMER.getName()
        + sql2
        + EXTERNEMITGLIEDSNUMMER
        + sql3 //
        + sql1
        + MitgliedVar.ANREDE.getName()
        + sql2
        + ANREDE
        + sql3 //
        + sql1
        + MitgliedVar.TITEL.getName()
        + sql2
        + TITEL
        + sql3 //
        + sql1
        + MitgliedVar.NAME.getName()
        + sql2
        + NAME
        + sql3 //
        + sql1
        + MitgliedVar.VORNAME.getName()
        + sql2
        + VORNAME
        + sql3 //
        + sql1
        + MitgliedVar.ADRESSIERUNGSZUSATZ.getName()
        + sql2
        + ADRESSIERUNGSZUSATZ
        + sql3 //
        + sql1
        + MitgliedVar.STRASSE.getName()
        + sql2
        + STRASSE
        + sql3 //
        + sql1
        + MitgliedVar.PLZ.getName()
        + sql2
        + PLZ
        + sql3 //
        + sql1
        + MitgliedVar.ORT.getName()
        + sql2
        + ORT
        + sql3 //
        + sql1
        + MitgliedVar.STAAT.getName()
        + sql2
        + STAAT
        + sql3 //
        + sql1
        + MitgliedVar.ZAHLUNGSRHYTMUS.getName()
        + sql2
        + ZAHLUNGSRHYTMUS
        + sql3 //
        + sql1
        + MitgliedVar.ZAHLUNGSWEG.getName()
        + sql2
        + ZAHLUNGSWEG
        + sql3 //
        + sql1
        + MitgliedVar.BLZ.getName()
        + sql2
        + BLZ
        + sql3 //
        + sql1
        + MitgliedVar.KONTO.getName()
        + sql2
        + KONTO
        + sql3 //
        + sql1
        + MitgliedVar.KONTOINHABER.getName()
        + sql2
        + KONTOINHABER
        + sql3 //
        + sql1
        + MitgliedVar.GEBURTSDATUM.getName()
        + sql2
        + GEBURTSDATUM
        + sql3 //
        + sql1
        + MitgliedVar.GESCHLECHT.getName()
        + sql2
        + GESCHLECHT
        + sql3 //
        + sql1
        + MitgliedVar.TELEFONDIENSTLICH.getName()
        + sql2
        + TELEFONDIENSTLICH
        + sql3 //
        + sql1
        + MitgliedVar.TELEFONPRIVAT.getName()
        + sql2
        + TELEFONPRIVAT
        + sql3 //
        + sql1
        + MitgliedVar.HANDY.getName()
        + sql2
        + HANDY
        + sql3 //
        + sql1
        + MitgliedVar.EMAIL.getName()
        + sql2
        + EMAIL
        + sql3 //
        + sql1
        + MitgliedVar.EINTRITT.getName()
        + sql2
        + EINTRITT
        + sql3 //
        + sql1
        + MitgliedVar.AUSTRITT.getName()
        + sql2
        + AUSTRITT
        + sql3 //
        + sql1
        + MitgliedVar.KUENDIGUNG.getName()
        + sql2
        + KUENDIGUNG
        + sql3 //
        + sql1
        + MitgliedVar.KUENDIGUNG.getName()
        + sql2
        + KUENDIGUNG
        + sql3 //
        + sql1
        + MitgliedVar.BEITRAGSGRUPPE_BEZEICHNUNG.getName()
        + sql2
        + BEITRAGSGRUPPE
        + sql3 //
        + sql1
        + MitgliedskontoVar.ZAHLUNGSGRUND.getName()
        + sql2
        + ZAHLUNGSGRUND
        + sql3//
        + sql1 + MitgliedskontoVar.ZAHLUNGSGRUND1.getName()
        + sql2
        + ZAHLUNGSGRUND1
        + sql3//
        + sql1 + MitgliedskontoVar.ZAHLUNGSGRUND2.getName() + sql2
        + ZAHLUNGSGRUND2
        + sql3//
        + sql1 + MitgliedskontoVar.BUCHUNGSDATUM.getName() + sql2
        + BUCHUNGSDATUM + sql3//
        + sql1 + "tagesdatum" + sql2 + "Tagesdatum" + sql3//
        + sql1 + MitgliedskontoVar.BETRAG.getName() + sql2 + BETRAG + sql3//
    ;
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 200);
  }

  @SuppressWarnings("unused")
  private void update0201(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "delete from arbeitseinsatz where (select count(*) from mitglied where mitglied.id = arbeitseinsatz.mitglied) = 0";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 201);
  }

  @SuppressWarnings("unused")
  private void update0202(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE arbeitseinsatz ADD CONSTRAINT fkArbeitseinsatz1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE arbeitseinsatz ADD CONSTRAINT fkArbeitseinsatz1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");

    execute(conn, statements, 202);
  }

  @SuppressWarnings("unused")
  private void update0203(Connection conn) throws ApplicationException
  {
    ArrayList<BooleanUpdate> bu = new ArrayList<JVereinUpdateProvider.BooleanUpdate>();
    bu.add(new BooleanUpdate("einstellung", "vorlaeufig"));
    bu.add(new BooleanUpdate("einstellung", "geburtsdatumpflicht"));
    bu.add(new BooleanUpdate("einstellung", "eintrittsdatumpflicht"));
    bu.add(new BooleanUpdate("einstellung", "kommunikationsdaten"));
    bu.add(new BooleanUpdate("einstellung", "zusatzabbuchung"));
    bu.add(new BooleanUpdate("einstellung", "vermerke"));
    bu.add(new BooleanUpdate("einstellung", "wiedervorlage"));
    bu.add(new BooleanUpdate("einstellung", "kursteilnehmer"));
    bu.add(new BooleanUpdate("einstellung", "mitgliedsbeitraege"));
    bu.add(new BooleanUpdate("einstellung", "lehrgaenge"));
    bu.add(new BooleanUpdate("einstellung", "juristischepersonen"));
    bu.add(new BooleanUpdate("einstellung", "mitgliedskonto"));
    bu.add(new BooleanUpdate("einstellung", "mitgliedfoto"));
    bu.add(new BooleanUpdate("einstellung", "zusatzadressen"));
    bu.add(new BooleanUpdate("einstellung", "auslandsadressen"));
    bu.add(new BooleanUpdate("einstellung", "arbeitseinsatz"));
    bu.add(new BooleanUpdate("einstellung", "dokumentenspeicherung"));
    bu.add(new BooleanUpdate("einstellung", "individuellebeitraege"));
    bu.add(new BooleanUpdate("einstellung", "externemitgliedsnummer"));
    bu.add(new BooleanUpdate("einstellung", "smtp_ssl"));
    bu.add(new BooleanUpdate("einstellung", "smtp_starttls"));
    bu.add(new BooleanUpdate("eigenschaftgruppe", "pflicht"));
    bu.add(new BooleanUpdate("eigenschaftgruppe", "max1"));
    bu.add(new BooleanUpdate("abrechnungslauf", "zusatzbetraege"));
    bu.add(new BooleanUpdate("abrechnungslauf", "kursteilnehmer"));
    bu.add(new BooleanUpdate("abrechnungslauf", "dtausdruck"));
    bu.add(new BooleanUpdate("buchungsart", "spende"));
    bu.add(new BooleanUpdate("spendenbescheinigung", "ersatzaufwendungen"));
    bu.add(new BooleanUpdate("spendenbescheinigung", "unterlagenwertermittlung"));
    bu.add(new BooleanUpdate("zusatzfelder", "feldjanein"));

    for (BooleanUpdate b : bu)
    {
      // H2
      Map<String, String[]> statements = new HashMap<String, String[]>();
      statements.put(
          DBSupportH2Impl.class.getName(),
          new String[] { "ALTER TABLE " + b.getTabelle() + " ALTER COLUMN "
              + b.getSpalte() + " BOOLEAN;\n" });
      execute(conn, statements, 203, true);
      // MySQL
      statements = new HashMap<String, String[]>();
      statements.put(
          DBSupportMySqlImpl.class.getName(),
          new String[] {
              "ALTER TABLE `" + b.getTabelle() + "` ADD COLUMN `"
                  + b.getSpalte() + "_b` BIT(1) AFTER `" + b.getSpalte()
                  + "`;\n",
              "UPDATE `" + b.getTabelle() + "` SET `" + b.getSpalte()
                  + "_b` = false;\n",
              "UPDATE `" + b.getTabelle() + "` SET `" + b.getSpalte()
                  + "_b` = true WHERE TRIM(`" + b.getSpalte()
                  + "`) = 'TRUE';\n",
              "ALTER TABLE `" + b.getTabelle() + "` DROP COLUMN `"
                  + b.getSpalte() + "`;\n",
              "ALTER TABLE `" + b.getTabelle() + "` CHANGE COLUMN `"
                  + b.getSpalte() + "_b` `" + b.getSpalte() + "` BIT(1);\n" });
      execute(conn, statements, 203, true);
    }
  }

  public class BooleanUpdate
  {

    private String tabelle;

    private String spalte;

    public BooleanUpdate(String tabelle, String spalte)
    {
      this.tabelle = tabelle;
      this.spalte = spalte;
    }

    public String getTabelle()
    {
      return tabelle;
    }

    public String getSpalte()
    {
      return spalte;
    }
  }

  @SuppressWarnings("unused")
  private void update0204(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "delete from eigenschaften where (select count(*) from eigenschaft where eigenschaft.id = eigenschaften.eigenschaft) = 0;";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 204);
  }

  @SuppressWarnings("unused")
  private void update0205(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften2 FOREIGN KEY (eigenschaft) REFERENCES eigenschaft (id) ON DELETE CASCADE;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften2 FOREIGN KEY (eigenschaft) REFERENCES eigenschaft (id) ON DELETE CASCADE;\n");

    execute(conn, statements, 205);
  }

  @SuppressWarnings("unused")
  private void update0206(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD sterbedatum boolean before kommunikationsdaten;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD sterbedatum bit(1) after eintrittsdatumpflicht;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 206);
  }

  @SuppressWarnings("unused")
  private void update0207(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE abrechnungslauf SET modus = 1 where modus >=2 and modus <=4;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 207);
  }

  @SuppressWarnings("unused")
  private void update0208(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE einstellung SET beitragsmodel = 1 where beitragsmodel >=2 and beitragsmodel <=4;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 208);
  }

  @SuppressWarnings("unused")
  private void update0209(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD dtaustextschluessel char(2) before altersgruppen;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD dtaustextschluessel char(2) after zahlungsrhytmus;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 209);
  }

  @SuppressWarnings("unused")
  private void update0210(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "UPDATE einstellung SET dtaustextschluessel = '05' WHERE dtaustextschluessel IS NULL";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 210);
  }

  @SuppressWarnings("unused")
  private void update0211(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();

    String sql = "ALTER TABLE buchung ADD verzicht BOOLEAN";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 211);
  }

  @SuppressWarnings("unused")
  private void update0212(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();

    String sql = "ALTER TABLE spendenbescheinigung ADD autocreate BOOLEAN default false";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 212);
  }

  @SuppressWarnings("unused")
  private void update0213(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung ADD namelang VARCHAR(100) before strasse;\n");
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung ADD namelang VARCHAR(100) after name;\n");
    execute(conn, statements, 213);
  }

  @SuppressWarnings("unused")
  private void update0214(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung ADD dateinamenmusterspende VARCHAR(50) before beginngeschaeftsjahr;\n");
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE einstellung ADD dateinamenmusterspende VARCHAR(50) after dateinamenmuster;\n");
    execute(conn, statements, 214);
  }

  @SuppressWarnings("unused")
  private void update0215(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung ADD spendenbescheinigungminbetrag DOUBLE before beginngeschaeftsjahr;\n");
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE einstellung ADD spendenbescheinigungminbetrag DOUBLE after dateinamenmusterspende;\n");
    execute(conn, statements, 215);
  }

  @SuppressWarnings("unused")
  private void update0216(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung ADD spendenbescheinigungverzeichnis VARCHAR(200) before beginngeschaeftsjahr;\n");
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE einstellung ADD spendenbescheinigungverzeichnis VARCHAR(200) after spendenbescheinigungminbetrag;\n");
    execute(conn, statements, 216);
  }

  @SuppressWarnings("unused")
  private void update0217(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung ADD spendenbescheinigungprintbuchungsart BOOLEAN DEFAULT FALSE before beginngeschaeftsjahr;\n");
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE einstellung ADD spendenbescheinigungprintbuchungsart BOOLEAN DEFAULT FALSE after spendenbescheinigungverzeichnis;\n");
    execute(conn, statements, 217);
  }

  @SuppressWarnings("unused")
  private void update0218(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung ALTER COLUMN strasse VARCHAR(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung MODIFY COLUMN strasse VARCHAR(50);\n");
    execute(conn, statements, 218);
  }

  @SuppressWarnings("unused")
  private void update0219(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung ALTER COLUMN ort VARCHAR(50);\n");
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung MODIFY COLUMN ort VARCHAR(50);\n");
    execute(conn, statements, 219);
  }

  @SuppressWarnings("unused")
  private void update0220(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung ALTER COLUMN beguenstigterzweck VARCHAR(100);\n");
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE einstellung MODIFY COLUMN beguenstigterzweck VARCHAR(100);\n");
    execute(conn, statements, 220);
  }

  @SuppressWarnings("unused")
  private void update0221(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE formularfeld ADD seite INTEGER DEFAULT 1 before x;\n");
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE formularfeld ADD Seite INTEGER DEFAULT 1 after name;\n");
    execute(conn, statements, 221);
  }

  @SuppressWarnings("unused")
  private void update0222(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(),
        "UPDATE einstellung SET namelang = name;\n");
    statements.put(DBSupportMySqlImpl.class.getName(),
        "UPDATE einstellung SET namelang = name;\n");
    execute(conn, statements, 222);
  }

  @SuppressWarnings("unused")
  private void update0223(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(),
        "UPDATE einstellung SET spendenbescheinigungminbetrag = 0;\n");
    statements.put(DBSupportMySqlImpl.class.getName(),
        "UPDATE einstellung SET spendenbescheinigungminbetrag = 0;\n");
    execute(conn, statements, 223);
  }

  @SuppressWarnings("unused")
  private void update0224(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(),
        "UPDATE formularfeld SET seite = 1;\n");
    statements.put(DBSupportMySqlImpl.class.getName(),
        "UPDATE formularfeld SET seite = 1;\n");
    execute(conn, statements, 224);
  }

  @SuppressWarnings("unused")
  private void update0225(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE lesefeld (");
    sb.append("  id IDENTITY(1),");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" script VARCHAR(1000),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE lesefeld (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" script VARCHAR(1000),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 225);
  }

  @SuppressWarnings("unused")
  private void update0226(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("-- nothing do to");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE formularfeld ");
    sb.append("  CHANGE COLUMN `Seite` `seite` INT(10) NULL;");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 226);
  }

  @SuppressWarnings("unused")
  private void update0227(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table projekt (");
    sb.append(" id IDENTITY(1),");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(");\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("create table projekt (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" bezeichnung VARCHAR(50),");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 227);
  }

  @SuppressWarnings("unused")
  private void update0228(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung add projekt integer;\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung add projekt integer;\n");

    execute(conn, statements, 228);
  }

  @SuppressWarnings("unused")
  private void update0229(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung6 FOREIGN KEY (projekt) REFERENCES projekt (id) ;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung6 FOREIGN KEY (projekt) REFERENCES projekt (id) ;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 229);
  }

  @SuppressWarnings("unused")
  private void update0230(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE formularfeld ALTER COLUMN font varchar(50);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE formularfeld MODIFY COLUMN font varchar(50);\n");

    execute(conn, statements, 230);
  }

  @SuppressWarnings("unused")
  private void update0231(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE zusatzbetragabrechnungslauf(");
    sb.append(" id IDENTITY(1),");
    sb.append(" abrechnungslauf integer not null,");
    sb.append(" zusatzbetrag integer not null,");
    sb.append(" letzteausfuehrung date, ");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE zusatzbetragabrechnungslauf ADD CONSTRAINT fkZusatzbetragabrechnungslauf1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    sb.append("ALTER TABLE zusatzbetragabrechnungslauf ADD CONSTRAINT fkZusatzbetragabrechnungslauf2 FOREIGN KEY (zusatzbetrag) REFERENCES zusatzabbuchung (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE zusatzbetragabrechnungslauf(");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" abrechnungslauf integer not null,");
    sb.append(" zusatzbetrag integer not null,");
    sb.append(" letzteausfuehrung date, ");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE zusatzbetragabrechnungslauf ADD CONSTRAINT fkZusatzbetragabrechnungslauf1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    sb.append("ALTER TABLE zusatzbetragabrechnungslauf ADD CONSTRAINT fkZusatzbetragabrechnungslauf2 FOREIGN KEY (zusatzbetrag) REFERENCES zusatzabbuchung (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 231);
  }

  @SuppressWarnings("unused")
  private void update0232(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD uselesefelder char(5) before auslandsadressen;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD uselesefelder char(5) after mitgliedfoto;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 232);
  }

  @SuppressWarnings("unused")
  private void update0233(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD versand TIMESTAMP;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD versand TIMESTAMP;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 233);
  }

  @SuppressWarnings("unused")
  private void update0234(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE buchung ALTER COLUMN zweck varchar(500);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE buchung MODIFY COLUMN zweck varchar(500);\n");

    execute(conn, statements, 234);
  }

  @SuppressWarnings("unused")
  private void update0235(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "update buchung set zweck = concat(zweck, char(13), char(10), zweck2) where zweck2 is not null and length(zweck2) >0;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 235);
  }

  @SuppressWarnings("unused")
  private void update0236(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE buchung DROP COLUMN zweck2;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE buchung DROP COLUMN zweck2;\n");

    execute(conn, statements, 236);
  }

  @SuppressWarnings("unused")
  private void update0237(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitgliedskonto ALTER COLUMN zweck1 varchar(500);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitgliedskonto MODIFY COLUMN zweck1 varchar(500);\n");

    execute(conn, statements, 237);
  }

  @SuppressWarnings("unused")
  private void update0238(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "update mitgliedskonto set zweck1 = concat(zweck1, char(13), char(10), zweck2) where zweck2 is not null and length(zweck2) >0;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 238);
  }

  @SuppressWarnings("unused")
  private void update0239(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitgliedskonto DROP COLUMN zweck2;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitgliedskonto DROP COLUMN zweck2;\n");

    execute(conn, statements, 239);
  }

  @SuppressWarnings("unused")
  private void update0240(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "update formularfeld set name = replace(name, '.','_');\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "update formularfeld set name = replace(name, '.','_');\n");

    execute(conn, statements, 240);
  }

  @SuppressWarnings("unused")
  private void update0241(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD defaultland char(2) before altersgruppen;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD defaultland char(2) after dtaustextschluessel;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 241);
  }

  @SuppressWarnings("unused")
  private void update0242(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger DROP CONSTRAINT FKMAILEMPFAENGER2;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger DROP FOREIGN KEY fkMailEmpfaenger2;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 242);
  }

  @SuppressWarnings("unused")
  private void update0243(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailempfaenger2 FOREIGN KEY (mitglied) REFERENCES mitglied (id)  ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailempfaenger2 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 243);
  }

  @SuppressWarnings("unused")
  private void update0244(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("DROP INDEX IXEIGENSCHAFT1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaft DROP INDEX ixEigenschaft1;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 244);
  }

  @SuppressWarnings("unused")
  private void update0245(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung, eigenschaftgruppe);\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung, eigenschaftgruppe);\n");

    execute(conn, statements, 245);
  }

  @SuppressWarnings("unused")
  private void update0246(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD bic varchar(11) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD bic varchar(11) after mitgliedsbeitraege;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 246);
  }

  @SuppressWarnings("unused")
  private void update0247(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD iban varchar(22) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD iban varchar(22) after bic;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 247);
  }

  @SuppressWarnings("unused")
  private void update0248(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD glaeubigerid varchar(35) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD glaeubigerid varchar(35) after iban;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 248);
  }

  @SuppressWarnings("unused")
  private void update0249(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_from_anzeigename varchar(50) before smtp_ssl;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD smtp_from_anzeigename varchar(50) after smtp_from_address;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 249);
  }

  @SuppressWarnings("unused")
  private void update0250(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD bic varchar(11) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD bic varchar(11) after zahlungsrhytmus;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 250);
  }

  @SuppressWarnings("unused")
  private void update0251(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD iban varchar(22) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD iban varchar(22) after bic;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 251);
  }

  @SuppressWarnings("unused")
  private void update0252(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE bank(");
    sb.append(" id IDENTITY(1),");
    sb.append(" bezeichnung varchar(27) not null,");
    sb.append(" blz varchar(8) not null,");
    sb.append(" bic varchar(11) not null, ");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (blz),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE bank(");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" bezeichnung varchar(27) not null,");
    sb.append(" blz varchar(8) not null,");
    sb.append(" bic varchar(11) not null, ");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (blz),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 252);
  }

  @SuppressWarnings("unused")
  private void update0253(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE INDEX IXBANK1 ON bank (bic)");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    // statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 253);
  }

  @SuppressWarnings("unused")
  private void update0254(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();

    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 254);
  }

  @SuppressWarnings("unused")
  private void update0255(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE sepaparam(");
    sb.append(" id CHAR(3),");
    sb.append(" bezeichnung varchar(20) not null,");
    sb.append(" bankidentifierlength integer not null,");
    sb.append(" accountlength integer not null,");
    sb.append(" bankidentifiersample varchar(20) not null,");
    sb.append(" accountsample varchar(20) not null,");
    sb.append(" ibansample varchar(33) not null,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE sepaparam(");
    sb.append(" id CHAR(3), ");
    sb.append(" bezeichnung varchar(20) not null,");
    sb.append(" bankidentifierlength integer not null,");
    sb.append(" accountlength integer not null,");
    sb.append(" bankidentifiersample varchar(20) not null,");
    sb.append(" accountsample varchar(20) not null,");
    sb.append(" ibansample varchar(33) not null,");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 255);
  }

  @SuppressWarnings("unused")
  private void update0256(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 + MySQL
    sb = new StringBuilder();
    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 256);
  }

  @SuppressWarnings("unused")
  private void update0257(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 + MySQL
    sb = new StringBuilder();
    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 257);
  }

  @SuppressWarnings("unused")
  private void update0258(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 + MySQL
    sb = new StringBuilder();
    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 258);
  }

  @SuppressWarnings("unused")
  private void update0259(Connection conn) throws ApplicationException
  {
    final String sqlUpdateH2 = "ALTER TABLE einstellung ADD jubilarstartalter INTEGER before jubilaeen;\n";
    final String sqlUpdateMySql = "ALTER TABLE einstellung ADD jubilarstartalter INTEGER after jubilaeen;\n";

    Map<String, String> statements = new HashMap<String, String>();
    statements.put(DBSupportH2Impl.class.getName(), sqlUpdateH2);
    statements.put(DBSupportMySqlImpl.class.getName(), sqlUpdateMySql);
    execute(conn, statements, 259);
  }

  @SuppressWarnings("unused")
  private void update0260(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD bic varchar(11) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD bic varchar(11) after name;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 260);
  }

  @SuppressWarnings("unused")
  private void update0261(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD iban varchar(22) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD iban varchar(22) after bic;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 261);
  }

  @SuppressWarnings("unused")
  private void update0262(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table einstellung alter column konto varchar(12);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table einstellung modify column  konto varchar(12);\n");

    execute(conn, statements, 262);
  }

  @SuppressWarnings("unused")
  private void update0263(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mitglied alter column konto varchar(12);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mitglied modify column  konto varchar(12);\n");

    execute(conn, statements, 263);
  }

  @SuppressWarnings("unused")
  private void update0264(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table kursteilnehmer alter column konto varchar(12);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table kursteilnehmer modify column  konto varchar(12);\n");

    execute(conn, statements, 264);
  }

  @SuppressWarnings("unused")
  private void update0265(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table anfangsbestand alter column konto varchar(12);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table kursteilnehmer modify column  konto varchar(12);\n");

    execute(conn, statements, 265);
  }

  @SuppressWarnings("unused")
  private void update0266(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE bank ADD land varchar(2) before blz;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE bank ADD land varchar(2) after bezeichnung;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 266);
  }

  @SuppressWarnings("unused")
  private void update0267(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 + MySQL
    sb = new StringBuilder();
    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 267);
  }

  @SuppressWarnings("unused")
  private void update0268(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 + MySQL
    sb = new StringBuilder();
    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 268);
  }

  @SuppressWarnings("unused")
  private void update0269(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "ALTER TABLE adresstyp ADD bezeichnungplural varchar(30) before jvereinid;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE adresstyp ADD bezeichnungplural varchar(30) after bezeichnung;\n");

    execute(conn, statements, 269);
  }

  @SuppressWarnings("unused")
  private void update0270(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE adresstyp SET bezeichnungplural = bezeichnung;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 270);
  }

  @SuppressWarnings("unused")
  private void update0271(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE adresstyp SET bezeichnungplural = 'Mitglieder' WHERE jvereinid=1;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 271);
  }

  @SuppressWarnings("unused")
  private void update0272(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE adresstyp SET bezeichnungplural = 'Spender/innen' WHERE jvereinid=2;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 272);
  }

  @SuppressWarnings("unused")
  private void update0273(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 + MySQL
    sb = new StringBuilder();
    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 274);
  }

  @SuppressWarnings("unused")
  private void update0274(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 + MySQL
    sb = new StringBuilder();
    sb.append("-- Nothing to do");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 274);
  }

  @SuppressWarnings("unused")
  private void update0275(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mitglied alter column konto varchar(16);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mitglied modify column  konto varchar(16);\n");

    execute(conn, statements, 275);
  }

  @SuppressWarnings("unused")
  private void update0276(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table einstellung alter column iban varchar(34);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table einstellung modify column  iban varchar(34);\n");

    execute(conn, statements, 276);
  }

  @SuppressWarnings("unused")
  private void update0277(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mitglied alter column iban varchar(34);\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mitglied modify column  iban varchar(34);\n");
    execute(conn, statements, 277);
  }

  @SuppressWarnings("unused")
  private void update0278(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table kursteilnehmer alter column iban varchar(34);\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table kursteilnehmer modify column  iban varchar(34);\n");
    execute(conn, statements, 278);
  }

  @SuppressWarnings("unused")
  private void update0279(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("alter table spendenbescheinigung alter column zeile1 varchar(80);\n");
    sb.append("alter table spendenbescheinigung alter column zeile2 varchar(80);\n");
    sb.append("alter table spendenbescheinigung alter column zeile3 varchar(80);\n");
    sb.append("alter table spendenbescheinigung alter column zeile4 varchar(80);\n");
    sb.append("alter table spendenbescheinigung alter column zeile5 varchar(80);\n");
    sb.append("alter table spendenbescheinigung alter column zeile6 varchar(80);\n");
    sb.append("alter table spendenbescheinigung alter column zeile7 varchar(80);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("alter table spendenbescheinigung modify column zeile1 varchar(80);\n");
    sb.append("alter table spendenbescheinigung modify column zeile2 varchar(80);\n");
    sb.append("alter table spendenbescheinigung modify column zeile3 varchar(80);\n");
    sb.append("alter table spendenbescheinigung modify column zeile4 varchar(80);\n");
    sb.append("alter table spendenbescheinigung modify column zeile5 varchar(80);\n");
    sb.append("alter table spendenbescheinigung modify column zeile6 varchar(80);\n");
    sb.append("alter table spendenbescheinigung modify column zeile7 varchar(80);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, 279);
  }

  @SuppressWarnings("unused")
  private void update0280(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mitglied add mandatdatum date before bic;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mitglied add mandatdatum date after zahlungsrhytmus;\n");

    execute(conn, statements, 280);
  }

  @SuppressWarnings("unused")
  private void update0281(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "UPDATE mitglied set mandatdatum = eintritt where zahlungsweg = "
        + Zahlungsweg.BASISLASTSCHRIFT + ";\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 281);
  }

  @SuppressWarnings("unused")
  private void update0282(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table kursteilnehmer add mandatdatum date before bic;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table kursteilnehmer add mandatdatum date after vzweck2;\n");

    execute(conn, statements, 282);
  }

  @SuppressWarnings("unused")
  private void update0283(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN mitgliedskonto;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN mitgliedskonto;\n");

    execute(conn, statements, 283);
  }

  @SuppressWarnings("unused")
  private void update0284(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN dtaustextschluessel;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN dtaustextschluessel;\n");

    execute(conn, statements, 284);
  }

  @SuppressWarnings("unused")
  private void update0285(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD strasse varchar(40) before vzweck1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD strasse varchar(40) after name;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 285);
  }

  @SuppressWarnings("unused")
  private void update0286(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD plz varchar(10) before vzweck1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD plz varchar(10) after strasse;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 286);
  }

  @SuppressWarnings("unused")
  private void update0287(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD ort varchar(40) before vzweck1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer ADD ort varchar(40) after plz;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 287);
  }

  @SuppressWarnings("unused")
  private void update0288(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table kursteilnehmer alter column vzweck1 varchar(140);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table kursteilnehmer modify column  vzweck1 varchar(140);\n");

    execute(conn, statements, 288);
  }

  @SuppressWarnings("unused")
  private void update0289(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "update kursteilnehmer set vzweck1 = trim(concat(vzweck1, vzweck2));\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 289);
  }

  @SuppressWarnings("unused")
  private void update0290(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer DROP vzweck2;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE kursteilnehmer DROP vzweck2;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 290);
  }

  @SuppressWarnings("unused")
  private void update0291(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table kursteilnehmer alter column name varchar(40);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table kursteilnehmer modify column  name varchar(40);\n");

    execute(conn, statements, 291);
  }

  @SuppressWarnings("unused")
  private void update0292(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE abrechnungslauf ADD faelligkeit date before stichtag;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE abrechnungslauf ADD faelligkeit date after modus;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 292);
  }

  @SuppressWarnings("unused")
  private void update0293(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE lastschrift(");
    sb.append(" id IDENTITY(1),");
    sb.append(" abrechnungslauf INTEGER NOT NULL,");
    sb.append(" mitglied INTEGER,");
    sb.append(" kursteilnehmer INTEGER,");
    sb.append(" personenart CHAR(1),");
    sb.append(" anrede VARCHAR(10),");
    sb.append(" titel VARCHAR(10),");
    sb.append(" name VARCHAR(40) NOT NULL,");
    sb.append(" vorname VARCHAR(40),");
    sb.append(" strasse VARCHAR(40),");
    sb.append(" adressierungszusatz VARCHAR(40),");
    sb.append(" plz VARCHAR(10),");
    sb.append(" ort VARCHAR(40),");
    sb.append(" staat VARCHAR(50),");
    sb.append(" mandatid VARCHAR(35) NOT NULL,");
    sb.append(" mandatdatum DATE NOT NULL,");
    sb.append(" bic VARCHAR(11) NOT NULL,");
    sb.append(" lsname VARCHAR(70) NOT NULL,");
    sb.append(" iban VARCHAR(35) NOT NULL,");
    sb.append(" verwendungszweck VARCHAR(140) NOT NULL,");
    sb.append(" betrag double NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE lastschrift(");
    sb.append(" id INTEGER AUTO_INCREMENT, ");
    sb.append(" abrechnungslauf INTEGER NOT NULL,");
    sb.append(" mitglied INTEGER,");
    sb.append(" kursteilnehmer INTEGER,");
    sb.append(" personenart CHAR(1),");
    sb.append(" anrede VARCHAR(10),");
    sb.append(" titel VARCHAR(10),");
    sb.append(" name VARCHAR(40) NOT NULL,");
    sb.append(" vorname VARCHAR(40),");
    sb.append(" strasse VARCHAR(40),");
    sb.append(" adressierungszusatz VARCHAR(40),");
    sb.append(" plz VARCHAR(10),");
    sb.append(" ort VARCHAR(40),");
    sb.append(" staat VARCHAR(50),");
    sb.append(" mandatid VARCHAR(35) NOT NULL,");
    sb.append(" mandatdatum DATE NOT NULL,");
    sb.append(" bic VARCHAR(11) NOT NULL,");
    sb.append(" lsname VARCHAR(70) NOT NULL,");
    sb.append(" iban VARCHAR(35) NOT NULL,");
    sb.append(" verwendungszweck VARCHAR(140) NOT NULL,");
    sb.append(" betrag double NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" KEY (abrechnungslauf),");
    sb.append(" KEY (mitglied),");
    sb.append(" KEY(kursteilnehmer),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 293);
  }

  @SuppressWarnings("unused")
  private void update0294(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id)  ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 294);
  }

  @SuppressWarnings("unused")
  private void update0295(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift2 FOREIGN KEY (mitglied) REFERENCES mitglied (id)  ON DELETE RESTRICT;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift2 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE RESTRICT;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 295);
  }

  @SuppressWarnings("unused")
  private void update0296(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift3 FOREIGN KEY (kursteilnehmer) REFERENCES kursteilnehmer (id)  ON DELETE RESTRICT;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift3 FOREIGN KEY (kursteilnehmer) REFERENCES kursteilnehmer (id) ON DELETE RESTRICT;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 296);
  }

  @SuppressWarnings("unused")
  private void update0297(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoipersonenart char(1) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoipersonenart char(1) after kontoinhaber;\n");

    execute(conn, statements, 297);
  }

  @SuppressWarnings("unused")
  private void update0298(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoianrede varchar(10) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoianrede varchar(10) after ktoipersonenart;\n");

    execute(conn, statements, 298);
  }

  @SuppressWarnings("unused")
  private void update0299(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoititel varchar(10) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoititel varchar(10) after ktoianrede;\n");

    execute(conn, statements, 299);
  }

  @SuppressWarnings("unused")
  private void update0300(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoiname varchar(40) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoiname varchar(40) after ktoititel;\n");

    execute(conn, statements, 300);
  }

  @SuppressWarnings("unused")
  private void update0301(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoivorname varchar(40) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoivorname varchar(40) after ktoiname;\n");

    execute(conn, statements, 301);
  }

  @SuppressWarnings("unused")
  private void update0302(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoistrasse varchar(40) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoistrasse varchar(40) after ktoivorname;\n");

    execute(conn, statements, 302);
  }

  @SuppressWarnings("unused")
  private void update0303(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoiadressierungszusatz varchar(40) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoiadressierungszusatz varchar(40) after ktoistrasse;\n");

    execute(conn, statements, 303);
  }

  @SuppressWarnings("unused")
  private void update0304(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoiplz varchar(10) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoiplz varchar(10) after ktoiadressierungszusatz;\n");

    execute(conn, statements, 304);
  }

  @SuppressWarnings("unused")
  private void update0305(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoiort varchar(40) before geburtsdatum;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mitglied add column ktoiort varchar(40) after ktoiplz;\n");

    execute(conn, statements, 305);
  }

  @SuppressWarnings("unused")
  private void update0306(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoistaat varchar(50) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoistaat varchar(50) after ktoiort;\n");

    execute(conn, statements, 306);
  }

  @SuppressWarnings("unused")
  private void update0307(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE mitglied set ktoiname = kontoinhaber;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 307);
  }

  @SuppressWarnings("unused")
  private void update0308(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied DROP kontoinhaber;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied DROP kontoinhaber;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 308);
  }

  // Hinweis Ruediger Wurth: neue einstellung "vorlagencsvverzeichnis"
  @SuppressWarnings("unused")
  private void update0309(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung ADD vorlagencsvverzeichnis VARCHAR(200) BEFORE SPENDENBESCHEINIGUNGMINBETRAG;\n");
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE einstellung ADD vorlagencsvverzeichnis VARCHAR(200) AFTER DATEINAMENMUSTERSPENDE;\n");
    execute(conn, statements, 309);
  }

  @SuppressWarnings("unused")
  private void update0310(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table kursteilnehmer add column personenart char(1) before name;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table kursteilnehmer add column personenart char(1) after id;\n");

    execute(conn, statements, 310);
  }

  @SuppressWarnings("unused")
  private void update0311(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table kursteilnehmer add column vorname varchar(40) before strasse;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table kursteilnehmer add column vorname varchar(40) after name;\n");

    execute(conn, statements, 311);
  }

  @SuppressWarnings("unused")
  private void update0312(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "alter table kursteilnehmer add column adressierungszusatz varchar(40) before plz;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "alter table kursteilnehmer add column adressierungszusatz varchar(40) after strasse;\n");

    execute(conn, statements, 312);
  }

  @SuppressWarnings("unused")
  private void update0313(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table kursteilnehmer add column staat varchar(50) before vzweck1;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table kursteilnehmer add column staat varchar(50) after ort;\n");

    execute(conn, statements, 313);
  }

  @SuppressWarnings("unused")
  private void update0314(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table kursteilnehmer add column anrede varchar(10) before name;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table kursteilnehmer add column anrede varchar(10) after personenart;\n");

    execute(conn, statements, 314);
  }

  @SuppressWarnings("unused")
  private void update0315(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table kursteilnehmer add column titel varchar(10) before name;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table kursteilnehmer add column titel varchar(10) after anrede;\n");

    execute(conn, statements, 315);
  }

  @SuppressWarnings("unused")
  private void update0316(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table kursteilnehmer add column email varchar(50) before vzweck1;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table kursteilnehmer add column email varchar(50) after staat;\n");

    execute(conn, statements, 316);
  }

  @SuppressWarnings("unused")
  private void update0317(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table mitglied add column ktoiemail varchar(50) before geburtsdatum;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table mitglied add column ktoiemail varchar(50) after ktoistaat;\n");

    execute(conn, statements, 317);
  }

  @SuppressWarnings("unused")
  private void update0318(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table lastschrift add column email varchar(50) before mandatid;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table lastschrift add column email varchar(50) after staat;\n");

    execute(conn, statements, 318);
  }

  @SuppressWarnings("unused")
  private void update0319(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift DROP lsname;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE lastschrift DROP lsname;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 319);
  }

  @SuppressWarnings("unused")
  private void update0320(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "DROP TABLE bank";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 320);
  }

  @SuppressWarnings("unused")
  private void update0321(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "DROP TABLE sepaparam";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 321);
  }

  @SuppressWarnings("unused")
  private void update0322(Connection conn) throws ApplicationException
  {
    if (driver.equals(H2))
    {
      return;
    }
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE anfangsbestand DROP FOREIGN KEY fkAnfangsbestand1;\n"
                + "ALTER TABLE arbeitseinsatz DROP FOREIGN KEY fkArbeitseinsatz1;\n"
                + "ALTER TABLE beitragsgruppe DROP FOREIGN KEY fkBeitragsgruppe1;\n"
                + "ALTER TABLE buchung DROP FOREIGN KEY fkBuchung1;\n"
                + "ALTER TABLE buchung DROP FOREIGN KEY fkBuchung2;\n"
                + "ALTER TABLE buchung DROP FOREIGN KEY fkBuchung3;\n"
                + "ALTER TABLE buchung DROP FOREIGN KEY fkBuchung4;\n"
                + "ALTER TABLE buchung DROP FOREIGN KEY fkBuchung5;\n"
                + "ALTER TABLE buchung DROP FOREIGN KEY fkBuchung6;\n"
                + "ALTER TABLE buchungdokument DROP FOREIGN KEY fkBuchungDokument1;\n"
                // +
                // "ALTER TABLE buchungsart DROP FOREIGN KEY fkBuchungsart1;\n"
                + "ALTER TABLE buchungsart DROP FOREIGN KEY fkBuchungsart2;\n"
                + "ALTER TABLE eigenschaft DROP FOREIGN KEY fkEigenschaft1;\n"
                + "ALTER TABLE eigenschaften DROP FOREIGN KEY fkEigenschaften2;\n"
                + "ALTER TABLE eigenschaften DROP FOREIGN KEY fkEigenschaften1;\n"
                + "ALTER TABLE formularfeld DROP FOREIGN KEY fkFormularfeld1;\n"
                + "ALTER TABLE lastschrift DROP FOREIGN KEY fkLastschrift1;\n"
                + "ALTER TABLE lastschrift DROP FOREIGN KEY fkLastschrift2;\n"
                + "ALTER TABLE lastschrift DROP FOREIGN KEY fkLastschrift3;\n"
                + "ALTER TABLE lehrgang DROP FOREIGN KEY fkLehrgang2;\n"
                + "ALTER TABLE lehrgang DROP FOREIGN KEY fkLehrgang1;\n"
                + "ALTER TABLE mailanhang DROP FOREIGN KEY fkMailAnhang1;\n"
                + "ALTER TABLE mailempfaenger DROP FOREIGN KEY fkMailempfaenger2;\n"
                + "ALTER TABLE mailempfaenger DROP FOREIGN KEY fkMailEmpfaenger1;\n"
                + "ALTER TABLE mitglied DROP FOREIGN KEY fkMitglied1;\n"
                + "ALTER TABLE mitglied DROP FOREIGN KEY fkMitglied2;\n"
                + "ALTER TABLE mitglieddokument DROP FOREIGN KEY fkMitgliedDokument1;\n"
                + "ALTER TABLE mitgliedfoto DROP FOREIGN KEY fkMitgliedfoto1;\n"
                + "ALTER TABLE mitgliedskonto DROP FOREIGN KEY fkMitgliedskonto1;\n"
                + "ALTER TABLE mitgliedskonto DROP FOREIGN KEY fkMitgliedskonto2;\n"
                + "ALTER TABLE spendenbescheinigung DROP FOREIGN KEY fkSpendenbescheinigung1;\n"
                + "ALTER TABLE spendenbescheinigung DROP FOREIGN KEY fkSpendenbescheinigung2;\n"
                + "ALTER TABLE wiedervorlage DROP FOREIGN KEY fkWiedervorlage1;\n"
                + "ALTER TABLE zusatzabbuchung DROP FOREIGN KEY fkZusatzabbuchung1;\n"
                + "ALTER TABLE zusatzbetragabrechnungslauf DROP FOREIGN KEY fkZusatzbetragabrechnungslauf2;\n"
                + "ALTER TABLE zusatzbetragabrechnungslauf DROP FOREIGN KEY fkZusatzbetragabrechnungslauf1;\n"
                + "ALTER TABLE zusatzfelder DROP FOREIGN KEY fkzusatzfelder1;\n"
                + "ALTER TABLE zusatzfelder DROP FOREIGN KEY fkZusatzfelder2;\n");

    execute(conn, statements, 322);
  }

  @SuppressWarnings("unused")
  private void update0323(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("anfangsbestand", "konto", "BIGINT");
    sql += alterColumn("arbeitseinsatz", "mitglied", "BIGINT");
    sql += alterColumn("beitragsgruppe", "buchungsart", "BIGINT");
    sql += alterColumn("buchung", "abrechnungslauf", "BIGINT");
    sql += alterColumn("buchung", "buchungsart", "BIGINT");
    sql += alterColumn("buchung", "konto", "BIGINT");
    sql += alterColumn("buchung", "mitgliedskonto", "BIGINT");
    sql += alterColumn("buchung", "projekt", "BIGINT");
    sql += alterColumn("buchung", "spendenbescheinigung", "BIGINT");
    sql += alterColumn("buchungdokument", "referenz", "BIGINT");
    sql += alterColumn("buchungsart", "buchungsklasse", "BIGINT");
    sql += alterColumn("eigenschaft", "eigenschaftgruppe", "BIGINT");
    sql += alterColumn("eigenschaften", "eigenschaft", "BIGINT");
    sql += alterColumn("eigenschaften", "mitglied", "BIGINT");
    sql += alterColumn("einstellung", "finanzamt", "VARCHAR(30)");
    sql += alterColumn("einstellung", "steuernummer", "VARCHAR(30)");
    sql += alterColumn("formularfeld", "formular", "BIGINT");
    sql += alterColumn("lastschrift", "abrechnungslauf", "BIGINT");
    sql += alterColumn("lastschrift", "kursteilnehmer", "BIGINT");
    sql += alterColumn("lastschrift", "mitglied", "BIGINT");
    sql += alterColumn("lehrgang", "lehrgangsart", "BIGINT");
    sql += alterColumn("lehrgang", "mitglied", "BIGINT");
    sql += alterColumn("mailanhang", "mail", "BIGINT");
    sql += alterColumn("mailempfaenger", "mail", "BIGINT");
    sql += alterColumn("mailempfaenger", "mitglied", "BIGINT");
    sql += alterColumn("mitglied", "adresstyp", "BIGINT");
    sql += alterColumn("mitglied", "beitragsgruppe", "BIGINT");
    sql += alterColumn("mitglieddokument", "referenz", "BIGINT");
    sql += alterColumn("mitgliedfoto", "mitglied", "BIGINT");
    sql += alterColumn("mitgliedskonto", "abrechnungslauf", "BIGINT");
    sql += alterColumn("mitgliedskonto", "mitglied", "BIGINT");
    sql += alterColumn("spendenbescheinigung", "formular", "BIGINT");
    sql += alterColumn("spendenbescheinigung", "mitglied", "BIGINT");
    sql += alterColumn("wiedervorlage", "mitglied", "BIGINT");
    sql += alterColumn("zusatzabbuchung", "mitglied", "BIGINT");
    sql += alterColumn("zusatzbetragabrechnungslauf", "abrechnungslauf",
        "BIGINT");
    sql += alterColumn("zusatzbetragabrechnungslauf", "zusatzbetrag", "BIGINT");
    sql += alterColumn("zusatzfelder", "felddefinition", "BIGINT");
    sql += alterColumn("zusatzfelder", "mitglied", "BIGINT");
    sql += alterColumn(MYSQL, "abrechnungslauf", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "adresstyp", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "anfangsbestand", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "arbeitseinsatz", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "beitragsgruppe", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "buchung", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "buchungdokument", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "buchungsart", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "buchungsklasse", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "eigenschaft", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "eigenschaften", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "eigenschaftgruppe", "id",
        "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "einstellung", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "felddefinition", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "formular", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "formularfeld", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "jahresabschluss", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "konto", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "kursteilnehmer", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "lastschrift", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "lehrgang", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "lehrgangsart", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "lesefeld", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mail", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mailanhang", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mailempfaenger", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mailvorlage", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mitglied", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mitglieddokument", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mitgliedfoto", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "mitgliedskonto", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "projekt", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "spendenbescheinigung", "id",
        "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "version", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "wiedervorlage", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "zusatzabbuchung", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "zusatzbetragabrechnungslauf", "id",
        "BIGINT AUTO_INCREMENT");
    sql += alterColumn(MYSQL, "zusatzfelder", "id", "BIGINT AUTO_INCREMENT");
    sql += alterColumn("abrechnungslauf", "dtausdruck", "tinyint(1)");
    sql += alterColumn("abrechnungslauf", "zusatzbetraege", "tinyint(1)");
    sql += alterColumn("abrechnungslauf", "kursteilnehmer", "tinyint(1)");
    sql += alterColumn("buchungsart", "spende", "tinyint(1)");
    sql += alterColumn("eigenschaftgruppe", "pflicht", "tinyint(1)");
    sql += alterColumn("eigenschaftgruppe", "max1", "tinyint(1)");
    sql += alterColumn("einstellung", "vorlaeufig", "tinyint(1)");
    sql += alterColumn("einstellung", "mitgliedsbeitraege", "tinyint(1)");
    sql += alterColumn("einstellung", "geburtsdatumpflicht", "tinyint(1)");
    sql += alterColumn("einstellung", "eintrittsdatumpflicht", "tinyint(1)");
    sql += alterColumn("einstellung", "sterbedatum", "tinyint(1)");
    sql += alterColumn("einstellung", "kommunikationsdaten", "tinyint(1)");
    sql += alterColumn("einstellung", "zusatzabbuchung", "tinyint(1)");
    sql += alterColumn("einstellung", "vermerke", "tinyint(1)");
    sql += alterColumn("einstellung", "wiedervorlage", "tinyint(1)");
    sql += alterColumn("einstellung", "kursteilnehmer", "tinyint(1)");
    sql += alterColumn("einstellung", "lehrgaenge", "tinyint(1)");
    sql += alterColumn("einstellung", "juristischepersonen", "tinyint(1)");
    sql += alterColumn("einstellung", "mitgliedfoto", "tinyint(1)");
    sql += alterColumn("einstellung", "auslandsadressen", "tinyint(1)");
    sql += alterColumn("einstellung", "arbeitseinsatz", "tinyint(1)");
    sql += alterColumn("einstellung", "dokumentenspeicherung", "tinyint(1)");
    sql += alterColumn("einstellung", "individuellebeitraege", "tinyint(1)");
    sql += alterColumn("einstellung", "externemitgliedsnummer", "tinyint(1)");
    sql += alterColumn("einstellung", "smtp_ssl", "tinyint(1)");
    sql += alterColumn("einstellung", "zusatzadressen", "tinyint(1)");
    sql += alterColumn("einstellung", "smtp_starttls", "tinyint(1)");
    sql += alterColumn("spendenbescheinigung", "ersatzaufwendungen",
        "tinyint(1)");
    sql += alterColumn("spendenbescheinigung", "unterlagenwertermittlung",
        "tinyint(1)");
    sql += alterColumn("zusatzfelder", "feldjanein", "tinyint(1)");

    statements.put(driver, sql);
    execute(conn, statements, 323);
  }

  @SuppressWarnings("unused")
  private void update0324(Connection conn) throws ApplicationException
  {
    if (driver.equals(H2))
    {
      return;
    }
    Map<String, String> statements = new HashMap<String, String>();
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id);"
                + "ALTER TABLE arbeitseinsatz ADD CONSTRAINT fkArbeitseinsatz1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE beitragsgruppe ADD CONSTRAINT fkBeitragsgruppe1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id);\n"
                + "ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id);\n"
                + "ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto) REFERENCES konto (id);\n"
                + "ALTER TABLE buchung ADD CONSTRAINT fkBuchung3 FOREIGN KEY (mitgliedskonto) REFERENCES mitgliedskonto (id);\n"
                + "ALTER TABLE buchung ADD CONSTRAINT fkBuchung4 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE buchung ADD CONSTRAINT fkBuchung5 FOREIGN KEY (spendenbescheinigung) REFERENCES spendenbescheinigung (id);\n"
                + "ALTER TABLE buchung ADD CONSTRAINT fkBuchung6 FOREIGN KEY (projekt) REFERENCES projekt (id);\n"
                + "ALTER TABLE buchungdokument ADD CONSTRAINT fkBuchungDokument1 FOREIGN KEY (referenz) REFERENCES buchung (id);\n"
                + "ALTER TABLE buchungsart ADD CONSTRAINT fkBuchungsart2 FOREIGN KEY (buchungsklasse) REFERENCES buchungsklasse (id);\n"
                + "ALTER TABLE eigenschaft ADD CONSTRAINT fkEigenschaft1 FOREIGN KEY (eigenschaftgruppe) REFERENCES eigenschaftgruppe (id);\n"
                + "ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften2 FOREIGN KEY (eigenschaft) REFERENCES eigenschaft (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;\n"
                + "ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift2 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n"
                + "ALTER TABLE lastschrift ADD CONSTRAINT fkLastschrift3 FOREIGN KEY (kursteilnehmer) REFERENCES kursteilnehmer (id);\n"
                + "ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE mailanhang ADD CONSTRAINT fkMailAnhang1 FOREIGN KEY (mail) REFERENCES mail (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailempfaenger2 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE mailempfaenger ADD CONSTRAINT fkMailEmpfaenger1 FOREIGN KEY (mail) REFERENCES mail (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id);\n"
                + "ALTER TABLE mitglied ADD CONSTRAINT fkMitglied2 FOREIGN KEY (adresstyp) REFERENCES adresstyp (id);\n"
                + "ALTER TABLE mitglieddokument ADD CONSTRAINT fkMitgliedDokument1 FOREIGN KEY (referenz) REFERENCES mitglied (id);\n"
                + "ALTER TABLE mitgliedfoto ADD CONSTRAINT fkMitgliedfoto1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE mitgliedskonto ADD CONSTRAINT fkMitgliedskonto1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE mitgliedskonto ADD CONSTRAINT fkMitgliedskonto2 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id);\n"
                + "ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung2 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n"
                + "ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n"
                + "ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n"
                + "ALTER TABLE zusatzbetragabrechnungslauf ADD CONSTRAINT fkZusatzbetragabrechnungslauf2 FOREIGN KEY (zusatzbetrag) REFERENCES zusatzabbuchung (id) ON DELETE CASCADE ON UPDATE CASCADE;\n"
                + "ALTER TABLE zusatzbetragabrechnungslauf ADD CONSTRAINT fkZusatzbetragabrechnungslauf1 FOREIGN KEY (abrechnungslauf) REFERENCES abrechnungslauf (id) ON DELETE CASCADE ON UPDATE CASCADE;\n"
                + "ALTER TABLE zusatzfelder ADD CONSTRAINT fkzusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n"
                + "ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition (id) ON DELETE CASCADE;\n");

    execute(conn, statements, 324);
  }

  @SuppressWarnings("unused")
  private void update0325(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "alter table abrechnungslauf alter column zahlungsgrund varchar(140);\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "alter table abrechnungslauf modify column zahlungsgrund varchar(140);\n");

    execute(conn, statements, 325);
  }

  @SuppressWarnings("unused")
  private void update0326(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "ALTER TABLE buchung ADD splittyp integer before spendenbescheinigung;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE buchung ADD splittyp integer after splitid;\n");

    execute(conn, statements, 326);
  }

  @SuppressWarnings("unused")
  private void update0327(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("buchung", "splitid", "BIGINT");

    statements.put(driver, sql);
    execute(conn, statements, 327);
  }

  @SuppressWarnings("unused")
  private void update0328(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(DBSupportH2Impl.class.getName(),
            "ALTER TABLE einstellung ADD arbeitsmodel integer not null default 1;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE einstellung ADD arbeitsmodel integer not null default 1;\n");

    execute(conn, statements, 328);
  }

  @SuppressWarnings("unused")
  private void update0329(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements
        .put(
            DBSupportH2Impl.class.getName(),
            "CREATE TABLE qifimporthead( "
                + " id IDENTITY(1), "
                + " name VARCHAR(30), "
                + " beschreibung VARCHAR(30), "
                + " startsalto DOUBLE, "
                + " startdate DATE, "
                + " konto INTEGER, "
                + " importdatum DATE NOT NULL, "
                + " importfile VARCHAR(256), "
                + " processdate DATE, "
                + " UNIQUE(id), "
                + " PRIMARY KEY(id) "
                + ");\n "
                +

                " CREATE TABLE qifimportpos( "
                + "   posid IDENTITY(1), "
                + "   headid INTEGER NOT NULL, "
                + "   datum DATE NOT NULL, "
                + "   betrag DOUBLE NOT NULL, "
                + "   beleg VARCHAR(30), "
                + "   name VARCHAR(100), "
                + "   zweck VARCHAR(100), "
                + "   buchartex VARCHAR(50), "
                + "   buchart INTEGER, "
                + "   mitgliedbar VARCHAR(1), "
                + "   mitglied INTEGER, "
                + "   sperre VARCHAR(1), "
                + "   UNIQUE(posid) "
                + " );\n "
                +

                " ALTER TABLE qifimportpos ADD CONSTRAINT fkImpKntPos1 FOREIGN KEY (headid) REFERENCES qifimporthead(id)  DEFERRABLE;\n");

    // Update fuer MySQL
    statements
        .put(
            DBSupportMySqlImpl.class.getName(),
            "CREATE TABLE qifimporthead( "
                + " id int(10) AUTO_INCREMENT, "
                + " name VARCHAR(30), "
                + " beschreibung VARCHAR(30), "
                + " startdate DATE, "
                + " startsalto DOUBLE, "
                + " konto int(10), "
                + " importdatum DATE NOT NULL, "
                + " importfile VARCHAR(256), "
                + " processdate DATE, "
                + " UNIQUE(id), "
                + " PRIMARY KEY(id) "
                + " );\n "
                +

                "CREATE TABLE qifimportpos( "
                + "  posid int(10) AUTO_INCREMENT, "
                + "  headid int(10) NOT NULL, "
                + "  datum DATE NOT NULL, "
                + "  betrag DOUBLE NOT NULL, "
                + "  beleg VARCHAR(30), "
                + "  name VARCHAR(100), "
                + "  zweck VARCHAR(100), "
                + "  buchartex VARCHAR(50), "
                + "  buchart int(10), "
                + "  mitgliedbar VARCHAR(1), "
                + "  mitglied int(10), "
                + "  sperre VARCHAR(1), "
                + "  UNIQUE(posid) "
                + " );\n "
                +

                "ALTER TABLE qifimportpos ADD CONSTRAINT fkImpKntPos1 FOREIGN KEY (headid) REFERENCES qifimporthead(id);\n");

    execute(conn, statements, 329);
  }

  @SuppressWarnings("unused")
  private void update0330(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE TABLE mitgliednextbgruppe( " + " id IDENTITY(1), "
            + " mitglied INTEGER, " + " beitragsgruppe INTEGER, "
            + " bemerkung VARCHAR(30), " + " abdatum DATE, " + " UNIQUE(id), "
            + " PRIMARY KEY(id) " + ");\n ");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE TABLE mitgliednextbgruppe( " + " id int(10) AUTO_INCREMENT, "
            + " mitglied int(10), " + " beitragsgruppe int(10), "
            + " bemerkung VARCHAR(30), " + " abdatum DATE, " + " UNIQUE(id), "
            + " PRIMARY KEY(id) " + " );\n ");

    execute(conn, statements, 330);
  }

  @SuppressWarnings("unused")
  private void update0331(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung ADD altermodel integer not null default 1;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung ADD altermodel integer not null default 1;\n");

    execute(conn, statements, 331);
  }

  @SuppressWarnings("unused")
  private void update0332(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ADD mandatversion integer before bic;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied ADD mandatversion integer after mandatdatum;\n");

    execute(conn, statements, 332);
  }

  @SuppressWarnings("unused")
  private void update0333(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE mitglied set mandatversion = 1 WHERE mandatdatum is not null";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 333);
  }

  @SuppressWarnings("unused")
  private void update0334(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ADD mandatsequence VARCHAR(4) before bic;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE mitglied ADD mandatsequence VARCHAR(4) after mandatversion;\n");

    execute(conn, statements, 334);
  }

  @SuppressWarnings("unused")
  private void update0335(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE mitglied set mandatsequence = 'RCUR' WHERE blz is not null or iban is not null";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 335);
  }

  @SuppressWarnings("unused")
  private void update0336(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE lastschrift ADD mandatsequence VARCHAR(4) before bic;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE lastschrift ADD mandatsequence VARCHAR(4) after mandatdatum;\n");

    execute(conn, statements, 336);
  }

  @SuppressWarnings("unused")
  private void update0337(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE mitglied set mandatsequence = 'RCUR'";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 337);
  }

  @SuppressWarnings("unused")
  private void update0338(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE abrechnungslauf ADD faelligkeit2 date before stichtag;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE abrechnungslauf ADD faelligkeit2 date after faelligkeit;\n");

    execute(conn, statements, 338);
  }

  @SuppressWarnings("unused")
  private void update0339(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE abrechnungslauf set faelligkeit2 = faelligkeit";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 339);
  }

  @SuppressWarnings("unused")
  private void update0340(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("einstellung", "name", "VARCHAR(70)");
    statements.put(driver, sql);
    execute(conn, statements, 340);
  }

  @SuppressWarnings("unused")
  private void update0341(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "UPDATE einstellung set name = left(namelang,70) where namelang is not null or length(namelang) > 0";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 341);
  }

  @SuppressWarnings("unused")
  private void update0342(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN namelang;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN namelang\n");

    execute(conn, statements, 342);
  }

  @SuppressWarnings("unused")
  private void update0343(Connection conn) throws ApplicationException
  {

    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("lastschrift", "titel", "VARCHAR(40)");
    statements.put(driver, sql);
    execute(conn, statements, 343);
  }

  @SuppressWarnings("unused")
  private void update0344(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("kursteilnehmer", "titel", "VARCHAR(40)");
    statements.put(driver, sql);
    execute(conn, statements, 344);
  }

  @SuppressWarnings("unused")
  private void update0345(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("konto", "nummer", "VARCHAR(35)");
    statements.put(driver, sql);
    execute(conn, statements, 345);
  }

  @SuppressWarnings("unused")
  private void update0346(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN delaytime;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE einstellung DROP COLUMN delaytime;\n");

    execute(conn, statements, 346);
  }

  @SuppressWarnings("unused")
  private void update0347(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE qifimporthead ADD startsaldo DOUBLE before startdate;\n");

    // Update fuer MySQL
    statements
        .put(DBSupportMySqlImpl.class.getName(),
            "ALTER TABLE qifimporthead ADD startsaldo DOUBLE after beschreibung;\n");

    execute(conn, statements, 347);
  }

  @SuppressWarnings("unused")
  private void update0348(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "update qifimporthead set startsaldo = startsaldo;\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 348);
  }

  @SuppressWarnings("unused")
  private void update0349(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE qifimporthead DROP COLUMN startsalto;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE qifimporthead DROP COLUMN startsalto;\n");

    execute(conn, statements, 349);
  }

  @SuppressWarnings("unused")
  private void update0350(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("zusatzabbuchung", "buchungstext", "VARCHAR(140)");
    statements.put(driver, sql);
    execute(conn, statements, 350);
  }

  @SuppressWarnings("unused")
  private void update0351(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "update zusatzabbuchung set buchungstext = trim(concat(buchungstext, ' ', IFNULL(buchungstext2,'')));\n";
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 351);
  }

  @SuppressWarnings("unused")
  private void update0352(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzabbuchung DROP buchungstext2;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE zusatzabbuchung DROP buchungstext2;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 352);
  }

  @SuppressWarnings("unused")
  private void update0353(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zusatzbetragausgetretene boolean;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung ADD zusatzbetragausgetretene bit(1);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 353);
  }

  @SuppressWarnings("unused")
  private void update0354(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2 und MySQL
    sb = new StringBuilder();
    sb.append(String.format("ALTER TABLE einstellung ADD %s INT DEFAULT 1;\n",
        Einstellung.COL_SEPA_MANDANTID_SOURCE));

    String statement = sb.toString();

    statements.put(DBSupportH2Impl.class.getName(), statement);
    statements.put(DBSupportMySqlImpl.class.getName(), statement);

    execute(conn, statements, 354);
  }

  @SuppressWarnings("unused")
  private void update0355(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "-- Nothing to do";

    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 355);
  }

  @SuppressWarnings("unused")
  private void update0356(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    sb = new StringBuilder();
    sb.append(String.format("ALTER TABLE einstellung ADD %s VARCHAR(140);\n",
        "mail_always_bcc"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s VARCHAR(140);\n",
        "mail_always_cc"));

    sb.append(String.format("ALTER TABLE einstellung ADD %s boolean;\n",
        "copy_to_imap_folder"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s boolean;\n",
        "imap_ssl"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s boolean;\n",
        "imap_starttls"));

    sb.append(String.format("ALTER TABLE einstellung ADD %s VARCHAR(140);\n",
        "imap_auth_user"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s VARCHAR(140);\n",
        "imap_auth_pwd"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s VARCHAR(140);\n",
        "imap_host"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s VARCHAR(140);\n",
        "imap_port"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s VARCHAR(140);\n",
        "imap_sent_folder"));

    String statement = sb.toString();

    statements.put(DBSupportH2Impl.class.getName(), statement);
    statements.put(DBSupportMySqlImpl.class.getName(), statement);

    execute(conn, statements, 356);
  }

  @SuppressWarnings("unused")
  private void update0357(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("mitglied", "vermerk1", "VARCHAR(2000)");
    sql += alterColumn("mitglied", "vermerk2", "VARCHAR(2000)");

    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 357);
  }

  @SuppressWarnings("unused")
  private void update0358(Connection conn) throws ApplicationException
  {
    String imapPwd = "";
    String smtpPwd = "";
    try
    {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt
          .executeQuery("select imap_auth_pwd from einstellung;");
      if (rs.next())
      {
        imapPwd = rs.getString(1);
      }
      stmt = conn.createStatement();
      rs = stmt.executeQuery("select smtp_auth_pwd from einstellung;");
      if (rs.next())
      {
        smtpPwd = rs.getString(1);
      }
    }
    catch (SQLException e)
    {
      Logger
          .error("Datenbankupdate 358: Auslesen der Emailpasswörter fehlgeschlagen. Fehler wird ignoriert, Passwörter in Einstellungsmaske bitte neu eingeben.");
    }

    try
    {
      Wallet wallet = new Wallet(EinstellungImpl.class);
      wallet.set("imap_auth_pwd", imapPwd);
      wallet.set("smtp_auth_pwd", smtpPwd);
    }
    catch (Exception e)
    {
      Logger
          .error("Datenbankupdate 358: Speichern der Emailpasswörter in Wallet fehlgeschlagen.");
    }

    Map<String, String> statements = new HashMap<String, String>();
    sb = new StringBuilder();
    sb.append("ALTER TABLE einstellung DROP imap_auth_pwd;\n");
    sb.append("ALTER TABLE einstellung DROP smtp_auth_pwd;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, 358);
  }

  @SuppressWarnings("unused")
  private void update0359(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = alterColumn("lastschrift", "anrede", "VARCHAR(40)");

    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, 359);
  }

  @SuppressWarnings("unused")
  private void update0360(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    sb = new StringBuilder();
    sb.append(String.format("ALTER TABLE einstellung ADD %s DATE;\n",
        "veranlagungvon"));
    sb.append(String.format("ALTER TABLE einstellung ADD %s DATE;\n",
        "veranlagungbis"));

    String statement = sb.toString();

    statements.put(DBSupportH2Impl.class.getName(), statement);
    statements.put(DBSupportMySqlImpl.class.getName(), statement);

    execute(conn, statements, 360);
  }

  private String alterColumn(String table, String column, String type)
  {
    return alterColumn(this.driver, table, column, type);
  }

  private String alterColumn(String _driver, String table, String column,
      String type)
  {
    if (driver.equals(H2) && _driver.equals(H2))
    {
      return "ALTER TABLE " + table + " ALTER COLUMN " + column + " " + type
          + ";\n";
    }
    if (driver.equals(MYSQL) && _driver.equals(MYSQL))
    {
      return "ALTER TABLE " + table + " MODIFY COLUMN " + column + " " + type
          + ";\n";
    }
    return "";
  }

}
