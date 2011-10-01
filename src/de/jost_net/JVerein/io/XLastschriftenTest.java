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
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class XLastschriftenTest extends TestCase
{

  public void test01()
  {
    XLastschriften ls = new XLastschriften();
    ls.add(getFall01());
    assertEquals(1, ls.getAnzahlLastschriften());
    assertEquals(0, new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP)
        .compareTo(ls.getLastschriften().get(0).getBetrag()));
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
    assertEquals(0, new BigDecimal(150).setScale(2, BigDecimal.ROUND_HALF_UP)
        .compareTo(ls.getSummeLastschriften()));
    ls.compact();
    assertEquals(1, ls.getAnzahlLastschriften());
    assertEquals(0, new BigDecimal(150).setScale(2, BigDecimal.ROUND_HALF_UP)
        .compareTo(ls.getLastschriften().get(0).getBetrag()));
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
    assertEquals(0, new BigDecimal(150).setScale(2, BigDecimal.ROUND_HALF_UP)
        .compareTo(ls.getSummeLastschriften()));
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
    BigDecimal bd = new BigDecimal(1450).setScale(2, BigDecimal.ROUND_HALF_UP);
    bd.setScale(2);
    assertEquals(0, bd.compareTo(ls.getSummeLastschriften()));
    ls.compact();
    assertEquals(16, ls.getAnzahlLastschriften());
    assertEquals(0, new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP)
        .compareTo(ls.getLastschriften().get(0).getBetrag()));
    assertEquals(0, new BigDecimal(30d).setScale(2, BigDecimal.ROUND_HALF_UP)
        .compareTo(ls.getLastschriften().get(15).getBetrag()));
    assertEquals(0, bd.compareTo(ls.getSummeLastschriften()));
  }

  public void test04()
  {
    XLastschriften ls = new XLastschriften();
    for (int i = 0; i < 3; i++)
    {
      ls.add(getFall04());
    }
    ls.compact();
    BigDecimal bd = new BigDecimal(39.6).setScale(2, BigDecimal.ROUND_HALF_UP);
    System.out.println(ls.getLastschriften().get(0).getBetrag());
    assertEquals(0, bd.compareTo(ls.getLastschriften().get(0).getBetrag()));
  }

  private XLastschrift getFall01()
  {
    XLastschrift l = getDefaultLastschrift();
    l.setBetrag(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP));
    l.addVerwendungszweck("Mitgliedsbeitrag 2011");
    l.addVerwendungszweck("Nora");
    return l;
  }

  private XLastschrift getFall02()
  {
    XLastschrift l = getDefaultLastschrift();
    l.setBetrag(new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_UP));
    l.addVerwendungszweck("Zusatzbetrag 1");
    l.addVerwendungszweck("Nora");
    return l;
  }

  private XLastschrift getFall03()
  {
    XLastschrift l = getDefaultLastschrift();
    l.setBetrag(new BigDecimal(30).setScale(2, BigDecimal.ROUND_HALF_UP));
    l.addVerwendungszweck("Zusatzbetrag Winterturnier");
    l.addVerwendungszweck("Nora");
    return l;
  }

  private XLastschrift getFall04()
  {
    XLastschrift l = getDefaultLastschrift();
    l.setBetrag(new BigDecimal(13.2).setScale(2, BigDecimal.ROUND_HALF_UP));
    l.addVerwendungszweck("Mitgliedsbeitrag");
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
