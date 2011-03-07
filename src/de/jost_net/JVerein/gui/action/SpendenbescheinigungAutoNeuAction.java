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

import de.jost_net.JVerein.gui.view.SpendenbescheinigungAutoNeuView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class SpendenbescheinigungAutoNeuAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(SpendenbescheinigungAutoNeuView.class.getName(), null);
  }
}
