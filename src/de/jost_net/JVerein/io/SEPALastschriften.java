/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

import java.math.BigDecimal;
import java.util.ArrayList;

public class SEPALastschriften
{
  private ArrayList<SEPALastschrift> lastschriften = new ArrayList<SEPALastschrift>();

  public SEPALastschriften()
  {

  }

  public void add(SEPALastschrift lastschrift)
  {
    lastschriften.add(lastschrift);
  }

  public ArrayList<SEPALastschrift> getLastschriften()
  {
    return lastschriften;
  }

  public int getAnzahlLastschriften()
  {
    return lastschriften.size();
  }

  public BigDecimal getSummeLastschriften()
  {
    BigDecimal sum = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    for (SEPALastschrift l : lastschriften)
    {
      sum = sum.add(l.getBetrag());
    }
    return sum;
  }
}
