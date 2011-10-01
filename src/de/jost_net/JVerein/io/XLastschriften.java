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
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class XLastschriften
{
  private ArrayList<XLastschrift> lastschriften = new ArrayList<XLastschrift>();

  public XLastschriften()
  {

  }

  public void add(XLastschrift lastschrift)
  {
    lastschriften.add(lastschrift);
  }

  public ArrayList<XLastschrift> getLastschriften()
  {
    return lastschriften;
  }

  /**
   * Buchungen mit gleichen Bankverbindungen werden zusammengefasst.
   * 
   * @throws Exception
   */
  public void compact()
  {
    // Schritt 1: Alle Lastschriften mit gleicher Bankverbindung werden in eine
    // ArrayList eingetragen, welche in eine HashMap aufgenommen wird.
    // Im Klartext: Unter der Bankverbindung gibt es eine Liste der
    // dazugehörigen Lastschriften
    HashMap<String, ArrayList<XLastschrift>> comp = new HashMap<String, ArrayList<XLastschrift>>();
    for (XLastschrift last : lastschriften)
    {
      ArrayList<XLastschrift> l1 = comp.get(last.getBankverbindung());
      if (l1 == null) // neue Bankverbindung
      {
        l1 = new ArrayList<XLastschrift>();
        l1.add(last);
        comp.put(last.getBankverbindung(), l1); // In die Liste eintragen
      }
      else
      // Bankverbindung ist schon in der Liste enthalten
      {
        l1.add(last);
      }
    }
    // Schritt 2: Die eigentliche Komprimierung findet statt. Dabei gelten
    // folgende Regeln:
    // - gibt es nur eine Buchung zu einer Bankverbindung ist nicht weiter zu
    // tun
    // - gibt es 2 bis 15 Buchungen zu einer Bankverbindung, werden sie
    // zusammengefügt.
    // - gibt es mehr als 15 Buchungen, findet keine Komprimierung statt
    lastschriften.clear();
    for (String key : comp.keySet())
    {
      ArrayList<XLastschrift> lastliste = comp.get(key);
      int anzvzw = 0;
      for (XLastschrift last : lastliste)
      {
        anzvzw += last.getAnzahlVerwendungszwecke();
      }
      if (lastliste.size() == 1)
      {
        lastschriften.add(lastliste.get(0));
      }
      else if (anzvzw > 15)
      {
        for (XLastschrift l : lastliste)
        {
          lastschriften.add(l);
        }
      }
      else
      {
        XLastschrift l1 = lastliste.get(0);
        l1.modifyVerwendungszweck(0, l1.getVerwendungszweck(0), l1.getBetrag());
        for (int i = 1; i < lastliste.size(); i++)
        {
          XLastschrift l2 = lastliste.get(i);
          l2.modifyVerwendungszweck(0, l2.getVerwendungszweck(0),
              l2.getBetrag());
          l1.add(l2);
        }
        lastschriften.add(l1);
      }
    }
  }

  public int getAnzahlLastschriften()
  {
    return lastschriften.size();
  }

  public BigDecimal getSummeLastschriften()
  {
    BigDecimal sum = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    for (XLastschrift l : lastschriften)
    {
      sum = sum.add(l.getBetrag());
    }
    return sum;
  }
}
