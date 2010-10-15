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
 * Revision 1.1  2010-05-18 20:18:26  jost
 * Anpassung Klassenname
 *
 * Revision 1.1  2010/04/25 13:53:30  jost
 * Vorarbeiten Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.AbrechnungslaufListView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class AbrechnunslaufListAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(AbrechnungslaufListView.class.getName(), null);
  }
}
