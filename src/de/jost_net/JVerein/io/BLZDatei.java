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
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.jost_net.JVerein.JVereinPlugin;

public class BLZDatei
{
  private BufferedInputStream bin;

  private HashMap<String, BLZSatz> datenbank;

  private Iterator<String> it;

  public BLZDatei(File file) throws IOException
  {
    ZipFile zip = new ZipFile(file);

    if (zip.size() > 1)
    {
      throw new IOException(JVereinPlugin.getI18n().tr(
          "Fehler: Die ZIP-Datei enthält mehr als eine Datei."));
    }
    for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();)
    {
      ZipEntry entry = e.nextElement();
      bin = new BufferedInputStream(zip.getInputStream(entry));
      datenbank = new HashMap<String, BLZSatz>();
      BLZSatz blzs = new BLZSatz(bin);
      while (blzs.hasNext())
      {
        if (blzs.getZahlungsdienstleister().equals("1"))
        {
          datenbank.put(blzs.getBlz(), blzs);
        }
        blzs = new BLZSatz(bin);
      }
      it = datenbank.keySet().iterator();
    }
  }

  public BLZSatz getNext() throws IOException
  {
    return datenbank.get(it.next());
  }

  public boolean hasNext()
  {
    return it.hasNext();
  }

  public BLZSatz get(String key)
  {
    return datenbank.get(key);
  }
}
