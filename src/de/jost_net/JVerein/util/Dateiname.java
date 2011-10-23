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
package de.jost_net.JVerein.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Generierung eines Dateinamens unter Berücksichtigung von vorgegebener
 * Variablen
 * </p>
 * 
 * <dl>
 * <dt>a$</dt>
 * <dd>Aufgabe (z. B. auswertung, abbuchung)</dd>
 * <dt>d$</dt>
 * <dd>Aktuelles Datum (z. B. 20080101)</dd>
 * <dt>$z</dt>
 * <dd>Aktuelle Zeit (z. B. 120503)</dd>
 * <dt>$s</dt>
 * <dd>Sortierung. Wird nicht immer gefüllt. Ggfls. Leerstring.</dd>
 * </dl>
 */

public class Dateiname
{
  private String aufgabe;

  private String muster;

  private String extension;

  private String sortierung;

  /**
   * Konstruktor
   * 
   * @param aufgabe
   * @param muster
   * @param extension
   */
  public Dateiname(String aufgabe, String muster, String extension)
  {
    this(aufgabe, "", muster, extension);
  }

  /**
   * Konstruktor
   * 
   * @param aufgabe
   * @param sortierung
   * @param muster
   * @param extension
   */
  public Dateiname(String aufgabe, String sortierung, String muster,
      String extension)
  {
    this.aufgabe = aufgabe;
    this.muster = muster;
    this.extension = extension;
    this.sortierung = sortierung;
  }

  /**
   * Gibt den aufbereiteten String zurück. Wurde ein leeres Muster übergeben,
   * wird ein Leerstring zurückgegeben.
   */
  public String get()
  {
    if (muster.length() == 0)
    {
      return "";
    }
    String ret = muster;
    ret = ret.replace("a$", aufgabe);
    ret = ret
        .replace("d$", new SimpleDateFormat("yyyyMMdd").format(new Date()));
    ret = ret.replace("z$", new SimpleDateFormat("HHmmss").format(new Date()));
    ret = ret.replace("s$", sortierung);
    return ret + "." + extension;
  }
}
