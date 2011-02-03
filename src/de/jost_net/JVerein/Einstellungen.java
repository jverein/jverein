/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * heiner@jverein.de
 * www.jverein.de
 * All rights reserved
 * $Log$
 * Revision 1.25  2011-01-09 14:28:14  jost
 * Stammdaten in die Einstellungen verschoben.
 *
 * Revision 1.24  2010-11-17 16:59:50  jost
 * Strikte Prüfung beim Datum-Parsen.
 *
 * Revision 1.23  2010-11-13 09:20:04  jost
 * Mit V 1.5 deprecatete Spalten und Tabellen entfernt.
 *
 * Revision 1.22  2010/01/21 21:36:47  jost
 * Zusätzlicher DateTime-Formatter
 *
 * Revision 1.21  2009/07/18 13:42:30  jost
 * Bugfix DecimalFormat
 *
 * Revision 1.20  2009/05/31 12:26:02  jost
 * Bugfix FirstStart / Existenz von Beitragsgruppen wird jetzt auch abgeprüft.
 *
 * Revision 1.19  2008/12/29 08:40:36  jost
 * Korrekte Verarbeitung bei fehlendem Geburts- und/oder Eintrittsdatum
 *
 * Revision 1.18  2008/12/22 21:04:52  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.17  2008/11/29 13:04:15  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.16  2008/11/23 13:03:09  jost
 * Debug-Meldungen entfernt.
 *
 * Revision 1.15  2008/11/16 16:55:52  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.14  2008/08/10 12:34:04  jost
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.13  2008/05/22 06:44:28  jost
 * BuchfÃ¼hrung: Beginn des GeschÃ¤ftsjahres
 *
 * Revision 1.12  2008/03/08 19:28:35  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.11  2008/01/01 13:12:26  jost
 * Neu: Dateinamenmuster
 *
 * Revision 1.10  2008/01/01 12:35:25  jost
 * Javadoc korrigiert
 *
 * Revision 1.9  2007/12/28 13:08:44  jost
 * Neue FirstStart-Box
 *
 * Revision 1.8  2007/12/02 13:38:46  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.7  2007/12/01 19:05:25  jost
 * Wegfall Standardtab fÃ¼r die Suche
 *
 * Revision 1.6  2007/12/01 10:04:44  jost
 * Ã„nderung wg. neuem Classloader in Jameica
 *
 * Revision 1.5  2007/10/18 18:18:04  jost
 * Vorbereitung H2-DB
 *
 * Revision 1.4  2007/08/23 18:42:27  jost
 * Standard-Tab fÃ¼r die Mitglieder-Suche
 *
 * Revision 1.3  2007/08/22 20:42:07  jost
 * Bug #011762
 *
 * Revision 1.2  2007/02/23 20:24:57  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:36:55  jost
 * *** empty log message ***
 *
 **********************************************************************/

package de.jost_net.JVerein;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Einstellung;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.messaging.QueryMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

/**
 * Diese Klasse speichert einige Einstellungen für dieses Plugin.
 * 
 * @author Heiner Jostkleigrewe
 */
public class Einstellungen
{

  private static DBService db;

  private static Einstellung einstellung;

  private static Settings settings = new Settings(Einstellungen.class);

  /**
   * Datums-Format dd.MM.yyyy.
   */
  public static DateFormat DATEFORMAT = new SimpleDateFormat("dd.MM.yyyy");

  public static DateFormat DATETIMEFORMAT = new SimpleDateFormat(
      "dd.MM.yyyy HH:mm:ss");

  /**
   * Timestamp-Format dd.MM.yyyy HH:mm.
   */
  public static DateFormat TIMESTAMPFORMAT = new SimpleDateFormat(
      "dd.MM.yyyy HH:mm");

  /**
   * Our decimal formatter.
   */
  public final static DecimalFormat DECIMALFORMAT = new DecimalFormat(
      "###,###.##");

  /**
   * Our currency name.
   */
  public final static String CURRENCY = "EUR";

  public static Date NODATE = new Date();

