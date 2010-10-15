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
 * Revision 1.1  2008-05-22 06:45:20  jost
 * Buchführung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.AnfangsbestandListView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class AnfangsbestandListAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(AnfangsbestandListView.class.getName(), null);
  }
}
