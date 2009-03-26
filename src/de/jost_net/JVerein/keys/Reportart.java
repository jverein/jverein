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
package de.jost_net.JVerein.keys;

import java.util.ArrayList;

/**
 * Reportart
 */
public class Reportart
{
  public static final int MITGLIED_AUSWERTUNG = 1;

  public static final int KURSTEILNEHMER = 100;

  private int reportart;

  public Reportart(int key)
  {
    this.reportart = key;
  }

  public int getKey()
  {
    return reportart;
  }

  public String getText()
  {
    return get(reportart);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case MITGLIED_AUSWERTUNG:
        return "Mitglied-Auswertung";
      case KURSTEILNEHMER:
        return "Kursteilnehmer";
      default:
        return null;
    }
  }

  public static ArrayList<Reportart> getArray()
  {
    ArrayList<Reportart> ret = new ArrayList<Reportart>();
    ret.add(new Reportart(MITGLIED_AUSWERTUNG));
    ret.add(new Reportart(KURSTEILNEHMER));
    return ret;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Reportart)
    {
      Reportart v = (Reportart) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public String toString()
  {
    return get(reportart);
  }
}
