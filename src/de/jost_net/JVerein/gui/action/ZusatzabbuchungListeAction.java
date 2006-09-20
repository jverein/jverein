/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.ZusatzabbuchunglisteView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class ZusatzabbuchungListeAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    GUI.startView(ZusatzabbuchunglisteView.class.getName(), null);
  }
}
