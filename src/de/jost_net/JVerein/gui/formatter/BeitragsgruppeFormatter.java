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

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.jameica.gui.formatter.Formatter;

public class BeitragsgruppeFormatter implements Formatter
{
  public String format(Object o)
  {
    Beitragsgruppe beitragsgruppe = (Beitragsgruppe) o;
    try
    {
      return beitragsgruppe.getBezeichnung();
    }
    catch (RemoteException e)
    {
      return e.getMessage();
    }
  }
}
