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
package de.jost_net.JVerein.io;

import de.willuhn.logging.Logger;

public class BuchungsuebernahmeThread implements Runnable
{
  private static final int DEFAULTCOUNTDOWN = 2;

  private int countdown;

  private static BuchungsuebernahmeThread instance = null;

  private BuchungsuebernahmeThread()
  {
    countdown = DEFAULTCOUNTDOWN;
    new Thread(this).start();
  }

  public static BuchungsuebernahmeThread getInstance()
  {
    if (instance == null)
    {
      instance = new BuchungsuebernahmeThread();
    }
    return instance;
  }

  public void newStart()
  {
    countdown = DEFAULTCOUNTDOWN;
  }

  @Override
  public void run()
  {
    while (true)
    {
      try
      {
        Thread.sleep(5000);
        countdown--;
        if (countdown == 0)
        {
          new Buchungsuebernahme();
        }
      }
      catch (InterruptedException e)
      {
        Logger.error("Fehler", e);
      }
    }

  }
}
