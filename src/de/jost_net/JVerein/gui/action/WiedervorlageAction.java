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
 * Revision 1.1  2007/05/07 19:23:41  jost
 * Neu: Wiedervorlage
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.WiedervorlageView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class WiedervorlageAction implements Action
{
  private Mitglied m;

  public WiedervorlageAction(Mitglied m)
  {
    super();
    this.m = m;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    Wiedervorlage w = null;

    if (context != null && (context instanceof Wiedervorlage))
    {
      w = (Wiedervorlage) context;
    }
    else
    {
      try
      {
        w = (Wiedervorlage) Einstellungen.getDBService().createObject(
            Wiedervorlage.class, null);
        if (m != null)
        {
          w.setMitglied(new Integer(m.getID()).intValue());
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Fehler bei der Erzeugung einer neuen Wiedervorlage"), e);
      }
    }
    GUI.startView(WiedervorlageView.class.getName(), w);
  }
}
