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
import java.text.DecimalFormat;
import java.util.ArrayList;

public class XLastschrift
{
  private ArrayList<String> zahlungspflichtiger = new ArrayList<String>();

  private ArrayList<String> verwendungszweck = new ArrayList<String>();

  private BigDecimal betrag;

  private int blz;

  private long konto;

  public final static DecimalFormat DECIMALFORMAT = new DecimalFormat(
      "###,##0.00");

  public XLastschrift()
  {
  }

  public void addZahlungspflichtigen(String zahlungspflichtiger)
  {
    this.zahlungspflichtiger.add(zahlungspflichtiger);
  }

  public String getZahlungspflichtigen(int pos)
  {
    return this.zahlungspflichtiger.get(pos);
  }

  public int getAnzahlZahlungspflichtige()
  {
    return this.zahlungspflichtiger.size();
  }

  public void addVerwendungszweck(String verwendungszweck)
  {
    this.verwendungszweck.add(verwendungszweck);
  }

  public String getVerwendungszweck(int pos)
  {
    return verwendungszweck.get(pos);
  }

  public void modifyVerwendungszweck(int pos, String verwendungszweck,
      BigDecimal betrag)
  {
    String btrg = DECIMALFORMAT.format(betrag);
    int restlaenge = 27 - btrg.length() - 1;
    if (verwendungszweck.length() < restlaenge)
    {
      while (verwendungszweck.length() < restlaenge)
      {
        verwendungszweck += " ";
      }
    }
    else
    {
      verwendungszweck = verwendungszweck.substring(0, restlaenge);
    }
    this.verwendungszweck.set(pos, verwendungszweck + " " + btrg);
  }

  public int getAnzahlVerwendungszwecke()
  {
    return verwendungszweck.size();
  }

  public void setBetrag(BigDecimal betrag)
  {
    this.betrag = betrag;
  }

  public BigDecimal getBetrag()
  {
    return betrag;
  }

  public void setBlz(int blz)
  {
    this.blz = blz;
  }

  public int getBlz()
  {
    return this.blz;
  }

  public void setKonto(long konto)
  {
    this.konto = konto;
  }

  public long getKonto()
  {
    return this.konto;
  }

  public String getBankverbindung()
  {
    return this.blz + "" + this.konto;
  }

  public void add(XLastschrift ls)
  {
    betrag = betrag.add(ls.getBetrag());
    for (int i = 0; i < ls.getAnzahlVerwendungszwecke(); i++)
    {
      addVerwendungszweck(ls.getVerwendungszweck(i));
    }
  }

  @Override
  public String toString()
  {
    StringBuilder zpfl = new StringBuilder();
    for (String z : zahlungspflichtiger)
    {
      zpfl.append(z);
    }
    return "BLZ: " + getBlz() + ", Konto: " + getKonto() + ", Betrag: "
        + getBetrag() + ", Zahlungspflichtiger: " + zpfl.toString()
        + ", Verwendungszweck: " + verwendungszweck;
  }
}
