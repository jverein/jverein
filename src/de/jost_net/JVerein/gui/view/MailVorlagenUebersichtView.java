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
 * Revision 1.1  2010/02/01 21:00:49  jost
 * Neu: Einfache Mailfunktion
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.MailVorlageDetailAction;
import de.jost_net.JVerein.gui.control.MailVorlageControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class MailVorlagenUebersichtView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Mail-Vorlagen"));

    MailVorlageControl control = new MailVorlageControl(this);

    control.getMailVorlageTable().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.MAIL, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&neu"),
        new MailVorlageDetailAction(), null, false, "document-new.png");
  }

  public void unbind() throws ApplicationException
  {
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Übersicht über die Mailvorlagen</span></p>"
        + "</form>";
  }
}
