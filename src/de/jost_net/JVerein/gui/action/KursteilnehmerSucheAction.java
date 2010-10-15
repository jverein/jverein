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
 * Revision 1.1  2007-02-25 19:11:53  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class KursteilnehmerSucheAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(
        de.jost_net.JVerein.gui.view.KursteilnehmerSucheView.class.getName(),
        null);
  }

}
