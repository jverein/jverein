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

import org.kapott.hbci.manager.HBCIUtils;

import de.willuhn.datasource.rmi.DBService;
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

  private static Settings settings = new Settings(Einstellungen.class);

  private static Boolean geburtsdatumPflicht;

  private static Boolean eintrittsdatumPflicht;

  private static Boolean kommunikationsdaten;

  private static Boolean zusatzabbuchung;

  private static Boolean vermerke;

  private static Boolean wiedervorlage;

  private static Boolean kursteilnehmer;

  private static String mitgliederstandardtab;

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
   * @see HBCIUtils#checkAccountCRC(java.lang.String, java.lang.String)
   * @param blz
   * @param kontonummer
   * @return true, wenn die Kombi ok ist.
   */

  public final static boolean checkAccountCRC(String blz, String kontonummer)
  {
    try
    {
      return HBCIUtils.checkAccountCRC(blz, kontonummer);
    }
    catch (Exception e)
    {
      Logger
          .warn("HBCI4Java subsystem seems to be not initialized for this thread group, adding thread group");
      return HBCIUtils.checkAccountCRC(blz, kontonummer);
    }
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
   * Standardtab für die Mitgliedersuche
   */
  public static String getMitgliederStandardTab()
  {
    if (mitgliederstandardtab != null)
    {
      return mitgliederstandardtab;
    }
    mitgliederstandardtab = settings.getString("mitglieder.standardtab", "A");
    return mitgliederstandardtab;
  }

  /**
   * Speichert den Standardtab für die Mitgliedersuche.
   */
  public static void setMitgliederStandardTab(String value)
  {
    settings.setAttribute("mitglieder.standardtab", value);
    mitgliederstandardtab = null;
  }

}
