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
 * Revision 1.9  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.8  2010-10-07 19:49:24  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.7  2010-10-01 13:30:21  jost
 * Neu: PDF-Ausgabe der Zusatzbuchungen
 *
 * Revision 1.6  2010-08-23 13:39:33  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.5  2009/10/20 17:59:12  jost
 * Neu: Import von Zusatzbeträgen
 *
 * Revision 1.4  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.3  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.2  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.1  2008/12/22 21:18:27  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.6  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.5  2008/01/01 19:53:45  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.4  2007/02/23 20:27:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.3  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.2  2006/10/07 14:47:25  jost
 * Alten Code entfernt
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.ZusatzbetragControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class ZusatzbetraegelisteView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(
        JVereinPlugin.getI18n().tr("Liste der Zusatzbeträge"));

    final ZusatzbetragControl control = new ZusatzbetragControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Ausführungstag"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Ausführungstag"),
        control.getAusfuehrungSuch());

    control.getZusatzbetraegeList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(control.getPDFAusgabeButton());
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.ZUSATZBETRAEGE, false,
        "help-browser.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Liste der Zusatzbeträge</span></p>"
        + "<p>Anzeige der Zusatzbeträge. Filterung auf alle, aktive, noch nicht ausgeführte und zu einem "
        + "bestimmten Datum ausgeführte Zusatzbeträge. Die angezeigte Liste kann als PDF-Dokument "
        + "ausgegeben werden.</p></form>";
  }
}
