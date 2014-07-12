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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.Zahlungstermin;
import de.jost_net.JVerein.rmi.Beitragsgruppe;

public class BeitragsUtil
{
  public static double getBeitrag(Beitragsmodel bm, Zahlungstermin zt, int zr,
      Beitragsgruppe bg, Date stichtag, Date eintritt, Date austritt)
      throws RemoteException
  {
    double betr = 0;
    if (eintritt != null && eintritt.after(stichtag))
    {
      return 0;
    }
    if (austritt != null && austritt.before(stichtag))
    {
      return 0;
    }
    switch (bm)
    {
      case GLEICHERTERMINFUERALLE:
        betr = bg.getBetrag();
        break;
      case MONATLICH12631:
        BigDecimal bbetr = new BigDecimal(bg.getBetrag());
        bbetr = bbetr.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal bmonate = new BigDecimal(zr);
        bbetr = bbetr.multiply(bmonate);
        betr = bbetr.doubleValue();
        break;
      case FLEXIBEL:
        switch (zt)
        {
          case MONATLICH:
            betr = bg.getBetragMonatlich();
            break;
          case VIERTELJAEHRLICH1:
          case VIERTELJAEHRLICH2:
          case VIERTELJAEHRLICH3:
            betr = bg.getBetragVierteljaehrlich();
            break;
          case HALBJAEHRLICH1:
          case HALBJAEHRLICH2:
          case HALBJAEHRLICH3:
          case HALBJAEHRLICH4:
          case HALBJAEHRLICH5:
          case HALBJAEHRLICH6:
            betr = bg.getBetragHalbjaehrlich();
            break;
          case JAERHLICH01:
          case JAERHLICH02:
          case JAERHLICH03:
          case JAERHLICH04:
          case JAERHLICH05:
          case JAERHLICH06:
          case JAERHLICH07:
          case JAERHLICH08:
          case JAERHLICH09:
          case JAERHLICH10:
          case JAERHLICH11:
          case JAERHLICH12:
            betr = bg.getBetragJaehrlich();
            break;
        }
        break;
    }
    return betr;
  }
}
