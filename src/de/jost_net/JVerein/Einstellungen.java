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
 * Revision 1.9  2007/12/28 13:08:44  jost
 * Neue FirstStart-Box
 *
 * Revision 1.8  2007/12/02 13:38:46  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.7  2007/12/01 19:05:25  jost
 * Wegfall Standardtab für die Suche
 *
 * Revision 1.6  2007/12/01 10:04:44  jost
 * Änderung wg. neuem Classloader in Jameica
 *
 * Revision 1.5  2007/10/18 18:18:04  jost
 * Vorbereitung H2-DB
 *
 * Revision 1.4  2007/08/23 18:42:27  jost
 * Standard-Tab für die Mitglieder-Suche
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

import de.jost_net.JVerein.gui.input.BeitragsmodelInput;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.messaging.QueryMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;

/**
 * Diese Klasse speichert einige Einstellungen f�r dieses Plugin.
 * 
 * @author Heiner Jostkleigrewe
 */
public class Einstellungen
{

  private static DBService db;

  private static Settings settings = new Settings(Einstellungen.class);

  private static Boolean geburtsdatumPflicht;

  private static Boolean eintrittsdatumPflicht;

  private static Boolean kommunikationsdaten;

  private static Boolean zusatzabbuchung;

  private static Boolean vermerke;

  private static Boolean wiedervorlage;

  private static Boolean kursteilnehmer;

  private static int beitragsmodel;

  /**
   * Datums-Format dd.MM.yyyy.
   */
  public static DateFormat DATEFORMAT = new SimpleDateFormat("dd.MM.yyyy");

  /**
   * Timestamp-Format dd.MM.yyyy HH:mm.
   */
  public static DateFormat TIMESTAMPFORMAT = new SimpleDateFormat(
      "dd.MM.yyyy HH:mm");

  /**
   * Our decimal formatter.
   */
  public final static DecimalFormat DECIMALFORMAT = (DecimalFormat) DecimalFormat
      .getInstance(Application.getConfig().getLocale());

  /**
   * Our currency name.
   */
  public final static String CURRENCY = "EUR";

  static
  {
    DECIMALFORMAT.setMinimumFractionDigits(2);
    DECIMALFORMAT.setMaximumFractionDigits(2);
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
    Application.getMessagingFactory().getMessagingQueue(
        "hibiscus.query.accountcrc").sendSyncMessage(q);
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
    Application.getMessagingFactory().getMessagingQueue(
        "hibiscus.query.bankname").sendSyncMessage(q);
    Object data = q.getData();

    // wenn wir nicht zurueckerhalten haben oder die Nachricht
    // noch unveraendert die BLZ enthaelt, liefern wir einen
    // Leerstring zurueck
    return (data == null || data.equals(blz)) ? "" : data.toString();
  }

  /**
   * Ist das Geburtsdatum ein Pflichtfeld?
   */
  public static boolean isGeburtsdatumPflicht()
  {
    if (geburtsdatumPflicht != null)
    {
      return geburtsdatumPflicht.booleanValue();
    }
    geburtsdatumPflicht = new Boolean(settings.getBoolean(
        "geburtsdatum.pflicht", true));
    return geburtsdatumPflicht;
  }

  /**
   * Speichert, ob das Geburtsdatum ein Pflichtfeld ist.
   */
  public static void setGeburtsdatumPflicht(boolean value)
  {
    settings.setAttribute("geburtsdatum.pflicht", value);
    geburtsdatumPflicht = null;
  }

  /**
   * Ist das Eintrittsdatum ein Pflichtfeld?
   */
  public static boolean isEintrittsdatumPflicht()
  {
    if (eintrittsdatumPflicht != null)
    {
      return eintrittsdatumPflicht.booleanValue();
    }
    eintrittsdatumPflicht = new Boolean(settings.getBoolean(
        "eintrittsdatum.pflicht", true));
    return eintrittsdatumPflicht;
  }

