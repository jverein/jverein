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
 * Revision 1.2  2009/06/11 21:01:50  jost
 * Vorbereitung I18N
 *
 * Revision 1.1  2007/05/26 16:25:42  jost
 * Neu: Auswertung Kursteilnehmer
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.AuswertungKursteilnehmerView;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class AuswertungKursteilnehmerAction implements Action
{

  public void handleAction(Object context) throws ApplicationException
  {
    Kursteilnehmer k = null;

    if (context != null && (context instanceof Kursteilnehmer))
    {
      k = (Kursteilnehmer) context;
    }
    else
    {
      try
      {
        k = (Kursteilnehmer) Einstellungen.getDBService().createObject(
            Kursteilnehmer.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Fehler beim erzeugen eines neuen Kursteilnehmer-Objektes: {0}",
            new String[] { e.getMessage() }));
      }
    }

    GUI.startView(AuswertungKursteilnehmerView.class.getName(), k);
  }

}
