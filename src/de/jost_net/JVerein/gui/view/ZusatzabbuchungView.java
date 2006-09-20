/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.ZusatzabbuchungDeleteAction;
import de.jost_net.JVerein.gui.control.ZusatzabbuchungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class ZusatzabbuchungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Zusatzabbuchung");
    final ZusatzabbuchungControl control = new ZusatzabbuchungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Zusatzabbuchung");
    group.addLabelPair("Fälligkeit", control.getFaelligkeit());
    group.addLabelPair("Buchungstext", control.getBuchungstext());
    group.addLabelPair("Betrag", control.getBetrag());
    group.addLabelPair("Ausführung", control.getAusfuehrung());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Löschen", new ZusatzabbuchungDeleteAction(), control
        .getZusatzabbuchung());
    buttons.addButton("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true);
  }

  public void unbind() throws ApplicationException
  {
  }
}
