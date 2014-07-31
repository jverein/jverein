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

import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.dialogs.SplitBuchungDialog;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SplitBuchungDetailAction implements Action
{
  private BuchungsControl control;

  private AbstractView view;

  public SplitBuchungDetailAction(BuchungsControl control, AbstractView view)
  {
    this.control = control;
    this.view = view;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    BuchungsControl bc = new BuchungsControl(view);
    SplitBuchungDialog spd = new SplitBuchungDialog(bc, view);
    try
    {
      spd.open();
      control.refreshSplitbuchungen();
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException(e);
    }
  }
}
