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
 * Revision 1.2  2009/11/22 16:19:25  jost
 * Typo
 *
 * Revision 1.1  2009/11/17 20:59:50  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.EigenschaftControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class EigenschaftDetailView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Eigenschaft"));

    final EigenschaftControl control = new EigenschaftControl(this);

    LabelGroup grEigenschaft = new LabelGroup(getParent(),
        JVereinPlugin.getI18n().tr("Eigenschaft"));
    grEigenschaft.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung"),
        control.getBezeichnung());
    grEigenschaft.addLabelPair(JVereinPlugin.getI18n().tr("Gruppe"),
        control.getEigenschaftGruppe());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.EIGENSCHAFT, false,
        "help-browser.png");
    // buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
    // new EigenschaftAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  // TODO getHelp()

}