  static
  {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 1900);
    cal.set(Calendar.MONTH, Calendar.JANUARY);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    NODATE.setTime(cal.getTimeInMillis());
    DECIMALFORMAT.setMinimumFractionDigits(2);
    DECIMALFORMAT.setMaximumFractionDigits(2);
    DATEFORMAT.setLenient(false);
    try
    {
      einstellung = (Einstellung) getDBService().createObject(
          Einstellung.class, "1");
    }
    catch (RemoteException e)
    {
      // Einstellungssatz existiert noch nicht. Deshalb neuen Satz anlegen
      try
      {
        einstellung = (Einstellung) getDBService().createObject(
            Einstellung.class, null);
        // Mit den folgenden Statements wird das Object initialisiert. Sofern
        // noch alte Einstellungen in den Property-Datei vorhanden sind, werden
        // diese verwendet. Ansonsten die Default-Werte.
        einstellung.setGeburtsdatumPflicht(settings.getBoolean(
            "geburtsdatum.pflicht", true));
        einstellung.setEintrittsdatumPflicht(settings.getBoolean(
            "eintrittsdatum.pflicht", true));
        einstellung.setKommunikationsdaten(settings.getBoolean(
            "kommunikationsdaten.einblenden", true));
        einstellung.setZusatzbetrag(settings.getBoolean(
            "zusatzabbuchung.einblenden", true));
        einstellung.setVermerke(settings
            .getBoolean("vermerke.einblenden", true));
        einstellung.setWiedervorlage(settings.getBoolean(
            "wiedervorlage.einblenden", true));
        einstellung.setKursteilnehmer(settings.getBoolean(
            "kursteilnehmer.einblenden", true));
        einstellung.setExterneMitgliedsnummer(settings.getBoolean(
            "externemitgliedsnummer.verwenden", false));
        einstellung.setBeitragsmodel(settings.getInt("beitragsmodel",
            Beitragsmodel.JAEHRLICH));
        einstellung.setDateinamenmuster(settings.getString("dateinamenmuster",
            "a$s$-d$-z$"));
        einstellung.setBeginnGeschaeftsjahr(settings.getString(
            "beginngeschaeftsjahr", "01.01."));
      }
      catch (RemoteException e1)
      {
        Logger.error("Fehler", e1);
      }
    }

  }

  /**
   * Small helper function to get the database service.
   * 
   * @return db service.
   * @throws RemoteException
   */
  public static DBService getDBService() throws RemoteException
  {
    if (db != null)
      return db;

    try
    {
      // We have to ask Jameica's ServiceFactory.
      // If we are running in Client/Server mode and we are the
      // client, the factory returns the remote dbService from the
      // Jameica server.
      // The name and class of the service is defined in plugin.xml
      db = (DBService) Application.getServiceFactory().lookup(
          JVereinPlugin.class, "database");
      return db;
    }
    catch (Exception e)
    {
      throw new RemoteException("error while getting database service", e);
    }
  }

  /**
   * Prueft die Gueltigkeit der BLZ/Kontonummer-Kombi anhand von Pruefziffern.
   * 
   * @param blz
   * @param kontonummer
   * @return true, wenn die Kombi ok ist.
   */
  public final static boolean checkAccountCRC(String blz, String kontonummer)
  {
    QueryMessage q = new QueryMessage(blz + ":" + kontonummer);
    Application.getMessagingFactory()
        .getMessagingQueue("hibiscus.query.accountcrc").sendSyncMessage(q);
    Object data = q.getData();

    // Wenn wir keine oder eine ungueltige Antwort erhalten haben,
    // ist Hibiscus vermutlich nicht installiert. In dem Fall
    // lassen wir die Konto/BLZ-Kombination mangels besserer
    // Informationen zu
    return (data == null || !(data instanceof Boolean)) ? true
        : ((Boolean) data).booleanValue();
  }

  /**
   * Liefert den Namen der Bank zu einer BLZ.
   * 
   * @param blz
   *          BLZ.
   * @return Name der Bank oder Leerstring.
   */
  public final static String getNameForBLZ(String blz)
  {
    QueryMessage q = new QueryMessage(blz);
    Application.getMessagingFactory()
        .getMessagingQueue("hibiscus.query.bankname").sendSyncMessage(q);
    Object data = q.getData();

    // wenn wir nicht zurueckerhalten haben oder die Nachricht
    // noch unveraendert die BLZ enthaelt, liefern wir einen
    // Leerstring zurueck
    return (data == null || data.equals(blz)) ? "" : data.toString();
  }

  public static Einstellung getEinstellung()
  {
    return einstellung;
  }

  public static void setEinstellung(Einstellung einst)
  {
    einstellung = einst;
  }

  /**
   * Prueft, ob die MD5-Checksumme der Datenbank geprueft werden soll.
   * 
   * @return true, wenn die Checksumme geprueft werden soll.
   */
  public static boolean getCheckDatabase()
  {
    return settings.getBoolean("checkdatabase", true);
  }

  public static boolean isFirstStart()
  {
    boolean beigen = false;
    boolean bbeitragsgruppe = false;
    try
    {
      DBIterator st = getDBService().createList(Einstellung.class);
      if (st.size() > 0)
      {
        beigen = true;
      }
      DBIterator bg = getDBService().createList(Beitragsgruppe.class);
      if (bg.size() > 0)
      {
        bbeitragsgruppe = true;
      }
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    return !beigen || !bbeitragsgruppe;
  }

}
