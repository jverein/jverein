/**********************************************************************
 * $Source$
 *  * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.LehrgangControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class LehrgangView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Lehrgang");
    final LehrgangControl control = new LehrgangControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Lehrgang");
    group.addLabelPair("Lehrgangsart", control.getLehrgangsart());
    group.addLabelPair("am/von", control.getVon());
    group.addLabelPair("bis", control.getBis());
    group.addLabelPair("Veranstalter", control.getVeranstalter());
    group.addLabelPair("Ergebnis", control.getErgebnis());

    ButtonArea buttons = new ButtonArea(getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.LEHRGANG, false, "help-browser.png");
    buttons.addButton("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  public void unbind() throws ApplicationException
  {
  }
}
