package de.jost_net.JVerein.server;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.willuhn.logging.Logger;
import de.willuhn.sql.ScriptExecutor;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;
import de.willuhn.util.ProgressMonitor;

public class JVereinUpdateProvider
{
  private Connection conn;

  private ProgressMonitor progressmonitor;

  private StringBuilder sb;

  public JVereinUpdateProvider(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    this.conn = conn;
    this.progressmonitor = progressmonitor;
    int cv = getCurrentVersion();
    int cvv = cv;
    if (cv == 0)
    {
      install(conn, progressmonitor);
    }
    if (cv < 16)
    {
      cv = update0016(conn, progressmonitor);
    }
    if (cv < 17)
    {
      cv = update0017(conn, progressmonitor);
    }
    if (cv < 18)
    {
      cv = update0018(conn, progressmonitor);
    }
    if (cv < 19)
    {
      cv = update0019(conn, progressmonitor);
    }
    if (cv < 20)
    {
      cv = update0020(conn, progressmonitor);
    }
    if (cv < 21)
    {
      cv = update0021(conn, progressmonitor);
    }
    if (cv < 22)
    {
      cv = update0022(conn, progressmonitor);
    }
    if (cv < 23)
    {
      cv = update0023(conn, progressmonitor);
    }
    if (cv < 24)
    {
      cv = update0024(conn, progressmonitor);
    }
    if (cv < 25)
    {
      cv = update0025(conn, progressmonitor);
    }
    if (cv < 26)
    {
      cv = update0026(conn, progressmonitor);
    }
    if (cv < 27)
    {
      cv = update0027(conn, progressmonitor);
    }
    if (cv < 28)
    {
      cv = update0028(conn, progressmonitor);
    }
    if (cv < 29)
    {
      cv = update0029(conn, progressmonitor);
    }
    if (cv < 30)
    {
      cv = update0030(conn, progressmonitor);
    }
    if (cv < 31)
    {
      cv = update0031(conn, progressmonitor);
    }
    if (cv < 32)
    {
      cv = update0032(conn, progressmonitor);
    }
    if (cv < 33)
    {
      cv = update0033(conn, progressmonitor);
    }
    if (cv < 34)
    {
      cv = update0034(conn, progressmonitor);
    }
    if (cv < 35)
    {
      cv = update0035(conn, progressmonitor);
    }
    if (cv < 36)
    {
      cv = update0036(conn, progressmonitor);
    }
    if (cv < 37)
    {
      cv = update0037(conn, progressmonitor);
    }
    if (cv < 38)
    {
      cv = update0038(conn, progressmonitor);
    }
    if (cv < 39)
    {
      cv = update0039(conn, progressmonitor);
    }
    if (cv < 40)
    {
      cv = update0040(conn, progressmonitor);
    }
    if (cv < 41)
    {
      cv = update0041(conn, progressmonitor);
    }
    if (cv < 42)
    {
      cv = update0042(conn, progressmonitor);
    }
    if (cv < 43)
    {
      cv = update0043(conn, progressmonitor);
    }
    if (cv < 44)
    {
      cv = update0044(conn, progressmonitor);
    }
    if (cv < 45)
    {
      cv = update0045(conn, progressmonitor);
    }
    if (cv < 46)
    {
      cv = update0046(conn, progressmonitor);
    }
    if (cv < 47)
    {
      cv = update0047(conn, progressmonitor);
    }
    if (cv < 48)
    {
      cv = update0048(conn, progressmonitor);
    }
    if (cv < 49)
    {
      cv = update0049(conn, progressmonitor);
    }
    if (cv < 50)
    {
      cv = update0050(conn, progressmonitor);
    }
    if (cv < 51)
    {
      cv = update0051(conn, progressmonitor);
    }
    if (cv < 52)
    {
      cv = update0052(conn, progressmonitor);
    }
    if (cv < 53)
    {
      cv = update0053(conn, progressmonitor);
    }
    if (cv < 54)
    {
      cv = update0054(conn, progressmonitor);
    }
    if (cv < 55)
    {
      cv = update0055(conn, progressmonitor);
    }
    if (cv < 56)
    {
      cv = update0056(conn, progressmonitor);
    }
    if (cv < 57)
    {
      cv = update0057(conn, progressmonitor);
    }
    if (cv < 58)
    {
      cv = update0058(conn, progressmonitor);
    }
    if (cv < 59)
    {
      cv = update0059(conn, progressmonitor);
    }
    if (cv < 60)
    {
      cv = update0060(conn, progressmonitor);
    }
    if (cv < 61)
    {
      cv = update0061(conn, progressmonitor);
    }
    if (cv < 62)
    {
      cv = update0062(conn, progressmonitor);
    }
    if (cv < 63)
    {
      cv = update0063(conn, progressmonitor);
    }
    if (cv < 64)
    {
      cv = update0064(conn, progressmonitor);
    }
    if (cv < 65)
    {
      cv = update0065(conn, progressmonitor);
    }
    if (cv < 66)
    {
      cv = update0066(conn, progressmonitor);
    }
    if (cv < 67)
    {
      cv = update0067(conn, progressmonitor);
    }
    if (cv < 68)
    {
      cv = update0068(conn, progressmonitor);
    }
    if (cv < 69)
    {
      cv = update0069(conn, progressmonitor);
    }
    if (cv < 70)
    {
      cv = update0070(conn, progressmonitor);
    }
    if (cv < 71)
    {
      cv = update0071(conn, progressmonitor);
    }
    if (cv < 72)
    {
      cv = update0072(conn, progressmonitor);
    }
    if (cv < 73)
    {
      cv = update0073(conn, progressmonitor);
    }
    if (cv < 74)
    {
      cv = update0074(conn, progressmonitor);
    }
    if (cv < 75)
    {
      cv = update0075(conn, progressmonitor);
    }
    if (cv < 76)
    {
      cv = update0076(conn, progressmonitor);
    }
    if (cv < 77)
    {
      cv = update0077(conn, progressmonitor);
    }
    if (cv < 78)
    {
      cv = update0078(conn, progressmonitor);
    }
    if (cv < 79)
    {
      cv = update0079(conn, progressmonitor);
    }
    if (cv < 80)
    {
      cv = update0080(conn, progressmonitor);
    }
    if (cv < 81)
    {
      cv = update0081(conn, progressmonitor);
    }
    if (cv < 82)
    {
      cv = update0082(conn, progressmonitor);
    }
    if (cv < 83)
    {
      cv = update0083(conn, progressmonitor);
    }
    if (cv < 84)
    {
      cv = update0084(conn, progressmonitor);
    }
    if (cv < 85)
    {
      cv = update0085(conn, progressmonitor);
    }
    if (cv != cvv)
    {
      setNewVersion(cv);
    }
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

  /**
   * @see de.willuhn.sql.version.Update#execute(de.willuhn.sql.version.UpdateProvider)
   */
  public void execute(Connection conn, Map<String, String> statements,
      String logstring) throws ApplicationException
  {
    Logger.info("TODO: " + logstring);
    String driver = JVereinDBService.SETTINGS
        .getString("database.driver", null);
    I18N i18n = JVereinPlugin.getI18n();
    String sql = (String) statements.get(driver);
    if (sql == null)
    {
      throw new ApplicationException(i18n.tr(
          "Datenbank {0} wird nicht unterstützt", driver));
    }
    try
    {
      ScriptExecutor.execute(new StringReader(sql), conn, progressmonitor);
      progressmonitor.log(logstring);
    }
    catch (Exception e)
    {
      Logger.error("unable to execute update", e);
      throw new ApplicationException(i18n
          .tr("Fehler beim Ausführen des Updates"), e);
    }
  }

  private int install(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitglied (");
    sb.append("id IDENTITY, ");
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
    sb.append("id IDENTITY,");
    sb.append("bezeichnung VARCHAR(30) NOT NULL,");
    sb.append("betrag DOUBLE,");
    sb.append("beitragsart INTEGER DEFAULT 0,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb
        .append("ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE zusatzabbuchung(");
    sb.append("id IDENTITY,");
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
    sb
        .append("ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE stammdaten(");
    sb.append("id IDENTITY,");
    sb.append("name VARCHAR(30) NOT NULL,");
    sb.append("blz VARCHAR(8)  NOT NULL,");
    sb.append("konto VARCHAR(10) NOT NULL,");
    sb.append("altersgruppen VARCHAR(50),");
    sb.append("jubilaeen VARCHAR(50),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE kursteilnehmer (");
    sb.append("id IDENTITY,");
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
    sb.append("id IDENTITY, ");
    sb.append("name VARCHAR(27) NOT NULL,");
    sb.append("vzweck1 VARCHAR(27) NOT NULL,");
    sb.append("vzweck2 VARCHAR(27),");
    sb.append("betrag DOUBLE NOT NULL,");
    sb.append("eingabedatum DATE NOT NULL,");
    sb.append("eingangsdatum DATE,");
    sb.append("UNIQUE (id), ");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE wiedervorlage(");
    sb.append("id IDENTITY,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("datum DATE NOT NULL,");
    sb.append("vermerk VARCHAR(50) NOT NULL,");
    sb.append("erledigung DATE,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb
        .append("ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE eigenschaften(");
    sb.append("id IDENTITY,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("eigenschaft VARCHAR(50) NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb
        .append("CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);\n");
    sb.append("CREATE TABLE `version` (");
    sb.append("`id` IDENTITY, ");
    sb.append("`version` INTEGER,");
    sb.append("UNIQUE (`id`), ");
    sb.append("PRIMARY KEY (`id`));\n");
    sb.append("create table felddefinition(");
    sb.append("id IDENTITY,");
    sb.append("name VARCHAR(50) NOT NULL,");
    sb.append("label VARCHAR(50) NOT NULL,");
    sb.append("laenge integer NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("create table zusatzfelder(");
    sb.append("id IDENTITY,");
    sb.append("mitglied integer NOT NULL,");
    sb.append("felddefinition integer NOT NULL,");
    sb.append("feld varchar(1000),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb
        .append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb
        .append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE DEFERRABLE;\n");
    sb.append("CREATE TABLE konto (");
    sb.append("id IDENTITY,");
    sb.append("nummer VARCHAR(10),");
    sb.append("bezeichnung VARCHAR(30),");
    sb.append("eroeffnung DATE,");
    sb.append("aufloesung DATE,");
    sb.append("hibiscusid INTEGER,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (nummer),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE buchungsart (");
    sb.append("id IDENTITY,");
    sb.append("nummer INTEGER,");
    sb.append("bezeichnung VARCHAR(30),");
    sb.append("art INTEGER,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (nummer),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE TABLE buchung (");
    sb.append("id IDENTITY,");
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
    sb
        .append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id) DEFERRABLE;\n");
    sb
        .append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE anfangsbestand (");
    sb.append("id IDENTITY,");
    sb.append("konto INTEGER,");
    sb.append("datum DATE,");
    sb.append("betrag DOUBLE,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (konto, datum),");
    sb.append("PRIMARY KEY (id));\n");
    sb
        .append("ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE jahresabschluss (");
    sb.append("id IDENTITY,");
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
    sb.append(")TYPE=InnoDB;\n");
    sb.append("CREATE TABLE beitragsgruppe (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("bezeichnung VARCHAR(30) NOT NULL,");
    sb.append("betrag DOUBLE,");
    sb.append("beitragsart INTEGER DEFAULT 0,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id) ;\n");
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
    sb.append(")TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ;\n");
    sb.append("CREATE TABLE stammdaten (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("name VARCHAR(30) NOT NULL,");
    sb.append("blz VARCHAR(8)  NOT NULL,");
    sb.append("konto VARCHAR(10) NOT NULL,");
    sb.append("altersgruppen VARCHAR(50),");
    sb.append("jubilaeen VARCHAR(50),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")TYPE=InnoDB;\n");
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
    sb.append(")TYPE=InnoDB;\n");
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
    sb.append(")TYPE=InnoDB;\n");
    sb.append("CREATE TABLE wiedervorlage (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("datum DATE NOT NULL,");
    sb.append("vermerk VARCHAR(50) NOT NULL,");
    sb.append("erledigung DATE,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ;\n");
    sb.append("CREATE TABLE eigenschaften (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("eigenschaft VARCHAR(50) NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")TYPE=InnoDB;\n");
    sb
        .append("CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);\n");
    sb.append("CREATE TABLE `version` (");
    sb.append("`id` INTEGER AUTO_INCREMENT,");
    sb.append("`version` INTEGER,");
    sb.append("UNIQUE (`id`),");
    sb.append("PRIMARY KEY (`id`)");
    sb.append(") TYPE=InnoDB;\n");
    sb.append("create table felddefinition (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append(" name VARCHAR(50) NOT NULL,");
    sb.append("label VARCHAR(50) NOT NULL,");
    sb.append("laenge integer NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")TYPE=InnoDB;\n");
    sb.append("create table zusatzfelder (");
    sb.append("id integer auto_increment,");
    sb.append("mitglied integer NOT NULL,");
    sb.append("felddefinition integer NOT NULL,");
    sb.append("feld varchar(1000),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n");
    sb
        .append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE;\n");
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
    sb.append(") TYPE=InnoDB;\n");
    sb.append("CREATE TABLE buchungsart (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("nummer INTEGER,");
    sb.append("bezeichnung VARCHAR(30),");
    sb.append("art INTEGER,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (nummer),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") TYPE=InnoDB;\n");
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
    sb.append(") TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id);\n");
    sb
        .append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id);\n");
    sb.append("CREATE TABLE anfangsbestand (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("konto INTEGER,");
    sb.append("datum DATE,");
    sb.append("BETRAG DOUBLE,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (konto, datum),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id);\n");
    sb.append("CREATE TABLE jahresabschluss (");
    sb.append("id INTEGER AUTO_INCREMENT,");
    sb.append("von DATE,");
    sb.append("bis DATE,");
    sb.append("datum DATE,");
    sb.append("name VARCHAR(50),");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(") TYPE=InnoDB;\n");
    sb.append("INSERT INTO version VALUES (1,15);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle formular erstellt");
    return 16;
  }

  private int update0016(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE formular (");
    sb.append("  id IDENTITY,");
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
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle formular erstellt");
    return 16;
  }

  private int update0017(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Logger.info("");
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE formularfeld(");
    sb.append(" id IDENTITY,");
    sb.append(" formular integer,");
    sb.append(" name VARCHAR(20),");
    sb.append(" x double,");
    sb.append(" y double,");
    sb.append(" font VARCHAR(20),");
    sb.append(" fontsize integer,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id));\n");
    sb
        .append("ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
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
    sb.append(") TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle formularfeld erstellt");
    return 17;
  }

  private int update0018(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung alter column  kommentar varchar(1000);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung modify column  kommentar varchar(1000);\n");

    execute(conn, statements, "Spalte kommentar der Tabelle buchung verlängert");
    return 18;
  }

  private int update0019(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table formularfeld add fontstyle integer;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table formularfeld add fontstyle integer;\n");

    execute(conn, statements,
        "Spalte fontstyle zur Tabelle formularfeld hinzugefügt");
    return 19;
  }

  private int update0020(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE spendenbescheinigung (");
    sb.append(" id IDENTITY,");
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
    sb
        .append("ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE RESTRICT;\n");
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
    sb.append(") TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE RESTRICT;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle spendenbescheinigung erstellt");
    return 20;
  }

  private int update0021(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE UNIQUE INDEX ixFormular1 ON formular(bezeichnung);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE UNIQUE INDEX ixFormular1 ON formular(bezeichnung);\n");

    execute(conn, statements, "Index für Tabelle formular erstellt");
    return 21;
  }

  private int update0022(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE abrechnung (");
    sb.append(" id IDENTITY, ");
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
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle abrechnung erstellt");
    return 22;
  }

  private int update0023(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE INDEX ixAbrechnung1 ON abrechnung(mitglied);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE INDEX ixAbrechnung1 ON abrechnung(mitglied);\n");

    execute(conn, statements, "Index für Tabelle abrechnung erstellt");
    return 23;
  }

  private int update0024(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle abrechnung erstellt");
    return 24;
  }

  private int update0025(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE stammdaten ADD altersjubilaeen varchar(50);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE stammdaten ADD altersjubilaeen varchar(50);\n");

    execute(conn, statements,
        "Spalte altersjubilaeen in die Tabelle stammdaten eingefügt");
    return 25;
  }

  private int update0026(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte adressierungszusatz in die Tabelle mitglied eingefügt");
    return 26;
  }

  private int update0027(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte adressierungszusatz in der Tabelle auf '' (Leerstring) gesetzt");
    return 27;
  }

  private int update0028(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE einstellung (");
    sb.append(" id IDENTITY, ");
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
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle einstellung erstellt");
    return 28;
  }

  private int update0029(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE buchung ADD auszugsnummer integer before name;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE buchung ADD auszugsnummer integer after konto;\n");

    execute(conn, statements,
        "Spalte auszugsnummer in die Tabelle buchung eingefügt");
    return 29;
  }

  private int update0030(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE buchung ADD blattnummer integer before name;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE buchung ADD blattnummer integer after auszugsnummer;\n");

    execute(conn, statements,
        "Spalte blattnummer in die Tabelle buchung eingefügt");
    return 30;
  }

  private int update0031(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN telefonprivat varchar(20);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN telefonprivat varchar(20);\n");

    execute(conn, statements,
        "Spalte telefonprivat in der Tabelle mitglied verlängert");
    return 31;
  }

  private int update0032(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN telefondienstlich varchar(20);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN telefondienstlich varchar(20);\n");

    execute(conn, statements,
        "Spalte telefondienstlich in der Tabelle mitglied verlängert");
    return 32;
  }

  private int update0033(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN handy varchar(20);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN handy varchar(20);\n");

    execute(conn, statements, "Spalte handy in der Tabelle mitglied verlängert");
    return 33;
  }

  private int update0034(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE spendenbescheinigung ADD ersatzaufwendungen char(5);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE spendenbescheinigung ADD ersatzaufwendungen char(5);\n");

    execute(conn, statements,
        "Spalte ersatzaufwendungen in die Tabelle spendenbescheinigung aufgenommen");
    return 34;
  }

  private int update0035(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE report");
    sb.append("(");
    sb.append(" id IDENTITY,");
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
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle report aufgenommen");
    return 35;
  }

  private int update0036(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Tabelle report gelöscht");
    return 36;
  }

  private int update0037(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table lehrgangsart (");
    sb.append(" id IDENTITY,");
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
    sb.append(");\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle lehrgangsart erstellt");
    return 37;
  }

  private int update0038(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD lehrgaenge char(5) before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD lehrgaenge char(5) after kursteilnehmer;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte lehrgaenge in die Tabelle lehrgangsart aufgenommen");
    return 38;
  }

  private int update0039(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table lehrgang (");
    sb.append(" id IDENTITY,");
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
    sb.append(");\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle lehrgang aufgenommen");
    return 39;
  }

  private int update0040(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key für Tabelle lehrgang aufgenommen");
    return 40;
  }

  private int update0041(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key für Tabelle lehrgang aufgenommen");
    return 41;
  }

  private int update0042(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang DROP CONSTRAINT fkLehrgang1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("-- nothing to do\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key für Tabelle lehrgang entfernt");
    return 42;
  }

  private int update0043(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE lehrgang DROP CONSTRAINT fkLehrgang2;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("-- nothing to do\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key für Tabelle lehrgang entfernt");
    return 43;
  }

  private int update0044(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("-- nothing to do\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key für Tabelle lehrgang eingerichtet");
    return 44;
  }

  private int update0045(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE lehrgang ADD CONSTRAINT fkLehrgang2 FOREIGN KEY (lehrgangsart) REFERENCES lehrgangsart (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key für Tabelle lehrgang eingerichtet");
    return 45;
  }

  private int update0046(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Spalte inhalt der Tabelle formular verlängert");
    return 46;
  }

  private int update0047(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD juristischepersonen char(5) before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD juristischepersonen char(5) after lehrgaenge;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte juristischepersonen in die Tabelle einstellung aufgenommen");
    return 47;
  }

  private int update0048(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD personenart char(1) before anrede;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE mitglied ADD personenart char(1) after externemitgliedsnummer;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte personenart in die Tabelle mitglied aufgenommen");
    return 48;
  }

  private int update0049(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Alle Mitglieder auf personenart 'n' gesetzt");
    return 49;
  }

  private int update0050(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte vorname in der Tabelle mitglied verlängert.");
    return 50;
  }

  private int update0051(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key der Tabelle zusatzfelder entfernt");
    return 51;
  }

  private int update0052(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkzusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkzusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Foreign Key der Tabelle zusatzfelder eingerichtet");
    return 52;
  }

  private int update0053(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD aktuellegeburtstagevorher integer default 3 before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD aktuellegeburtstagevorher integer default 3 after juristischepersonen;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte aktuellegeburtstagevorher in die Tabelle einstellung aufgenommen");
    return 53;
  }

  private int update0054(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD aktuellegeburtstagenachher integer default 7 before externemitgliedsnummer;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD aktuellegeburtstagenachher integer default 7 after aktuellegeburtstagevorher;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte aktuellegeburtstagenachher in die Tabelle einstellung aufgenommen");
    return 54;
  }

  private int update0055(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE buchungsklasse (");
    sb.append(" id IDENTITY,");
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
    sb.append(") TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle buchungsklasse aufgenommen");
    return 55;
  }

  private int update0056(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(
        conn,
        statements,
        "Spalte buchungsklasse in die Tabelle buchungsart aufgenommen, Index zur Tabelle buchungsart aufgenommen");
    return 56;
  }

  private int update0057(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE buchungsart ADD CONSTRAINT fkBuchungsart2 FOREIGN KEY (buchungsklasse) REFERENCES buchungsklasse (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE buchungsart ADD CONSTRAINT fkBuchungsart2 FOREIGN KEY (buchungsklasse) REFERENCES buchungsklasse (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key zur Tabelle buchungsart hinzugefügt");
    return 57;
  }

  private int update0058(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD updateinterval integer default 30;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD updateinterval integer default 30;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte updateinterval zur Tabelle einstellung hinzugefügt");
    return 58;
  }

  private int update0059(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD updatediaginfos char(5)default 'true';\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE einstellung ADD updatediaginfos char(5)default 'true';\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte updatediaginfos zur Tabelle einstellung hinzugefügt");
    return 59;
  }

  private int update0060(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte updatelastcheck zur Tabelle einstellung hinzugefügt");
    return 60;
  }

  private int update0061(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("-- nothing to do\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("alter table `anfangsbestand` change BETRAG `betrag` double default NULL;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte betrag der Tabelle anfangsbestand geändert");
    return 61;
  }

  private int update0062(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte smtp_server in die Tabelle einstellung aufgenommen");
    return 62;
  }

  private int update0063(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte smtp_port in die Tabelle einstellung aufgenommen");
    return 63;
  }

  private int update0064(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte smtp_auth_user in die Tabelle einstellung aufgenommen");
    return 64;
  }

  private int update0065(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte smtp_auth_pwd in die Tabelle einstellung aufgenommen");
    return 65;
  }

  private int update0066(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte smtp_from_address in die Tabelle einstellung aufgenommen");
    return 66;
  }

  private int update0067(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte smtp_ssl in die Tabelle einstellung aufgenommen");
    return 67;
  }

  private int update0068(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE eigenschaftgruppe (");
    sb.append(" id IDENTITY,");
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
    sb.append(") TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle eigenschaftgruppe aufgenommen");
    return 68;
  }

  private int update0069(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE eigenschaft (");
    sb.append("id                IDENTITY,");
    sb.append("eigenschaftgruppe     INTEGER,");
    sb.append("bezeichnung     VARCHAR(30) NOT NULL,");
    sb.append("UNIQUE        (id),");
    sb.append("PRIMARY KEY   (id));\n");
    sb
        .append("ALTER TABLE eigenschaft ADD CONSTRAINT fkEigenschaft1 FOREIGN KEY (eigenschaftgruppe) REFERENCES eigenschaftgruppe (id) DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE eigenschaft (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append("eigenschaftgruppe     INTEGER,");
    sb.append(" bezeichnung VARCHAR(30) NOT NULL,");
    sb.append(" UNIQUE (id),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(") TYPE=InnoDB;\n");
    sb
        .append("ALTER TABLE eigenschaft ADD CONSTRAINT fkEigenschaft1 FOREIGN KEY (eigenschaftgruppe) REFERENCES eigenschaftgruppe (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle eigenschaftgruppe aufgenommen");
    return 69;
  }

  private int update0070(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN anrede varchar(40);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN anrede varchar(40);\n");

    execute(conn, statements,
        "Spalte anrede in der Tabelle mitglied verlängert");
    return 70;
  }

  private int update0071(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN titel varchar(40);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN titel varchar(40);\n");

    execute(conn, statements,
        "Spalte anrede in der Tabelle mitglied verlängert");
    return 71;
  }

  private int update0072(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    return 72;
  }

  private int update0073(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    return 73;
  }

  private int update0074(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Typ der Spalte eigenschaft der Tabelle eigenschaften verändert");
    return 74;
  }

  private int update0075(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "INSERT INTO eigenschaftgruppe (id, bezeichnung) values ('1', 'keine Zuordnung');";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Default-Wert in die Tabelle eigenschaftgruppe eingetragen");
    return 75;
  }

  private int update0076(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "UPDATE eigenschaft SET eigenschaftgruppe = '1' WHERE eigenschaftgruppe IS NULL;";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Default-Wert in die Tabelle eigenschaft eingetragen");
    return 76;
  }

  private int update0077(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung);\n");

    execute(conn, statements, "Index für Tabelle eigenschaft erstellt");
    return 77;
  }

  private int update0078(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Index für Tabelle eigenschaft erstellt");
    return 78;
  }

  private int update0079(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Spalten aus Tabelle einstellung entfernt");
    return 79;
  }

  private int update0080(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE felddefinition ADD datentyp integer default 1 before laenge;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb
        .append("ALTER TABLE felddefinition ADD datentyp integer default 1 after label;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte datentyp in die Tabelle felddefinition aufgenommen");
    return 80;
  }

  private int update0081(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen");
    return 81;
  }

  private int update0082(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen");
    return 82;
  }

  private int update0083(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen");
    return 83;
  }

  private int update0084(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen");
    return 84;
  }

  private int update0085(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen");
    return 85;
  }
}
