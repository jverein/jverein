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
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.gui.view.MitgliedskontoDetailView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoDetailSollNeuAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    MitgliedskontoNode mkn = null;
    Mitgliedskonto mk = null;

    if (context != null && (context instanceof MitgliedskontoNode))
    {
      mkn = (MitgliedskontoNode) context;
      try
      {
        Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, mkn.getID());
        mk = (Mitgliedskonto) Einstellungen.getDBService().createObject(
            Mitgliedskonto.class, null);
        mk.setMitglied(m);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung eines Mitgliedskontos");
      }
    }
    GUI.startView(new MitgliedskontoDetailView(MitgliedskontoNode.SOLL), mk);
  }
}
