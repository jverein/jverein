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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.MitgliedDetailView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class MitgliedDetailAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Mitglied m = null;

    if (context != null && (context instanceof Mitglied))
    {
      m = (Mitglied) context;
    }
    else
    {
      try
      {
        m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung eines neuen Mitgliedes", e);
      }
    }
    GUI.startView(MitgliedDetailView.class.getName(), m);
  }
}
