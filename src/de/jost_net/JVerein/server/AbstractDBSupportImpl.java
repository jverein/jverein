/**********************************************************************
 * Kopie aus Hibiscus
 * Copyright (c) by willuhn software & services
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
import java.io.FileReader;
import java.rmi.RemoteException;
import java.sql.Connection;

import de.jost_net.JVerein.rmi.DBSupport;
import de.willuhn.logging.Logger;
import de.willuhn.sql.ScriptExecutor;
import de.willuhn.util.ApplicationException;

/**
 * Abstrakte Basisklasse fuer den Datenbank-Support.
 */
public abstract class AbstractDBSupportImpl implements DBSupport
{

  private static final long serialVersionUID = 8344265686929785808L;

  @Override
  public void checkConsistency(Connection conn) throws ApplicationException
  {
    // Leere Dummy-Implementierung
  }

  @Override
  public void execute(Connection conn, File sqlScript) throws RemoteException
  {
    if (sqlScript == null)
      return;

    if (!sqlScript.canRead() || !sqlScript.exists())
      return;

    Logger.info("executing sql script: " + sqlScript.getAbsolutePath());

    FileReader reader = null;

    try
    {
      reader = new FileReader(sqlScript);
      ScriptExecutor.execute(reader, conn);
    }
    catch (RemoteException re)
    {
      throw re;
    }
    catch (Exception e)
    {
      throw new RemoteException(
          "error while executing sql script " + sqlScript, e);
    }
    finally
    {
      try
      {
        if (reader != null)
          reader.close();
      }
      catch (Exception e3)
      {
        Logger.error("error while closing file " + sqlScript, e3);
      }
    }
  }

  @Override
  public void install()
  {
    // Leere Dummy-Implementierung
  }

  @Override
  public int getTransactionIsolationLevel()
  {
    return -1;
  }

}
