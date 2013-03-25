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

public class SEPALastschrift
{
  private String referenz = null;

  private String zahlungspflichtiger = null;

  private String verwendungszweck = null;

  private BigDecimal betrag;

  private String bic = null;

  private String iban = null;

  public final static DecimalFormat DECIMALFORMAT = new DecimalFormat(
      "###,##0.00");

  public SEPALastschrift()
  {
  }

  public void setReferenz(String referenz)
  {
    this.referenz = referenz;
  }

  public String getReferenz()
  {
    return this.referenz;
  }

  public void setZahlungspflichtigen(String zahlungspflichtiger)
  {
    this.zahlungspflichtiger = zahlungspflichtiger;
  }

  public String getZahlungspflichtigen()
  {
    return this.zahlungspflichtiger;
  }

  public void addVerwendungszweck(String verwendungszweck)
  {
    if (this.verwendungszweck == null)
    {
      this.verwendungszweck = verwendungszweck;
    }
    else
    {
      this.verwendungszweck = this.verwendungszweck + ", " + verwendungszweck;
    }
  }

  public String getVerwendungszweck()
  {
    return verwendungszweck;
  }

  public void setBetrag(BigDecimal betrag)
  {
    this.betrag = betrag;
  }

  public BigDecimal getBetrag()
  {
    return betrag;
  }

  public void setBIC(String bic)
  {
    this.bic = bic;
  }

  public String getBIC()
  {
    return this.bic;
  }

  public void setIBAN(String iban)
  {
    this.iban = iban;
  }

  public String getIBAN()
  {
    return this.iban;
  }

}
