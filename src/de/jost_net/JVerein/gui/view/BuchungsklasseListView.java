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
 * Revision 1.2  2010-08-23 13:39:33  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.1  2009/09/10 18:18:22  jost
 * neu: Buchungsklassen
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BuchungsklasseAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsklasseControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class BuchungsklasseListView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchungsklassen"));

    BuchungsklasseControl control = new BuchungsklasseControl(this);

    control.getBuchungsklasseList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGSKLASSEN, false,
        "help-browser.png");

    buttons.addButton(JVereinPlugin.getI18n().tr("&neu"),
        new BuchungsklasseAction(), null, false, "document-new.png");
  }

  public void unbind() throws ApplicationException
  {
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchungsklasse</span></p>"
        + "<p>Buchungsklassen dienen der Gruppierung von Buchungsarten. Z. B. können die "
        + "Buchungsarten nach Ideeller Betrieb, Wirtschaftlicher Betrieb und Vermögen gruppiert werden.</p>"
        + "<p>Die Buchungsklassen sind den Buchungsarten zuzuordnen.</p></form>";
  }
}
