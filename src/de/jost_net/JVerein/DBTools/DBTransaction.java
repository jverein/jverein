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
package de.jost_net.JVerein.DBTools;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Version;
import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class DBTransaction
{
  private static DBObject transactionObjekt;

  public static void starten() throws ApplicationException
  {
    if (null != transactionObjekt)
    {
      Logger.info("Datenbank Transaktion läuft bereits");
      return;
    }

    try
    {
      transactionObjekt = Einstellungen.getDBService().createObject(
          Version.class, null);
      transactionObjekt.transactionBegin();
    }
    catch (RemoteException ex)
    {
      transactionObjekt = null;
      throw new ApplicationException(
          "Datenbank Transaktion kann nicht gestartet werden!", ex);
    }
  }

  public static void commit() throws ApplicationException
  {
    if (null == transactionObjekt)
    {
      Logger.warn("Commit! Keine Datenbank Transaktion gestartet!");
      return;
    }

    try
    {
      transactionObjekt.transactionCommit();
    }
    catch (RemoteException ex)
    {
      throw new ApplicationException(
          "Datenbank Transaktion kann nicht abgeschlossen werden!", ex);
    }
    finally
    {
      transactionObjekt = null;
    }
  }

  public static void rollback()
  {
    if (null == transactionObjekt)
    {
      Logger.warn("Rollback! Keine Datenbank Transaktion gestartet!");
      return;
    }

    try
    {
      transactionObjekt.transactionRollback();
    }
    catch (RemoteException ex)
    {
      Logger.error("Datenbankänderungen können nicht zurückgenommen werden!",
          ex);
    }
    finally
    {
      transactionObjekt = null;
    }
  }

}
