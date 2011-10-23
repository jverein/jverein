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

import de.jost_net.JVerein.gui.view.SpendenView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

/**
 * Action zum Starten der Spenden-View.
 */
public class SpendenAction implements Action
{

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context) throws ApplicationException
  {
    GUI.startView(SpendenView.class, null);
  }

}

/**********************************************************************
 * $Log$
 * Revision 1.2  2011/10/01 21:40:00  jost
 * Log-Einträge entfernt. Zeigt Eclipse-History-View viel besser an. Macht den Quelltext schlanker.
 * Revision 1.1 2011-07-24 18:02:54 jost Neu:
 * Spenden für JVerein Revision 1.1 2010-08-20 12:42:02 willuhn
 * 
 * @N Neuer Spenden-Aufruf. Ich bin gespannt, ob das klappt ;)
 * 
 **********************************************************************/
