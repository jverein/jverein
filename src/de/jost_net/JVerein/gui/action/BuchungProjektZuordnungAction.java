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

import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.dialogs.ProjektAuswahlDialog;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Projekt;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Projekt zuordnen.
 */
public class BuchungProjektZuordnungAction implements Action
{
  private BuchungsControl control;

  public BuchungProjektZuordnungAction(BuchungsControl control)
  {
    this.control = control;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Buchung)
        && !(context instanceof Buchung[]))
    {
      throw new ApplicationException("Keine Buchung(en) ausgewählt");
    }
    try
    {
      Buchung[] b = null;
      if (context instanceof Buchung)
      {
        b = new Buchung[1];
        b[0] = (Buchung) context;
      }
      if (context instanceof Buchung[])
      {
        b = (Buchung[]) context;
      }
      if (b != null && b.length > 0 && b[0].isNewObject())
      {
        return;
      }
      try
      {
        ProjektAuswahlDialog pad = new ProjektAuswahlDialog(
            AbstractDialog.POSITION_CENTER);
        Projekt open = pad.open();

        if (open == null)
        {
          GUI.getStatusBar().setErrorText("Kein Projekt ausgewählt");
          return;
        }

        for (Buchung buchung : b)
        {
          buchung.setProjekt(open);
          buchung.store();
        }
        control.getBuchungsList();
        GUI.getStatusBar().setSuccessText("Projekt zugeordnet");
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
        GUI.getStatusBar().setErrorText(
            "Fehler bei der Zuordnung des Projektes");
        return;
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Speichern.";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
