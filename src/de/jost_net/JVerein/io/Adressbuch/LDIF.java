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