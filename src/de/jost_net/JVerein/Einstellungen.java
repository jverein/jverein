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
import de.willuhn.logging.Logger;

/**
 * Diese Klasse speichert einige Einstellungen für dieses Plugin.
 * 
 * @author Heiner Jostkleigrewe
 */
public class Einstellungen
{

  private static DBService db;

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

}
