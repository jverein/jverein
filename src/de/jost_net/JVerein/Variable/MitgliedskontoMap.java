/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * heiner@jverein.de
 * www.jverein.de
 * All rights reserved
 * $Log$
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
    
    return map;
  }
}
