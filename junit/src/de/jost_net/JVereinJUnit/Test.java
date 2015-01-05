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
package de.jost_net.JVereinJUnit;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import de.jost_net.JVereinJUnit.io.AbrechnungSEPA;
import de.jost_net.JVereinJUnit.io.AltersgruppenParserTest;
import de.jost_net.JVereinJUnit.io.BankarbeitstageTest;
import de.jost_net.JVereinJUnit.io.BeitragsUtilTest;
import de.jost_net.JVereinJUnit.io.SuchbetragTest;
import de.jost_net.JVereinJUnit.io.Adressbuch.AdressaufbereitungTest;
import de.jost_net.JVereinJUnit.util.CheckerTest;
import de.jost_net.JVereinJUnit.util.TableColumnReplacerTest;

public class Test
{
  /**
   * Startet die Tests.
   * 
   * @throws Exception
   */
  public static void start() throws Exception
  {
    Result result = JUnitCore.runClasses(AbrechnungSEPA.class,
        AdressaufbereitungTest.class, AltersgruppenParserTest.class,
        BankarbeitstageTest.class, BeitragsUtilTest.class, CheckerTest.class,
        SuchbetragTest.class, TableColumnReplacerTest.class, Settings.class,
        /* ShutdownTest muss der letzte Test sein */ShutdownTest.class);

    for (Failure failure : result.getFailures())
    {
      System.out.println(failure.toString());
    }
    if (result.getFailureCount() == 0)
    {
      System.out.println("Keine Fehler aufgetreten");
    }
  }
}