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
 * Revision 1.3  2008/05/24 14:04:08  jost
 * Redatkionelle √Ñnderung
 *
 * Revision 1.2  2008/01/01 19:51:34  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.1  2007/12/22 08:26:23  jost
 * Neu: Jubil√§enliste
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class JubilaeenView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Jubil‰en");

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Parameter");
    group.addLabelPair("Jahr", control.getJubeljahr());
    group.addLabelPair("Art", control.getJubelArt());

    ButtonArea buttons = new ButtonArea(getParent(), 3);

    buttons.addButton("<< Zur¸ck", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.JUBILAEEN);
    buttons.addButton(control.getStartJubilaeenButton());
  }

  public void unbind() throws ApplicationException
  {
  }
}
