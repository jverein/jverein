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
      install(conn);
    }
    if (cv < 16)
    {
      update0016(conn);
    }
    if (cv < 17)
    {
      update0017(conn);
    }
    if (cv < 18)
    {
      update0018(conn);
    }
    if (cv < 19)
    {
      update0019(conn);
    }
    if (cv < 20)
    {
      update0020(conn);
    }
    if (cv < 21)
    {
      update0021(conn);
    }
    if (cv < 22)
    {
      update0022(conn);
    }
    if (cv < 23)
    {
      update0023(conn);
    }
    if (cv < 24)
    {
      update0024(conn);
    }
    if (cv < 25)
    {
      update0025(conn);
    }
    if (cv < 26)
    {
      update0026(conn);
    }
    if (cv < 27)
    {
      update0027(conn);
    }
    if (cv < 28)
    {
      update0028(conn);
    }
    if (cv < 29)
    {
      update0029(conn);
    }
    if (cv < 30)
    {
      update0030(conn);
    }
    if (cv < 31)
    {
      update0031(conn);
    }
    if (cv < 32)
    {
      update0032(conn);
    }
    if (cv < 33)
    {
      update0033(conn);
    }
    if (cv < 34)
    {
      update0034(conn);
    }
    if (cv < 35)
    {
      update0035(conn);
    }
    if (cv < 36)
    {
      update0036(conn);
    }
    if (cv < 37)
    {
      update0037(conn);
    }
    if (cv < 38)
    {
      update0038(conn);
    }
    if (cv < 39)
    {
      update0039(conn);
    }
    if (cv < 40)
    {
      update0040(conn);
    }
    if (cv < 41)
    {
      update0041(conn);
    }
    if (cv < 42)
    {
      update0042(conn);
    }
    if (cv < 43)
    {
      update0043(conn);
    }
    if (cv < 44)
    {
      update0044(conn);
    }
    if (cv < 45)
    {
      update0045(conn);
    }
    if (cv < 46)
    {
      update0046(conn);
    }
    if (cv < 47)
    {
      update0047(conn);
    }
    if (cv < 48)
    {
      update0048(conn);
    }
    if (cv < 49)
    {
      update0049(conn);
    }
    if (cv < 50)
    {
      update0050(conn);
    }
    if (cv < 51)
    {
      update0051(conn);
    }
    if (cv < 52)
    {
      update0052(conn);
    }
    if (cv < 53)
    {
      update0053(conn);
    }
    if (cv < 54)
    {
      update0054(conn);
    }
    if (cv < 55)
    {
      update0055(conn);
    }
    if (cv < 56)
    {
      update0056(conn);
    }
    if (cv < 57)
    {
      update0057(conn);
    }
    if (cv < 58)
    {
      update0058(conn);
    }
    if (cv < 59)
    {
      update0059(conn);
    }
    if (cv < 60)
    {
      update0060(conn);
    }
    if (cv < 61)
    {
      update0061(conn);
    }
    if (cv < 62)
    {
      update0062(conn);
    }
    if (cv < 63)
    {
      update0063(conn);
    }
    if (cv < 64)
    {
      update0064(conn);
    }
    if (cv < 65)
    {
      update0065(conn);
    }
    if (cv < 66)
    {
      update0066(conn);
    }
    if (cv < 67)
    {
      update0067(conn);
    }
    if (cv < 68)
    {
      update0068(conn);
    }
    if (cv < 69)
    {
      update0069(conn);
    }
    if (cv < 70)
    {
      update0070(conn);
    }
    if (cv < 71)
    {
      update0071(conn);
    }
    if (cv < 72)
    {
      update0072(conn);
    }
    if (cv < 73)
    {
      update0073(conn);
    }
    if (cv < 74)
    {
      update0074(conn);
    }
    if (cv < 75)
    {
      update0075(conn);
    }
    if (cv < 76)
    {
      update0076(conn);
    }
    if (cv < 77)
    {
      update0077(conn);
    }
    if (cv < 78)
    {
      update0078(conn);
    }
    if (cv < 79)
    {
      update0079(conn);
    }
    if (cv < 80)
    {
      update0080(conn);
    }
    if (cv < 81)
    {
      update0081(conn);
    }
    if (cv < 82)
    {
      update0082(conn);
    }
    if (cv < 83)
    {
      update0083(conn);
    }
    if (cv < 84)
    {
      update0084(conn);
    }
    if (cv < 85)
    {
      update0085(conn);
    }
    if (cv < 86)
    {
      update0086(conn);
    }
    if (cv < 87)
    {
      update0087(conn);
    }
    if (cv < 88)
    {
      update0088(conn);
    }
    if (cv < 89)
    {
      update0089(conn);
    }
    if (cv < 90)
    {
      update0090(conn);
    }
    if (cv < 91)
    {
      update0091(conn);
    }
    if (cv < 92)
    {
      update0092(conn);
    }
    if (cv < 93)
    {
      update0093(conn);
    }
    if (cv < 94)
    {
      update0094(conn);
    }
    if (cv < 95)
    {
      update0095(conn);
    }
    if (cv < 96)
    {
      update0096(conn);
    }
    if (cv < 97)
    {
      update0097(conn);
    }
    if (cv < 98)
    {
      update0098(conn);
    }
    if (cv < 99)
    {
      update0099(conn);
    }
    if (cv < 100)
    {
      update0100(conn);
    }
    if (cv < 101)
    {
      update0101(conn);
    }
    if (cv < 102)
    {
      update0102(conn);
    }
    if (cv < 103)
    {
      update0103(conn);
    }
    if (cv < 104)
    {
      update0104(conn);
    }
    if (cv < 105)
    {
      update0105(conn);
    }
    if (cv < 106)
    {
      update0106(conn);
    }
    if (cv < 107)
    {
      update0107(conn);
    }
    if (cv < 108)
    {
      update0108(conn);
    }
    if (cv < 109)
    {
      update0109(conn);
    }
    if (cv < 110)
    {
      update0110(conn);
    }
    if (cv < 111)
    {
      update0111(conn);
    }
    if (cv < 112)
    {
      update0112(conn);
    }
    if (cv < 113)
    {
      update0113(conn);
    }
    if (cv < 114)
    {
      update0114(conn);
    }
    if (cv < 115)
    {
      update0115(conn);
    }
    if (cv < 116)
    {
      update0116(conn);
    }
    if (cv < 117)
    {
      update0117(conn);
    }
    if (cv < 118)
    {
      update0118(conn);
    }
    if (cv < 119)
    {
      update0119(conn);
    }
    if (cv < 120)
    {
      update0120(conn);
    }
    if (cv < 121)
    {
      update0121(conn);
    }
    if (cv < 122)
    {
      update0122(conn);
    }
    if (cv < 123)
    {
      update0123(conn);
    }
    if (cv < 124)
    {
      update0124(conn);
    }
    if (cv < 125)
    {
      update0125(conn);
    }
    if (cv < 126)
    {
      update0126(conn);
    }
    if (cv < 127)
    {
      update0127(conn);
    }
    if (cv < 128)
    {
      update0128(conn);
    }
    if (cv < 129)
    {
      update0129(conn);
    }
    if (cv < 130)
    {
      update0130(conn);
    }
    if (cv < 131)
    {
      update0131(conn);
    }
    if (cv < 132)
    {
      update0132(conn);
    }
    if (cv < 133)
    {
      update0133(conn);
    }
    if (cv < 134)
    {
      update0134(conn);
    }
    if (cv < 135)
    {
      update0135(conn);
    }
    if (cv < 136)
    {
      update0136(conn);
    }
    if (cv < 137)
    {
      update0137(conn);
    }
    if (cv < 138)
    {
      update0138(conn);
    }
    if (cv < 139)
    {
      update0139(conn);
    }
    if (cv < 140)
    {
      update0140(conn);
    }
    if (cv < 141)
    {
      update0141(conn);
    }
    if (cv < 142)
    {
      update0142(conn);
    }
    if (cv < 143)
    {
      update0143(conn);
    }
    if (cv < 144)
    {
      update0144(conn);
    }
    if (cv < 145)
    {
      update0145(conn);
    }
    if (cv < 146)
    {
      update0146(conn);
    }
    if (cv < 147)
    {
      update0147(conn);
    }
    if (cv < 148)
    {
      update0148(conn);
    }
    if (cv < 149)
    {
      update0149(conn);
    }
    if (cv < 150)
    {
      update0150(conn);
    }
    if (cv < 151)
    {
      update0151(conn);
    }
    if (cv < 152)
    {
      update0152(conn);
    }
    if (cv < 153)
    {
      update0153(conn);
    }
    if (cv < 154)
    {
      update0154(conn);
    }
    if (cv < 155)
    {
      update0155(conn);
    }
    if (cv < 156)
    {
      update0156(conn);
    }
    if (cv < 157)
    {
      update0157(conn);
    }
    if (cv < 158)
    {
      update0158(conn);
    }
    if (cv < 159)
    {
      update0159(conn);
    }
    if (cv < 160)
    {
      update0160(conn);
    }
    if (cv < 161)
    {
      update0161(conn);
    }
    if (cv < 162)
    {
      update0162(conn);
    }
    if (cv < 163)
    {
      update0163(conn);
    }
    if (cv < 164)
    {
      update0164(conn);
    }
    if (cv < 165)
    {
      update0165(conn);
    }
    if (cv < 166)
    {
      update0166(conn);
    }
    if (cv < 167)
    {
      update0167(conn);
    }
    if (cv < 168)
    {
      update0168(conn);
    }
    if (cv < 169)
    {
      update0169(conn);
    }
    if (cv < 170)
    {
      update0170(conn);
    }
    if (cv < 171)
    {
      update0171(conn);
    }
    if (cv < 172)
    {
      update0172(conn);
    }
    if (cv < 173)
    {
      update0173(conn);
    }
    if (cv < 174)
    {
      update0174(conn);
    }
    if (cv < 175)
    {
      update0175(conn);
    }
    if (cv < 176)
    {
      update0176(conn);
    }
    if (cv < 177)
    {
      update0177(conn);
    }
    if (cv < 178)
    {
      update0178(conn);
    }
    if (cv < 179)
    {
      update0179(conn);
    }
    if (cv < 180)
    {
      update0180(conn);
    }
    if (cv < 181)
    {
      update0181(conn);
    }
    if (cv < 182)
    {
      update0182(conn);
    }
    if (cv < 183)
    {
      update0183(conn);
    }
    if (cv < 184)
    {
      update0184(conn);
    }
    if (cv < 185)
    {
      update0185(conn);
    }
    if (cv < 186)
    {
      update0186(conn);
    }
    if (cv < 187)
    {
      update0187(conn);
    }
    if (cv < 188)
    {
      update0188(conn);
    }
    if (cv < 189)
    {
      update0189(conn);
    }
    if (cv < 190)
    {
      update0190(conn);
    }
    if (cv < 191)
    {
      update0191(conn);
    }
    if (cv < 192)
    {
      update0192(conn);
    }
    if (cv < 193)
    {
      update0193(conn);
    }
    if (cv < 194)
    {
      update0194(conn);
    }
    if (cv < 195)
    {
      update0195(conn);
    }
    if (cv < 196)
    {
      update0196(conn);
    }
  }

  public Connection getConnection()
  {
    return conn;
  }

  public int getCurrentVersion()
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
      Logger.error(
          JVereinPlugin.getI18n().tr(
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
    String driver = JVereinDBService.SETTINGS
        .getString("database.driver", null);
    I18N i18n = JVereinPlugin.getI18n();
    String sql = statements.get(driver);
    if (sql == null)
    {
      throw new ApplicationException(i18n.tr(
          "Datenbank {0} wird nicht unterstützt", driver));
    }
    try
    {
      progressmonitor.log(logstring);
      ScriptExecutor.execute(new StringReader(sql), conn, null);
      setNewVersion(version);
    }
    catch (Exception e)
    {
      Logger.error("unable to execute update", e);
      throw new ApplicationException(
          i18n.tr("Fehler beim Ausführen des Updates"), e);
    }
  }

  private void install(Connection conn) throws ApplicationException
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

    execute(conn, statements, "Tabelle jahresabschluss erstellt", 15);
  }

  private void update0016(Connection conn) throws ApplicationException
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
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle formular erstellt", 16);
  }

  private void update0017(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle formularfeld erstellt", 17);
  }

  private void update0018(Connection conn) throws ApplicationException
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

  private void update0019(Connection conn) throws ApplicationException
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

  private void update0020(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE RESTRICT;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle spendenbescheinigung erstellt", 20);
  }

  private void update0021(Connection conn) throws ApplicationException
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

  private void update0022(Connection conn) throws ApplicationException
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
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle abrechnung erstellt", 22);
  }

  private void update0023(Connection conn) throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle abrechnung erstellt", 24);
  }

  private void update0025(Connection conn) throws ApplicationException
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

    execute(conn, statements,
        "Spalte adressierungszusatz in die Tabelle mitglied eingefügt", 26);
  }

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

    execute(
        conn,
        statements,
        "Spalte adressierungszusatz in der Tabelle auf '' (Leerstring) gesetzt",
        27);
  }

  private void update0028(Connection conn) throws ApplicationException
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
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle einstellung erstellt", 28);
  }

  private void update0029(Connection conn) throws ApplicationException
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

  private void update0030(Connection conn) throws ApplicationException
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

  private void update0031(Connection conn) throws ApplicationException
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

  private void update0032(Connection conn) throws ApplicationException
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

  private void update0033(Connection conn) throws ApplicationException
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

  private void update0034(Connection conn) throws ApplicationException
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

  private void update0035(Connection conn) throws ApplicationException
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
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle report aufgenommen", 35);
  }

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

    execute(conn, statements, "Tabelle report gelöscht", 36);
  }

  private void update0037(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle lehrgangsart erstellt", 37);
  }

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

    execute(conn, statements,
        "Spalte lehrgaenge in die Tabelle lehrgangsart aufgenommen", 38);
  }

  private void update0039(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle lehrgang aufgenommen", 39);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang aufgenommen",
        40);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang aufgenommen",
        41);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang entfernt", 42);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang entfernt", 43);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang eingerichtet",
        44);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle lehrgang eingerichtet",
        45);
  }

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

    execute(conn, statements, "Spalte inhalt der Tabelle formular ängert", 46);
  }

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

    execute(conn, statements,
        "Spalte juristischepersonen in die Tabelle einstellung aufgenommen", 47);
  }

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

    execute(conn, statements,
        "Spalte personenart in die Tabelle mitglied aufgenommen", 48);
  }

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

    execute(conn, statements, "Alle Mitglieder auf personenart 'n' gesetzt", 49);
  }

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

    execute(conn, statements, "Spalte vorname in der Tabelle mitglied ängert.",
        50);
  }

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

    execute(conn, statements, "Foreign Key der Tabelle zusatzfelder entfernt",
        51);
  }

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

    execute(conn, statements,
        "Foreign Key der Tabelle zusatzfelder eingerichtet", 52);
  }

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

    execute(
        conn,
        statements,
        "Spalte aktuellegeburtstagevorher in die Tabelle einstellung aufgenommen",
        53);
  }

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

    execute(
        conn,
        statements,
        "Spalte aktuellegeburtstagenachher in die Tabelle einstellung aufgenommen",
        54);
  }

  private void update0055(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle buchungsklasse aufgenommen", 55);
  }

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

    execute(
        conn,
        statements,
        "Spalte buchungsklasse in die Tabelle buchungsart aufgenommen, Index zur Tabelle buchungsart aufgenommen",
        56);
  }

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

    execute(conn, statements,
        "Foreign Key zur Tabelle buchungsart hinzugefügt", 57);
  }

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

    execute(conn, statements,
        "Spalte updateinterval zur Tabelle einstellung hinzugefügt", 58);
  }

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

    execute(conn, statements,
        "Spalte updatediaginfos zur Tabelle einstellung hinzugefügt", 59);
  }

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

    execute(conn, statements,
        "Spalte updatelastcheck zur Tabelle einstellung hinzugefügt", 60);
  }

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

    execute(conn, statements,
        "Spalte betrag der Tabelle anfangsbestand geändert", 61);
  }

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

    execute(conn, statements,
        "Spalte smtp_server in die Tabelle einstellung aufgenommen", 62);
  }

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

    execute(conn, statements,
        "Spalte smtp_port in die Tabelle einstellung aufgenommen", 63);
  }

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

    execute(conn, statements,
        "Spalte smtp_auth_user in die Tabelle einstellung aufgenommen", 64);
  }

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

    execute(conn, statements,
        "Spalte smtp_auth_pwd in die Tabelle einstellung aufgenommen", 65);
  }

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

    execute(conn, statements,
        "Spalte smtp_from_address in die Tabelle einstellung aufgenommen", 66);
  }

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

    execute(conn, statements,
        "Spalte smtp_ssl in die Tabelle einstellung aufgenommen", 67);
  }

  private void update0068(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle eigenschaftgruppe aufgenommen", 68);
  }

  private void update0069(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    sb.append("ALTER TABLE eigenschaft ADD CONSTRAINT fkEigenschaft1 FOREIGN KEY (eigenschaftgruppe) REFERENCES eigenschaftgruppe (id);\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle eigenschaftgruppe aufgenommen", 69);
  }

  private void update0070(Connection conn) throws ApplicationException
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

  private void update0071(Connection conn) throws ApplicationException
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
    setNewVersion(72);
  }

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
    setNewVersion(73);
  }

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

    execute(conn, statements,
        "Typ der Spalte eigenschaft der Tabelle eigenschaften verändert", 74);
  }

  private void update0075(Connection conn) throws ApplicationException
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

  private void update0076(Connection conn) throws ApplicationException
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

  private void update0077(Connection conn) throws ApplicationException
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

    execute(conn, statements, "Index für Tabelle eigenschaft erstellt", 78);
  }

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

    execute(conn, statements, "Spalten aus Tabelle einstellung entfernt", 79);
  }

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

    execute(conn, statements,
        "Spalte datentyp in die Tabelle felddefinition aufgenommen", 80);
  }

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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        81);
  }

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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        82);
  }

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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        83);
  }

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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        84);
  }

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

    execute(conn, statements, "Div. Datentypen f. d. Zusatzfelder aufgenommen",
        85);
  }

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

    execute(conn, statements,
        "Default-Zahlungsweg in die Tabelle einstellung aufgenommen", 86);
  }

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

    execute(conn, statements,
        "Default-Zahlungsrhytmus in die Tabelle einstellung aufgenommen", 87);
  }

  private void update0088(Connection conn) throws ApplicationException
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
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mailvorlage erstellt", 88);
  }

  private void update0089(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mail erstellt", 89);
  }

  private void update0090(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, "Tabelle mailempfaenger erstellt", 90);
  }

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

    execute(conn, statements, "Foreign Key 1 für mailempfaenger erstellt", 91);
  }

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

    execute(conn, statements, "Foreign Key 2 für mailempfaenger erstellt", 92);
  }

  private void update0093(Connection conn) throws ApplicationException
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
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mailanhang erstellt", 93);
  }

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

    execute(conn, statements, "Foreign Key 1 für mailempfaenger erstellt", 94);
  }

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

  private void update0096(Connection conn) throws ApplicationException
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
    sb.append(")  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle abrechnungslauf erstellt", 96);
  }

  private void update0097(Connection conn) throws ApplicationException
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
    sb.append(" )  ENGINE=InnoDB;\n");
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements, "Tabelle mitgliedskonto erstellt", 97);
  }

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

    execute(conn, statements, "Foreign Key 1 für mitgliedskonto erstellt", 98);
  }

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

    execute(conn, statements, "Foreign Key 2 für mitgliedskonto erstellt", 99);
  }

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

    execute(
        conn,
        statements,
        "Spalte mitgliedskontistzahlung in die Tabelle einstellung aufgenommen",
        100);
  }

  private void update0101(Connection conn) throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle buchung aufgenommen",
        102);
  }

  private void update0103(Connection conn) throws ApplicationException
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

    execute(conn, statements, "Foreign Key für Tabelle buchung aufgenommen",
        104);
  }

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

    execute(conn, statements,
        "Spalte manuellezahlungen in die Tabelle einstellung aufgenommen", 105);
  }

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

    execute(conn, statements,
        "Spalte rechnungen13 in die Tabelle einstellung aufgenommen", 106);
  }

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

    execute(conn, statements,
        "Spalte rechnungtextabbuchung in die Tabelle einstellung aufgenommen",
        107);
  }

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

    execute(
        conn,
        statements,
        "Spalte rechnungtextueberweisung in die Tabelle einstellung aufgenommen",
        108);
  }

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

    execute(conn, statements,
        "Spalte rechnungtextbar in die Tabelle einstellung aufgenommen", 109);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle buchung erneuert ", 110);
  }

  private void update0111(Connection conn) throws ApplicationException
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

  private void update0112(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE mitgliedfoto (");
    sb.append(" id IDENTITY,");
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

    execute(conn, statements, "Tabelle mitgliedfoto erstellt", 112);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle mitgliedfoto angelegt ",
        113);
  }

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

    execute(conn, statements,
        "Spalte mitgliedfoto in die Tabelle einstellung aufgenommen", 114);
  }

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

    execute(conn, statements,
        "Spalte pflicht in die Tabelle eigenschaftgruppe aufgenommen", 115);
  }

  private void update0116(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mail alter column  txt varchar(10000);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mail modify column  txt varchar(10000);\n");

    execute(conn, statements, "Spalte txt der Tabelle mail geändert", 116);
  }

  private void update0117(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mailvorlage alter column  txt varchar(10000);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mailvorlage modify column  txt varchar(10000);\n");

    execute(conn, statements, "Spalte txt der Tabelle mailvorlage geändert",
        117);
  }

  private void update0118(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table konto alter column  bezeichnung varchar(255);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table konto modify column  bezeichnung varchar(255);\n");

    execute(conn, statements, "Spalte bezeichnung der Tabelle konto geängert",
        118);
  }

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

    execute(conn, statements,
        "Spalte auslandsadressen in die Tabelle einstellung aufgenommen", 119);
  }

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

    execute(conn, statements,
        "Spalte staat in die Tabelle mitglied aufgenommen", 120);
  }

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

    execute(conn, statements,
        "Spalte sterbetag in die Tabelle mitglied aufgenommen", 121);
  }

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

    execute(conn, statements,
        "Spalte rechnungen13 aus der Tabelle einstellung entfernt", 122);
  }

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

    execute(conn, statements,
        "Spalte manuellezahlungen aus der Tabelle einstellung entfernt", 123);
  }

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

    execute(conn, statements,
        "Spalte rechnungfuerabbuchung aus der Tabelle einstellung entfernt",
        124);
  }

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

    execute(conn, statements,
        "Spalte rechnungfuerueberweisung aus der Tabelle einstellung entfernt",
        125);
  }

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

    execute(conn, statements,
        "Spalte rechnungfuerbarzahlung aus der Tabelle einstellung entfernt",
        126);
  }

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

    execute(conn, statements, "Tabelle abrechnung gelöscht", 127);
  }

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

    execute(conn, statements, "Tabelle manuellerzahlungseingang gelöscht", 128);
  }

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

    execute(conn, statements,
        "Spalte arbeitseinsatz in die Tabelle einstellung aufgenommen", 129);
  }

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

    execute(
        conn,
        statements,
        "Spalte arbeitseinsatzstunden in die Tabelle beitragsgruppe aufgenommen",
        130);
  }

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

    execute(
        conn,
        statements,
        "Spalte arbeitseinsatzbetrag in die Tabelle beitragsgruppe aufgenommen",
        131);
  }

  private void update0132(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table arbeitseinsatz (");
    sb.append(" id IDENTITY,");
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

    execute(conn, statements, "Tabelle arbeitseinsatz aufgenommen", 132);
  }

  private void update0133(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table formularfeld alter column  name varchar(30);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table formularfeld modify column  name varchar(30);\n");

    execute(conn, statements,
        "Spalte name der Tabelle formularfeld verlängert", 133);
  }

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

    execute(conn, statements,
        "Spalte max1 in die Tabelle eigenschaftgruppe aufgenommen", 134);
  }

  private void update0135(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table buchungdokument (");
    sb.append(" id IDENTITY,");
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

    execute(conn, statements, "Tabelle buchungdokument aufgenommen", 135);
  }

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
    execute(conn, statements, "Tabelle buchungdokument gelöscht", 136);
  }

  private void update0137(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table buchungdokument (");
    sb.append(" id IDENTITY,");
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

    execute(conn, statements, "Tabelle buchungdokument aufgenommen", 137);
  }

  private void update0138(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("create table mitglieddokument (");
    sb.append(" id IDENTITY,");
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

    execute(conn, statements, "Tabelle mitglieddokument aufgenommen", 138);
  }

  private void update0139(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table buchung add splitid integer;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table buchung add splitid integer;\n");

    execute(conn, statements, "Spalte splitid zur Tabelle buchung hinzugefügt",
        139);
  }

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

    execute(conn, statements,
        "Spalte dokumentenspeicherung in die Tabelle einstellung aufgenommen",
        140);
  }

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

    execute(conn, statements,
        "Spalte name in die Tabelle einstellung aufgenommen", 141);
  }

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

    execute(conn, statements,
        "Spalte blz in die Tabelle einstellung aufgenommen", 142);
  }

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

    execute(conn, statements,
        "Spalte konto in die Tabelle einstellung aufgenommen", 143);
  }

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

    execute(conn, statements,
        "Spalte altersgruppen in die Tabelle einstellung aufgenommen", 144);
  }

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

    execute(conn, statements,
        "Spalte jubilaeen in die Tabelle einstellung aufgenommen", 145);
  }

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

    execute(conn, statements,
        "Spalte altersjubilaeen in die Tabelle einstellung aufgenommen", 146);
  }

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

    execute(conn, statements,
        "Spalte name aus Tabelle stammdaten in Tabelle einstellung kopiert",
        147);
  }

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

    execute(conn, statements,
        "Spalte blz aus Tabelle stammdaten in Tabelle einstellung kopiert", 148);
  }

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

    execute(conn, statements,
        "Spalte konto aus Tabelle stammdaten in Tabelle einstellung kopiert",
        149);
  }

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

    execute(
        conn,
        statements,
        "Spalte altersgruppen aus Tabelle stammdaten in Tabelle einstellung kopiert",
        150);
  }

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

    execute(
        conn,
        statements,
        "Spalte jubilaeen aus Tabelle stammdaten in Tabelle einstellung kopiert",
        151);
  }

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

    execute(
        conn,
        statements,
        "Spalte altersjubilaeen aus Tabelle stammdaten in Tabelle einstellung kopiert",
        152);
  }

  private void update0153(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE adresstyp (");
    sb.append("  id IDENTITY,");
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

    execute(conn, statements, "Tabelle adresstyp erstellt", 153);
  }

  private void update0154(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    sb = new StringBuilder();
    sb.append("INSERT into adresstyp VALUES (1, 'Mitglied', 1);\n");
    sb.append("INSERT into adresstyp VALUES (2, 'Spender/in', 2);\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements, "Tabelle adresstyp befüllt", 154);
  }

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

    execute(conn, statements,
        "Spalte adresstyp in die Tabelle mitglied aufgenommen", 155);
  }

  private void update0156(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    sb = new StringBuilder();
    sb.append("UPDATE mitglied set adresstyp = 1;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());
    execute(conn, statements,
        "Spalte adresstyp der Tabelle mitglied initial befüllt", 156);
  }

  private void update0157(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "CREATE INDEX ixMitglied_1 ON mitglied(adresstyp);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "CREATE INDEX ixMitglied_1 ON mitglied(adresstyp);\n");

    execute(conn, statements, "Index für Tabelle mitglied erstellt", 157);
  }

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

    execute(conn, statements, "Foreign Key für Tabelle mitglied angelegt ", 158);
  }

  private void update0159(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "alter table mitglied alter column  adresstyp integer not null;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "alter table mitglied modify column  adresstyp integer not null;\n");

    execute(conn, statements,
        "Spalte adresstyp der Tabelle mitglied verändert", 159);
  }

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

    execute(conn, statements,
        "Spalte delaytime in die Tabelle einstellung aufgenommen", 160);
  }

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

    execute(conn, statements,
        "Spalte zusatzadressen in die Tabelle einstellung aufgenommen", 161);
  }

  private void update0162(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("ALTER TABLE mitglied ADD letzteaenderung date;\n");
    statements.put(DBSupportH2Impl.class.getName(), sb.toString());
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sb.toString());

    execute(conn, statements,
        "Spalte letzteaenderung in die Tabelle mitglied aufgenommen", 162);
  }

  private void update0163(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE mailempfaenger DROP COLUMN adresse;\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE mailempfaenger DROP COLUMN adresse;\n");

    execute(conn, statements,
        "Spalte adresse aus Tabelle mailempfaenger entfernt", 163);
  }

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

    execute(conn, statements, "Tabelle stammdaten gelöscht", 164);
  }

  private void update0165(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "alter table buchungsart add spende char(5) ;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Spalte spende zur Tabelle buchungsart hinzugefügt", 165);
  }

  private void update0166(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "alter table buchung add spendenbescheinigung integer ;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Spalte spendenbescheinigung zur Tabelle buchung hinzugefügt", 166);
  }

  private void update0167(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "CREATE INDEX ixBuchung1 ON buchung(spendenbescheinigung);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements, "Index für Tabelle abrechnung erstellt", 167);
  }

  private void update0168(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "ALTER TABLE buchung ADD CONSTRAINT fkBuchung5 FOREIGN KEY (spendenbescheinigung) REFERENCES spendenbescheinigung (id);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, "Foreign Key für Tabelle buchung angelegt ", 168);
  }

  private void update0169(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "alter table spendenbescheinigung add mitglied integer ;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Spalte mitglied zur Tabelle spendenbescheinigung hinzugefügt", 169);
  }

  private void update0170(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "CREATE INDEX ixSpendenbescheinigung2 ON spendenbescheinigung(mitglied);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements,
        "Index für Tabelle spendenbescheinigung erstellt", 170);
  }

  private void update0171(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung2 FOREIGN KEY (mitglied) REFERENCES mitglied (id);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Foreign Key für Tabelle spendenbescheinigung angelegt ", 171);
  }

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

    execute(conn, statements,
        "Spalte strasse in die Tabelle einstellung aufgenommen", 172);
  }

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

    execute(conn, statements,
        "Spalte plz in die Tabelle einstellung aufgenommen", 173);
  }

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

    execute(conn, statements,
        "Spalte ort in die Tabelle einstellung aufgenommen", 174);
  }

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

    execute(conn, statements,
        "Spalte finanzamt in die Tabelle einstellung aufgenommen", 175);
  }

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

    execute(conn, statements,
        "Spalte steuernummer in die Tabelle einstellung aufgenommen", 176);
  }

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

    execute(conn, statements,
        "Spalte bescheiddatum die Tabelle einstellung aufgenommen", 177);
  }

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

    execute(conn, statements,
        "Spalte vorlaeufig in die Tabelle einstellung aufgenommen", 178);
  }

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

    execute(conn, statements,
        "Spalte beguenstigterzweck in die Tabelle einstellung aufgenommen", 179);
  }

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

    execute(conn, statements,
        "Spalte mitgliedsbeitraege in die Tabelle einstellung aufgenommen", 180);
  }

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

    execute(conn, statements,
        "Spalte vorlaeufigab die Tabelle einstellung aufgenommen", 181);
  }

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

    execute(conn, statements,
        "Spalte vorlaeufigab die Tabelle spendenbescheinigung aufgenommen", 182);
  }

  private void update0183(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE spendenbescheinigung ADD bezeichnungsachzuwendung varchar(100);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(
        conn,
        statements,
        "Spalte bezeichnungssachzuwendung in die Tabelle spendenbescheinigung aufgenommen",
        183);
  }

  private void update0184(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE spendenbescheinigung ADD herkunftspende int;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(
        conn,
        statements,
        "Spalte herkunftspende in die Tabelle spendenbescheinigung aufgenommen",
        184);
  }

  private void update0185(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE spendenbescheinigung ADD unterlagenwertermittlung char(5);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(
        conn,
        statements,
        "Spalte unterlagenwertermittlung in die Tabelle spendenbescheinigung aufgenommen",
        185);
  }

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

    execute(conn, statements, "Spalten aus Tabelle einstellung entfernt", 186);
  }

  private void update0187(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(), "-- nothing to do;\n");
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE `mail`  DROP INDEX `betreff`;\n");

    execute(conn, statements, "Index von mail entfernt (nur MySQL)", 187);
  }

  private void update0188(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    String sql = "ALTER TABLE beitragsgruppe ADD buchungsart integer;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);
    execute(conn, statements,
        "Spalte buchungsart in die Tabelle beitragsgruppe aufgenommen", 188);
  }

  private void update0189(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "ALTER TABLE beitragsgruppe ADD CONSTRAINT fkBeitragsgruppe1 FOREIGN KEY (buchungsart) REFERENCES buchungsart(id);\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Foreign Key für Tabelle beitragsgruppe angelegt ", 189);
  }

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

    execute(conn, statements,
        "Spalte smtp_startls in die Tabelle einstellung aufgenommen", 190);
  }

  private void update0191(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    statements.put(DBSupportH2Impl.class.getName(),
        "ALTER TABLE formularfeld ALTER COLUMN name varchar(60);\n");

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(),
        "ALTER TABLE formularfeld MODIFY COLUMN name varchar(60);\n");

    execute(conn, statements,
        "Spalte name in der Tabelle formularfeld geändert", 191);
  }

  private void update0192(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    String sql = "UPDATE formularfeld set name = replace(name,'.','_');\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);

    // Update fuer MySQL
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements, "Umsetzung Formularfeldnamen", 192);
  }

  private void update0193(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE auswertung (");
    sb.append(" id IDENTITY,");
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

    execute(conn, statements, "Tabelle auswertung aufgenommen", 193);
  }

  private void update0194(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();
    // Update fuer H2
    sb = new StringBuilder();
    sb.append("CREATE TABLE auswertungpos (");
    sb.append(" id IDENTITY,");
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

    execute(conn, statements, "Tabelle auswertungpos aufgenommen", 194);
  }

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

    execute(conn, statements,
        "Foreign Key für Tabelle  auswertungpos aufgenommen", 195);
  }

  private void update0196(Connection conn) throws ApplicationException
  {
    Map<String, String> statements = new HashMap<String, String>();

    String sql = "drop table auswertungpos\n;" + "drop table auswertung;\n";
    statements.put(DBSupportH2Impl.class.getName(), sql);
    statements.put(DBSupportMySqlImpl.class.getName(), sql);

    execute(conn, statements,
        "Foreign Key für Tabelle  auswertungstabellen gelöscht", 196);
  }

}
