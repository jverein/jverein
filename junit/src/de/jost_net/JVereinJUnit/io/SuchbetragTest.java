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
package de.jost_net.JVereinJUnit.io;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.io.Suchbetrag;
import de.jost_net.JVerein.io.Suchbetrag.Suchstrategie;

@RunWith(JUnit4.class)
public class SuchbetragTest
{
  @Test
  public void test01() throws Exception
  {
    Suchbetrag sb = new Suchbetrag(null);
    org.junit.Assert.assertEquals(Suchstrategie.KEINE, sb.getSuchstrategie());
  }

  @Test
  public void test02() throws Exception
  {
    Suchbetrag sb = new Suchbetrag("1,23");
    Assert.assertEquals(new BigDecimal("1.23"), sb.getBetrag());
    Assert.assertEquals(Suchstrategie.GLEICH, sb.getSuchstrategie());
  }

  @Test
  public void test02m() throws Exception
  {
    Suchbetrag sb = new Suchbetrag("-1,23");
    Assert.assertEquals(new BigDecimal("-1.23"), sb.getBetrag());
    Assert.assertEquals(Suchstrategie.GLEICH, sb.getSuchstrategie());
  }

  @Test
  public void test03() throws Exception
  {
    Suchbetrag sb = new Suchbetrag("=1,23");
    Assert.assertEquals(new BigDecimal("1.23"), sb.getBetrag());
    Assert.assertEquals(Suchstrategie.GLEICH, sb.getSuchstrategie());
  }

  @Test
  public void test04() throws Exception
  {
    Suchbetrag sb = new Suchbetrag(">1,23");
    Assert.assertEquals(new BigDecimal("1.23"), sb.getBetrag());
    Assert.assertEquals(Suchstrategie.GRÖSSER, sb.getSuchstrategie());
  }

  @Test
  public void test05() throws Exception
  {
    Suchbetrag sb = new Suchbetrag(">=1,23");
    Assert.assertEquals(new BigDecimal("1.23"), sb.getBetrag());
    Assert.assertEquals(Suchstrategie.GRÖSSERGLEICH, sb.getSuchstrategie());
  }

  @Test
  public void test06() throws Exception
  {
    Suchbetrag sb = new Suchbetrag("<1,23");
    Assert.assertEquals(new BigDecimal("1.23"), sb.getBetrag());
    Assert.assertEquals(Suchstrategie.KLEINER, sb.getSuchstrategie());
  }

  @Test
  public void test07() throws Exception
  {
    Suchbetrag sb = new Suchbetrag("<=1,23");
    Assert.assertEquals(new BigDecimal("1.23"), sb.getBetrag());
    Assert.assertEquals(Suchstrategie.KLEINERGLEICH, sb.getSuchstrategie());
  }

  @Test
  public void test08() throws Exception
  {
    Suchbetrag sb = new Suchbetrag("1,23..2,34");
    Assert.assertEquals(new BigDecimal("1.23"), sb.getBetrag());
    Assert.assertEquals(new BigDecimal("2.34"), sb.getBetrag2());
    Assert.assertEquals(Suchstrategie.BEREICH, sb.getSuchstrategie());
  }

  @Test
  public void test09() throws Exception
  {
    try
    {
      new Suchbetrag("1,23...2,34");
      Assert.fail("Hier sollte eine Exception aufgetreten sein");
    }
    catch (Exception e)
    {
      Assert.assertEquals("Wert ungültig", e.getMessage());
    }
  }

  @Test
  public void test10() throws Exception
  {
    try
    {
      new Suchbetrag("=>1,23");
      Assert.fail("Hier sollte eine Exception aufgetreten sein");
    }
    catch (Exception e)
    {
      Assert.assertEquals("Wert ungültig", e.getMessage());
    }
  }

  @Test
  public void test11() throws Exception
  {
    try
    {
      new Suchbetrag("1.23");
      Assert.fail("Hier sollte eine Exception aufgetreten sein");
    }
    catch (Exception e)
    {
      Assert.assertEquals("Wert ungültig", e.getMessage());
    }
  }

  @Test
  public void test12() throws Exception
  {
    try
    {
      new Suchbetrag("X1,23");
      Assert.fail("Hier sollte eine Exception aufgetreten sein");
    }
    catch (Exception e)
    {
      Assert.assertEquals("Wert ungültig", e.getMessage());
    }
  }
}
