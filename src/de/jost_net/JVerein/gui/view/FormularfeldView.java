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
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.FormularfeldControl;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class FormularfeldView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Formularfeld");
    Formularfeld ff = (Formularfeld) getCurrentObject();

    final FormularfeldControl control = new FormularfeldControl(this, ff
        .getFormular());

    LabelGroup group = new LabelGroup(getParent(), "Formularfeld");
    group.addLabelPair("Name", control.getName());
    group.addLabelPair("von links", control.getX());
    group.addLabelPair("von unten", control.getY());
    group.addLabelPair("Font", control.getFont());
    group.addLabelPair("Font-Höhe", control.getFontsize());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.FORMULARE);
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
