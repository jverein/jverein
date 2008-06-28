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
 * Revision 1.1  2008/05/22 06:45:04  jost
 * Buchführung
 *
 * Revision 1.2  2007/02/23 20:25:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:38:12  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.AnfangsbestandView;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class AnfangsbestandDetailAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Anfangsbestand a = null;

    if (context != null && (context instanceof Anfangsbestand))
    {
      a = (Anfangsbestand) context;
      try
      {
        Jahresabschluss ja = a.getJahresabschluss();
        if (ja != null)
        {
          throw new ApplicationException(
              "Anfangsbestand ist bereits abgeschlossen.");
        }
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(e.getMessage());
      }
    }
    else
    {
      try
      {
        a = (Anfangsbestand) Einstellungen.getDBService().createObject(
            Anfangsbestand.class, null);
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler bei der Erzeugung eines neuen Anfangsbestandes", e);
      }
    }
    GUI.startView(AnfangsbestandView.class.getName(), a);
  }
}
