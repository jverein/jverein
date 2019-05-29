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
package de.jost_net.JVereinJUnit.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.jost_net.JVerein.util.EmailValidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckerTest
{
  @Test
  public void test01()
  {
    assertTrue(
        EmailValidator.isValid("willi.wichtig@jverein.de"));
  }

  @Test
  public void test02()
  {
    assertFalse(EmailValidator.isValid(null));
  }

  @Test
  public void test05()
  {
    assertFalse(EmailValidator.isValid("willi wichtig@jverein.de"));
  }

  @Test
  public void test06()
  {
    assertFalse(EmailValidator.isValid("willi@wichtig@jverein.de"));
  }

  @Test
  public void test07()
  {
    assertFalse(EmailValidator.isValid("willi.wichtig.jverein.de"));
  }

  @Test
  public void test08()
  {
    assertFalse(EmailValidator.isValid("willi.wichtig@jvereinde"));
  }

  @Test
  public void test09()
  {
    assertFalse(EmailValidator.isValid("willi.wichtig.@jverein.de"));
  }

  @Test
  public void test10()
  {
    assertTrue(EmailValidator.isValid("jupp.schmitz@köln.de"));
  }

  @Test
  public void test11()
  {
    assertTrue(EmailValidator.isValid("name@internetsite.shop"));
  }

  @Test
  public void test12()
  {
    assertTrue(EmailValidator.isValid("Gruppenname: erste.adresse@example-eins.tld, zweite.adresse@example-zwei.tld;"));
  }

  @Test
  public void test13()
  {
    assertFalse(EmailValidator.isValid("Gruppenname: erste.adresse@example-eins.tld, zweite.adresse@example-zweitld;"));
  }

  @Test
  public void test14()
  {
    assertFalse(EmailValidator.isValid("Gruppenname: erste.adresse@example-eins.tld, zweite.adresse;"));
  }

  @Test
  public void test15()
  {
    assertFalse(EmailValidator.isValid("willi.wichtig"));
  }

}