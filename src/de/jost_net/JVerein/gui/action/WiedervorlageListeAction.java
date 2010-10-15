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
 * Revision 1.1  2007-05-07 19:24:40  jost
 * Neu: Wiedervorlage
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.WiedervorlagelisteView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class WiedervorlageListeAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(WiedervorlagelisteView.class.getName(), null);
  }
}
