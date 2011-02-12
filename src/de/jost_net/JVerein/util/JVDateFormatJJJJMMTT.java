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
package de.jost_net.JVerein.util;

import java.text.SimpleDateFormat;

public class JVDateFormatJJJJMMTT extends SimpleDateFormat
{
  private static final long serialVersionUID = 4017644423840096050L;

  public JVDateFormatJJJJMMTT()
  {
    super("yyyy-MM-dd");
  }
}
