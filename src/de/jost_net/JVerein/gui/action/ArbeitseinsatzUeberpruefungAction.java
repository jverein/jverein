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

import de.jost_net.JVerein.gui.view.ArbeitseinsatzUeberpruefungView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class ArbeitseinsatzUeberpruefungAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(ArbeitseinsatzUeberpruefungView.class.getName(), null);
  }
}
