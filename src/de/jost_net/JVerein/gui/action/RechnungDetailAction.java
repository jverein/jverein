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

import de.jost_net.JVerein.gui.view.RechnungDetailView;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class RechnungDetailAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Abrechnung a = null;

    if (context != null && (context instanceof Abrechnung))
    {
      a = (Abrechnung) context;
    }
    GUI.startView(RechnungDetailView.class.getName(), a);
  }
}
