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

import de.jost_net.JVerein.gui.view.KontoauszugView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class KontoauszugAction implements Action
{

  public void handleAction(Object context)
  {
    if (context != null
        && (context instanceof Mitglied || context instanceof Mitglied[]))
    {
      GUI.startView(KontoauszugView.class.getName(), context);
    }
  }
}
