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
 * Revision 1.1  2008-04-10 18:57:49  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.FelddefinitionenUebersichtView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class FelddefinitionenAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(FelddefinitionenUebersichtView.class.getName(), null);
  }
}
