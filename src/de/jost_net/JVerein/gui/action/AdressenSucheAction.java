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
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.AdressenSucheView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class AdressenSucheAction implements Action
{

  public AdressenSucheAction()
  {
  }

  public void handleAction(Object context) throws ApplicationException
  {
    GUI.startView(AdressenSucheView.class.getName(), null);
  }

}
