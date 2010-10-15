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
 * Revision 1.1  2008-09-16 18:27:00  jost
 * Neu: Rechnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.RechnungDetailView;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class RechnungDetailAction implements Action
{

  public void handleAction(Object context)
  {
    Abrechnung a = null;

    if (context != null && (context instanceof Abrechnung))
    {
      a = (Abrechnung) context;
    }
    GUI.startView(RechnungDetailView.class.getName(), a);
  }
}
