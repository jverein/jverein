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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.AuswertungMitgliedView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class AuswertungMitgliedAction implements Action
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
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Fehler beim erzeugen eines neuen Mitglied-Objektes: {0}",
            new String[] { e.getMessage() }));
      }
    }

    GUI.startView(AuswertungMitgliedView.class.getName(), m);
  }

}
