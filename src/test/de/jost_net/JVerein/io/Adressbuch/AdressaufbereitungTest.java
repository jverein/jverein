package test.de.jost_net.JVerein.io.Adressbuch;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.io.IAdresse;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;

@RunWith(JUnit4.class)
public class AdressaufbereitungTest
{
  @Test
  public void test01() throws RemoteException
  {
    IAdresse adr = getAdresse("n", "Herrn", "Dr.", "Willi", "Wichtig",
        "bei Lieschen Müller", "Bahnhofstr. 1", "12345", "Testenhausen",
        "Deutschland");
    assertEquals(
        "Herrn\nDr. Willi Wichtig\nbei Lieschen Müller\nBahnhofstr. 1\n12345 Testenhausen\nDeutschland",
        Adressaufbereitung.getAdressfeld(adr));
    assertEquals(
        "bei Lieschen Müller, Bahnhofstr. 1, 12345 Testenhausen, Deutschland",
        Adressaufbereitung.getAnschrift(adr));
    assertEquals("Wichtig, Dr. Willi", Adressaufbereitung.getNameVorname(adr));
    assertEquals("Dr. Willi Wichtig", Adressaufbereitung.getVornameName(adr));
  }

  @Test
  public void test02() throws RemoteException
  {
    IAdresse adr = getAdresse("n", "Herrn", "Dr.", "Willi", "Wichtig", null,
        "Bahnhofstr. 1", "12345", "Testenhausen", null);
    assertEquals("Herrn\nDr. Willi Wichtig\nBahnhofstr. 1\n12345 Testenhausen",
        Adressaufbereitung.getAdressfeld(adr));
    assertEquals("Bahnhofstr. 1, 12345 Testenhausen",
        Adressaufbereitung.getAnschrift(adr));
    assertEquals("Wichtig, Dr. Willi", Adressaufbereitung.getNameVorname(adr));
    assertEquals("Dr. Willi Wichtig", Adressaufbereitung.getVornameName(adr));
  }

  @Test
  public void test03() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, null, "Willi", "Wichtig", null,
        "Bahnhofstr. 1", "12345", "Testenhausen", null);
    assertEquals("Willi Wichtig\nBahnhofstr. 1\n12345 Testenhausen",
        Adressaufbereitung.getAdressfeld(adr));
    assertEquals("Bahnhofstr. 1, 12345 Testenhausen",
        Adressaufbereitung.getAnschrift(adr));
    assertEquals("Wichtig, Willi", Adressaufbereitung.getNameVorname(adr));
    assertEquals("Willi Wichtig", Adressaufbereitung.getVornameName(adr));
  }

  @Test
  public void test04() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, null, "Willi", "Wichtig", null, null,
        null, null, null);
    assertEquals("Willi Wichtig\n", Adressaufbereitung.getAdressfeld(adr));
    assertEquals("", Adressaufbereitung.getAnschrift(adr));
    assertEquals("Wichtig, Willi", Adressaufbereitung.getNameVorname(adr));
    assertEquals("Willi Wichtig", Adressaufbereitung.getVornameName(adr));
  }

  @Test
  public void test05() throws RemoteException
  {
    IAdresse adr = getAdresse("n", "", "", "Willi", "Wichtig", "", "", "", "",
        "");
    assertEquals("Willi Wichtig\n", Adressaufbereitung.getAdressfeld(adr));
    assertEquals("", Adressaufbereitung.getAnschrift(adr));
    assertEquals("Wichtig, Willi", Adressaufbereitung.getNameVorname(adr));
    assertEquals("Willi Wichtig", Adressaufbereitung.getVornameName(adr));
  }

  private static IAdresse getAdresse(final String personenart,
      final String anrede, final String titel, final String vorname,
      final String name, final String adressierungszusatz,
      final String strasse, final String plz, final String ort,
      final String staat)
  {
    return new IAdresse()
    {
      @Override
      public String getPersonenart() throws RemoteException
      {
        return personenart;
      }

      @Override
      public String getAnrede() throws RemoteException
      {
        return anrede;
      }

      @Override
      public String getTitel() throws RemoteException
      {
        return titel;
      }

      @Override
      public String getVorname() throws RemoteException
      {
        return vorname;
      }

      @Override
      public String getName() throws RemoteException
      {
        return name;
      }

      @Override
      public String getAdressierungszusatz() throws RemoteException
      {
        return adressierungszusatz;
      }

      @Override
      public String getStrasse() throws RemoteException
      {
        return strasse;
      }

      @Override
      public String getPlz() throws RemoteException
      {
        return plz;
      }

      @Override
      public String getOrt() throws RemoteException
      {
        return ort;
      }

      @Override
      public String getStaat() throws RemoteException
      {
        return staat;
      }

    };

  }

}