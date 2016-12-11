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
package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.BuchungBuchungsartAuswahl;
import de.jost_net.JVerein.keys.BuchungsartSort;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.AbstractInput;
import de.willuhn.jameica.gui.input.SelectInput;

public class BuchungsartInput
{
  public AbstractInput getBuchungsartInput(AbstractInput buchungsart,
      Buchungsart bart) throws RemoteException
  {
    DBIterator<Buchungsart> list = Einstellungen.getDBService()
        .createList(Buchungsart.class);
    if (Einstellungen.getEinstellung()
        .getBuchungsartSort() == BuchungsartSort.NACH_NUMMER)
    {
      list.setOrder("ORDER BY nummer");
    }
    else
    {
      list.setOrder("ORDER BY bezeichnung");
    }

    switch (Einstellungen.getEinstellung().getBuchungBuchungsartAuswahl())
    {
      case BuchungBuchungsartAuswahl.ComboBox:
        buchungsart = new SelectInput(list, bart);
        switch (Einstellungen.getEinstellung().getBuchungsartSort())
        {
          case BuchungsartSort.NACH_NUMMER:
            ((SelectInput) buchungsart).setAttribute("nrbezeichnung");
            break;
          case BuchungsartSort.NACH_BEZEICHNUNG_NR:
            ((SelectInput) buchungsart).setAttribute("bezeichnungnr");
            break;
          default:
            ((SelectInput) buchungsart).setAttribute("bezeichnung");
            break;
        }
        ((SelectInput) buchungsart).setPleaseChoose("Bitte auswählen");
        break;
      case BuchungBuchungsartAuswahl.SearchInput:
      default: // default soll SearchInput sein. Eigentlich sollten die
        // Settings immer gesetzt sein, aber man weiss ja nie.
        buchungsart = new BuchungsartSearchInput();
        ((BuchungsartSearchInput) buchungsart).setAttribute("nrbezeichnung");
        ((BuchungsartSearchInput) buchungsart)
            .setSearchString("Zum Suchen tippen ...");
    }
    buchungsart.setValue(bart);
    return buchungsart;
  }

}
