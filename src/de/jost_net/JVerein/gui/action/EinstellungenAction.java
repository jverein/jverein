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
 * Revision 1.1  2007-08-22 20:42:41  jost
 * Bug #011762
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.EinstellungenView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class EinstellungenAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(EinstellungenView.class.getName(), null);
  }
}
