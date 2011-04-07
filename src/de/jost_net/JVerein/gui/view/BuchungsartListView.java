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
 * Revision 1.14  2011-03-14 19:24:00  jost
 * Tippfehler beseitigt.
 *
 * Revision 1.13  2011-02-03 22:32:39  jost
 * Neu: Liste der Buchungsarten
 *
 * Revision 1.12  2011-01-15 09:46:49  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.11  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.10  2010-10-07 19:49:24  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.9  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.8  2009/08/19 20:59:22  jost
 * Hilfebutton aufgenommen.
 *
 * Revision 1.7  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.6  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.5  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.4  2008/03/16 07:36:29  jost
 * Reaktivierung BuchfÃ¼hrung
 *
 * Revision 1.2  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
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
