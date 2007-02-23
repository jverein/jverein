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
 * Revision 1.1  2006/09/20 15:38:12  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
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
        throw new ApplicationException(
            "Fehler beim erzeugen eines neuen Mitglied-Objectes", e);
      }
    }

    GUI.startView(AuswertungMitgliedView.class.getName(), m);
  }

}
