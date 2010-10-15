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
 * Revision 1.1  2008-09-16 18:27:11  jost
 * Neu: Rechnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.RechnungListeView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class RechnungListeAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(RechnungListeView.class.getName(), null);
  }
}
