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

import de.jost_net.JVerein.gui.control.FamilienbeitragNode;
import de.jost_net.JVerein.gui.dialogs.FamilienmitgliedEntfernenDialog;
import de.willuhn.jameica.gui.Action;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FamilienmitgliedEntfernenAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof FamilienbeitragNode))
    {
      throw new ApplicationException("kein Familienmitglied ausgewählt");
    }
    FamilienbeitragNode fbn = (FamilienbeitragNode) context;
    FamilienmitgliedEntfernenDialog fed = new FamilienmitgliedEntfernenDialog(
        fbn);
    try
    {
      fed.open();
      // if (!choice.booleanValue())
      // {
      // return;
      // }
    }

    catch (Exception e)
    {
      Logger.error("Fehler", e);
      return;
    }
    // MitgliedskontoNode mkn = null;
    // Buchung bu = null;
    //
    // if (context != null && (context instanceof MitgliedskontoNode))
    // {
    // mkn = (MitgliedskontoNode) context;
    // try
    // {
    // bu = (Buchung) Einstellungen.getDBService().createObject(Buchung.class,
    // mkn.getID());
    // bu.setMitgliedskonto(null);
    // bu.store();
    // GUI.getStatusBar().setSuccessText(
    // "Istbuchung vom Mitgliedskonto gelöst.");
    // Application.getMessagingFactory().sendMessage(
    // new MitgliedskontoMessage(mkn.getMitglied()));
    // }
    // catch (RemoteException e)
    // {
    // throw new ApplicationException(
    // "Fehler beim lösen der Istbuchung vom Mitgliedskonto");
    // }
    // }
  }
}
