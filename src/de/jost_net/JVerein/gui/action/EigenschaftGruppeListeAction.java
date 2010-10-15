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
 * Revision 1.1  2009-11-17 20:55:40  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.EigenschaftGruppeListeView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class EigenschaftGruppeListeAction implements Action
{

  public void handleAction(Object context)
  {
    GUI.startView(EigenschaftGruppeListeView.class.getName(), null);
  }
}
