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
 * Revision 1.5  2011-01-15 09:46:47  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.4  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.3  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.2  2010-08-23 13:38:43  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.1  2010/05/18 20:20:43  jost
 * Anpassung Klassenname
 *
 * Revision 1.1  2010/04/25 13:55:18  jost
 * Vorarbeiten Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.AbrechnungslaufControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class AbrechnungslaufListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Abrechnungsläufe"));

    AbrechnungslaufControl control = new AbrechnungslaufControl(this);

    control.getAbrechungslaeufeList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGSARTEN, false,
        "help-browser.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Liste der Abrechnungsläufe</span></p>"
        + "<p>Mit einem Rechtsklick kann ein Abrechnungslauf gelöscht werden.</p>"
        + "</form>";
  }
}
