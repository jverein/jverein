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
 * Revision 1.3  2007/02/23 20:26:00  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.1  2006/09/20 15:38:12  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.ZusatzbetragView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class ZusatzbetraegeAction implements Action
{
  private Mitglied m;

  public ZusatzbetraegeAction(Mitglied m)
  {
    super();
    this.m = m;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    Zusatzbetrag z = null;

    if (context != null && (context instanceof Zusatzbetrag))
    {
      z = (Zusatzbetrag) context;
    }
    else
    {
      try
      {
        z = (Zusatzbetrag) Einstellungen.getDBService().createObject(
            Zusatzbetrag.class, null);
        if (m != null)
        {
          z.setMitglied(new Integer(m.getID()).intValue());
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung eines neuen Zusatzbetrages", e);
      }
    }
    GUI.startView(ZusatzbetragView.class.getName(), z);
  }
}
