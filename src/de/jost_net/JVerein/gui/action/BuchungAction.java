/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.BuchungView;
import de.jost_net.JVerein.gui.view.SplitBuchungView;
import de.jost_net.JVerein.io.SplitbuchungsContainer;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class BuchungAction implements Action
{
  private boolean splitbuchung;

  public BuchungAction(boolean splitbuchung)
  {
    this.splitbuchung = splitbuchung;
  }

  @Override
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
            "Fehler bei der Erzeugung einer neuen Buchung", e);
      }
    }
    try
    {
      if (b.getSplitId() == null || splitbuchung)
      {
        GUI.startView(BuchungView.class.getName(), b);
      }
      else
      {
        SplitbuchungsContainer.init(b);
        GUI.startView(SplitBuchungView.class.getName(), b);
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
  }
}
