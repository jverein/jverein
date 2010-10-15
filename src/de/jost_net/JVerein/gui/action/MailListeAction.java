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
 * Revision 1.1  2010-02-01 20:57:35  jost
 * Neu: Einfache Mailfunktion
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.MailUebersichtView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class MailListeAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(MailUebersichtView.class.getName(), null);
  }
}
