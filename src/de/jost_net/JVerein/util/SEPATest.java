package de.jost_net.JVerein.util;

import junit.framework.TestCase;

public class SEPATest extends TestCase
{
  // public void test01()
  // {
  // try
  // {
  // assertEquals("DE89370400440532013000",
  // SEPA.createIban("532013000", "37040044", "DE"));
  // assertEquals("AT611904300234573201",
  // SEPA.createIban("234573201", "19043", "AT"));
  // assertEquals("CH9300762011623852957",
  // SEPA.createIban("11623852957", "00762", "CH"));
  // }
  // catch (IBANException e)
  // {
  // e.printStackTrace();
  // }
  // }
  //
  // public void test02()
  // {
  // try
  // {
  // SEPA.createIban("532013000", "37040044", "AA");
  // fail("Hätte IBANException: 'Ungültiges Land: AA' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("Ungültiges Land: AA", e.getMessage());
  // }
  // }
  //
  // public void test03()
  // {
  // try
  // {
  // SEPA.createIban("532013000", "3704004", "DE");
  // fail("Hätte eine IBANException werfen sollen: Bankleitzahl hat falsche Länge für Deutschland");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("Bankleitzahl hat falsche Länge für Deutschland",
  // e.getMessage());
  // }
  // }
  //
  // public void test04()
  // {
  // try
  // {
  // SEPA.createIban("532013000", "370400444", "DE");
  // fail("Hätte eine IBANException werfen sollen: Bankleitzahl hat falsche Länge für Deutschland");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("Bankleitzahl hat falsche Länge für Deutschland",
  // e.getMessage());
  // }
  // }
  //
  // public void test05()
  // {
  // try
  // {
  // SEPA.createIban("12345678901", "37040044", "DE");
  // fail("Hätte eine IBANException werfen sollen: Kontonummer zu lang für Deutschland");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("Kontonummer zu lang für Deutschland", e.getMessage());
  // }
  // }
  //
  // public void test11()
  // {
  // try
  // {
  // assertTrue(SEPA.isValidIBAN("DE89370400440532013000"));
  // assertTrue(SEPA.isValidIBAN("AT611904300234573201"));
  // assertTrue(SEPA.isValidIBAN("CH9300762011623852957"));
  // }
  // catch (IBANException e)
  // {
  // e.printStackTrace();
  // }
  // }
  //
  // public void test12()
  // {
  // try
  // {
  // SEPA.isValidIBAN("AA89370400440532013000");
  // fail("Hätte IBANException: 'Ungültiges Land: AA' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("Ungültiges Land: AA", e.getMessage());
  // }
  // }
  //
  // public void test13()
  // {
  // try
  // {
  // SEPA.isValidIBAN("DE893704004405320130000");
  // fail("Hätte IBANException: 'Ungültige IBAN. Vorgeschrieben sind 22 für Deutschland' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals(
  // "Ungültige IBAN-Länge. Vorgeschrieben sind 22 Stellen für Deutschland",
  // e.getMessage());
  // }
  // }
  //
  // public void test14()
  // {
  // try
  // {
  // SEPA.isValidIBAN("AT6119043002345732010");
  // fail("Hätte IBANException: 'Ungültige IBAN. Vorgeschrieben sind 20 für Österreich' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals(
  // "Ungültige IBAN-Länge. Vorgeschrieben sind 20 Stellen für Österreich",
  // e.getMessage());
  // }
  // }
  //
  // public void test15()
  // {
  // try
  // {
  // SEPA.isValidIBAN("CH93007620116238529570");
  // fail("Hätte IBANException: 'Ungültige IBAN. Vorgeschrieben sind 21 für Schweiz' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals(
  // "Ungültige IBAN-Länge. Vorgeschrieben sind 21 Stellen für Schweiz",
  // e.getMessage());
  // }
  // }
  //
  // public void test16()
  // {
  // try
  // {
  // SEPA.isValidIBAN(null);
  // fail("Hätte IBANException: 'IBAN ist leer' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("IBAN ist leer", e.getMessage());
  // }
  // }
  //
  // public void test17()
  // {
  // try
  // {
  // SEPA.isValidIBAN("DE");
  // fail("Hätte IBANException: 'Ungültige IBAN. Landeskennung und/oder Prüfziffer fehlen' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("Ungültige IBAN. Landeskennung und/oder Prüfziffer fehlen",
  // e.getMessage());
  // }
  // }
  //
  // public void test21()
  // {
  // try
  // {
  // assertTrue(SEPA.isValidBIC("WELADED1WDB"));
  // }
  // catch (IBANException e)
  // {
  // fail("Fehler " + e);
  // }
  // }
  //
  // public void test22()
  // {
  // try
  // {
  // assertTrue(SEPA.isValidBIC("WELADED1"));
  // }
  // catch (IBANException e)
  // {
  // fail("Fehler " + e);
  // }
  // }
  //
  // public void test23()
  // {
  // try
  // {
  // SEPA.isValidBIC("WELAXXD1WDB");
  // fail("Hätte IBANException: 'Ungültige IBAN. Landeskennung und/oder Prüfziffer fehlen' werfen müssen");
  // }
  // catch (IBANException e)
  // {
  // assertEquals("Ungültiges Land: XX", e.getMessage());
  // }
  // }

}
