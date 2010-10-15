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
 * Revision 1.2  2010-07-28 07:25:48  jost
 * deprecated
 *
 * Revision 1.1  2008/09/16 18:26:29  jost
 * Neu: Rechnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.RechnungView;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

/**
 * @deprecated In Version 1.5 ausmustern
 * @author heiner
 * 
 */
@Deprecated
public class RechnungAction implements Action
{

  public void handleAction(Object context)
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
