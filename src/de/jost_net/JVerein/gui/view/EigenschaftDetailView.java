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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.EigenschaftControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class EigenschaftDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Eigenschaft"));

    final EigenschaftControl control = new EigenschaftControl(this);

    LabelGroup grEigenschaft = new LabelGroup(getParent(), JVereinPlugin
        .getI18n().tr("Eigenschaft"));
    grEigenschaft.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung"),
        control.getBezeichnung());
    grEigenschaft.addLabelPair(JVereinPlugin.getI18n().tr("Gruppet"), control
        .getEigenschaftGruppe());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.EIGENSCHAFT, false,
        "help-browser.png");
    // buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
    // new EigenschaftAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  public void unbind() throws ApplicationException
  {
  }
}
