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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

public class Util
{

  public static boolean getBoolean(Object o) throws RemoteException
  {
    if (o == null)
    {
      return false;
    }
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      String v = (String) o;
      return (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("j") || v
          .equalsIgnoreCase("1"));
    }
    throw new RemoteException("Weder null, noch String oder Boolean");
  }
}
