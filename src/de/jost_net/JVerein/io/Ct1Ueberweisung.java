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
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Lastschrift;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.Ueberweisung.Empfaenger;
import de.jost_net.OBanToo.SEPA.Ueberweisung.Ueberweisung;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.rmi.AuslandsUeberweisung;
import de.willuhn.jameica.hbci.rmi.HibiscusAddress;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

public class Ct1Ueberweisung
{
  private Ueberweisung ueb;

  public Ct1Ueberweisung()
  {
  }

  public int write(Abrechnungslauf abrl, File file, Date faell, int ct1ausgabe,
      String textvorher, String textnachher) throws Exception
  {
    switch (ct1ausgabe)
    {
      case Abrechnungsausgabe.SEPA_DATEI:
        return dateiausgabe(abrl, file, faell, ct1ausgabe, textvorher,
            textnachher);

      case Abrechnungsausgabe.HIBISCUS:
        return hibiscusausgabe(abrl, file, faell, ct1ausgabe, textvorher,
            textnachher);

    }
    return -1;
  }

  private int dateiausgabe(Abrechnungslauf abrl, File file, Date faell,
      int ct1ausgabe, String textvorher, String textnachher)
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

    DBIterator it = getIterator(abrl);
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
    return Integer.parseInt(ueb.getAnzahlBuchungen());
  }

  private int hibiscusausgabe(Abrechnungslauf abrl, File file, Date faell,
      int ct1ausgabe, String textvorher, String textnachher) throws Exception
  {
    try
    {
      de.willuhn.jameica.hbci.rmi.Konto hibk = Einstellungen.getEinstellung()
          .getHibiscusKonto();
      DBIterator it = getIterator(abrl);
      while (it.hasNext())
      {
        Lastschrift ls = (Lastschrift) it.next();
        DBService service = (DBService) Application.getServiceFactory().lookup(
            HBCI.class, "database");

        AuslandsUeberweisung ue = (AuslandsUeberweisung) service.createObject(
            AuslandsUeberweisung.class, null);
        ue.setBetrag(ls.getBetrag());
        HibiscusAddress ad = (HibiscusAddress) service.createObject(
            HibiscusAddress.class, null);
        ad.setBic(ls.getBIC());
        ad.setIban(ls.getIBAN());
        ue.setGegenkonto(ad);
        ue.setEndtoEndId(ls.getMandatID());
        ue.setGegenkontoName(ls.getName());
        ue.setTermin(faell);
        ue.setZweck(ls.getVerwendungszweck());
        ue.setKonto(hibk);
        ue.store();
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
    catch (SEPAException e)
    {
      throw new ApplicationException(e);
    }

    return 1;
  }

  private DBIterator getIterator(Abrechnungslauf abrl) throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(Lastschrift.class);
    it.addFilter("abrechnungslauf = ?", abrl.getID());
    it.setOrder("order by name, vorname");
    return it;
  }
}
