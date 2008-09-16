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
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.RechnungView;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class RechnungAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context != null && context instanceof Abrechnung)
    {
      Abrechnung abr = (Abrechnung) context;
      GUI.startView(RechnungView.class.getName(), abr);
    }
    else if (context != null && context instanceof Abrechnung[])
    {
      Abrechnung[] abr = (Abrechnung[]) context;
      GUI.startView(RechnungView.class.getName(), abr);
    }
  }
}
