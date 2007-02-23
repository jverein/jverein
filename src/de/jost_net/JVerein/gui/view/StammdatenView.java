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
 * Revision 1.2  2006/10/29 07:49:29  jost
 * Neu: Mitgliederstatistik
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.StammdatenControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class StammdatenView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Stammdaten");

    final StammdatenControl control = new StammdatenControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Stammdaten");
    group.addLabelPair("Name", control.getName());
    group.addLabelPair("Bankleitzahl", control.getBlz());
    group.addLabelPair("Konto", control.getKonto());
    group.addLabelPair("Altersgruppen", control.getAltersgruppen());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton("<< Zurück", new BackAction());
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
