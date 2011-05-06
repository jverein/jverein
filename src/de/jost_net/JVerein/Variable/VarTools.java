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

import java.util.Map;

import org.apache.velocity.VelocityContext;

public class VarTools
{
  /**
   * Übertragung einer Map<String, Object> in einen VelocityContext
   */
  public static void add(VelocityContext context, Map<String, Object> map)
  {
    for (String key : map.keySet())
    {
      context.put(key, map.get(key));
    }
  }
}
