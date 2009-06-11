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
 * Revision 1.11  2009/02/07 20:32:01  jost
 * Neu: Button neue Buchung
 *
 * Revision 1.10  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.9  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.8  2008/12/03 22:00:37  jost
 * Erweiterung um Auszugs- und Blattnummer
 *
 * Revision 1.7  2008/05/24 16:40:05  jost
 * Wegfall der Spalte Saldo
 *
 * Revision 1.6  2008/05/24 14:04:08  jost
 * Redatkionelle Änderung
 *
 * Revision 1.5  2008/05/22 06:53:14  jost
 * Buchführung
 *
 * Revision 1.4  2008/03/16 07:36:29  jost
 * Reaktivierung Buchführung
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
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class BuchungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchung"));

    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup grKontoauszug = new LabelGroup(getParent(), JVereinPlugin
        .getI18n().tr("Buchung"));
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsnummer"),
        control.getID());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Umsatz-ID"), control
        .getUmsatzid());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Konto"), control
        .getKonto());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Name"), control
        .getName());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"), control
        .getBetrag());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck"),
        control.getZweck());
    grKontoauszug.addLabelPair(
        JVereinPlugin.getI18n().tr("Verwendungszweck 2"), control.getZweck2());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Datum"), control
        .getDatum());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Art"), control
        .getArt());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Kommentar"), control
        .getKommentar());

    LabelGroup grBuchungsinfos = new LabelGroup(getParent(), JVereinPlugin
        .getI18n().tr("Buchungsinfos"));
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsart"),
        control.getBuchungsart());
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Auszugsnummer"),
        control.getAuszugsnummer());
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Blattnummer"),
        control.getBlattnummer());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new BuchungNeuAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
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
}
