/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.SpendenbescheinigungView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Spendenbescheinigung spb = null;

    try
    {
      if (context != null && context instanceof Spendenbescheinigung)
      {
        spb = (Spendenbescheinigung) context;
      }
      else
      {
        spb = (Spendenbescheinigung) Einstellungen.getDBService().createObject(
            Spendenbescheinigung.class, null);

        if (context != null && (context instanceof Mitglied))
        {
          Mitglied m = (Mitglied) context;
          spb.setZeile1(m.getAnrede());
          spb.setZeile2(m.getVornameName());
          spb.setZeile3(m.getStrasse());
          spb.setZeile4(m.getPlz() + " " + m.getOrt());
          if (m.getStaat() != null && m.getStaat().length() > 0)
          {
            spb.setZeile5(m.getStaat());
          }
        }
      }
      GUI.startView(SpendenbescheinigungView.class.getName(), spb);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler bei der Erstellung der Spendenbescheinigung"));
    }
  }
}
