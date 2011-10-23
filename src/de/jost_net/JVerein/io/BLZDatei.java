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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BLZDatei
{
  private BufferedInputStream bin;

  public BLZDatei(String file) throws IOException
  {
    ZipFile zip = new ZipFile(file);

    if (zip.size() > 1)
    {
      throw new IOException(
          "Fehler: Die ZIP-Datei enthält mehr als eine Datei.");
    }
    for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();)
    {
      ZipEntry entry = e.nextElement();
      bin = new BufferedInputStream(zip.getInputStream(entry));
    }
  }

  public BLZSatz getNext() throws IOException
  {
    return new BLZSatz(bin);
  }

  public static void main(String[] args) throws IOException
  {
    BLZDatei blz = new BLZDatei("C:/Users/heiner/Desktop/blz_20110905txt.zip");
    BLZSatz satz = blz.getNext();
    while (satz.hasNext())
    {
      if (satz.getZahlungsdienstleister().equals("1")
      // && satz.getAenderungskennzeichen().equals("U")
          && !satz.getNachfolgeblz().equals("00000000"))
      {
        System.out.println(satz.getBlz() + ";" + satz.getBezeichnung() + ";"
            + satz.getNachfolgeblz() + satz.getAenderungskennzeichen());
      }
      satz = blz.getNext();
    }
  }

}
