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
 * Revision 1.12  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.11  2009/07/24 20:23:07  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.10  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.9  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.8  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.7  2008/09/21 08:45:58  jost
 * Neu: AltersjubliÃ¤en
 *
 * Revision 1.6  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.5  2008/01/01 19:52:59  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.4  2007/12/22 08:26:35  jost
 * Neu: JubilÃ¤enliste
 *
 * Revision 1.3  2007/02/23 20:27:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/29 07:49:29  jost
 * Neu: Mitgliederstatistik
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.StammdatenControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class StammdatenView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Stammdaten");

    final StammdatenControl control = new StammdatenControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Stammdaten"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Name"), control
        .getName(true));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Bankleitzahl"), control
        .getBlz());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Konto"), control.getKonto());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Altersgruppen"), control
        .getAltersgruppen());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Jubiläen"), control
        .getJubilaeen());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Altersjubiläen"), control
        .getAltersjubilaeen());

    ButtonArea buttons = new ButtonArea(getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.STAMMDATEN, false,
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
    return "<form><p><span color=\"header\" font=\"header\">Stammdaten</span></p>"
        + "<li>Name: Name des Vereins</li>"
        + "<li>Bankverbindung des Vereins für Abbuchungen</li>"
        + "<li>Altersgruppen für die Statistik sind nach folgendem Muster einzugeben: 0-6,7-10,11-14,15-18,19-26,27-40,41-60,61-100 </li>"
        + "</form>";
  }
}
