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
package de.jost_net.JVerein.Variable;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.gui.control.FormularfeldControl;
import de.jost_net.JVerein.rmi.Mitgliedskonto;

public class MitgliedskontoMap
{

  public MitgliedskontoMap()
  {

  }

  public Map<String, Object> getMap(ArrayList<Mitgliedskonto> mk,
      Map<String, Object> inma) throws RemoteException
  {
    Map<String, Object> map = null;
    if (inma == null)
    {
      map = new HashMap<String, Object>();
    }
    else
    {
      map = inma;
    }

    ArrayList<Date> buda = new ArrayList<Date>();
    ArrayList<String> zg = new ArrayList<String>();
    ArrayList<String> zg1 = new ArrayList<String>();
    ArrayList<String> zg2 = new ArrayList<String>();
    ArrayList<Double> betrag = new ArrayList<Double>();
    double summe = 0;
    for (Mitgliedskonto mkto : mk)
    {
      buda.add(mkto.getDatum());
      zg.add(mkto.getZweck1() + " " + mkto.getZweck2());
      zg1.add(mkto.getZweck1());
      zg2.add(mkto.getZweck2());
      betrag.add(new Double(mkto.getBetrag()));
      summe += mkto.getBetrag();
    }
    if (buda.size() > 1)
    {
      zg1.add("Summe");
      zg.add("Summe");
      betrag.add(summe);
    }
    map.put(FormularfeldControl.BUCHUNGSDATUM, buda.toArray());
    map.put(FormularfeldControl.ZAHLUNGSGRUND, zg.toArray());
    map.put(FormularfeldControl.ZAHLUNGSGRUND1, zg1.toArray());
    map.put(FormularfeldControl.ZAHLUNGSGRUND2, zg2.toArray());
    map.put(FormularfeldControl.BETRAG, betrag.toArray());
    map.put(MitgliedskontoVar.BUCHUNGSDATUM.getName(), buda.toArray());
    map.put(MitgliedskontoVar.ZAHLUNGSGRUND.getName(), zg.toArray());
    map.put(MitgliedskontoVar.ZAHLUNGSGRUND1.getName(), zg1.toArray());
    map.put(MitgliedskontoVar.ZAHLUNGSGRUND2.getName(), zg2.toArray());
    map.put(MitgliedskontoVar.BETRAG.getName(), betrag.toArray());

    return map;
  }
}
