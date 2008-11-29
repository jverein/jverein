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
package de.jost_net.JVerein.gui.formatter;

import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.willuhn.jameica.gui.formatter.Formatter;

public class ZahlungsrhytmusFormatter implements Formatter
{
  public String format(Object o)
  {
    Integer zr = (Integer) o;
    return Zahlungsrhytmus.get(zr);
  }
}
