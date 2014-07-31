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

import java.util.Map;

public class ATBank
{
  private String blz;

  private String name;

  private String bic;

  private String kennzeichen;

  public ATBank(Map<String, ? super Object> bankMap)
  {
    blz = (String) bankMap.get("Bankleitzahl");
    name = (String) bankMap.get("Bankenname");
    bic = (String) bankMap.get("SWIFT-Code");
    kennzeichen = (String) bankMap.get("Kennzeichen");
  }

  public String getBlz()
  {
    return blz;
  }

  public String getName()
  {
    if (name.length() > 27)
    {
      name = name.substring(0, 27);
    }
    return name;
  }

  public String getBic()
  {
    return bic;
  }

  public String getKennzeichen()
  {
    return kennzeichen;
  }

  @Override
  public String toString()
  {
    return (blz + ", " + bic + ", " + name + ", " + kennzeichen);
  }

}
