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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.io.Bankarbeitstage;
import de.willuhn.util.ApplicationException;

@RunWith(JUnit4.class)
public class BankarbeitstageTest
{
  @Test
  public void test01() throws ApplicationException
  {
    Bankarbeitstage bat = new Bankarbeitstage();
    Calendar cal = Calendar.getInstance();
    cal.set(2013, Calendar.DECEMBER, 30);
    cal = bat.getCalendar(cal, 1);
    Calendar cal2 = Calendar.getInstance();
    cal2.set(Calendar.YEAR, 2014);
    cal2.set(Calendar.MONTH, Calendar.JANUARY);
    cal2.set(Calendar.DAY_OF_MONTH, 2);
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    assertEquals(sdf.format(cal2.getTime()), sdf.format(cal.getTime()));
  }
}
