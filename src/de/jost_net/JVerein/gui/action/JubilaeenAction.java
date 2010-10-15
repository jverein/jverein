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
 * Revision 1.1  2007-12-22 08:24:57  jost
 * Neu: Jubiläenliste
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.JubilaeenView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class JubilaeenAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(JubilaeenView.class.getName(), null);
  }
}
