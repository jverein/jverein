/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe 
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Ein Dialog, ueber den man die Personenart eines neuen Mitglieds auswählen
 * kann.
 */
public class BuchungsjournalSortDialog extends AbstractDialog
{

  public final static String DATUM = JVereinPlugin.getI18n().tr("Datum");

  public final static String BUCHUNGSNUMMER = JVereinPlugin.getI18n().tr(
      "Buchungsnummer");

  private String selected = null;

  private SelectInput sortierung = null;

  public BuchungsjournalSortDialog(int position)
  {
    super(position);

    setTitle(JVereinPlugin.getI18n().tr("Buchungsjournal-Sortierung"));
    setSize(250, 150);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup options = new LabelGroup(parent, JVereinPlugin.getI18n().tr(
        "Buchungsjournal-Sortierung"));
    options.addInput(this.getSortierung());
    ButtonArea b = new ButtonArea(parent, 2);
    b.addButton(JVereinPlugin.getI18n().tr("weiter"), new Action()
    {

      public void handleAction(Object context)
      {
        close();
      }
    });
    b.addButton(JVereinPlugin.getI18n().tr("abbrechen"), new Action()
    {

      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
  }

  @Override
  protected Object getData() throws Exception
  {
    return this.selected;
  }

  private SelectInput getSortierung()
  {
    if (this.sortierung != null)
    {
      return this.sortierung;
    }
    this.sortierung = new SelectInput(new Object[] { DATUM, BUCHUNGSNUMMER },
        null);
    this.sortierung.setName(JVereinPlugin.getI18n().tr("Sortierung"));
    this.sortierung.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        selected = (String) sortierung.getValue();
      }
    });
    return this.sortierung;
  }
}
