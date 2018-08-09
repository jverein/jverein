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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.io.AltersgruppenParser;
import de.jost_net.JVerein.util.VonBis;
import de.willuhn.util.ApplicationException;

@RunWith(JUnit4.class)
public class AltersgruppenParserTest
{
  @Test
  public void test01() throws ApplicationException
  {
    AltersgruppenParser ap = new AltersgruppenParser(
        "0-5,6-10,11-16,17-25,25-100");
    VonBis vb = ap.getNext();
    assertEquals(0, vb.getVon());
    assertEquals(5, vb.getBis());
    vb = ap.getNext();
    assertEquals(6, vb.getVon());
    assertEquals(10, vb.getBis());
    vb = ap.getNext();
    assertEquals(11, vb.getVon());
    assertEquals(16, vb.getBis());
    vb = ap.getNext();
    assertEquals(17, vb.getVon());
    assertEquals(25, vb.getBis());
    vb = ap.getNext();
    assertEquals(25, vb.getVon());
    assertEquals(100, vb.getBis());
  }

  @SuppressWarnings("unused")
  @Test
  public void test02() throws ApplicationException
  {
    new AltersgruppenParser("0-5,4-10");
  }
}
