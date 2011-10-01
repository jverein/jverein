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
import de.jost_net.JVerein.gui.action.HibiscusKontenImportAction;
import de.jost_net.JVerein.gui.action.KontoAction;
import de.jost_net.JVerein.gui.control.KontoControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class KontoListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Konten"));

    KontoControl control = new KontoControl(this);

    control.getKontenList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.KONTEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("Hibiscus-Konten-Import"),
        new HibiscusKontenImportAction(control), null, false, "go.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"), new KontoAction(),
        null, false, "document-new.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Konten</span></p>"
        + "<p>Konten können entweder von Hibuscus übernommen werden oder durch einen "
        + "Klick auf neu aufgenommen werden.</p>"
        + "<p>Durch einen Rechtsklick auf ein Konto kann entweder der Anfangsbestand des "
        + "Kontos zu einem Zeitpunkt eingegeben werden oder das Konto kann gelöscht werden, "
        + "sofern keine Buchungen für das Konto existieren.</p></form>";
  }
}
