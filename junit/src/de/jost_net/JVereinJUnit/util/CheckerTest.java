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

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.util.Checker;

@RunWith(JUnit4.class)
public class CheckerTest
{
  @Test
  public void test01() throws RemoteException
  {
    assertTrue(Checker.isValidEmailAddress("willi.wichtig@jverein.de"));
  }

  @Test
  public void test02() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress(null));
  }

  @Test
  public void test03() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress(" willi.wichtig@jverein.de"));
  }

  @Test
  public void test04() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress("willi.wichtig@jverein.de "));
  }

  @Test
  public void test05() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress("willi wichtig@jverein.de"));
  }

  @Test
  public void test06() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress("willi@wichtig@jverein.de"));
  }

  @Test
  public void test07() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress("willi.wichtig.jverein.de"));
  }

  @Test
  public void test08() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress("willi.wichtig@jvereinde"));
  }

  @Test
  public void test09() throws RemoteException
  {
    assertTrue(!Checker.isValidEmailAddress("willi.wichtig.@jverein.de"));
  }

}