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

import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.willuhn.jameica.gui.formatter.Formatter;

public class EigenschaftGruppeFormatter implements Formatter
{
  public String format(Object o)
  {
    EigenschaftGruppe eg = (EigenschaftGruppe) o;
    if (eg == null)
    {
      return null;
    }
    String bez = null;
    try
    {
      bez = eg.getBezeichnung();
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    return bez;
  }
}
