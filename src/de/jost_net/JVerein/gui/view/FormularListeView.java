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
import de.jost_net.JVerein.gui.action.FormularAction;
import de.jost_net.JVerein.gui.control.FormularControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class FormularListeView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Formulare"));

    FormularControl control = new FormularControl(this);

    control.getFormularList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.FORMULARE, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"), new FormularAction(),
        null, false, "document-new.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Formulare</span></p>"
        + "<p> Alle verfügbaren Formulare werden aufgelistet.</p>"
        + "<p>Durch einen Doppelklick auf ein Formular wird die Detailansicht zur "
        + "Bearbeitung geöffnet.</p>"
        + "<p> Mit einem rechten Mausklick öffnet sich ein Kontext-Menü. Damit können "
        + "die Formularfelder bearbeitet werden. Das Formular kann angezeigt und "
        + "gelöscht werden.</p></form>";
  }
}
