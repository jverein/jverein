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
 * Revision 1.1  2010-08-16 20:16:02  jost
 * Neu: Mahnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.MitgliedskontoMahnungView;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class MitgliedskontoMahnungAction implements Action
{

  public void handleAction(Object context)
  {
    if (context != null && context instanceof Mitgliedskonto)
    {
      Mitgliedskonto mk = (Mitgliedskonto) context;
      GUI.startView(MitgliedskontoMahnungView.class.getName(), mk);
    }
    else if (context != null && context instanceof Mitgliedskonto[])
    {
      Mitgliedskonto[] mk = (Mitgliedskonto[]) context;
      GUI.startView(MitgliedskontoMahnungView.class.getName(), mk);
    }
    else
    {
      GUI.startView(MitgliedskontoMahnungView.class, null);
    }
  }
}
