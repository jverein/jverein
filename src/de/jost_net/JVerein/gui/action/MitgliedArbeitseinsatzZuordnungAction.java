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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.dialogs.MitgliedArbeitseinsatzZuordnungDialog;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Arbeitseinsätze den Mitgliedern zuordnen.
 */
public class MitgliedArbeitseinsatzZuordnungAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null
        || (!(context instanceof Mitglied) && !(context instanceof Mitglied[])))
    {
      throw new ApplicationException("Kein Mitglied ausgewählt");
    }
    Mitglied[] m = null;
    if (context instanceof Mitglied)
    {
      m = new Mitglied[] { (Mitglied) context };
    }
    else if (context instanceof Mitglied[])
    {
      m = (Mitglied[]) context;
    }

    MitgliedArbeitseinsatzZuordnungDialog maz = new MitgliedArbeitseinsatzZuordnungDialog(
        AbstractDialog.POSITION_CENTER);
    try
    {
      Arbeitseinsatz part = maz.open();
      int count = 0;
      for (Mitglied mit : m)
      {
        Arbeitseinsatz ae = (Arbeitseinsatz) Einstellungen.getDBService()
            .createObject(Arbeitseinsatz.class, null);
        ae.setDatum(part.getDatum());
        ae.setMitglied(Integer.parseInt(mit.getID()));
        ae.setStunden(part.getStunden());
        ae.setBemerkung(part.getBemerkung());
        ae.store();
        count++;
      }
      GUI.getStatusBar().setSuccessText(
          String.format("%d Arbeitseinsätze gespeichert.", count));
    }
    catch (ApplicationException e)
    {
      SimpleDialog sd = new SimpleDialog(AbstractDialog.POSITION_CENTER);
      sd.setText(e.getMessage());
      try
      {
        sd.open();
      }
      catch (Exception e1)
      {
        Logger.error("Fehler", e);
      }
      Logger.error("Fehler", e);
    }

    catch (Exception e)
    {
      Logger.error("Fehler", e);
    }
  }
}
