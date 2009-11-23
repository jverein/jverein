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
 * Revision 1.1  2009/11/17 21:00:18  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.EigenschaftGruppeDeleteAction;
import de.jost_net.JVerein.gui.action.EigenschaftGruppeDetailAction;
import de.jost_net.JVerein.gui.control.EigenschaftGruppeControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class EigenschaftGruppeListeView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Eigenschaften Gruppe"));

    EigenschaftGruppeControl control = new EigenschaftGruppeControl(this);

    control.getEigenschaftGruppeList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.EIGENSCHAFTGRUPPE, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("löschen"),
        new EigenschaftGruppeDeleteAction(),
        control.getEigenschaftGruppeList(), false, "user-trash.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new EigenschaftGruppeDetailAction(true), null, false,
        "document-new.png");
  }

  public void unbind() throws ApplicationException
  {
  }
}
