/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.MitgliedAddress;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.hbci.rmi.Address;
import de.willuhn.jameica.hbci.rmi.Addressbook;

public class JVereinAdressbuch implements Addressbook
{

  public String getName() throws RemoteException
  {
    return "JVerein-Adressbuch";
  }

  public List findAddresses(String text) throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(Mitglied.class);
    String su = "%" + text + "%";
    it.addFilter("(name like ? or vorname like ?)", new Object[] { su, su });
    it.addFilter("konto is not null and length(konto)>0 and blz is not null and length(blz) > 0");
    ArrayList list = new ArrayList();
    while (it.hasNext())
    {
      Mitglied m = (Mitglied) it.next();
      MitgliedAddress ma = new MitgliedAddress(m.getKonto(), m.getBlz(),
          m.getNameVorname(), "", "", m.getIban(), m.getBeitragsgruppe()
              .getBezeichnung());
      list.add(ma);
    }
    return list;
  }

  public Address contains(Address address) throws RemoteException
  {
    System.out.println(address.getName());
    return null;
  }

}
