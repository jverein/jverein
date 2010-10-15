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
 * Revision 1.1  2008-05-22 06:45:47  jost
 * Buchführung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.BuchungView;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

public class BuchungNeuAction implements Action
{

  public void handleAction(Object context)
  {
    Buchung buch;
    try
    {
      buch = (Buchung) Einstellungen.getDBService().createObject(Buchung.class,
          null);
      GUI.startView(BuchungView.class, buch);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
}
