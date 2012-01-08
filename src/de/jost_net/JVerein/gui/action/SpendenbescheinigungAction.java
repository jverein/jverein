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
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.gui.view.SpendenbescheinigungView;
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
        spb = (Spendenbescheinigung) Einstellungen.getDBService().createObject(
            Spendenbescheinigung.class, null);

        if (context != null && (context instanceof Mitglied))
        {
          Mitglied m = (Mitglied) context;
          spb.setMitglied(m);
          spb.setZeile1(m.getAnrede());
          spb.setZeile2(m.getVornameName());
          spb.setZeile3(m.getStrasse());
          spb.setZeile4(m.getPlz() + " " + m.getOrt());
          if (m.getStaat() != null && m.getStaat().length() > 0)
          {
            spb.setZeile5(m.getStaat());
          }
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
              spb.setZeile1(m.getAnrede());
              spb.setZeile2(m.getVornameName());
              spb.setZeile3(m.getStrasse());
              spb.setZeile4(m.getPlz() + " " + m.getOrt());
              if (m.getStaat() != null && m.getStaat().length() > 0)
              {
                spb.setZeile5(m.getStaat());
              }
        	}
            if (mkn.getType() == MitgliedskontoNode.IST) {
              // Buchung eintragen
              Object o = Einstellungen.getDBService().createObject(Buchung.class, mkn.getID());
              if (o != null) {
            	Buchung b = (Buchung) o;
            	if (b.getSpendenbescheinigung() != null) {
            	  throw new ApplicationException("Die Buchung ist bereits auf einer Spendenbescheinigung eingetragen!");
            	}
            	spb.setBuchung(b);
            	spb.setSpendedatum(b.getDatum());
              }
            } else if (mkn.getType() == MitgliedskontoNode.MITGLIED) {
                /* Ermitteln der Buchungen zu der neuen Spendenbescheinigung */
                Date minDatum = Calendar.getInstance().getTime();
                Date maxDatum = Calendar.getInstance().getTime();
                DBIterator itMk = Einstellungen.getDBService().createList(Mitgliedskonto.class);
                itMk.addFilter("mitglied = ?",
                        new Object[] { mkn.getID() });
                //it.addFilter("spendenbescheinigung = ?",
                //    new Object[] { null });
                itMk.setOrder("ORDER BY datum asc");

                while (itMk.hasNext())
                {
                  Mitgliedskonto mk = (Mitgliedskonto) itMk.next();
                  
                  DBIterator it = Einstellungen.getDBService().createList(Buchung.class);
                  it.addFilter("mitgliedskonto = ?",
                          new Object[] { mk.getID() });
                  it.addFilter("spendenbescheinigung is null");
                  it.setOrder("ORDER BY datum asc");

                  while (it.hasNext())
                  {
                    Buchung bu = (Buchung) it.next();
                    if (bu.getSpendenbescheinigung() == null) {
                      if (bu.getBuchungsart().getSpende()) {
                        if (minDatum.after(bu.getDatum())) {
                          minDatum = bu.getDatum();
                        }
                        if (maxDatum.before(bu.getDatum())) {
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
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler bei der Erstellung der Spendenbescheinigung"));
    }
  }
}
