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
 * Revision 1.5  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.4  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.3  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.2  2009/11/23 20:41:45  jost
 * Bugfix Lösch-Button
 *
 * Revision 1.1  2009/11/17 21:00:42  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.EigenschaftDeleteAction;
import de.jost_net.JVerein.gui.action.EigenschaftDetailAction;
import de.jost_net.JVerein.gui.control.EigenschaftControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class EigenschaftListeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Eigenschaften"));

    EigenschaftControl control = new EigenschaftControl(this);

    control.getEigenschaftList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.EIGENSCHAFT, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("löschen"),
        new EigenschaftDeleteAction(), control.getEigenschaftList(), false,
        "user-trash.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new EigenschaftDetailAction(true), null, false, "document-new.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Eigenschaften</span></p>"
        + "</form>";
  }
}
