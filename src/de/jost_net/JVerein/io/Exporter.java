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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import com.itextpdf.text.DocumentException;

import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;
import de.willuhn.util.Session;

/**
 * Basis-Interface aller Exporter. Alle Klassen, die dieses Interface
 * implementieren, werden automatisch von Hibiscus erkannt und dem Benutzer als
 * Export-Moeglichkeit angeboten insofern sie einen parameterlosen Konstruktor
 * mit dem Modifier "public" besitzen (Java-Bean-Konvention).
 */
public interface Exporter extends IO
{
  /**
   * Eine Session fuer zusaetzliche Parameter.
   */
  public final static Session SESSION = new Session();

  /**
   * Exportiert die genannten Objekte in den angegebenen OutputStream.
   * 
   * @param objects
   *          die zu exportierenden Objekte.
   * @param format
   *          das vom User ausgewaehlte Export-Format.
   * @param file
   *          File-Object für die Ausgabe. Der Exporter muss die Datei selbe
   *          erstellen und schliessen!
   * @param monitor
   *          ein Monitor, an den der Exporter Ausgaben ueber seinen
   *          Bearbeitungszustand ausgeben kann.
   * @throws RemoteException
   * @throws ApplicationException
   */
  public void doExport(Object[] objects, IOFormat format, File file,
      ProgressMonitor monitor) throws RemoteException, ApplicationException,
      FileNotFoundException, DocumentException, IOException;

  public String getDateiname();
}
