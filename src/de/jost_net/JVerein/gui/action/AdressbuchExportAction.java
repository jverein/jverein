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
 * Revision 1.1  2009-03-26 20:54:48  jost
 * Neu: Adressbuchexport
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.AdressbuchExportView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class AdressbuchExportAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(AdressbuchExportView.class.getName(), null);
  }
}
