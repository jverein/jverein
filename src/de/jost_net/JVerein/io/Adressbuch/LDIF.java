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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Mitglied;

public class LDIF
{
  private OutputStreamWriter out;

  public LDIF(String filename) throws Exception
  {
    // Vermerk für mich: Encodings siehe
    // http://www.cafeconleche.org/books/xmljava/chapters/ch03s03.html
    out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(
        filename)), "Cp1250");
    out.write("version:1\n");
  }

  public void add(Mitglied adresse) throws IOException
  {
    out.write(new LDIFRecord(adresse).get());
  }

  public void close() throws IOException
  {
    out.close();
  }
}

class LDIFRecord
{
  private Mitglied adresse;

  public LDIFRecord(Mitglied adresse)
  {
    this.adresse = adresse;
  }

  public String get() throws RemoteException
  {
    StringBuilder sb = new StringBuilder();
    final String NL = System.getProperty("line.separator");
    sb.append("dn: cn=" + adresse.getName() + ",mail=" + adresse.getEmail()
        + NL);
    sb.append("objectclass: top" + NL);
    sb.append("objectclass: person" + NL);
    sb.append("objectclass: organizationalPerson" + NL);
    sb.append("objectclass: inetOrgPerson" + NL);
    sb.append("objectclass: mozillaAbPersonAlpha" + NL);
    sb.append("givenName: " + adresse.getVorname() + NL);
    sb.append("sn: " + adresse.getName() + NL);
    sb.append("cn: " + adresse.getVorname() + " " + adresse.getName() + NL);
    sb.append("mail: " + adresse.getEmail() + NL);
    sb.append("modifytimestamp: 0Z" + NL);
    sb.append("homePhone: " + adresse.getTelefonprivat() + NL);
    sb.append("mobile: " + adresse.getHandy() + NL);
    sb.append("homeStreet: " + adresse.getStrasse() + NL);
    sb.append("mozillaHomeLocalityName: " + adresse.getOrt() + NL);
    sb.append("mozillaHomePostalCode: " + adresse.getPlz() + NL);
    sb.append(NL);
    return sb.toString();
  }
}