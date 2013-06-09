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
import java.text.MessageFormat;

import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.dialogs.BuchungsartZuordnungDialog;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Buchungsart zuordnen.
 */
public class BuchungBuchungsartZuordnungAction implements Action
{
  private BuchungsControl control;

  public BuchungBuchungsartZuordnungAction(BuchungsControl control)
  {
    this.control = control;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null
        || (!(context instanceof Buchung) && !(context instanceof Buchung[])))
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
        BuchungsartZuordnungDialog baz = new BuchungsartZuordnungDialog(
            BuchungsartZuordnungDialog.POSITION_MOUSE);
        baz.open();
        Buchungsart ba = baz.getBuchungsart();
        int counter = 0;

        for (Buchung buchung : b)
        {
          boolean protect = buchung.getBuchungsart() != null
              && !baz.getOverride();
          if (protect)
          {
            counter++;
          }
          else
          {
            buchung.setBuchungsart(new Long(ba.getID()));
            buchung.store();
          }
        }
        control.getBuchungsList();
        String protecttext = "";
        if (counter > 0)
        {
          protecttext = MessageFormat.format(
              ", {0} Buchungen wurden nicht überschrieben. ",
              new Object[] { counter + "" });
        }
        GUI.getStatusBar().setSuccessText(
            "Buchungsarten zugeordnet" + protecttext);
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
        GUI.getStatusBar().setErrorText(
            "Fehler bei der Zuordnung der Buchungsart");
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
