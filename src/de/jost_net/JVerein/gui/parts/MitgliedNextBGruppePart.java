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
import de.jost_net.JVerein.gui.util.GuiRepainter;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.logging.Logger;

/**
 * @author Rlf Mamat
 */
public class MitgliedNextBGruppePart implements Part
{
  private TabFolder tab;

  private MitgliedControl control;

  private Container cont;

  private Composite parent;

  private boolean isVisible;

  public MitgliedNextBGruppePart(MitgliedControl control)
  {
    this.control = control;
    this.isVisible = true;
  }

  @Override
  public void paint(Composite parent) throws RemoteException
  {
    if (this.parent == null)
      this.parent = parent;

    cont = new SimpleContainer(parent, true, 5);
    final GridData grid = new GridData(GridData.FILL_HORIZONTAL);
    grid.grabExcessHorizontalSpace = true;
    cont.getComposite().setLayoutData(grid);
    if (this.isVisible == false)
      return;

    tab = new TabFolder(cont.getComposite(), SWT.NONE);
    tab.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    TabGroup tg1 = new TabGroup(tab, "zukünftige Beitragsgruppen");

    control.getMitgliedBeitraegeTabelle().paint(tg1.getComposite());
  }

  public void setVisible(boolean visible)
  {
    if (this.isVisible == visible)
      return;
    this.isVisible = visible;
    if (null == cont)
      return;
    cont.getComposite().dispose();
    cont = null;

    try
    {
      paint(parent);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    GuiRepainter.repaint(parent);

  }
}
