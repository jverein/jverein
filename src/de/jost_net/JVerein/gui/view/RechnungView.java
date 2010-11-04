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
 * Revision 1.9  2010-10-15 09:58:23  jost
 * Code aufgeräumt
 *
 * Revision 1.8  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.7  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.6  2010-07-28 07:27:24  jost
 * deprecated
 *
 * Revision 1.5  2009/07/13 20:52:32  jost
 * Bugfix Datenfeld
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
 * Revision 1.1  2008/09/16 18:52:44  jost
 * Neu: Rechnung
 *
 * Revision 1.14  2008/08/10 12:36:02  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.13  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.12  2008/01/31 19:39:37  jost
 * BerÃ¼cksichtigung eines Stichtages fÃ¼r die Abbuchung
 *
 * Revision 1.11  2008/01/01 19:47:16  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.10  2007/12/26 18:13:19  jost
 * Lastschriften kÃ¶nnen jetzt als Einzellastschriften oder Sammellastschriften direkt in Hibuscus verbucht werden.
 *
 * Revision 1.9  2007/12/21 13:35:58  jost
 * Ausgabe der DTAUS-Datei im PDF-Format
 *
 * Revision 1.8  2007/12/02 13:41:18  jost
 * Ã¼berflÃ¼ssiges Import-Statement entfernt.
 *
 * Revision 1.7  2007/08/22 20:44:10  jost
 * Bug #011762
 *
 * Revision 1.6  2007/07/20 20:15:40  jost
 * Bessere Fehlermeldung
 *
 * Revision 1.5  2007/07/06 11:37:18  jost
 * Zur Kompatibilität: Änderung der Plausi.
 *
 * Revision 1.4  2007/02/25 19:13:05  jost
 * Neu: Kursteilnehmer
 *
 * Revision 1.3  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/21 09:18:54  jost
 * Zusätzliche Plausi.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.RechnungControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

/**
 * @deprecated In Version 1.5 ausmustern
 */
@Deprecated
public class RechnungView extends AbstractView
{
  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Rechnung  - wird ab Version 1.5 nicht mehr unterstützt"));

    final RechnungControl control = new RechnungControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Parameter"));
    if (this.getCurrentObject() == null)
    {
      group.addLabelPair(JVereinPlugin.getI18n().tr("von Datum"), control
          .getVondatum());
      group.addLabelPair(JVereinPlugin.getI18n().tr("bis Datum"), control
          .getBisdatum());
    }
    group.addLabelPair(JVereinPlugin.getI18n().tr("Formular"), control
        .getFormular());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.RECHNUNG, false,
        "help-browser.png");
    buttons.addButton(control.getStartButton(this.getCurrentObject()));
  }

  // TODO getHelp()

}
