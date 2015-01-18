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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Ein Dialog, ueber den man die Personenart eines neuen Mitglieds auswählen
 * kann.
 */
public class BuchungsjournalSortDialog extends AbstractDialog<String>
{

  public final static String DATUM = "Datum";

  public final static String DATUM_NAME = "Datum, Name, Buchungsnummer";

  public final static String BUCHUNGSNUMMER = "Buchungsnummer";

  private String selected = DATUM;

  private SelectInput sortierung = null;

  public BuchungsjournalSortDialog(int position)
  {
    super(position);

    setTitle("Buchungsjournal-Sortierung");
    setSize(300, 200);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup options = new LabelGroup(parent, "Buchungsjournal-Sortierung");
    options.addInput(this.getSortierung());
    ButtonArea b = new ButtonArea();
    b.addButton("weiter", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
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

  private SelectInput getSortierung()
  {
    if (this.sortierung != null)
    {
      return this.sortierung;
    }
    this.sortierung = new SelectInput(new Object[] { DATUM, DATUM_NAME,
        BUCHUNGSNUMMER }, DATUM);
    this.sortierung.setName("Sortierung");
    this.sortierung.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        selected = (String) sortierung.getValue();
      }
    });
    return this.sortierung;
  }
}
