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
    if (cv == 0)
    {
      install(conn, progressmonitor);
    }
    if (cv < 16)
    {
      update0016(conn, progressmonitor);
    }
    if (cv < 17)
    {
      update0017(conn, progressmonitor);
    }
    if (cv < 18)
    {
      update0018(conn, progressmonitor);
    }
    if (cv < 19)
    {
      update0019(conn, progressmonitor);
    }
    if (cv < 20)
    {
      update0020(conn, progressmonitor);
    }
    if (cv < 21)
    {
      update0021(conn, progressmonitor);
    }
    if (cv < 22)
    {
      update0022(conn, progressmonitor);
    }
    if (cv < 23)
    {
      update0023(conn, progressmonitor);
    }
    if (cv < 24)
    {
      update0024(conn, progressmonitor);
    }
    if (cv < 25)
    {
      update0025(conn, progressmonitor);
    }
    if (cv < 26)
    {
      update0026(conn, progressmonitor);
    }
    if (cv < 27)
    {
      update0027(conn, progressmonitor);
    }
    if (cv < 28)
    {
      update0028(conn, progressmonitor);
    }
    if (cv < 29)
    {
      update0029(conn, progressmonitor);
    }
    if (cv < 30)
    {
      update0030(conn, progressmonitor);
    }
    if (cv < 31)
    {
      update0031(conn, progressmonitor);
    }
    if (cv < 32)
    {
      update0032(conn, progressmonitor);
    }
    if (cv < 33)
    {
      update0033(conn, progressmonitor);
    }
    if (cv < 34)
    {
      update0034(conn, progressmonitor);
    }
    if (cv < 35)
    {
      update0035(conn, progressmonitor);
    }
    if (cv < 36)
    {
      update0036(conn, progressmonitor);
    }
    if (cv < 37)
    {
      update0037(conn, progressmonitor);
    }
    if (cv < 38)
    {
      update0038(conn, progressmonitor);
    }
    if (cv < 39)
    {
      update0039(conn, progressmonitor);
    }
    if (cv < 40)
    {
      update0040(conn, progressmonitor);
    }
    if (cv < 41)
    {
      update0041(conn, progressmonitor);
    }
    if (cv < 42)
    {
      update0042(conn, progressmonitor);
    }
    if (cv < 43)
    {
      update0043(conn, progressmonitor);
    }
    if (cv < 44)
    {
      update0044(conn, progressmonitor);
    }
    if (cv < 45)
    {
      update0045(conn, progressmonitor);
    }
    if (cv < 46)
    {
      update0046(conn, progressmonitor);
    }
    if (cv < 47)
    {
      update0047(conn, progressmonitor);
    }
    if (cv < 48)
    {
      update0048(conn, progressmonitor);
    }
    if (cv < 49)
    {
      update0049(conn, progressmonitor);
    }
    if (cv < 50)
    {
      update0050(conn, progressmonitor);
    }
    if (cv < 51)
    {
      update0051(conn, progressmonitor);
    }
    if (cv < 52)
    {
      update0052(conn, progressmonitor);
    }
    if (cv < 53)
    {
      update0053(conn, progressmonitor);
    }
    if (cv < 54)
    {
      update0054(conn, progressmonitor);
    }
    if (cv < 55)
    {
      update0055(conn, progressmonitor);
    }
    if (cv < 56)
    {
      update0056(conn, progressmonitor);
    }
    if (cv < 57)
    {
      update0057(conn, progressmonitor);
    }
    if (cv < 58)
    {
      update0058(conn, progressmonitor);
    }
    if (cv < 59)
    {
      update0059(conn, progressmonitor);
    }
    if (cv < 60)
    {
      update0060(conn, progressmonitor);
    }
    if (cv < 61)
    {
      update0061(conn, progressmonitor);
    }
    if (cv < 62)
    {
      update0062(conn, progressmonitor);
    }
    if (cv < 63)
    {
      update0063(conn, progressmonitor);
    }
    if (cv < 64)
    {
      update0064(conn, progressmonitor);
    }
    if (cv < 65)
    {
      update0065(conn, progressmonitor);
    }
    if (cv < 66)
    {
      update0066(conn, progressmonitor);
    }
    if (cv < 67)
    {
      update0067(conn, progressmonitor);
    }
    if (cv < 68)
    {
      update0068(conn, progressmonitor);
    }
    if (cv < 69)
    {
      update0069(conn, progressmonitor);
    }
    if (cv < 70)
    {
      update0070(conn, progressmonitor);
    }
    if (cv < 71)
    {
      update0071(conn, progressmonitor);
    }
    if (cv < 72)
    {
      update0072(conn, progressmonitor);
    }
    if (cv < 73)
    {
      update0073(conn, progressmonitor);
    }
    if (cv < 74)
    {
      update0074(conn, progressmonitor);
    }
    if (cv < 75)
    {
      update0075(conn, progressmonitor);
    }
    if (cv < 76)
    {
      update0076(conn, progressmonitor);
    }
    if (cv < 77)
    {
      update0077(conn, progressmonitor);
    }
    if (cv < 78)
    {
      update0078(conn, progressmonitor);
    }
    if (cv < 79)
    {
      update0079(conn, progressmonitor);
    }
    if (cv < 80)
    {
      update0080(conn, progressmonitor);
    }
    if (cv < 81)
    {
      update0081(conn, progressmonitor);
    }
    if (cv < 82)
    {
      update0082(conn, progressmonitor);
    }
    if (cv < 83)
    {
      update0083(conn, progressmonitor);
    }
    if (cv < 84)
    {
      update0084(conn, progressmonitor);
    }
    if (cv < 85)
    {
      update0085(conn, progressmonitor);
    }
    if (cv < 86)
    {
      update0086(conn, progressmonitor);
    }
    if (cv < 87)
    {
      update0087(conn, progressmonitor);
    }
    if (cv < 88)
    {
      update0088(conn, progressmonitor);
    }
    if (cv < 89)
    {
      update0089(conn, progressmonitor);
    }
    if (cv < 90)
    {
      update0090(conn, progressmonitor);
    }
    if (cv < 91)
    {
      update0091(conn, progressmonitor);
    }
    if (cv < 92)
    {
      update0092(conn, progressmonitor);
    }
    if (cv < 93)
    {
      update0093(conn, progressmonitor);
    }
    if (cv < 94)
    {
      update0094(conn, progressmonitor);
    }
    if (cv < 95)
    {
      update0095(conn, progressmonitor);
    }
    if (cv < 96)
    {
      update0096(conn, progressmonitor);
    }
    if (cv < 97)
    {
      update0097(conn, progressmonitor);
    }
    if (cv < 98)
    {
      update0098(conn, progressmonitor);
    }
    if (cv < 99)
    {
      update0099(conn, progressmonitor);
    }
    if (cv < 100)
    {
      update0100(conn, progressmonitor);
    }
    if (cv < 101)
    {
      update0101(conn, progressmonitor);
    }
    if (cv < 102)
    {
      update0102(conn, progressmonitor);
    }
    if (cv < 103)
    {
      update0103(conn, progressmonitor);
    }
    if (cv < 104)
    {
      update0104(conn, progressmonitor);
    }
    if (cv < 105)
    {
      update0105(conn, progressmonitor);
    }
    if (cv < 106)
    {
      update0106(conn, progressmonitor);
    }
    if (cv < 107)
    {
      update0107(conn, progressmonitor);
    }
    if (cv < 108)
    {
      update0108(conn, progressmonitor);
    }
    if (cv < 109)
    {
      update0109(conn, progressmonitor);
    }
    if (cv < 110)
    {
      update0110(conn, progressmonitor);
    }
    if (cv < 111)
    {
      update0111(conn, progressmonitor);
    }
    if (cv < 112)
    {
      update0112(conn, progressmonitor);
    }
    if (cv < 113)
    {
      update0113(conn, progressmonitor);
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
      ResultSet rs = stmt.executeQuery("SELECT version FROM version WHERE id = 1");
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
        stmt.executeUpdate("INSERT INTO version VALUES (1, " + newVersion + ")");
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
      String logstring, int version) throws ApplicationException
  {
    Logger.info("TODO: " + logstring);
    String driver = JVereinDBService.SETTINGS.getString("database.driver", null);
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
      setNewVersion(version);
    }
    catch (Exception e)
    {
      Logger.error("unable to execute update", e);
      throw new ApplicationException(
          i18n.tr("Fehler beim Ausführen des Updates"), e);
    }
  }

  private void install(Connection conn, ProgressMonitor progressmonitor)
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
    sb.append("ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id) DEFERRABLE;\n");
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
    sb.append("ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
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
    sb.append("ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE eigenschaften(");
    sb.append("id IDENTITY,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("eigenschaft VARCHAR(50) NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);\n");
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
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;\n");
    sb.append("ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE DEFERRABLE;\n");
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
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id) DEFERRABLE;\n");
    sb.append("ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id) DEFERRABLE;\n");
    sb.append("CREATE TABLE anfangsbestand (");
    sb.append("id IDENTITY,");
    sb.append("konto INTEGER,");
    sb.append("datum DATE,");
    sb.append("betrag DOUBLE,");
    sb.append("UNIQUE (id),");
    sb.append("UNIQUE (konto, datum),");
    sb.append("PRIMARY KEY (id));\n");
    sb.append("ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id) DEFERRABLE;\n");
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
    sb.append(")TYPE=InnoDB;\n");
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
    sb.append("ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ;\n");
    sb.append("CREATE TABLE eigenschaften (");
    sb.append("id int(10) AUTO_INCREMENT,");
    sb.append("mitglied INTEGER NOT NULL,");
    sb.append("eigenschaft VARCHAR(50) NOT NULL,");
    sb.append("UNIQUE (id),");
    sb.append("PRIMARY KEY (id)");
    sb.append(")TYPE=InnoDB;\n");
    sb.append("CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);\n");
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
    sb.append(") TYPE=InnoDB;\n");
    sb.append("ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id);\n");
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

    execute(conn, statements, "Tabelle jahresabschluss erstellt", 15);
  }

  private void update0016(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Tabelle formular erstellt", 16);
  }

  private void update0017(Connection conn, ProgressMonitor progressmonitor)
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
    sb.append(") TYPE=InnoDB;\n");
    sb.append("ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle formularfeld erstellt", 17);
  }

  private void update0018(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung alter column  kommentar varchar(1000);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung modify column  kommentar varchar(1000);\n");

    execute(conn, statements, "Spalte kommentar der Tabelle buchung ängert", 18);
  }

  private void update0019(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte fontstyle zur Tabelle formularfeld hinzugefügt", 19);
  }

  private void update0020(Connection conn, ProgressMonitor progressmonitor)
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
    sb.append(") TYPE=InnoDB;\n");
    sb.append("ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE RESTRICT;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle spendenbescheinigung erstellt", 20);
  }

  private void update0021(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE UNIQUE INDEX ixFormular1 ON formular(bezeichnung);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE UNIQUE INDEX ixFormular1 ON formular(bezeichnung);\n");

    execute(conn, statements, "Index für Tabelle formular erstellt", 21);
  }

  private void update0022(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Tabelle abrechnung erstellt", 22);
  }

  private void update0023(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE INDEX ixAbrechnung1 ON abrechnung(mitglied);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE INDEX ixAbrechnung1 ON abrechnung(mitglied);\n");

    execute(conn, statements, "Index für Tabelle abrechnung erstellt", 23);
  }

  private void update0024(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(
        DBSupportH2Impl.class.getName(),
        "ALTER TABLE abrechnung ADD CONSTRAINT fkAbrechnung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE RESTRICT;\n");

    // Update fuer MySQL
    statements.put(
        DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE abrechnung ADD CONSTRAINT fkAbrechnung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE RESTRICT;\n");

    execute(conn, statements, "Foreign Key für Tabelle abrechnung erstellt", 24);
  }

  private void update0025(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte altersjubilaeen in die Tabelle stammdaten eingefügt", 25);
  }

  private void update0026(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ADD adressierungszusatz varchar(40) before strasse;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied ADD adressierungszusatz varchar(40) after vorname;\n");

    execute(conn, statements,
        "Spalte adressierungszusatz in die Tabelle mitglied eingefügt", 26);
  }

  private void update0027(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(
        DBSupportH2Impl.class.getName(),
        "UPDATE mitglied SET adressierungszusatz = '' WHERE adressierungszusatz is null;\n");

    // Update fuer MySQL
    statements.put(
        DBSupportMySqlImpl.class.getName(),
        "UPDATE mitglied SET adressierungszusatz = '' WHERE adressierungszusatz is null;\n");

    execute(
        conn,
        statements,
        "Spalte adressierungszusatz in der Tabelle auf '' (Leerstring) gesetzt",
        27);
  }

  private void update0028(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Tabelle einstellung erstellt", 28);
  }

  private void update0029(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte auszugsnummer in die Tabelle buchung eingefügt", 29);
  }

  private void update0030(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte blattnummer in die Tabelle buchung eingefügt", 30);
  }

  private void update0031(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte telefonprivat in der Tabelle mitglied ängert", 31);
  }

  private void update0032(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte telefondienstlich in der Tabelle mitglied ängert", 32);
  }

  private void update0033(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN handy varchar(20);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN handy varchar(20);\n");

    execute(conn, statements, "Spalte handy in der Tabelle mitglied ängert", 33);
  }

  private void update0034(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE spendenbescheinigung ADD ersatzaufwendungen char(5);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE spendenbescheinigung ADD ersatzaufwendungen char(5);\n");

    execute(
        conn,
        statements,
        "Spalte ersatzaufwendungen in die Tabelle spendenbescheinigung aufgenommen",
        34);
  }

  private void update0035(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Tabelle report aufgenommen", 35);
  }

  private void update0036(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Tabelle report gelöscht", 36);
  }

  private void update0037(Connection conn, ProgressMonitor progressmonitor)
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
    sb.append(") TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle lehrgangsart erstellt", 37);
  }

  private void update0038(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte lehrgaenge in die Tabelle lehrgangsart aufgenommen", 38);
  }

  private void update0039(Connection conn, ProgressMonitor progressmonitor)
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
    sb.append(") TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle lehrgang aufgenommen", 39);
  }

  private void update0040(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang aufgenommen",
        40);
  }

  private void update0041(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang aufgenommen",
        41);
  }

  private void update0042(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang entfernt", 42);
  }

  private void update0043(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang entfernt", 43);
  }

  private void update0044(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang eingerichtet",
        44);
  }

  private void update0045(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang eingerichtet",
        45);
  }

  private void update0046(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Spalte inhalt der Tabelle formular ängert", 46);
  }

  private void update0047(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte juristischepersonen in die Tabelle einstellung aufgenommen", 47);
  }

  private void update0048(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte personenart in die Tabelle mitglied aufgenommen", 48);
  }

  private void update0049(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Alle Mitglieder auf personenart 'n' gesetzt", 49);
  }

  private void update0050(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Spalte vorname in der Tabelle mitglied ängert.",
        50);
  }

  private void update0051(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Foreign Key der Tabelle zusatzfelder entfernt",
        51);
  }

  private void update0052(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Foreign Key der Tabelle zusatzfelder eingerichtet", 52);
  }

  private void update0053(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(
        conn,
        statements,
        "Spalte aktuellegeburtstagevorher in die Tabelle einstellung aufgenommen",
        53);
  }

  private void update0054(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(
        conn,
        statements,
        "Spalte aktuellegeburtstagenachher in die Tabelle einstellung aufgenommen",
        54);
  }

  private void update0055(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Tabelle buchungsklasse aufgenommen", 55);
  }

  private void update0056(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte buchungsklasse in die Tabelle buchungsart aufgenommen, Index zur Tabelle buchungsart aufgenommen",
        56);
  }

  private void update0057(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Foreign Key zur Tabelle buchungsart hinzugefügt", 57);
  }

  private void update0058(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte updateinterval zur Tabelle einstellung hinzugefügt", 58);
  }

  private void update0059(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte updatediaginfos zur Tabelle einstellung hinzugefügt", 59);
  }

  private void update0060(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte updatelastcheck zur Tabelle einstellung hinzugefügt", 60);
  }

  private void update0061(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte betrag der Tabelle anfangsbestand geändert", 61);
  }

  private void update0062(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte smtp_server in die Tabelle einstellung aufgenommen", 62);
  }

  private void update0063(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte smtp_port in die Tabelle einstellung aufgenommen", 63);
  }

  private void update0064(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte smtp_auth_user in die Tabelle einstellung aufgenommen", 64);
  }

  private void update0065(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte smtp_auth_pwd in die Tabelle einstellung aufgenommen", 65);
  }

  private void update0066(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte smtp_from_address in die Tabelle einstellung aufgenommen", 66);
  }

  private void update0067(Connection conn, ProgressMonitor progressmonitor)
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
        "Spalte smtp_ssl in die Tabelle einstellung aufgenommen", 67);
  }

  private void update0068(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Tabelle eigenschaftgruppe aufgenommen", 68);
  }

  private void update0069(Connection conn, ProgressMonitor progressmonitor)
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
    sb.append(") TYPE=InnoDB;\n");
    sb.append("ALTER TABLE eigenschaft ADD CONSTRAINT fkEigenschaft1 FOREIGN KEY (eigenschaftgruppe) REFERENCES eigenschaftgruppe (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle eigenschaftgruppe aufgenommen", 69);
  }

  private void update0070(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN anrede varchar(40);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN anrede varchar(40);\n");

    execute(conn, statements, "Spalte anrede in der Tabelle mitglied ängert",
        70);
  }

  private void update0071(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mitglied ALTER COLUMN titel varchar(40);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mitglied MODIFY COLUMN titel varchar(40);\n");

    execute(conn, statements, "Spalte anrede in der Tabelle mitglied ängert",
        71);
  }

  private void update0072(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    try
    {
      List<String> eigenschaften = new ArrayList<String>();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select eigenschaft from eigenschaften group by eigenschaft order by eigenschaft");
      while (rs.next())
      {
        eigenschaften.add(rs.getString(1));
      }
      rs.close();
      stmt.close();
      PreparedStatement pstmt = conn.prepareStatement("INSERT INTO eigenschaft (bezeichnung) values (?)");
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
    setNewVersion(72);
  }

  private void update0073(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    try
    {
      Map<String, String> eigenschaften = new HashMap<String, String>();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select id, bezeichnung from eigenschaft");
      while (rs.next())
      {
        eigenschaften.put(rs.getString(1), rs.getString(2));
      }
      rs.close();
      stmt.close();
      PreparedStatement pstmt = conn.prepareStatement("UPDATE eigenschaften SET eigenschaft = ? WHERE eigenschaft = ?");
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
    setNewVersion(73);
  }

  private void update0074(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table eigenschaften alter column  eigenschaft integer not null;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table eigenschaften modify column  eigenschaft integer not null;\n");

    execute(conn, statements,
        "Typ der Spalte eigenschaft der Tabelle eigenschaften verändert", 74);
  }

  private void update0075(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "INSERT INTO eigenschaftgruppe (id, bezeichnung) values ('1', 'keine Zuordnung');";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Default-Wert in die Tabelle eigenschaftgruppe eingetragen", 75);
  }

  private void update0076(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "UPDATE eigenschaft SET eigenschaftgruppe = '1' WHERE eigenschaftgruppe IS NULL;";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Default-Wert in die Tabelle eigenschaft eingetragen", 76);
  }

  private void update0077(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaft1 ON eigenschaft(bezeichnung);\n");

    execute(conn, statements, "Index für Tabelle eigenschaft erstellt", 77);
  }

  private void update0078(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaftGruppe1 ON eigenschaftgruppe(bezeichnung);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE UNIQUE INDEX ixEigenschaftGruppe1 ON eigenschaftgruppe(bezeichnung);\n");

    execute(conn, statements, "Index für Tabelle eigenschaft erstellt", 78);
  }

  private void update0079(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Spalten aus Tabelle einstellung entfernt", 79);
  }

  private void update0080(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte datentyp in die Tabelle felddefinition aufgenommen", 80);
  }

  private void update0081(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        81);
  }

  private void update0082(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        82);
  }

  private void update0083(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        83);
  }

  private void update0084(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        84);
  }

  private void update0085(Connection conn, ProgressMonitor progressmonitor)
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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        85);
  }

  private void update0086(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Default-Zahlungsweg in die Tabelle einstellung aufgenommen", 86);
  }

  private void update0087(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Default-Zahlungsrhytmus in die Tabelle einstellung aufgenommen", 87);
  }

  private void update0088(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailvorlage (");
    sb.append(" id IDENTITY, ");
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
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mailvorlage erstellt", 88);
  }

  private void update0089(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mail (");
    sb.append(" id IDENTITY, ");
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
    sb.append(") TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mail erstellt", 89);
  }

  private void update0090(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailempfaenger (");
    sb.append(" id IDENTITY, ");
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
    sb.append(") TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, "Tabelle mailempfaenger erstellt", 90);
  }

  private void update0091(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key 1 für mailempfaenger erstellt", 91);
  }

  private void update0092(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key 2 für mailempfaenger erstellt", 92);
  }

  private void update0093(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mailanhang (");
    sb.append("  id IDENTITY,");
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
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mailanhang erstellt", 93);
  }

  private void update0094(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key 1 für mailempfaenger erstellt", 94);
  }

  private void update0095(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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
    execute(conn, statements, "Fehlerhafte Eigenschaften entfernt", 95);

    statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE  DEFERRABLE;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("ALTER TABLE eigenschaften ADD CONSTRAINT fkEigenschaften1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) on delete cascade;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Foreign Key 1 für eigenschaften erstellt", 95);
  }

  private void update0096(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE abrechnungslauf (");
    sb.append(" id IDENTITY, ");
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
    sb.append(") TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle abrechnungslauf erstellt", 96);
  }

  private void update0097(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedskonto (");
    sb.append(" id IDENTITY, ");
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
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mitgliedskonto erstellt", 97);
  }

  private void update0098(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key 1 für mitgliedskonto erstellt", 98);
  }

  private void update0099(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key 2 für mitgliedskonto erstellt", 99);
  }

  private void update0100(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(
        conn,
        statements,
        "Spalte mitgliedskontistzahlung in die Tabelle einstellung aufgenommen",
        100);
  }

  private void update0101(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung add mitgliedskonto integer;\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung add mitgliedskonto integer;\n");

    execute(conn, statements,
        "Spalte mitgliedskonto zur Tabelle buchung hinzugefügt", 101);
  }

  private void update0102(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle buchung aufgenommen",
        102);
  }

  private void update0103(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung add abrechnungslauf integer;\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung add abrechnungslauf integer;\n");

    execute(conn, statements,
        "Spalte abrechnungslauf zur Tabelle buchung hinzugefügt", 103);
  }

  private void update0104(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle buchung aufgenommen",
        104);
  }

  private void update0105(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte manuellezahlungen in die Tabelle einstellung aufgenommen", 105);
  }

  private void update0106(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte rechnungen13 in die Tabelle einstellung aufgenommen", 106);
  }

  private void update0107(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte rechnungtextabbuchung in die Tabelle einstellung aufgenommen",
        107);
  }

  private void update0108(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(
        conn,
        statements,
        "Spalte rechnungtextueberweisung in die Tabelle einstellung aufgenommen",
        108);
  }

  private void update0109(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements,
        "Spalte rechnungtextbar in die Tabelle einstellung aufgenommen", 109);
  }

  private void update0110(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle buchung erneuert ", 110);
  }

  private void update0111(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchungsart alter column  bezeichnung varchar(50);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchungsart modify column  bezeichnung varchar(50);\n");

    execute(conn, statements,
        "Spalte bezeichnung der Tabelle buchungsart verlängert", 111);
  }

  private void update0112(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedfoto (");
    sb.append(" id IDENTITY,");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" foto BLOB,");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (mitglied),");
    sb.append(" PRIMARY KEY (id));\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());

    // Update fuer MySQL
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedfoto (");
    sb.append(" id INTEGER AUTO_INCREMENT,");
    sb.append(" mitglied INTEGER NOT NULL,");
    sb.append(" foto BLOB,");
    sb.append(" UNIQUE (id),");
    sb.append(" UNIQUE (mitglied),");
    sb.append(" PRIMARY KEY (id)");
    sb.append(" ) TYPE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mitgliedfoto erstellt", 112);
  }

  private void update0113(Connection conn, ProgressMonitor progressmonitor)
      throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle mitgliedfoto angelegt ",
        113);
  }

}
