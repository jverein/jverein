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
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

public class Familienverband implements Part
{
  private TabFolder tab;

  private Beitragsgruppe gruppe;

  private MitgliedControl control;

  private Container cont;

  public Familienverband(MitgliedControl control, Beitragsgruppe gruppe)
  {
    this.control = control;
    this.gruppe = gruppe;
  }

  public void paint(Composite parent) throws RemoteException
  {
    cont = new SimpleContainer(parent);
    final GridData g = new GridData(GridData.FILL_HORIZONTAL);

    tab = new TabFolder(cont.getComposite(), SWT.NONE);
    tab.setLayoutData(g);
    new TabGroup(tab, "");
    TabGroup tg1 = new TabGroup(tab, "");
    control.getFamilienangehoerigenTable().paint(tg1.getComposite());
    TabGroup tg2 = new TabGroup(tab, "");
    tg2.addLabelPair("Zahler", control.getZahler());
    if (gruppe != null)
    {
      tab.setSelection(gruppe.getBeitragsArt());
      tab.layout(true);
    }
  }

  public void setBeitragsgruppe(Beitragsgruppe gruppe)
  {
    this.gruppe = gruppe;
    try
    {
      tab.setSelection(gruppe.getBeitragsArt());
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    tab.redraw();
    tab.layout(true);
  }

}
