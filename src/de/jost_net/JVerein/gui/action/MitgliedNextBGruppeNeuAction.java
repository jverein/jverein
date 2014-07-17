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

import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.view.MitgliedNextBGruppeView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

/**
 * @author Rolf Mamat
 */
public class MitgliedNextBGruppeNeuAction implements Action
{
    MitgliedControl control;
    
    public MitgliedNextBGruppeNeuAction(MitgliedControl control)
    {
        this.control = control;
    }
    
    @Override
    public void handleAction(Object context) 
    {
        Mitglied mitglied = control.getMitglied();
        GUI.startView(MitgliedNextBGruppeView.class.getName(), mitglied);
    }

}
