/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/

package de.jost_net.JVerein;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import de.jost_net.JVerein.io.MailSender.IMAPCopyData;
import de.jost_net.JVerein.keys.Altermodel;
import de.jost_net.JVerein.keys.ArbeitsstundenModel;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.SepaMandatIdSource;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.hbci.rmi.HBCIDBService;
import de.willuhn.jameica.messaging.QueryMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

/**
 * Diese Klasse speichert einige Einstellungen für dieses Plugin.
 * 
 * @author Heiner Jostkleigrewe
 */
public class Einstellungen {

  private static DBService db;

  private static Einstellung einstellung;

  private static Settings settings = new Settings(Einstellungen.class);

  /**
   * Our decimal formatter.
   */
  public final static DecimalFormat DECIMALFORMAT = new DecimalFormat("###,###.##");

  /**
   * Int formatter.
   */
  public final static DecimalFormat INTFORMAT = new DecimalFormat("###,###,###");

  /**
   * Our currency name.
   */
  public final static String CURRENCY = "EUR";

  public static Date NODATE = new Date();

  public final static String ZUSATZFELD_PRE = "mitglied_zusatzfeld_";

  public final static String LESEFELD_PRE = "mitglied_lesefelder_";

  static {
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
    try {
      einstellung = (Einstellung) getDBService().createObject(Einstellung.class, "1");
    } catch (RemoteException e) {
      // Einstellungssatz existiert noch nicht. Deshalb neuen Satz anlegen
      try {
        einstellung = (Einstellung) getDBService().createObject(Einstellung.class, null);
        // Mit den folgenden Statements wird das Object initialisiert. Sofern
        // noch alte Einstellungen in den Property-Datei vorhanden sind, werden
        // diese verwendet. Ansonsten die Default-Werte.
        einstellung.setGeburtsdatumPflicht(settings.getBoolean("geburtsdatum.pflicht", true));
        einstellung.setEintrittsdatumPflicht(settings.getBoolean("eintrittsdatum.pflicht", true));
        einstellung.setSterbedatum(settings.getBoolean("sterbedatum", false));
        einstellung
            .setKommunikationsdaten(settings.getBoolean("kommunikationsdaten.einblenden", true));
        einstellung.setZusatzbetrag(settings.getBoolean("zusatzabbuchung.einblenden", true));
        einstellung.setVermerke(settings.getBoolean("vermerke.einblenden", true));
        einstellung.setWiedervorlage(settings.getBoolean("wiedervorlage.einblenden", true));
        einstellung.setKursteilnehmer(settings.getBoolean("kursteilnehmer.einblenden", true));
        einstellung.setExterneMitgliedsnummer(
            settings.getBoolean("externemitgliedsnummer.verwenden", false));

        einstellung.setBeitragsmodel(
            settings.getInt("beitragsmodel", Beitragsmodel.GLEICHERTERMINFUERALLE.getKey()));
        einstellung.setArbeitsstundenmodel(
            settings.getInt(Einstellung.COL_ARBEITS_MODEL, ArbeitsstundenModel.STANDARD));
        einstellung.setDateinamenmuster(settings.getString("dateinamenmuster", "a$s$-d$-z$"));
        einstellung.setBeginnGeschaeftsjahr(settings.getString("beginngeschaeftsjahr", "01.01."));
        einstellung.setAltersModel(
            settings.getInt(Einstellung.COL_ALTER_MODEL, Altermodel.AKTUELLES_DATUM));
        einstellung.setSepaMandatIdSource(
            settings.getInt(Einstellung.COL_SEPA_MANDANTID_SOURCE, SepaMandatIdSource.DBID));
      } catch (RemoteException e1) {
        Logger.error("Fehler", e1);
      }
    }

  }

  public static HBCIDBService getHibiscusDBService() throws RemoteException {
    return de.willuhn.jameica.hbci.Settings.getDBService();
  }

