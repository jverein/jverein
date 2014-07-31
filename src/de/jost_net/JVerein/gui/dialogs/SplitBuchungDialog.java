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
package de.jost_net.JVerein.gui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.parts.BuchungPart;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;

/**
 * Dialog zur Bearbeitung einer Splitbuchung.
 */
public class SplitBuchungDialog extends AbstractDialog<Buchung>
{

  private BuchungsControl control;

  private AbstractView view;

  public SplitBuchungDialog(BuchungsControl control, AbstractView view)
  {
    super(AbstractDialog.POSITION_CENTER);
    setTitle("Zuordnung Buchungsart");
    setSize(450, 650);
    this.control = control;
    this.view = view;
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    BuchungPart part = new BuchungPart(control, view, true);
    part.paint(parent);

    getShell().setMinimumSize(getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));

  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  public Buchung getData() throws Exception
  {
    return null;
  }

}
