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
import de.jost_net.JVerein.gui.control.ZusatzabbuchungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class ZusatzabbuchunglisteView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Liste der Zusatzabbuchungen");

    final ZusatzabbuchungControl control = new ZusatzabbuchungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Ausführungstag");
    group.addLabelPair("Ausführungstag", control.getAusführungSuch());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    // Button button = new Button("suchen", new Action()
    // {
    // public void handleAction(Object context) throws ApplicationException
    // {
    // try
    // {
    // control.getBuchungsList();
    // }
    // catch (RemoteException e)
    // {
    // e.printStackTrace();
    // }
    // }
    // }, null, true);
    //
    // buttons.addButton(button);
    buttons.addButton("<< Zurück", new BackAction());

    control.getZusatzabbuchungsList().paint(this.getParent());
  }

  public void unbind() throws ApplicationException
  {
  }

}
