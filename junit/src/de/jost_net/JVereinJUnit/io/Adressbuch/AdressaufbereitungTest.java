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
package de.jost_net.JVereinJUnit.io.Adressbuch;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.gui.input.GeschlechtInput;
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
        "Deutschland", GeschlechtInput.MAENNLICH);
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
        "Bahnhofstr. 1", "12345", "Testenhausen", null,
        GeschlechtInput.MAENNLICH);
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
        "Bahnhofstr. 1", "12345", "Testenhausen", null,
        GeschlechtInput.MAENNLICH);
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
        null, null, null, GeschlechtInput.MAENNLICH);
    assertEquals("Willi Wichtig\n", Adressaufbereitung.getAdressfeld(adr));
    assertEquals("", Adressaufbereitung.getAnschrift(adr));
    assertEquals("Wichtig, Willi", Adressaufbereitung.getNameVorname(adr));
    assertEquals("Willi Wichtig", Adressaufbereitung.getVornameName(adr));
  }

  @Test
  public void test05() throws RemoteException
  {
    IAdresse adr = getAdresse("n", "", "", "Willi", "Wichtig", "", "", "", "",
        "", GeschlechtInput.MAENNLICH);
    assertEquals("Willi Wichtig\n", Adressaufbereitung.getAdressfeld(adr));
    assertEquals("", Adressaufbereitung.getAnschrift(adr));
    assertEquals("Wichtig, Willi", Adressaufbereitung.getNameVorname(adr));
    assertEquals("Willi Wichtig", Adressaufbereitung.getVornameName(adr));
  }

  @Test
  public void test06() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, null, "Willi", "Wichtig", null, null,
        null, null, null, GeschlechtInput.MAENNLICH);
    assertEquals("Sehr geehrter Herr Wichtig,",
        Adressaufbereitung.getAnredeFoermlich(adr));
    assertEquals("Hallo Willi,", Adressaufbereitung.getAnredeDu(adr));
  }

  @Test
  public void test07() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, null, "Luise", "Lustig", null, null,
        null, null, null, GeschlechtInput.WEIBLICH);
    assertEquals("Sehr geehrte Frau Lustig,",
        Adressaufbereitung.getAnredeFoermlich(adr));
    assertEquals("Hallo Luise,", Adressaufbereitung.getAnredeDu(adr));
  }

  @Test
  public void test08() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, null, "Neutrum", "Neutral", null,
        null, null, null, null, GeschlechtInput.OHNEANGABE);
    assertEquals("Guten Tag Neutrum Neutral,",
        Adressaufbereitung.getAnredeFoermlich(adr));
    assertEquals("Hallo Neutrum,", Adressaufbereitung.getAnredeDu(adr));
  }

  @Test
  public void test09() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, "Dr.", "Willi", "Wichtig", null, null,
        null, null, null, GeschlechtInput.MAENNLICH);
    assertEquals("Sehr geehrter Herr Dr. Wichtig,",
        Adressaufbereitung.getAnredeFoermlich(adr));
    assertEquals("Hallo Willi,", Adressaufbereitung.getAnredeDu(adr));
  }

  @Test
  public void test10() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, "Dr.", "Luise", "Lustig", null, null,
        null, null, null, GeschlechtInput.WEIBLICH);
    assertEquals("Sehr geehrte Frau Dr. Lustig,",
        Adressaufbereitung.getAnredeFoermlich(adr));
    assertEquals("Hallo Luise,", Adressaufbereitung.getAnredeDu(adr));
  }

  @Test
  public void test11() throws RemoteException
  {
    IAdresse adr = getAdresse("n", null, "Dr.", "Neutrum", "Neutral", null,
        null, null, null, null, GeschlechtInput.OHNEANGABE);
    assertEquals("Guten Tag Dr. Neutrum Neutral,",
        Adressaufbereitung.getAnredeFoermlich(adr));
    assertEquals("Hallo Neutrum,", Adressaufbereitung.getAnredeDu(adr));
  }

  private static IAdresse getAdresse(final String personenart,
      final String anrede, final String titel, final String vorname,
      final String name, final String adressierungszusatz,
      final String strasse, final String plz, final String ort,
      final String staat, final String geschlecht)
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

      @Override
      public String getGeschlecht() throws RemoteException
      {
        return geschlecht;
      }

    };

  }

}