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

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Lastschrift;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.Ueberweisung.Empfaenger;
import de.jost_net.OBanToo.SEPA.Ueberweisung.Ueberweisung;
import de.willuhn.datasource.rmi.DBIterator;

public class Ct1Ueberweisung
{
  private Ueberweisung ueb;

  public Ct1Ueberweisung()
  {
  }

  public Ueberweisung write(Abrechnungslauf abrl, File file, Date faell,
      String ct1ausgabe, String textvorher, String textnachher)
      throws RemoteException, SEPAException, DatatypeConfigurationException,
      JAXBException
  {
    ueb = new Ueberweisung();
    ueb.setAusfuehrungsdatum(faell);
    ueb.setBIC(Einstellungen.getEinstellung().getBic());
    ueb.setIBAN(Einstellungen.getEinstellung().getIban());
    ueb.setMessageID(abrl.getID() + " "
        + Einstellungen.DATETIMEFORMAT.format(abrl.getDatum()));
    ueb.setName(Einstellungen.getEinstellung().getName());
    ueb.setSammelbuchung(false);

    DBIterator it = Einstellungen.getDBService().createList(Lastschrift.class);
    it.addFilter("abrechnungslauf = ?", abrl.getID());
    it.setOrder("order by name, vorname");
    while (it.hasNext())
    {
      Lastschrift ls = (Lastschrift) it.next();
      Empfaenger e = new Empfaenger();
      e.setBetrag(new BigDecimal("0.01"));
      e.setBic(ls.getBIC());
      e.setIban(ls.getIBAN());
      e.setName(ls.getName());
      e.setReferenz(ls.getMandatID());
      String v = textvorher + " " + ls.getVerwendungszweck() + " "
          + textnachher;
      if (v.length() > 140)
      {
        v = v.substring(0, 140);
      }
      e.setVerwendungszweck(v);
      ueb.add(e);
    }
    ueb.write(file);
    return ueb;
  }
}
