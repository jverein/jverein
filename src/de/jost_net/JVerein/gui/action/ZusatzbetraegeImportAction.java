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
 * Revision 1.1  2009-10-20 17:57:17  jost
 * Neu: Import von Zusatzbeträgen
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.ZusatzbetraegeImportView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class ZusatzbetraegeImportAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(ZusatzbetraegeImportView.class.getName(), null);
  }

}
