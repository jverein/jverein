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

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerSucheAction implements Action
{

  public void handleAction(Object context) throws ApplicationException
  {
    GUI.startView(de.jost_net.JVerein.gui.view.KursteilnehmerSucheView.class
        .getName(), null);
  }

}
