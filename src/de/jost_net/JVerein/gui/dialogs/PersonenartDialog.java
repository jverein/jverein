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

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.input.PersonenartInput;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Ein Dialog, ueber den man die Personenart eines neuen Mitglieds auswählen
 * kann.
 */
public class PersonenartDialog extends AbstractDialog<String>
{

  private String selected = null;

  private PersonenartInput personenart = null;

  public PersonenartDialog(int position)
  {
    super(position);

    setTitle("Personenart");
    setSize(450, 150);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup options = new LabelGroup(parent, "Personenart");
    options.addInput(this.getPersonenartInput());
    ButtonArea b = new ButtonArea();
    b.addButton("weiter", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        String s = (String) personenart.getValue();
        selected = s.substring(0, 1);
        close();
      }
    });
    b.addButton("abbrechen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
    b.paint(parent);
  }

  @Override
  protected String getData() throws Exception
  {
    return this.selected;
  }

  private PersonenartInput getPersonenartInput()
  {
    if (personenart != null)
    {
      return personenart;
    }
    return personenart = new PersonenartInput("n");
  }
}
