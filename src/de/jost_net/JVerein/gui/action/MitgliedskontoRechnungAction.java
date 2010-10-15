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
 * Revision 1.3  2010-08-15 19:00:23  jost
 * Rechnungen auch für einen vorgegebenen Zeitraum ausgeben.
 *
 * Revision 1.2  2010-08-08 19:32:03  jost
 * Code bereinigt.
 *
 * Revision 1.1  2010-08-04 10:39:50  jost
 * Prerelease Rechnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.MitgliedskontoRechnungView;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class MitgliedskontoRechnungAction implements Action
{

  public void handleAction(Object context)
  {
    if (context != null && context instanceof Mitgliedskonto)
    {
      Mitgliedskonto mk = (Mitgliedskonto) context;
      GUI.startView(MitgliedskontoRechnungView.class.getName(), mk);
    }
    else if (context != null && context instanceof Mitgliedskonto[])
    {
      Mitgliedskonto[] mk = (Mitgliedskonto[]) context;
      GUI.startView(MitgliedskontoRechnungView.class.getName(), mk);
    }
    else
    {
      GUI.startView(MitgliedskontoRechnungView.class, null);
    }
  }
}
