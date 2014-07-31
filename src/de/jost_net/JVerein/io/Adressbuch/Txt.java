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
package de.jost_net.JVerein.io.Adressbuch;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Mitglied;

public class Txt
{
  private OutputStreamWriter out;

  private String separator;

  public Txt(File file, String separator) throws IOException 
  {
    // Vermerk für mich: Encodings siehe
    // http://www.cafeconleche.org/books/xmljava/chapters/ch03s03.html
    out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(
        file)));
    out.write("\"Name\"" + separator + "\"Vorname\"" + separator
        + "\"Strasse\"" + separator + "\"PLZ\"" + separator + "\"Ort\""
        + separator + "\"Staat\"" + separator + "\"Anzeigename\"" + separator
        + "\"Email\"" + separator + "\"TelefonPrivat\"" + separator
        + "\"TelefonMobil\"\n");
    this.separator = separator;
  }

  public void add(Mitglied mitglied) throws IOException
  {
    out.write(new TxtRecord(mitglied, separator).get());
  }

  public void close() throws IOException
  {
    out.close();
  }
}

class TxtRecord
{
  private Mitglied mitglied;

  private String separator;

  public TxtRecord(Mitglied mitglied, String separator)
  {
    this.mitglied = mitglied;
    this.separator = separator;
  }

  public String get() throws RemoteException
  {
    StringBuilder sb = new StringBuilder();
    sb.append("\"" + mitglied.getName() + "\"" + separator);
    sb.append("\"" + mitglied.getVorname() + "\"" + separator);
    sb.append("\"" + mitglied.getStrasse() + "\"" + separator);
    sb.append("\"" + mitglied.getPlz() + "\"" + separator);
    sb.append("\"" + mitglied.getOrt() + "\"" + separator);
    sb.append("\"" + (mitglied.getStaat() != null ? mitglied.getStaat() : "")
        + "\"" + separator);
    sb.append("\"" + mitglied.getVorname() + " " + mitglied.getName() + "\""
        + separator);
    sb.append("\"" + mitglied.getEmail() + "\"" + separator);
    sb.append("\"" + mitglied.getTelefonprivat() + "\"" + separator);
    sb.append("\"" + mitglied.getHandy() + "\"" + separator);
    sb.append("\n");
    return sb.toString();
  }
}