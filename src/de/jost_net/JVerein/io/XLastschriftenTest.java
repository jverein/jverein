/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.1  2011-02-12 09:40:32  jost
 * Vorbereitung kompakte Abbuchung
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import junit.framework.TestCase;

public class XLastschriftenTest extends TestCase
{

  public void test01()
  {
    XLastschriften ls = new XLastschriften();
    ls.add(getFall01());
    assertEquals(1, ls.getAnzahlLastschriften());
    assertEquals(100d, ls.getLastschriften().get(0).getBetrag());
    assertEquals("Nora Nolte", ls.getLastschriften().get(0)
        .getZahlungspflichtigen(0));
    assertEquals("Mitgliedsbeitrag 2011", ls.getLastschriften().get(0)
        .getVerwendungszweck(0));
    assertEquals("Nora", ls.getLastschriften().get(0).getVerwendungszweck(1));
  }

  public void test02()
  {
    XLastschriften ls = new XLastschriften();
    ls.add(getFall01());
    ls.add(getFall02());
    ls.add(getFall03());
    assertEquals(150d, ls.getSummeLastschriften());
    ls.compact();
    assertEquals(1, ls.getAnzahlLastschriften());
    assertEquals(150d, ls.getLastschriften().get(0).getBetrag());
    assertEquals("Name Zahlungspflichtiger", "Nora Nolte", ls
        .getLastschriften().get(0).getZahlungspflichtigen(0));
    assertEquals("Mitgliedsbeitrag 201 100,00", ls.getLastschriften().get(0)
        .getVerwendungszweck(0));
    assertEquals(27, ls.getLastschriften().get(0).getVerwendungszweck(0)
        .length());
    assertEquals("Zusatzbetrag 1        20,00", ls.getLastschriften().get(0)
        .getVerwendungszweck(2));
    assertEquals("Zusatzbetrag Wintertu 30,00", ls.getLastschriften().get(0)
        .getVerwendungszweck(4));
    assertEquals(150d, ls.getSummeLastschriften());
  }

  public void test03()
  {
    XLastschriften ls = new XLastschriften();
    for (int i = 0; i < 14; i++)
    {
      ls.add(getFall01());
    }
    ls.add(getFall02());
    ls.add(getFall03());
    assertEquals(1450d, ls.getSummeLastschriften());
    ls.compact();
    assertEquals(16, ls.getAnzahlLastschriften());
    assertEquals(100d, ls.getLastschriften().get(0).getBetrag());
    assertEquals(30d, ls.getLastschriften().get(15).getBetrag());
    assertEquals(1450d, ls.getSummeLastschriften());
  }

  private XLastschrift getFall01()
  {
    XLastschrift l = getDefaultLastschrift();
    l.setBetrag(100);
    l.addVerwendungszweck("Mitgliedsbeitrag 2011");
    l.addVerwendungszweck("Nora");
    return l;
  }

  private XLastschrift getFall02()
  {
    XLastschrift l = getDefaultLastschrift();
    l.setBetrag(20);
    l.addVerwendungszweck("Zusatzbetrag 1");
    l.addVerwendungszweck("Nora");
    return l;
  }

  private XLastschrift getFall03()
  {
    XLastschrift l = getDefaultLastschrift();
    l.setBetrag(30);
    l.addVerwendungszweck("Zusatzbetrag Winterturnier");
    l.addVerwendungszweck("Nora");
    return l;
  }

  private XLastschrift getDefaultLastschrift()
  {
    XLastschrift l = new XLastschrift();
    l.addZahlungspflichtigen("Nora Nolte");
    l.setBlz(11111111);
    l.setKonto(4711);
    return l;
  }
}
