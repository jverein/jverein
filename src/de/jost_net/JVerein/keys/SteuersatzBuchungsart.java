    
    /**********************************************************************
 * Copyright (c) by Vinzent Rudolf
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
 * vinzent.rudolf@web.de
 * www.jverein.de
 **********************************************************************/

package de.jost_net.JVerein.keys;

import java.util.ArrayList;

/**
 * Steuersatz: Entweder 7% oder 19%
 */

public class SteuersatzBuchungsart
{
  private double steuersatz_;

  public SteuersatzBuchungsart(double steuersatz)
  {
    steuersatz_ = steuersatz;
  }

  public double getSteuersatz()
  {
    return steuersatz_;
  }

  public String getText()
  {
    return get(steuersatz_);
  }

  @Override
  public String toString()
  {
    return get(steuersatz_);
  }

  public static String get(double steuersatz)
  {
    if (steuersatz == 0.00) {
      return "";
    }
    else {
      return String.format ("%.2f", steuersatz) + "%";
    }
  }
  
  public static ArrayList<SteuersatzBuchungsart> getArray()
  {
    ArrayList<SteuersatzBuchungsart> ret = new ArrayList<>();
    ret.add(new SteuersatzBuchungsart(0));
    ret.add(new SteuersatzBuchungsart(19));
    ret.add(new SteuersatzBuchungsart(7));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SteuersatzBuchungsart)
    {
      SteuersatzBuchungsart v = (SteuersatzBuchungsart) obj;
      return (getSteuersatz() == v.getSteuersatz());
    }
    return false;
  }
}