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
 * Revision 1.36  2010-11-25 15:11:38  jost
 * Initial Commit
 *
 * Revision 1.35  2010-11-22 20:59:44  jost
 * Arbeitseinsatzüberprüfung
 *
 * Revision 1.34  2010-11-13 09:25:41  jost
 * Mit V 1.5 deprecatete Spalten und Tabellen entfernt.
 *
 * Revision 1.33  2010-11-03 21:28:26  jost
 * Redakt.
 *
 * Revision 1.32  2010-10-16 15:35:54  jost
 * neue Icons
 *
 * Revision 1.31  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.30  2010-08-16 20:17:14  jost
 * Neu: Mahnung
 *
 * Revision 1.29  2010-08-15 19:01:13  jost
 * Rechnungen auch für einen vorgegebenen Zeitraum ausgeben.
 *
 * Revision 1.28  2010-08-10 05:39:41  jost
 * Reaktivierung alter Rechnungen
 *
 * Revision 1.27  2010-07-26 08:23:01  jost
 * Manuelle Zahlungen defaultmäßig deaktviert. Reaktvierbar durch Einstellungen.
 *
 * Revision 1.26  2010-07-25 18:36:26  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.25  2010/02/21 20:15:53  jost
 * Adressbuchexport ins Mail-Menü verschoben.
 *
 * Revision 1.24  2010/02/01 21:00:07  jost
 * Neu: Einfache Mailfunktion
 *
 * Revision 1.23  2009/11/22 16:19:01  jost
 * Reihenfolge korrigiert.
 *
 * Revision 1.22  2009/11/17 20:59:01  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 * Revision 1.21  2009/10/20 17:58:27  jost
 * Neu: Import von Zusatzbeträgen
 *
 * Revision 1.20  2009/09/10 18:17:47  jost
 * neu: Buchungsklassen
 *
 * Revision 1.19  2009/06/11 21:03:13  jost
 * Vorbereitung I18N
 *
 * Revision 1.18  2009/06/01 08:32:56  jost
 * Icons aufgenommen.
 *
 * Revision 1.17  2009/05/31 12:27:08  jost
 * Menüpunkte aus Plugins>JVerein ins Hauptmenü übernommen.
 *
 * Revision 1.16  2009/04/13 11:39:54  jost
 * Neu: Lehrgänge
 *
 * Revision 1.15  2009/04/10 09:43:04  jost
 * Versuch "Reports" abgebrochen
 *
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
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.AbbuchungAction;
import de.jost_net.JVerein.gui.action.AboutAction;
import de.jost_net.JVerein.gui.action.AdressbuchExportAction;
import de.jost_net.JVerein.gui.action.AnfangsbestandListAction;
import de.jost_net.JVerein.gui.action.ArbeitseinsatzUeberpruefungAction;
import de.jost_net.JVerein.gui.action.AuswertungKursteilnehmerAction;
import de.jost_net.JVerein.gui.action.AuswertungMitgliedAction;
import de.jost_net.JVerein.gui.action.BackupCreateAction;
import de.jost_net.JVerein.gui.action.BackupRestoreAction;
import de.jost_net.JVerein.gui.action.BeitragsgruppeSucheAction;
import de.jost_net.JVerein.gui.action.BuchungsListeAction;
import de.jost_net.JVerein.gui.action.BuchungsartListAction;
import de.jost_net.JVerein.gui.action.BuchungsklasseListAction;
import de.jost_net.JVerein.gui.action.BuchungsklasseSaldoAction;
import de.jost_net.JVerein.gui.action.BuchungsuebernahmeAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.EigenschaftGruppeListeAction;
import de.jost_net.JVerein.gui.action.EigenschaftListeAction;
import de.jost_net.JVerein.gui.action.EinstellungenAction;
import de.jost_net.JVerein.gui.action.FelddefinitionenAction;
import de.jost_net.JVerein.gui.action.FormularListeAction;
import de.jost_net.JVerein.gui.action.JahresabschlussListAction;
import de.jost_net.JVerein.gui.action.JahressaldoAction;
import de.jost_net.JVerein.gui.action.JubilaeenAction;
import de.jost_net.JVerein.gui.action.KontoListAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerSucheAction;
import de.jost_net.JVerein.gui.action.LehrgaengeListeAction;
import de.jost_net.JVerein.gui.action.LehrgangsartListeAction;
import de.jost_net.JVerein.gui.action.MailListeAction;
import de.jost_net.JVerein.gui.action.MailVorlagenAction;
import de.jost_net.JVerein.gui.action.MitgliedImportAction;
import de.jost_net.JVerein.gui.action.MitgliedSucheAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoListeAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoMahnungAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoRechnungAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungListeAction;
import de.jost_net.JVerein.gui.action.StatistikMitgliedAction;
import de.jost_net.JVerein.gui.action.TermineAction;
import de.jost_net.JVerein.gui.action.WiedervorlageListeAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeImportAction;
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
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
          "Mitglieder"), new MitgliedSucheAction(), "system-users.png"));
      if (Einstellungen.getEinstellung().getKursteilnehmer())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Kursteilnehmer"), new KursteilnehmerSucheAction(),
            "system-users.png"));
      }
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
          "Abrechnung"), new AbbuchungAction(), "accessories-calculator.png"));
      if (Einstellungen.getEinstellung().getMitgliedskonto())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Mitgliedskonten"), new MitgliedskontoListeAction(),
            "human_folder_public.png"));
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Rechnungen"), new MitgliedskontoRechnungAction(), "invoice.png"));
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Mahnungen"), new MitgliedskontoMahnungAction(), "rechnung.png"));
      }
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n()
          .tr("Termine"), new TermineAction(), "office-calendar.png"));
      if (Einstellungen.getEinstellung().getArbeitseinsatz())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Arbeitseinsätze prüfen"), new ArbeitseinsatzUeberpruefungAction(),
            "shovel.png"));
      }

      if (Einstellungen.getEinstellung().getZusatzbetrag())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Zusatzbeträge"), new ZusatzbetraegeListeAction(),
            "zusatzbetraege.png"));
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Zusatzbeträge importieren"), new ZusatzbetraegeImportAction(),
            "zusatzbetraege.png"));
      }
      if (Einstellungen.getEinstellung().getWiedervorlage())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Wiedervorlage"), new WiedervorlageListeAction(),
            "office-calendar.png"));
      }
      if (Einstellungen.getEinstellung().getLehrgaenge())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Lehrgänge"), new LehrgaengeListeAction(),
            "x-office-presentation.png"));
      }
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
          "Spendenbescheinigungen"), new SpendenbescheinigungListeAction(),
          "rechnung.png"));

      NavigationItem auswertung = null;
      auswertung = new MyItem(auswertung, JVereinPlugin.getI18n().tr(
          "Auswertungen"), null);
      auswertung.addChild(new MyItem(auswertung, JVereinPlugin.getI18n().tr(
          "Mitglieder"), new AuswertungMitgliedAction(), "document-open.png"));
      auswertung.addChild(new MyItem(auswertung, JVereinPlugin.getI18n().tr(
          "Jubiläen"), new JubilaeenAction(), "document-open.png"));
      if (Einstellungen.getEinstellung().getKursteilnehmer())
      {
        auswertung.addChild(new MyItem(auswertung, JVereinPlugin.getI18n().tr(
            "Kursteilnehmer"), new AuswertungKursteilnehmerAction(),
            "document-open.png"));
      }
      auswertung.addChild(new MyItem(auswertung, JVereinPlugin.getI18n().tr(
          "Statistik"), new StatistikMitgliedAction(), "document-open.png"));
      jverein.addChild(auswertung);

      NavigationItem mail = null;
      mail = new MyItem(mail, JVereinPlugin.getI18n().tr("Mail"), null);
      mail.addChild(new MyItem(mail, JVereinPlugin.getI18n().tr("Mails"),
          new MailListeAction()));
      mail.addChild(new MyItem(mail, JVereinPlugin.getI18n()
          .tr("Mail-Vorlagen"), new MailVorlagenAction()));
      mail.addChild(new MyItem(auswertung, JVereinPlugin.getI18n().tr(
          "Adressbuchexport"), new AdressbuchExportAction(),
          "document-open.png"));
      jverein.addChild(mail);

      NavigationItem buchfuehrung = null;
      buchfuehrung = new MyItem(buchfuehrung, JVereinPlugin.getI18n().tr(
          "Buchführung"), null);
      buchfuehrung.addChild(new MyItem(buchfuehrung, JVereinPlugin.getI18n()
          .tr("Konten"), new KontoListAction(), "system-file-manager.png"));
      buchfuehrung
          .addChild(new MyItem(buchfuehrung, JVereinPlugin.getI18n().tr(
              "Anfangsbestände"), new AnfangsbestandListAction(), "tab-new.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, JVereinPlugin.getI18n()
          .tr("Buchungsübernahme"), new BuchungsuebernahmeAction(),
          "view-refresh.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, JVereinPlugin.getI18n()
          .tr("Buchungen"), new BuchungsListeAction(),
          "preferences-system-windows.png"));
      buchfuehrung
          .addChild(new MyItem(buchfuehrung, JVereinPlugin.getI18n().tr(
              "Buchungsklassen"), new BuchungsklasseSaldoAction(), "summe.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, JVereinPlugin.getI18n()
          .tr("Jahressaldo"), new JahressaldoAction(), "summe.png"));
      buchfuehrung
          .addChild(new MyItem(buchfuehrung, JVereinPlugin.getI18n().tr(
              "Jahresabschlüsse"), new JahresabschlussListAction(), "summe.png"));
      jverein.addChild(buchfuehrung);

      NavigationItem einstellungen = null;
      einstellungen = new MyItem(einstellungen, JVereinPlugin.getI18n().tr(
          "Administration"), null);
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Einstellungen"), new EinstellungenAction(), "settings.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Beitragsgruppen"), new BeitragsgruppeSucheAction(),
          "breakpoint_view.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Buchungsklassen"), new BuchungsklasseListAction(),
          "activity_category.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Buchungsarten"), new BuchungsartListAction(),
          "activity_category.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Eigenschaften-Gruppen"), new EigenschaftGruppeListeAction(),
          "activity_category.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Eigenschaften"), new EigenschaftListeAction(),
          "activity_category.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Felddefinitionen"), new FelddefinitionenAction(),
          "category_obj.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Formulare"), new FormularListeAction(), "layout_co.gif"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Lehrgangsarten"), new LehrgangsartListeAction(),
          "x-office-presentation.png"));
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("Import"), new MitgliedImportAction(), "import_obj.gif"));

      NavigationItem einstellungenerweitert = null;
      einstellungenerweitert = new MyItem(einstellungenerweitert, JVereinPlugin
          .getI18n().tr("Erweitert"), null);
      einstellungenerweitert.addChild(new MyItem(einstellungenerweitert,
          JVereinPlugin.getI18n().tr("Diagnose-Backup erstellen"),
          new BackupCreateAction(), "thread_obj.gif"));
      einstellungenerweitert.addChild(new MyItem(einstellungenerweitert,
          JVereinPlugin.getI18n().tr("Diagnose-Backup importieren"),
          new BackupRestoreAction(), "thread2_obj.gif"));
      einstellungen.addChild(einstellungenerweitert);
      jverein.addChild(einstellungen);
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
          "Dokumentation"), new DokumentationAction(), "help_view.gif"));
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr("über"),
          new AboutAction(), "info_tsk.gif"));
    }
    catch (Exception e)
    {
      Logger.error("unable to extend navigation");
    }

  }
}
