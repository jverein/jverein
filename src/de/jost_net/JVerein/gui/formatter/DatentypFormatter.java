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
package de.jost_net.JVerein.gui.formatter;

import de.jost_net.JVerein.keys.Datentyp;
import de.willuhn.jameica.gui.formatter.Formatter;

public class DatentypFormatter implements Formatter
{
  public String format(Object o)
  {
    Integer art = (Integer) o;
    return Datentyp.get(art);
  }
}
