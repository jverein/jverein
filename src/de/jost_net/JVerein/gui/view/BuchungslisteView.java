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

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class BuchungslisteView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Liste der Buchungen"));

    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Suche Buchungen"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Konto"),
        control.getSuchKonto());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsart"),
        control.getSuchBuchungsart());
    group.addLabelPair(JVereinPlugin.getI18n().tr("von Datum"),
        control.getVondatum());
    group.addLabelPair(JVereinPlugin.getI18n().tr("bis Datum"),
        control.getBisdatum());
    group.addLabelPair(JVereinPlugin.getI18n().tr("enthaltener Text"),
        control.getSuchtext());

    ButtonArea buttons = new ButtonArea(this.getParent(), 1);
    Button button = new Button("suchen", new Action()
    {

      public void handleAction(Object context)
      {
        try
        {
          control.getBuchungsList();
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }
    }, null, true, "system-search.png");

    buttons.addButton(button);

    control.getBuchungsList().paint(this.getParent());

    ButtonArea buttons2 = new ButtonArea(this.getParent(), 5);
    buttons2.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGEN, false,
        "help-browser.png");
    buttons2.addButton(control.getStartAuswertungBuchungsjournalButton());
    buttons2.addButton(control.getStartAuswertungEinzelbuchungenButton());
    buttons2.addButton(control.getStartAuswertungSummenButton());
    buttons2.addButton(JVereinPlugin.getI18n().tr("neu"),
        new BuchungNeuAction(), null, false, "document-new.png");

  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchungen</span></p>"
        + "<p>Alle Buchungen aus dem vorgegebenen Zeitraum werden angezeigt. Durch einen "
        + "Doppelklick auf eine Buchung kann die Buchungsart zugeordnet werden.</p>"
        + "</form>";
  }
}
