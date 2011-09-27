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
import de.jost_net.JVerein.gui.control.BLZUpdateControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class BLZUpdateView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("BLZ-Update"));

    final BLZUpdateControl control = new BLZUpdateControl(this);

    LabelGroup group = new LabelGroup(getParent(), "BLZ-Datei");
    group.addInput(control.getBlzdatei());
    ButtonArea topbuttons = new ButtonArea();
    topbuttons.addButton(control.getDateiauswahlButton());
    topbuttons.paint(group.getComposite());
    control.getList().paint(getParent());
    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BLZUPDATE, false,
        "help-browser.png");
    buttons.addButton(control.getSpeichernButton());
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">BLZ-Update</span></p>"
        + "<p>Bankleitzahlendatei unter http://www.bundesbank.de/zahlungsverkehr/zahlungsverkehr_bankleitzahlen_download.php "
        + "im Format PC-Text ZIP gepackt herunterladen und hier ausw‰hlen. Anschlieﬂend auf speichern klicken."
        + "</p></form>";
  }
}
