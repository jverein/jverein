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
package de.jost_net.JVerein.Variable;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Lastschrift;

public class LastschriftMap
{

  public Map<String, Object> getMap(Lastschrift ls, Map<String, Object> inma)
      throws RemoteException
  {
    Map<String, Object> map = null;
    if (inma == null)
    {
      map = new HashMap<>();
    }
    else
    {
      map = inma;
    }
    Abrechnungslauf abrl = null;
    if (ls.getID() == null)
    {
      abrl = (Abrechnungslauf) Einstellungen.getDBService()
          .createObject(Abrechnungslauf.class, null);
      abrl.setDatum(new Date());
      abrl.setFaelligkeit(new Date());
      abrl.setID("123");
      ls.setAdressierungszusatz("Hinterhaus bei Lieschen Müller");
      ls.setAnrede("Herrn");
      ls.setBetrag(123.45d);
      ls.setBIC("XXXXXXXXXXX");
      ls.setEmail("willi.wichtig@mail.de");
      ls.setIBAN("DE89370400440532013000");
      ls.setIBAN("DE89370400440532013000");
      ls.setGeschlecht(GeschlechtInput.MAENNLICH);
      ls.setMandatDatum(new Date());
      ls.setMandatSequence("FRST");
      ls.setMandatID("1234");
      ls.setName("Wichtig");
      ls.setOrt("Testenhausen");
      ls.setPersonenart("n");
      ls.setPlz("12345");
      ls.setStaat("Deutschland");
      ls.setStrasse("Bahnhofstr. 1");
      ls.setTitel("Dr.");
      ls.setVerwendungszweck("Beitrag 2013 Willi Wichtig");
      ls.setVorname("Willi");
    }
    else
    {
      abrl = ls.getAbrechnungslauf();
    }

    map.put(LastschriftVar.ABRECHNUNGSLAUF_NR.getName(), abrl.getID());
    map.put(LastschriftVar.ABRECHUNGSLAUF_DATUM.getName(), abrl.getDatum());
    // Damit Pre-Notifications für mit Versionen bis 2.8.18 erstellte Abrechnungsläufe
    // korrekt erstellt werden, werden beide Felder verwendet.
    if (ls.getMandatSequence().equals("FRST"))
    {
      map.put(LastschriftVar.ABRECHNUNGSLAUF_FAELLIGKEIT.getName(),
          abrl.getFaelligkeit());
    }
    else
    {
      Date d = (Date) abrl.getAttribute("faelligkeit2");
      if (d == null)
      {
        d = Einstellungen.NODATE;
      }
      map.put(LastschriftVar.ABRECHNUNGSLAUF_FAELLIGKEIT.getName(), d);
    }
    map.put(LastschriftVar.PERSONENART.getName(), ls.getPersonenart());
    map.put(LastschriftVar.GESCHLECHT.getName(), ls.getGeschlecht());
    map.put(LastschriftVar.ANREDE.getName(), ls.getAnrede());
    map.put(LastschriftVar.ANREDE_DU.getName(),
        Adressaufbereitung.getAnredeDu(ls));
    map.put(LastschriftVar.ANREDE_FOERMLICH.getName(),
        Adressaufbereitung.getAnredeFoermlich(ls));
    map.put(LastschriftVar.TITEL.getName(), ls.getTitel());
    map.put(LastschriftVar.NAME.getName(), ls.getName());
    map.put(LastschriftVar.VORNAME.getName(), ls.getVorname());
    map.put(LastschriftVar.STRASSE.getName(), ls.getStrasse());
    map.put(LastschriftVar.ADRESSSIERUNGSZUSATZ.getName(),
        ls.getAdressierungszusatz());
    map.put(LastschriftVar.PLZ.getName(), ls.getPlz());
    map.put(LastschriftVar.ORT.getName(), ls.getOrt());
    map.put(LastschriftVar.STAAT.getName(), ls.getStaat());
    map.put(LastschriftVar.EMAIL.getName(), ls.getEmail());
    map.put(LastschriftVar.MANDATID.getName(), ls.getMandatID());
    map.put(LastschriftVar.MANDATDATUM.getName(), ls.getMandatDatum());
    map.put(LastschriftVar.BIC.getName(), ls.getBIC());
    map.put(LastschriftVar.IBAN.getName(), ls.getIBAN());
    map.put(LastschriftVar.IBANMASKIERT.getName(),
        VarTools.maskieren(ls.getIBAN()));
    map.put(LastschriftVar.VERWENDUNGSZWECK.getName(),
        ls.getVerwendungszweck());
    map.put(LastschriftVar.BETRAG.getName(),
        ls.getBetrag() != null
            ? Einstellungen.DECIMALFORMAT.format(ls.getBetrag())
            : "");

    map.put(LastschriftVar.EMPFAENGER.getName(),
        Adressaufbereitung.getAdressfeld(ls));

    return map;
  }

}
