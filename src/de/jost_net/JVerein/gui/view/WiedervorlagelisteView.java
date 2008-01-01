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
 * Revision 1.2  2007/12/20 20:33:30  jost
 * Neu: Wiedervorlage-Ãœbersicht in der Jameica-Startseite
 *
 * Revision 1.1  2007/05/07 19:26:20  jost
 * Neu: Wiedervorlage
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.WiedervorlageListeAction;
import de.jost_net.JVerein.gui.parts.WiedervorlageList;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class WiedervorlagelisteView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Wiedervorlagen");
    new WiedervorlageList(new WiedervorlageListeAction())
        .getWiedervorlageList().paint(this.getParent());
    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.wiedervorlage);

  }

  public void unbind() throws ApplicationException
  {
  }

}
