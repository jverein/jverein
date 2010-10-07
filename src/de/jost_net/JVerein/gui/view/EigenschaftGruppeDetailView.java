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
 * Revision 1.3  2010-09-09 18:50:40  jost
 * Eigenschaftengruppen können jetzt auch das Merkmal "Pflicht" haben. Dann muß mindestens eine Eigenschaft ausgewählt werden.
 *
 * Revision 1.2  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.1  2009/11/17 21:00:04  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.EigenschaftGruppeListeAction;
import de.jost_net.JVerein.gui.control.EigenschaftGruppeControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class EigenschaftGruppeDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Eigenschaften-Gruppe"));

    final EigenschaftGruppeControl control = new EigenschaftGruppeControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Eigenschaften-Gruppe"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung"), control
        .getBezeichnung());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Pflicht"), control
        .getPflicht());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.EIGENSCHAFTGRUPPE, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("s&uche"),
        new EigenschaftGruppeListeAction(), null, false, "system-search.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&speichern"), new Action()
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

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Eigenschaften Gruppe</span></p>"
        + "</form>";
  }
}
