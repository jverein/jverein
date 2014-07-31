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

/**
 * Basis-Interface aller Importer und Exporter.
 */
public interface IO
{
  /**
   * Liefert einen sprechenden Namen des Exporters/Importers. Z.Bsp.
   * "Swift MT-940 Format".
   * 
   * @return Name
   */
  public String getName();

  /**
   * Liefert eine Liste der von diesem unterstuetzten Datei-Formate.
   * 
   * @param objectType
   *          Art der zu exportierenden/importierenden Objekte. Z.Bsb.:
   *          Umsatz.class oder SammellastBuchung.class. Abhaengig davon kann
   *          der Exporter/Importer eine unterschiedliche Liste von
   *          Dateiformaten liefern, die er zu dieser Objektart unterstuetzt.
   * @return Liste der Export-Formate.
   */
  public IOFormat[] getIOFormats(Class<?> objectType);

}
