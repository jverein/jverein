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
import de.jost_net.JVerein.gui.control.AbbuchungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class AbbuchungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Abbuchung");

    final AbbuchungControl control = new AbbuchungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Parameter");
    group.addLabelPair("Jahresabbuchung", control.getJahresabbuchung());
    group.addLabelPair("Von Eingabedatum", control.getVondatum());
    group.addLabelPair("Zahlungsgrund", control.getZahlungsgrund());
    group.addLabelPair("Zusatzabbuchung", control.getZusatzabbuchung());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(control.getStartButton());
    buttons.addButton("<< Zurück", new BackAction());

  }

  public void unbind() throws ApplicationException
  {
  }
}
