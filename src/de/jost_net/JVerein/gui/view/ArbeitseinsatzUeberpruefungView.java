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

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.ArbeitseinsatzControl;
import de.jost_net.JVerein.gui.input.ArbeitseinsatzUeberpruefungInput;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class ArbeitseinsatzUeberpruefungView extends AbstractView
{
  Button butArbeitseinsaetze = null;

  ArbeitseinsatzUeberpruefungInput aui = null;

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(
        JVereinPlugin.getI18n().tr("Arbeitsdienst-Überprüfung"));

    final ArbeitseinsatzControl control = new ArbeitseinsatzControl(this);
    butArbeitseinsaetze = control.getArbeitseinsatzAusgabeButton();
    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Filter"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Jahr"),
        control.getSuchJahr());
    aui = control.getAuswertungSchluessel();
    aui.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        int i = (Integer) aui.getValue();
        butArbeitseinsaetze
            .setEnabled(i == ArbeitseinsatzUeberpruefungInput.MINDERLEISTUNG);
      }

    });
    group.addLabelPair(JVereinPlugin.getI18n().tr("Auswertung"), aui);

    ButtonArea buttons = new ButtonArea(this.getParent(), 1);
    Button button = new Button(JVereinPlugin.getI18n().tr("suchen"),
        new Action()
        {

          public void handleAction(Object context) throws ApplicationException
          {
            control.getArbeitseinsatzUeberpruefungList();
          }
        }, null, true, "system-search.png");
    buttons.addButton(button);

    control.getArbeitseinsatzUeberpruefungList().paint(getParent());
    ButtonArea buttons2 = new ButtonArea(this.getParent(), 4);
    buttons2.addButton(control.getPDFAusgabeButton());
    buttons2.addButton(control.getCSVAusgabeButton());
    buttons2.addButton(control.getArbeitseinsatzAusgabeButton());
    buttons2.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.ARBEITSEINSATZ, false,
        "help-browser.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Jahressaldo</span></p>"
        + "</form>";
  }
}
