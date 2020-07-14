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

import java.util.Date;

import de.jost_net.JVerein.gui.dialogs.BuchungenMitgliedskontenZuordnungDialog;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungMitgliedskontoZuordnungAutomatischAction implements Action
{
  private DateInput vondatum;
  private DateInput bisdatum;

  public BuchungMitgliedskontoZuordnungAutomatischAction(DateInput vondatum, DateInput bisdatum) {
    this.vondatum = vondatum;
    this.bisdatum = bisdatum;
  }

 /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    try
    {
      BuchungenMitgliedskontenZuordnungDialog d = new BuchungenMitgliedskontenZuordnungDialog((Date)vondatum.getValue(), (Date)bisdatum.getValue());
      d.open();
    }
    catch (OperationCanceledException oce)
    {
      Logger.info(oce.getMessage());
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      Logger.error("error while assign transfers to members", e);
      GUI.getStatusBar().setErrorText("Fehler beim Zuordnen von Buchungen zu Mitgliedskonten");
    }
  }

}
