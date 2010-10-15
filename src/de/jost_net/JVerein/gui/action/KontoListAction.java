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
 * Revision 1.1  2008-05-22 06:47:00  jost
 * Buchführung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.KontoListView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class KontoListAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(KontoListView.class.getName(), null);
  }
}
