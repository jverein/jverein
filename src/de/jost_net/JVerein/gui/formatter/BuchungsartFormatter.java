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
package de.jost_net.JVerein.gui.formatter;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.BuchungsartSort;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.logging.Logger;

public class BuchungsartFormatter implements Formatter
{
  @Override
  public String format(Object o)
  {
    Buchungsart ba = (Buchungsart) o;
    if (ba == null)
    {
      return null;
    }
    String bez = null;
    try
    {
      switch (Einstellungen.getEinstellung().getBuchungsartSort())
      {
        case BuchungsartSort.NACH_NUMMER:
          bez = ba.getNummer() + " - " + ba.getBezeichnung(); 
          break;
        case BuchungsartSort.NACH_BEZEICHNUNG_NR:
          bez = ba.getBezeichnung() + " (" + ba.getNummer() + ")"; 
      	  break;
        default:
          bez = ba.getBezeichnung();
      	  break;
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    return bez;
  }
}
