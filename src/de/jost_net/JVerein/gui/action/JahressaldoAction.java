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
 * Revision 1.1  2008-05-25 19:36:00  jost
 * Neu: Jahressaldo
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.JahressaldoView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class JahressaldoAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(JahressaldoView.class.getName(), null);
  }
}
