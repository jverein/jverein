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
 * Revision 1.1  2008-12-22 21:07:17  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.2  2007/02/23 20:26:00  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:38:12  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.ZusatzbetraegelisteView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class ZusatzbetraegeListeAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(ZusatzbetraegelisteView.class.getName(), null);
  }
}
