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
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.gui.view.SpendenbescheinigungView;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.Spendenart;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Spendenbescheinigung spb = null;

    try
    {
      if (context != null && context instanceof Spendenbescheinigung)
      {
        spb = (Spendenbescheinigung) context;
      }
      else
      {
        spb = (Spendenbescheinigung) Einstellungen.getDBService()
            .createObject(Spendenbescheinigung.class, null);

        if (context != null && (context instanceof Mitglied))
        {
          Mitglied m = (Mitglied) context;
          adressaufbereitung(m, spb);
        }
        else if (context != null && (context instanceof MitgliedskontoNode))
        {
          MitgliedskontoNode mkn = (MitgliedskontoNode) context;
          spb.setSpendenart(Spendenart.GELDSPENDE);
          spb.setErsatzAufwendungen(false);
          spb.setBescheinigungsdatum(new Date());
          if (mkn.getMitglied() != null)
          {
            // Mitglied aus Mitgliedskonto lesen
            Mitglied m = mkn.getMitglied();
            spb.setMitglied(m);
            adressaufbereitung(m, spb);
          }
          if (mkn.getType() == MitgliedskontoNode.IST)
          {
            // Buchung eintragen
            Object o = Einstellungen.getDBService().createObject(Buchung.class,
                mkn.getID());
            if (o != null)
            {
              Buchung b = (Buchung) o;
              if (b.getSpendenbescheinigung() != null)
              {
                throw new ApplicationException(
                    "Die Buchung ist bereits auf einer Spendenbescheinigung eingetragen!");
              }
              spb.setBuchung(b);
              spb.setSpendedatum(b.getDatum());
            }
          }
          else if (mkn.getType() == MitgliedskontoNode.MITGLIED)
          {
            /* Ermitteln der Buchungen zu der neuen Spendenbescheinigung */
            Date minDatum = Calendar.getInstance().getTime();
            Date maxDatum = Calendar.getInstance().getTime();
            DBIterator<Mitgliedskonto> itMk = Einstellungen.getDBService()
                .createList(Mitgliedskonto.class);
            itMk.addFilter("mitglied = ?", new Object[] { mkn.getID() });
            // it.addFilter("spendenbescheinigung = ?",
            // new Object[] { null });
            itMk.setOrder("ORDER BY datum asc");

            while (itMk.hasNext())
            {
              Mitgliedskonto mk = itMk.next();

              DBIterator<Buchung> it = Einstellungen.getDBService()
                  .createList(Buchung.class);
              it.addFilter("mitgliedskonto = ?", new Object[] { mk.getID() });
              it.addFilter("spendenbescheinigung is null");
              it.setOrder("ORDER BY datum asc");

              while (it.hasNext())
              {
                Buchung bu = it.next();
                if (bu.getSpendenbescheinigung() == null)
                {
                  if (bu.getBuchungsart().getSpende())
                  {
                    if (minDatum.after(bu.getDatum()))
                    {
                      minDatum = bu.getDatum();
                    }
                    if (maxDatum.before(bu.getDatum()))
                    {
                      maxDatum = bu.getDatum();
                    }
                    spb.addBuchung(bu);
                  }
                }
              }
            }
            spb.setSpendedatum(minDatum);
          }
        }
      }
      GUI.startView(SpendenbescheinigungView.class.getName(), spb);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException(
          "Fehler bei der Erstellung der Spendenbescheinigung");
    }
  }

  private void adressaufbereitung(Mitglied m, Spendenbescheinigung spb)
      throws RemoteException
  {
    ArrayList<String> adresse = new ArrayList<>();
    spb.setMitglied(m);
    if (m.getAnrede() != null && m.getAnrede().length() > 0)
    {
      adresse.add(m.getAnrede());
    }
    adresse.add(Adressaufbereitung.getVornameName(m));
    if (m.getAdressierungszusatz() != null
        && m.getAdressierungszusatz().length() > 0)
    {
      adresse.add(m.getAdressierungszusatz());
    }
    adresse.add(m.getStrasse());
    adresse.add(m.getPlz() + " " + m.getOrt());
    if (m.getStaat() != null && m.getStaat().length() > 0)
    {
      adresse.add(m.getStaat());
    }
    switch (adresse.size())
    {
      case 7:
        spb.setZeile7(adresse.get(6));
      case 6:
        spb.setZeile6(adresse.get(5));
      case 5:
        spb.setZeile5(adresse.get(4));
      case 4:
        spb.setZeile4(adresse.get(3));
      case 3:
        spb.setZeile3(adresse.get(2));
      case 2:
        spb.setZeile2(adresse.get(1));
      case 1:
        spb.setZeile1(adresse.get(0));
    }

  }
}
