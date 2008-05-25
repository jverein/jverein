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
import de.jost_net.JVerein.gui.control.JahressaldoControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class JahressaldoView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Jahressaldo");

    final JahressaldoControl control = new JahressaldoControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Jahr");
    group.addLabelPair("Jahr", control.getSuchJahr());

    ButtonArea buttons = new ButtonArea(this.getParent(), 1);
    Button button = new Button("suchen", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.getSaldoList();
      }
    }, null, true);
    buttons.addButton(button);

    control.getSaldoList().paint(this.getParent());

    ButtonArea buttons2 = new ButtonArea(this.getParent(), 3);
    buttons2.addButton("<< Zurück", new BackAction());
    buttons2.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.JAHRESSALDO);
    buttons2.addButton(control.getStartAuswertungButton());
  }

  public void unbind() throws ApplicationException
  {
  }
}