  /**
   * Small helper function to get the database service.
   * 
   * @return db service.
   * @throws RemoteException
   */
  public static DBService getDBService() throws RemoteException {
    if (db != null)
      return db;

    try {
      // We have to ask Jameica's ServiceFactory.
      // If we are running in Client/Server mode and we are the
      // client, the factory returns the remote dbService from the
      // Jameica server.
      // The name and class of the service is defined in plugin.xml
      db = (DBService) Application.getServiceFactory().lookup(JVereinPlugin.class, "database");
      return db;
    } catch (Exception e) {
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
  public final static boolean checkAccountCRC(String blz, String kontonummer) {
    QueryMessage q = new QueryMessage(blz + ":" + kontonummer);
    Application.getMessagingFactory().getMessagingQueue("hibiscus.query.accountcrc")
        .sendSyncMessage(q);
    Object data = q.getData();

    // Wenn wir keine oder eine ungueltige Antwort erhalten haben,
    // ist Hibiscus vermutlich nicht installiert. In dem Fall
    // lassen wir die Konto/BLZ-Kombination mangels besserer
    // Informationen zu
    return (data == null || !(data instanceof Boolean)) ? true : ((Boolean) data).booleanValue();
  }

  /**
   * Liefert den Namen der Bank zu einer BLZ.
   * 
   * @param blz BLZ.
   * @return Name der Bank oder Leerstring.
   */
  public final static String getNameForBLZ(String blz) {
    QueryMessage q = new QueryMessage(blz);
    Application.getMessagingFactory().getMessagingQueue("hibiscus.query.bankname")
        .sendSyncMessage(q);
    Object data = q.getData();

    // wenn wir nicht zurueckerhalten haben oder die Nachricht
    // noch unveraendert die BLZ enthaelt, liefern wir einen
    // Leerstring zurueck
    return (data == null || data.equals(blz)) ? "" : data.toString();
  }

  public static Einstellung getEinstellung() {
    return einstellung;
  }

  public static void setEinstellung(Einstellung einst) {
    einstellung = einst;
  }

  public static GregorianCalendar getBeginnGeschaeftsjahr(GregorianCalendar date) {
    GregorianCalendar BeginnGeschaeftsjahr = new GregorianCalendar();
    try {
      BeginnGeschaeftsjahr
          .setTime(new JVDateFormatTTMMJJJJ().parse(getEinstellung().getBeginnGeschaeftsjahr()
              + ((Integer) date.get(Calendar.YEAR)).toString()));
      if (BeginnGeschaeftsjahr.compareTo(date) > 0) {
        BeginnGeschaeftsjahr.add(Calendar.YEAR, -1);
      }
    } catch (RemoteException e) {
      Logger.error("Error while reading \"BeginnGeschaeftsjahr!\"", e);
      BeginnGeschaeftsjahr.set(Calendar.YEAR, date.get(Calendar.YEAR));
      BeginnGeschaeftsjahr.set(Calendar.MONTH, 1);
      BeginnGeschaeftsjahr.set(Calendar.DAY_OF_MONTH, 1);
    } catch (ParseException e) {
      Logger.error("Error while parsing \"BeginnGeschaeftsjahr!\"", e);
      BeginnGeschaeftsjahr.set(Calendar.YEAR, date.get(Calendar.YEAR));
      BeginnGeschaeftsjahr.set(Calendar.MONTH, 1);
      BeginnGeschaeftsjahr.set(Calendar.DAY_OF_MONTH, 1);
    }
    return BeginnGeschaeftsjahr;
  }

  public static GregorianCalendar getEndeGeschaeftsjahr(GregorianCalendar date) {
    GregorianCalendar EndeGeschaeftsjahr = getBeginnGeschaeftsjahr(date);
    EndeGeschaeftsjahr.add(Calendar.YEAR, 1);
    EndeGeschaeftsjahr.add(Calendar.DAY_OF_MONTH, -1);
    return EndeGeschaeftsjahr;
  }

  /**
   * Prueft, ob die MD5-Checksumme der Datenbank geprueft werden soll.
   * 
   * @return true, wenn die Checksumme geprueft werden soll.
   */
  public static boolean getCheckDatabase() {
    return settings.getBoolean("checkdatabase", true);
  }

  public static boolean isFirstStart() {
    boolean beigen = false;
    boolean bbeitragsgruppe = false;
    try {
      DBIterator<Einstellung> st = getDBService().createList(Einstellung.class);
      if (st.size() > 0) {
        beigen = true;
      }
      DBIterator<Beitragsgruppe> bg = getDBService().createList(Beitragsgruppe.class);
      if (bg.size() > 0) {
        bbeitragsgruppe = true;
      }
    } catch (RemoteException e) {
      Logger.error("Fehler", e);
    }
    return !beigen || !bbeitragsgruppe;
  }

  /**
   * Get the IMAP folder copy data from Einstellungen
   * 
   * @return IMAP copy data
   * @throws RemoteException
   */
  public static IMAPCopyData getImapCopyData() throws RemoteException {
    IMAPCopyData imapCopyData =
        new IMAPCopyData(getEinstellung().getCopyToImapFolder(), getEinstellung().getImapAuthUser(),
            getEinstellung().getImapAuthPwd(), getEinstellung().getImapHost(),
            getEinstellung().getImapPort(), getEinstellung().getImapSsl(),
            getEinstellung().getImapStartTls(), getEinstellung().getImapSentFolder());
    return imapCopyData;
  }
}
