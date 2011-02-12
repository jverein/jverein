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
 * Revision 1.2  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.1  2009/04/13 11:38:01  jost
 * Neu: Lehrgänge
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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
          if (m.getID() == null)
          {
            throw new ApplicationException(
                "Neues Mitglied bitte erst speichern. Dann können Lehrgänge aufgenommen werden.");
          }

          l.setMitglied(new Integer(m.getID()).intValue());
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Fehler bei der Erzeugung eines neuen Lehrgangs"), e);
      }
    }
    GUI.startView(LehrgangView.class.getName(), l);
  }
}
