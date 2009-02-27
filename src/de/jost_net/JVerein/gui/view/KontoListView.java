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
 * Revision 1.5  2009/01/22 18:24:24  jost
 * neue Icons
 *
 * Revision 1.4  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.3  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.2  2008/05/24 14:04:08  jost
 * Redatkionelle Änderung
 *
 * Revision 1.1  2008/05/22 06:53:57  jost
 * Buchführung: Beginn des Geschäftsjahres
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.HibiscusKontenImportAction;
import de.jost_net.JVerein.gui.action.KontoAction;
import de.jost_net.JVerein.gui.control.KontoControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class KontoListView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Konten");

    KontoControl control = new KontoControl(this);

    control.getKontenList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.KONTEN, false, "help-browser.png");
    buttons.addButton("Hibiscus-Import",
        new HibiscusKontenImportAction(control), null, false, "go.png");
    buttons
        .addButton("neu", new KontoAction(), null, false, "document-new.png");
  }

  public void unbind() throws ApplicationException
  {
  }
}