  /**
   * Speichert, ob das Eintrittsdatum ein Pflichtfeld ist.
   */
  public static void setEintrittsdatumPflicht(boolean value)
  {
    settings.setAttribute("eintrittsdatum.pflicht", value);
    eintrittsdatumPflicht = null;
  }

  /**
   * Kommunikationsdaten einblenden?
   */
  public static boolean isKommunikationsdaten()
  {
    if (kommunikationsdaten != null)
    {
      return kommunikationsdaten.booleanValue();
    }
    kommunikationsdaten = new Boolean(settings.getBoolean(
        "kommunikationsdaten.einblenden", true));
    return kommunikationsdaten;
  }

  /**
   * Speichert, ob Kommunikationsdaten eingeblendet werden sollen.
   */
  public static void setKommunikationsdaten(boolean value)
  {
    settings.setAttribute("kommunikationsdaten.einblenden", value);
    kommunikationsdaten = null;
  }

  /**
   * Zusatzabbuchung einblenden?
   */
  public static boolean isZusatzabbuchung()
  {
    if (zusatzabbuchung != null)
    {
      return zusatzabbuchung.booleanValue();
    }
    zusatzabbuchung = new Boolean(settings.getBoolean(
        "zusatzabbuchung.einblenden", true));
    return zusatzabbuchung;
  }

  /**
   * Speichert, ob Zusatzabbuchungen eingeblendet werden sollen.
   */
  public static void setZusatzabbuchungen(boolean value)
  {
    settings.setAttribute("zusatzabbuchung.einblenden", value);
    zusatzabbuchung = null;
  }

  /**
   * Vermerke einblenden?
   */
  public static boolean isVermerke()
  {
    if (vermerke != null)
    {
      return vermerke.booleanValue();
    }
    vermerke = new Boolean(settings.getBoolean("vermerke.einblenden", true));
    return vermerke;
  }

  /**
   * Speichert, ob Vermerke eingeblendet werden sollen.
   */
  public static void setVermerke(boolean value)
  {
    settings.setAttribute("vermerke.einblenden", value);
    vermerke = null;
  }

  /**
   * Wiedervorlage einblenden?
   */
  public static boolean isWiedervorlage()
  {
    if (wiedervorlage != null)
    {
      return wiedervorlage.booleanValue();
    }
    wiedervorlage = new Boolean(settings.getBoolean("wiedervorlage.einblenden",
        true));
    return wiedervorlage;
  }

  /**
   * Speichert, ob die Wiedervorlage eingeblendet werden sollen.
   */
  public static void setWiedervorlage(boolean value)
  {
    settings.setAttribute("wiedervorlage.einblenden", value);
    wiedervorlage = null;
  }

  /**
   * Kursteilnehmer einblenden?
   */
  public static boolean isKursteilnehmer()
  {
    if (kursteilnehmer != null)
    {
      return kursteilnehmer.booleanValue();
    }
    kursteilnehmer = new Boolean(settings.getBoolean(
        "kursteilnehmer.einblenden", true));
    return kursteilnehmer;
  }

  /**
   * Speichert, ob Kursteilnehmer eingeblendet werden sollen.
   */
  public static void setKursteilnehmer(boolean value)
  {
    settings.setAttribute("kursteilnehmer.einblenden", value);
    kursteilnehmer = null;
  }

  /**
   * Beitragsmodel
   */
  public static int getBeitragsmodel()
  {
    beitragsmodel = settings.getInt("beitragsmodel",
        BeitragsmodelInput.JAEHRLICH);
    return beitragsmodel;
  }

  /**
   * Speichert das Beitragsmodel
   */
  public static void setBeitragsmodel(int value)
  {
    settings.setAttribute("beitragsmodel", value);
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
    boolean bstamm = false;
    boolean bbeitragsgruppe = false;
    try
    {
      DBIterator st = getDBService().createList(Stammdaten.class);
      if (st.size() > 0)
      {
        bstamm = true;
      }
      DBIterator bg = getDBService().createList(Stammdaten.class);
      if (bg.size() > 0)
      {
        bbeitragsgruppe = true;
      }
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    return !bstamm || !bbeitragsgruppe;
  }

}
