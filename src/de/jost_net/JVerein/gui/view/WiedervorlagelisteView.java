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
 * Revision 1.5  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.4  2008/05/24 14:04:08  jost
 * Redatkionelle Änderung
 *
 * Revision 1.3  2008/01/01 19:53:22  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.2  2007/12/20 20:33:30  jost
 * Neu: Wiedervorlage-Übersicht in der Jameica-Startseite
 *
 * Revision 1.1  2007/05/07 19:26:20  jost
 * Neu: Wiedervorlage
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.WiedervorlageListeAction;
import de.jost_net.JVerein.gui.parts.WiedervorlageList;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
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
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.WIEDERVORLAGE, false, "help-browser.png");

  }

  public void unbind() throws ApplicationException
  {
  }

}
