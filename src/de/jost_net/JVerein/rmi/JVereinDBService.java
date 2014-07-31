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
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.plugin.Version;
import de.willuhn.jameica.system.Settings;
import de.willuhn.util.ApplicationException;

/**
 * Interface fuer den Datenbank-Service von JVerein.
 */
public interface JVereinDBService extends DBService
{
  /**
   * Einstellungen fuer die DB-Services.
   */
  public final static Settings SETTINGS = new Settings(JVereinDBService.class);

  /**
   * Aktualisiert die Datenbank.
   */
  public void update(Version oldVersion, Version newVersion)
      throws RemoteException;

  /**
   * Initialisiert/Erzeugt die Datenbank.
   * 
   * @throws RemoteException
   *           Wenn beim Initialisieren ein Fehler auftrat.
   */
  public void install() throws RemoteException;

  /**
   * Checkt die Konsistenz der Datenbank.
   * 
   * @throws RemoteException
   *           Wenn es beim Pruefen der Datenbank-Konsistenz zu einem Fehler
   *           kam.
   * @throws ApplicationException
   *           wenn die Datenbank-Konsistenz nicht gewaehrleistet ist.
   */
  public void checkConsistency() throws RemoteException, ApplicationException;

  /**
   * Liefert den Namen der SQL-Funktion, mit der die Datenbank aus einem
   * DATE-Feld einen UNIX-Timestamp macht. Bei MySQL ist das z.Bsp.
   * "UNIX_TIMESTAMP" und bei McKoi schlicht "TONUMBER".
   * 
   * @param content
   *          der Feld-Name.
   * @return Name der SQL-Funktion samt Parameter. Also zum Beispiel
   *         "TONUMBER(datum)".
   * @throws RemoteException
   */
  public String getSQLTimestamp(String content) throws RemoteException;

}
