package de.jost_net.JVerein.io.Adressbuch;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Mitglied;

public class Txt
{
  private OutputStreamWriter out;

  private String separator;

  public Txt(String filename, String encoding, String separator)
      throws Exception
  {
    // Vermerk für mich: Encodings siehe
    // http://www.cafeconleche.org/books/xmljava/chapters/ch03s03.html
    out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(
        filename)), encoding);
    out.write("\"Name\"" + separator + "\"Vorname\"" + separator
        + "\"Strasse\"" + separator + "\"PLZ\"" + separator + "\"Ort\""
        + separator + "\"Anzeigename\"" + separator + "\"Email\"" + separator
        + "\"TelefonPrivat\"" + separator + "\"TelefonMobil\"\n");
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
    sb.append("\"" + mitglied.getVorname() + " " + mitglied.getName() + "\""
        + separator);
    sb.append("\"" + mitglied.getEmail() + "\"" + separator);
    sb.append("\"" + mitglied.getTelefonprivat() + "\"" + separator);
    sb.append("\"" + mitglied.getHandy() + "\"" + separator);
    sb.append("\n");
    return sb.toString();
  }
}