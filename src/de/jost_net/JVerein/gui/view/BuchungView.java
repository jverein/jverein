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
 * Revision 1.22  2010-12-17 13:41:10  jost
 * Vermeidung NPE
 *
 * Revision 1.21  2010-12-14 21:41:52  jost
 * Neu: Speicherung von Dokumenten
 *
 * Revision 1.20  2010-12-12 12:44:17  jost
 * redakt.
 *
 * Revision 1.19  2010-12-12 08:13:01  jost
 * Neu: Speicherung von Dokumenten
 *
 * Revision 1.18  2010-10-15 09:58:24  jost
 * Code aufger�umt
 *
 * Revision 1.17  2010-10-07 19:49:24  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.16  2010-08-23 13:39:33  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.15  2010-07-25 18:42:11  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.14  2010/05/28 19:55:32  jost
 * Scrollbar aufgenommen.
 *
 * Revision 1.13  2009/07/24 20:21:02  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.12  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.control.DokumentControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.BuchungDokument;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;

public class BuchungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchung"));

    final BuchungsControl control = new BuchungsControl(this);

    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    LabelGroup grKontoauszug = new LabelGroup(scrolled.getComposite(),
        JVereinPlugin.getI18n().tr("Buchung"));
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsnummer"),
        control.getID());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Umsatz-ID"),
        control.getUmsatzid());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Konto"),
        control.getKonto(true));
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Name"),
        control.getName());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"),
        control.getBetrag());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck"),
        control.getZweck());
    grKontoauszug.addLabelPair(
        JVereinPlugin.getI18n().tr("Verwendungszweck 2"), control.getZweck2());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Datum"),
        control.getDatum());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Art"),
        control.getArt());
    if (Einstellungen.getEinstellung().getMitgliedskonto())
    {
      grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Mitgliedskonto"),
          control.getMitgliedskonto());
    }
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Kommentar"),
        control.getKommentar());

    LabelGroup grBuchungsinfos = new LabelGroup(scrolled.getComposite(),
        JVereinPlugin.getI18n().tr("Buchungsinfos"));
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsart"),
        control.getBuchungsart());
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Auszugsnummer"),
        control.getAuszugsnummer());
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Blattnummer"),
        control.getBlattnummer());

    if (JVereinPlugin.isArchiveServiceActive())
    {
      Buchung bu = (Buchung) control.getCurrentObject();
      if (!bu.isNewObject())
      {
        LabelGroup grDokument = new LabelGroup(scrolled.getComposite(),
            "Dokumente");
        BuchungDokument budo = (BuchungDokument) Einstellungen.getDBService()
            .createObject(BuchungDokument.class, null);
        budo.setReferenz(new Integer(bu.getID()));
        DokumentControl dcontrol = new DokumentControl(this, "buchungen");
        grDokument.addPart(dcontrol.getDokumenteList(budo));
        ButtonArea butts = new ButtonArea(grDokument.getComposite(), 1);
        butts.addButton(dcontrol.getNeuButton(budo));
      }
    }

    ButtonArea buttons = new ButtonArea(scrolled.getComposite(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&neu"),
        new BuchungNeuAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchung</span></p>"
        + "<p>Zuordnung einer Buchungsart zu einer Buchung.</p></form>";
  }
}
