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
 * Revision 1.1  2009/04/25 05:29:03  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
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
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den man die Personenart eines neuen Mitglieds auswählen
 * kann.
 */
public class PersonenartDialog extends AbstractDialog
{
  private final static String NATUERLICHE_PERSON = JVereinPlugin.getI18n().tr(
      "natürliche Person");

  private final static String JURISTISCHE_PERSON = JVereinPlugin.getI18n().tr(
      "juristische Person (Firma, Organisation, Behörde)");

  private String selected = null;

  private SelectInput personenart = null;

  public PersonenartDialog(int position)
  {
    super(position);

    setTitle(JVereinPlugin.getI18n().tr("Personenart"));
    setSize(450, 150);
  }

  protected void paint(Composite parent) throws Exception
  {
    LabelGroup options = new LabelGroup(parent, JVereinPlugin.getI18n().tr(
        "Personenart"));
    options.addInput(this.getPersonenart());
    ButtonArea b = new ButtonArea(parent, 2);
    b.addButton(JVereinPlugin.getI18n().tr("weiter"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        close();
      }
    });
    b.addButton(JVereinPlugin.getI18n().tr("abbrechen"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        throw new OperationCanceledException();
      }
    });
  }

  protected Object getData() throws Exception
  {
    return this.selected;
  }

  private SelectInput getPersonenart()
  {
    if (this.personenart != null)
    {
      return this.personenart;
    }
    this.personenart = new SelectInput(new Object[] { NATUERLICHE_PERSON,
        JURISTISCHE_PERSON }, NATUERLICHE_PERSON);
    this.personenart.setName(JVereinPlugin.getI18n().tr("Personenart"));
    this.personenart.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        String s = (String) personenart.getValue();
        selected = s.substring(0, 1);
      }
    });
    return this.personenart;
  }
}
