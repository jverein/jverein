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
 * Revision 1.2  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.BeitragsgruppeDeleteAction;
import de.jost_net.JVerein.gui.action.BeitragsgruppeDetailAction;
import de.jost_net.JVerein.gui.control.BeitragsgruppeControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeSucheView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Beitragsgruppen");

    BeitragsgruppeControl control = new BeitragsgruppeControl(this);

    control.getBeitragsgruppeTable().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton("Löschen", new BeitragsgruppeDeleteAction(), control
        .getBeitragsgruppeTable());
    buttons.addButton("Neu", new BeitragsgruppeDetailAction());
    buttons.addButton("<< Zurück", new BackAction());
  }

  public void unbind() throws ApplicationException
  {
  }
}
