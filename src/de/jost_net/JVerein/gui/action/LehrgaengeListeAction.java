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
 * Revision 1.1  2009-04-13 11:37:46  jost
 * Neu: Lehrgänge
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.LehrgaengeListeView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class LehrgaengeListeAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(LehrgaengeListeView.class.getName(), null);
  }
}
