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
import de.jost_net.JVerein.gui.view.LehrgangView;
import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class LehrgangAction implements Action
{
  private Mitglied m;

  public LehrgangAction(Mitglied m)
  {
    super();
    this.m = m;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    Lehrgang l = null;

    if (context != null && (context instanceof Lehrgang))
    {
      l = (Lehrgang) context;
    }
    else
    {
      try
      {
        l = (Lehrgang) Einstellungen.getDBService().createObject(
            Lehrgang.class, null);
        if (m != null)
        {
          l.setMitglied(new Integer(m.getID()).intValue());
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung eines neuen Lehrgangs", e);
      }
    }
    GUI.startView(LehrgangView.class.getName(), l);
  }
}
