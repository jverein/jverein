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

import junit.framework.TestCase;

/**
 * Testet den Shutdown.
 */
public class ShutdownTest extends TestCase
{

  /**
   * Constructor for ShutdownTest.
   */
  public ShutdownTest(String arg0)
  {
    super(arg0);
  }

  /**
   * Testet den Shutdown.
   * 
   * @throws Exception
   */
  public void testShutdown() throws Exception
  {
    // Muss aus einem extra Test heraus erfolgen
    Thread t = new Thread()
    {
      @Override
      public void run()
      {
        System.exit(0);
      }
    };
    t.start();
  }
}