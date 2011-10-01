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
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.AdressbuchExportControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class AdressbuchExportView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Adressbuch-Export"));

    final AdressbuchExportControl control = new AdressbuchExportControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Parameter"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("nur mit Email"),
        control.getNurEmail());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Encoding"),
        control.getEncoding());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Trennzeichen"),
        control.getTrennzeichen());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.ADRESSBUCHEXPORT, false,
        "help-browser.png");
    buttons.addButton(control.getStartButton());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Adressbuchexport</span></p>"
        + "<p>Export der Mitgliederdaten für den Import in ein Adressbuch.</p>"
        + "<p>Das Trennzeichen und das Encoding können bestimmt werden. Es kann "
        + "festgelegt werden, ob alle Mitglieder oder nur die Mitglieder mit Email-Adresse "
        + "ausgegeben werden.</p></form>";
  }
}
