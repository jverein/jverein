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
 * Revision 1.7  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.6  2009/07/27 15:26:04  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.5  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.3  2009/01/20 19:14:15  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.2  2008/05/24 14:04:08  jost
 * Redatkionelle Änderung
 *
 * Revision 1.1  2008/05/22 06:52:26  jost
 * Buchführung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.AnfangsbestandControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class AnfangsbestandView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Anfangsbestand"));

    final AnfangsbestandControl control = new AnfangsbestandControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Anfangsbestand"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Konto"), control.getKonto());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Datum"), control
        .getDatum(true));
    if (control.getAnfangsbestand().getID() != null)
    {
      control.getDatum(false).setEnabled(false);
    }
    group.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"), control
        .getBetrag());

    ButtonArea buttons = new ButtonArea(getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.ANFANGSBESTAENDE, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&speichern"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  public void unbind() throws ApplicationException
  {
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Anfangsbestand</span></p>"
        + "</form>";
  }
}
