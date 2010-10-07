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
import de.willuhn.util.ApplicationException;

public class AbrechnungslaufListView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Abrechnungsläufe"));

    AbrechnungslaufControl control = new AbrechnungslaufControl(this);

    control.getAbrechungslaeufeList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGSARTEN, false,
        "help-browser.png");
  }

  public void unbind() throws ApplicationException
  {
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Liste der Abrechnungsläufe</span></p>"
        + "<p>Mit einem Rechtsklick kann ein Abrechnungslauf gelöscht werden.</p>"
        + "</form>";
  }
}
