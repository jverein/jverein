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
import de.jost_net.JVerein.gui.action.BuchungsartAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsartControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class BuchungsartListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchungsarten"));

    BuchungsartControl control = new BuchungsartControl(this);

    control.getBuchungsartList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGSARTEN, false,
        "help-browser.png");
    buttons.addButton(control.getPDFAusgabeButton());
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new BuchungsartAction(), null, false, "document-new.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchungsart</span></p>"
        + "<p>Die Nummer und die Bezeichung der Buchungsart sind zu erfassen.</p>"
        + "<p>Bei der Vergabe der Nummern sollten Nummernkreise für Eingaben "
        + "und Ausgaben gebildet werden. Beispielsweise die 1000er Nummern "
        + "für Einnahmen und die 2000er Nummern für Ausgaben. Die Sortierung "
        + "der Buchungsauswertung erfolgt nach diesen Nummern.</p>" + "</form>";
  }
}
