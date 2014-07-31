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

package de.jost_net.JVerein.io;

import java.io.File;
import java.rmi.RemoteException;

import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Basis-Interface aller Importer.
 */
public interface Importer extends IO
{

  /**
   * Importiert Daten aus dem InputStream.
   * 
   * @param context
   *          Context, der dem Importer hilft, den Zusammenhang zu erkennen, in
   *          dem er aufgerufen wurde. Das kann zum Beispiel ein Konto sein.
   * @param format
   *          das vom User ausgewaehlte Import-Format.
   * @param file
   *          der File-Object.
   * @param monitor
   *          ein Monitor, an den der Importer Ausgaben ueber seinen
   *          Bearbeitungszustand ausgeben kann. Der Importer muss den
   *          Import-Stream selbst schliessen!
   * @throws RemoteException
   * @throws ApplicationException
   */
  public void doImport(Object context, IOFormat format, File file,
      String encoding, ProgressMonitor monitor) throws Exception;

}