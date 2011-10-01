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
import de.jost_net.JVerein.gui.action.FormularAnzeigeAction;
import de.jost_net.JVerein.gui.action.FormularfeldAction;
import de.jost_net.JVerein.gui.control.FormularfeldControl;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class FormularfelderListeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Formularfelder"));

    FormularfeldControl control = new FormularfeldControl(this,
        (Formular) getCurrentObject());
    control.getFormularfeldList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.FORMULARE, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("anzeigen"),
        new FormularAnzeigeAction(), getCurrentObject(), false, "edit.png");
    buttons
        .addButton(JVereinPlugin.getI18n().tr("neu"), new FormularfeldAction(),
            getCurrentObject(), false, "document-new.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Formularfelder</span></p>"
        + "<p>Jedem Formular müssen Formularfelder zugeordnet werden. Mit <i>neu</i> "
        + "wird ein neues Formularfeld aufgenommen. Mit einem Doppelklick öffnet "
        + "sich das Bearbeitungsfenster für ein Formularfeld. Durch einen Rechtsklick "
        + "erscheint ein Kontextmenü. Damit können Formularfelder gelöscht werden.</p>"
        + "</form>";
  }
}
