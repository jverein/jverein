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

import de.jost_net.JVerein.keys.Zahlungsweg;
import de.willuhn.jameica.gui.formatter.Formatter;

public class ZahlungswegFormatter implements Formatter
{
  public String format(Object o)
  {
    Integer zw = (Integer) o;
    return Zahlungsweg.get(zw);
  }
}
