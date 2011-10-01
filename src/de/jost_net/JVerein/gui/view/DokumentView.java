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
import de.jost_net.JVerein.gui.control.DokumentControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;

public class DokumentView extends AbstractView
{
  private String verzeichnis;

  public DokumentView(String verzeichnis)
  {
    this.verzeichnis = verzeichnis;
  }

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Dokument"));

    final DokumentControl control = new DokumentControl(this, verzeichnis);

    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    LabelGroup grDokument = new LabelGroup(scrolled.getComposite(),
        JVereinPlugin.getI18n().tr("Dokument"));
    grDokument.addLabelPair(JVereinPlugin.getI18n().tr("Datei"),
        control.getDatei());
    grDokument.addLabelPair(JVereinPlugin.getI18n().tr("Datum"),
        control.getDatum());
    grDokument.addLabelPair(JVereinPlugin.getI18n().tr("Bemerkung"),
        control.getBemerkung());
    ButtonArea buttons = new ButtonArea(getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGEN, false,
        "help-browser.png");
    buttons.addButton(control.getSpeichernButton(verzeichnis + "."));
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Dokument</span></p>";
  }
}
