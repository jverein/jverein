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
import de.jost_net.JVerein.gui.view.AuswertungBuchungView;
import de.jost_net.JVerein.gui.view.AuswertungMitgliedView;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class AuswertungBuchungenAction implements Action
{

  public void handleAction(Object context) throws ApplicationException
  {

    Buchung b = null;

    if (context != null && (context instanceof Buchung))
    {
      b = (Buchung) context;
    }
    else
    {
      try
      {
        b = (Buchung) Einstellungen.getDBService().createObject(Buchung.class,
            null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler beim erzeugen eines neuen Buchung-Objectes", e);
      }
    }

    GUI.startView(AuswertungBuchungView.class.getName(), b);
  }

}
