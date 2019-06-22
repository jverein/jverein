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
package de.jost_net.JVerein;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.MitgliedAddress;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.hbci.rmi.Address;
import de.willuhn.jameica.hbci.rmi.Addressbook;

public class JVereinAdressbuch implements Addressbook
{

  @Override
  public String getName()
  {
    return "JVerein-Adressbuch";
  }

  @Override
  public List<MitgliedAddress> findAddresses(String text) throws RemoteException
  {
    DBIterator<Mitglied> it = Einstellungen.getDBService()
        .createList(Mitglied.class);
    String su = "%" + text.toLowerCase() + "%";
    it.addFilter("(lower(name) like ? or lower(vorname) like ?)",
        new Object[] { su, su });
    it.addFilter("(iban is not null and length(iban)>0)");
    ArrayList<MitgliedAddress> list = new ArrayList<>();
    while (it.hasNext())
    {
      Mitglied m = (Mitglied) it.next();
      String kategorie = m.getAdresstyp().getBezeichnung();
      if (m.getAdresstyp().getID().equals("1"))
      {
        kategorie = m.getBeitragsgruppe().getBezeichnung();
      }
      MitgliedAddress ma = new MitgliedAddress(
          Adressaufbereitung.getNameVorname(m), "", m.getBic(), m.getIban(),
          kategorie);
      list.add(ma);
    }
    return list;
  }

  @Override
  public Address contains(Address address)
  {
    return null;
  }

}
