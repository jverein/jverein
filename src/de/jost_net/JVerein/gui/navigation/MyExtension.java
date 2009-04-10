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
 * Revision 1.14  2009/03/26 21:01:19  jost
 * Neu: Adressbuchexport
 *
 * Revision 1.13  2009/02/15 20:04:06  jost
 * Testcode auskommentiert.
 *
 * Revision 1.12  2008/12/22 21:16:25  jost
 * Icons ins MenÃ¼ aufgenommen.
 *
 * Revision 1.11  2008/11/16 16:57:30  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.10  2008/09/16 18:51:56  jost
 * Neu: Rechnung
 *
 * Revision 1.9  2008/08/10 12:35:50  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.8  2008/07/18 20:11:53  jost
 * Neu: Spendenbescheinigung
 *
 * Revision 1.7  2008/06/28 16:58:42  jost
 * Neu: Jahresabschluss
 *
 * Revision 1.6  2008/05/25 19:36:26  jost
 * Neu: Jahressaldo
 *
 * Revision 1.5  2008/05/22 06:51:20  jost
 * BuchfÃ¼hrung
 *
 * Revision 1.4  2007/12/22 08:25:43  jost
 * Neu: JubilÃ¤enliste
 *
 * Revision 1.3  2007/09/06 17:16:36  jost
 * Korrekte Behandlung des MenÃ¼punktes Auswertung | Kursteilnehmer
 *
 * Revision 1.2  2007/08/23 19:25:05  jost
 * Header korrigiert.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.navigation;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.AbbuchungAction;
import de.jost_net.JVerein.gui.action.AdressbuchExportAction;
import de.jost_net.JVerein.gui.action.AnfangsbestandListAction;
import de.jost_net.JVerein.gui.action.AuswertungKursteilnehmerAction;
import de.jost_net.JVerein.gui.action.AuswertungMitgliedAction;
import de.jost_net.JVerein.gui.action.BuchungsListeAction;
import de.jost_net.JVerein.gui.action.BuchungsuebernahmeAction;
import de.jost_net.JVerein.gui.action.JahresabschlussListAction;
import de.jost_net.JVerein.gui.action.JahressaldoAction;
import de.jost_net.JVerein.gui.action.JubilaeenAction;
import de.jost_net.JVerein.gui.action.KontoListAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerSucheAction;
import de.jost_net.JVerein.gui.action.ManuellerZahlungseingangListeAction;
import de.jost_net.JVerein.gui.action.MitgliedSucheAction;
import de.jost_net.JVerein.gui.action.RechnungListeAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungListeAction;
import de.jost_net.JVerein.gui.action.StatistikMitgliedAction;
import de.jost_net.JVerein.gui.action.WiedervorlageListeAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeListeAction;
import de.willuhn.jameica.gui.NavigationItem;
import de.willuhn.jameica.gui.extension.Extendable;
import de.willuhn.jameica.gui.extension.Extension;
import de.willuhn.logging.Logger;

public class MyExtension implements Extension
{

  /**
   * @see de.willuhn.jameica.gui.extension.Extension#extend(de.willuhn.jameica.gui.extension.Extendable)
   */
  public void extend(Extendable extendable)
  {
    try
    {
      NavigationItem jverein = (NavigationItem) extendable;
      jverein.addChild(new MyItem(jverein, "Mitglieder",
          new MitgliedSucheAction(), "system-users.png"));
      if (Einstellungen.getEinstellung().getKursteilnehmer())
      {
        jverein.addChild(new MyItem(jverein, "Kursteilnehmer",
            new KursteilnehmerSucheAction(), "system-users.png"));
      }
      jverein.addChild(new MyItem(jverein, "Abrechnung", new AbbuchungAction(),
          "accessories-calculator.png"));
      if (Einstellungen.getEinstellung().getRechnungFuerAbbuchung()
          || Einstellungen.getEinstellung().getRechnungFuerUeberweisung()
          || Einstellungen.getEinstellung().getRechnungFuerBarzahlung())
      {
        jverein.addChild(new MyItem(jverein, "Rechnung",
            new RechnungListeAction(), "rechnung.png"));
      }
      if (Einstellungen.getEinstellung().getZusatzbetrag())
      {
        jverein.addChild(new MyItem(jverein, "Zusatzbeträge",
            new ZusatzbetraegeListeAction(), "zusatzbetraege.png"));
      }
      jverein
          .addChild(new MyItem(jverein, "Manueller Zahlungseingang",
              new ManuellerZahlungseingangListeAction(),
              "folder-saved-search.png"));
      if (Einstellungen.getEinstellung().getWiedervorlage())
      {
        jverein.addChild(new MyItem(jverein, "Wiedervorlage",
            new WiedervorlageListeAction(), "office-calendar.png"));
      }
      jverein.addChild(new MyItem(jverein, "Spendenbescheinigung",
          new SpendenbescheinigungListeAction(), "rechnung.png"));

      NavigationItem auswertung = null;
      auswertung = new MyItem(auswertung, "Auswertungen", null);
      auswertung.addChild(new MyItem(auswertung, "Mitglieder",
          new AuswertungMitgliedAction(), "document-open.png"));
      auswertung.addChild(new MyItem(auswertung, "Jubiläen",
          new JubilaeenAction(), "document-open.png"));
      if (Einstellungen.getEinstellung().getKursteilnehmer())
      {
        auswertung.addChild(new MyItem(auswertung, "Kursteilnehmer",
            new AuswertungKursteilnehmerAction(), "document-open.png"));
      }
      auswertung.addChild(new MyItem(auswertung, "Statistik",
          new StatistikMitgliedAction(), "document-open.png"));
      auswertung.addChild(new MyItem(auswertung, "Adressbuchexport",
          new AdressbuchExportAction(), "document-open.png"));
      jverein.addChild(auswertung);

      NavigationItem buchfuehrung = null;
      buchfuehrung = new MyItem(buchfuehrung, "Buchführung", null);
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Konten",
          new KontoListAction(), "system-file-manager.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Anfangsbestand",
          new AnfangsbestandListAction(), "tab-new.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Buchungsübernahme",
          new BuchungsuebernahmeAction(), "view-refresh.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Buchungen",
          new BuchungsListeAction(), "preferences-system-windows.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Jahressaldo",
          new JahressaldoAction(), "summe.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Jahresabschluss",
          new JahresabschlussListAction(), "summe.png"));
      jverein.addChild(buchfuehrung);

    }
    catch (Exception e)
    {
      Logger.error("unable to extend navigation");
    }

  }
}
