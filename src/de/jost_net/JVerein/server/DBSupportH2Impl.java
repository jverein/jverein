/**********************************************************************
 * Kopie aus Hibiscus
 * Copyright (c) by willuhn software & services
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

import java.io.File;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.HashMap;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Implementierung des Datenbank-Supports fuer H2-Database
 * (http://www.h2database.com).
 */
public class DBSupportH2Impl extends AbstractDBSupportImpl
{

  private static final long serialVersionUID = 8429636916402991936L;

  // Mapper von Datenbank-Hash zu Versionsnummer
  private static HashMap<String, Double> DBMAPPING = new HashMap<>();

  static
  {
    DBMAPPING.put("Bw0vbcBX5SInOkfnSb+DHA==", Double.valueOf(0.9));
  }

  @SuppressWarnings("unchecked")
  public DBSupportH2Impl()
  {
    // H2-Datenbank verwendet uppercase Identifier
    Logger.info("switching dbservice to uppercase");
    System.setProperty(JVereinDBServiceImpl.class.getName() + ".uppercase",
        "true");

    try
    {
      Method m = Application.getClassLoader().load("org.h2.engine.Constants")
          .getMethod("getVersion", (Class<?>[]) null);
      Logger.info("h2 version: " + m.invoke(null, (Object[]) null));
    }
    catch (Throwable t)
    {
      Logger.warn("unable to determine h2 version");
    }

  }

  @Override
  public String getJdbcDriver()
  {
    return "org.h2.Driver";
  }

  @Override
  public String getJdbcPassword()
  {
    // Zunächst "schlichte" Version: Passwort wird hart codiert
    return "jverein";
    // String password = JVereinDBService.SETTINGS.getString(
    // "database.driver.h2.encryption.encryptedpassword", null);
    // try
    // {
    // Existiert noch nicht. Also neu erstellen.
    // if (password == null)
    // {
    // Wir koennen als Passwort nicht so einfach das Masterpasswort
    // nehmen, weil der User es aendern kann. Wir koennen zwar
    // das Passwort der Datenbank aendern. Allerdings kriegen wir
    // hier nicht mit, wenn sich das Passwort geaendert hat.
    // Daher erzeugen wir ein selbst ein Passwort.
    // Logger.info("generating new random password for database");
    // byte[] data = new byte[8];
    // SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    // random.setSeed((long) (new Date().getTime()));
    // random.nextBytes(data);

    // Jetzt noch verschluesselt abspeichern
    // Logger.info("encrypting password with system certificate");
    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
    // Application.getSSLFactory()
    // .encrypt(new ByteArrayInputStream(data), bos);

    // Verschluesseltes Passwort als Base64 speichern
    // JVereinDBService.SETTINGS.setAttribute(
    // "database.driver.h2.encryption.encryptedpassword", Base64
    // .encode(bos.toByteArray()));

    // Entschluesseltes Passwort als Base64 zurueckliefern, damit keine
    // Binaer-Daten drin sind.
    // Die Datenbank will es doppelt mit Leerzeichen getrennt haben.
    // Das erste ist fuer den User. Das zweite fuer die Verschluesselung.
    // String encoded = Base64.encode(data);
    // return encoded + " " + encoded;
    // }
    //
    // Logger.debug("decrypting database password");
    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
    // Application.getSSLFactory().decrypt(
    // new ByteArrayInputStream(Base64.decode(password)), bos);
    //
    // String encoded = Base64.encode(bos.toByteArray());
    // return encoded + " " + encoded;
    // }
    // catch (Exception e)
    // {
    // throw new RuntimeException("error while determining database password",
    // e);
    // }
  }

  @Override
  public String getJdbcUrl()
  {
    String url = "jdbc:h2:" + Application.getPluginLoader()
        .getPlugin(JVereinPlugin.class).getResources().getWorkPath()
        + "/h2db/jverein";

    // if (JVereinDBService.SETTINGS.getBoolean("database.driver.h2.encryption",
    // true))
    // url += ";CIPHER="
    // + JVereinDBService.SETTINGS.getString(
    // "database.driver.h2.encryption.algorithm", "XTEA");
    return url;
  }

  @Override
  public String getJdbcUsername()
  {
    return "jverein";
  }

  @Override
  public void checkConsistency(Connection conn) throws ApplicationException
  {
    if (!Application.inClientMode())
    {
      try
      {
        // PluginResources res = Application.getPluginLoader().getPlugin(
        // JVereinPlugin.class).getResources();
        // JVereinUpdateProvider udp = new JVereinUpdateProvider(conn, res
        // .getPath()
        // + File.separator + "sql.h2", Application.getCallback()
        // .getStartupMonitor());

        new JVereinUpdateProvider(conn,
            Application.getCallback().getStartupMonitor());
      }
      catch (Exception e2)
      {
        Logger.error("Datenbankupdate kann nicht ausgeführt werden.", e2);
        throw new ApplicationException(e2);
      }
    }
  }

  /**
   * Ueberschrieben, weil SQL-Scripts bei H2 mit einem Prefix versehen werden.
   * Das soll der Admin sicherheitshalber manuell durchfuehren. Wir hinterlassen
   * stattdessen nur einen Hinweistext mit den auszufuehrenden SQL-Scripts.
   * 
   */
  @Override
  public void execute(Connection conn, File sqlScript) throws RemoteException
  {
    if (sqlScript == null)
      return; // Ignore

    // Wir schreiben unseren Prefix davor.
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
  @Override
  public String getSQLTimestamp(String content)
  {
    return content;
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#getInsertWithID()
   */
  @Override
  public boolean getInsertWithID()
  {
    return false;
  }

  /**
   * @see de.willuhn.jameica.hbci.rmi.DBSupport#checkConnection(java.sql.Connection)
   */
  @Override
  public void checkConnection(Connection conn)
  {
    // brauchen wir bei nicht, da Embedded
  }
}
