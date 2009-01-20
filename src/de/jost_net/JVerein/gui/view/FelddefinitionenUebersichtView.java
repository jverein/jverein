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
 * Revision 1.2  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.1  2008/04/10 19:00:05  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.FelddefinitionDetailAction;
import de.jost_net.JVerein.gui.control.FelddefinitionControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class FelddefinitionenUebersichtView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Felddefinitionen");

    FelddefinitionControl control = new FelddefinitionControl(this);

    control.getFelddefinitionTable().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.FELDDEFINITIONEN);
    // buttons.addButton("Löschen", new BeitragsgruppeDeleteAction(), control
    // .getBeitragsgruppeTable());
    buttons.addButton("Neu", new FelddefinitionDetailAction());
  }

  public void unbind() throws ApplicationException
  {
  }
}
