/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

/**
 * Dieses Interface kapselt die Datei-Formate. Jeder Importer oder Exporter
 * unterstuetzt ein oder mehrere Dateiformate. Ueber
 * <code>de.willuhn.jameica.hbci.io.IO#getIOFormats(Class type)</code> kann ein
 * Importer/Exporter abgefragt werden, welche Formate er unterstuetzt.
 */
public interface IOFormat
{
  /**
   * Liefert einen sprechenden Namen fuer das Datei-Format. Zum Beispiel
   * &quotCSV-Datei&quot;
   * 
   * @return Sprechender Name des Datei-Formats.
   */
  public String getName();

  /**
   * Liefert die Datei-Endungen des Formats. Zum Beispiel "*.csv" oder "*.txt".
   * 
   * @return Datei-Endung.
   */
  public String[] getFileExtensions();
}

/*********************************************************************
 * $Log$ Revision 1.2 2006/01/23 23:07:23 willuhn
 * 
 * @N csv import stuff
 * 
 *    Revision 1.1 2006/01/17 00:22:36 willuhn
 * @N erster Code fuer Swift MT940-Import
 * 
 *    Revision 1.1 2005/06/30 23:52:42 web0
 * @N export via velocity
 * 
 **********************************************************************/
