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
 * Revision 1.1  2010-11-17 04:48:29  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.ArbeitseinsatzDetailView;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class ArbeitseinsatzAction implements Action
{
  private Mitglied m;

  public ArbeitseinsatzAction(Mitglied m)
  {
    super();
    this.m = m;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    Arbeitseinsatz aeins = null;

    if (context != null && (context instanceof Arbeitseinsatz))
    {
      aeins = (Arbeitseinsatz) context;
    }
    else
    {
      try
      {
        aeins = (Arbeitseinsatz) Einstellungen.getDBService().createObject(
            Arbeitseinsatz.class, null);
        if (m != null)
        {
          if (m.getID() == null)
          {
            throw new ApplicationException(
                "Neues Mitglied bitte erst speichern. Dann können Arbeitseinsätze aufgenommen werden.");
          }

          aeins.setMitglied(new Integer(m.getID()).intValue());
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Fehler bei der Erzeugung eines neuen Arbeitseinsatzes"), e);
      }
    }
    GUI.startView(ArbeitseinsatzDetailView.class.getName(), aeins);
  }
}
