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
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

public class MitgliedSekundaereBeitragsgruppePart implements Part
{
  private TabFolder tab;

  private MitgliedControl control;

  private Container cont;

  private Composite parent;

  public MitgliedSekundaereBeitragsgruppePart(MitgliedControl control)
  {
    this.control = control;
  }

  @Override
  public void paint(Composite parent) throws RemoteException
  {
    if (this.parent == null)
    {
      this.parent = parent;
    }
    cont = new SimpleContainer(parent, true, 5);
    final GridData grid = new GridData(GridData.FILL_HORIZONTAL);
    grid.grabExcessHorizontalSpace = true;
    cont.getComposite().setLayoutData(grid);
    tab = new TabFolder(cont.getComposite(), SWT.NONE);
    tab.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    TabGroup tg1 = new TabGroup(tab, "Sekundäre Beitragsgruppen");

    control.getSekundaereBeitragsgruppe().paint(tg1.getComposite());
  }
}
