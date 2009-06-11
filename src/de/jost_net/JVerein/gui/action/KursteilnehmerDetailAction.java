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
 * Revision 1.1  2007/02/25 19:11:40  jost
 * Neu: Kursteilnehmer
 *
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.KursteilnehmerDetailView;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerDetailAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Kursteilnehmer kt = null;

    if (context != null && (context instanceof Kursteilnehmer))
    {
      kt = (Kursteilnehmer) context;
    }
    else
    {
      try
      {
        kt = (Kursteilnehmer) Einstellungen.getDBService().createObject(
            Kursteilnehmer.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Fehler bei der Erzeugung eines neuen Kursteilnehmers"), e);
      }
    }
    GUI.startView(KursteilnehmerDetailView.class.getName(), kt);
  }
}
