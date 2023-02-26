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

package de.jost_net.JVerein.gui.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.TabFolder;

/**
 * @author Rolf Mamat
 */
public class GuiRepainter
{
  /***
   * Nach einem größerem Update des Layouts soll eine Componente neu gezeichnet
   * werden. Es wird diejenige mit einem Layout gesucht und dieser mitgeteilt
   * dass wir neu zeichnen wollen.
   * 
   * @param composite
   */
  public static void repaint(Composite composite)
  {
    while (composite != null)
    {
      if (composite.getLayout() != null)
        composite.layout();
      if (composite instanceof TabFolder || composite instanceof ScrolledComposite)
        return;
      composite = composite.getParent();
    }
  }
}
