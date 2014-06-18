/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.navigation;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.AboutAction;
import de.jost_net.JVerein.gui.action.AbrechnungSEPAAction;
import de.jost_net.JVerein.gui.action.AbrechnunslaufListAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenAbrechnungAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenAllgemeinAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenAnzeigeAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenBuchfuehrungAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenDateinamenAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenMailAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenMitgliedAnsichtAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenMitgliederSpaltenAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenRechnungenAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenSpendenbescheinigungenAction;
import de.jost_net.JVerein.gui.action.AdministrationEinstellungenStatistikAction;
import de.jost_net.JVerein.gui.action.AdressenSucheAction;
import de.jost_net.JVerein.gui.action.AdresstypListAction;
import de.jost_net.JVerein.gui.action.AnfangsbestandListAction;
import de.jost_net.JVerein.gui.action.ArbeitseinsatzUeberpruefungAction;
import de.jost_net.JVerein.gui.action.AuswertungAdressenAction;
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
import de.jost_net.JVerein.gui.action.FamilienbeitragAction;
import de.jost_net.JVerein.gui.action.FelddefinitionenAction;
import de.jost_net.JVerein.gui.action.FormularListeAction;
import de.jost_net.JVerein.gui.action.JahresabschlussListAction;
import de.jost_net.JVerein.gui.action.JahressaldoAction;
import de.jost_net.JVerein.gui.action.JubilaeenAction;
import de.jost_net.JVerein.gui.action.KontenrahmenExportAction;
import de.jost_net.JVerein.gui.action.KontenrahmenImportAction;
import de.jost_net.JVerein.gui.action.KontoListAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerSucheAction;
import de.jost_net.JVerein.gui.action.LehrgaengeListeAction;
import de.jost_net.JVerein.gui.action.LehrgangsartListeAction;
import de.jost_net.JVerein.gui.action.LesefelddefinitionenAction;
import de.jost_net.JVerein.gui.action.MailListeAction;
import de.jost_net.JVerein.gui.action.MailVorlagenAction;
import de.jost_net.JVerein.gui.action.MitgliedImportAction;
import de.jost_net.JVerein.gui.action.MitgliedSucheAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoListeAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoMahnungAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoRechnungAction;
import de.jost_net.JVerein.gui.action.ProjektListAction;
import de.jost_net.JVerein.gui.action.QIFBuchungsImportViewAction;
import de.jost_net.JVerein.gui.action.SEPAKonvertierungAction;
import de.jost_net.JVerein.gui.action.SpendenAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungListeAction;
import de.jost_net.JVerein.gui.action.StatistikJahrgaengeAction;
import de.jost_net.JVerein.gui.action.StatistikMitgliedAction;
import de.jost_net.JVerein.gui.action.WiedervorlageListeAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeImportAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeListeAction;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.NavigationItem;
import de.willuhn.jameica.gui.extension.Extendable;
import de.willuhn.jameica.gui.extension.Extension;
import de.willuhn.logging.Logger;

public class MyExtension implements Extension
{

  /**
   * @see de.willuhn.jameica.gui.extension.Extension#extend(de.willuhn.jameica.gui.extension.Extendable)
   */
  @Override
  public void extend(Extendable extendable)
  {
    try
    {
      NavigationItem jverein = (NavigationItem) extendable;
      jverein.addChild(new MyItem(jverein, "Mitglieder",
          new MitgliedSucheAction(), "system-users.png"));
      if (Einstellungen.getEinstellung().getZusatzadressen())
      {
        jverein.addChild(new MyItem(jverein, "Adressen",
            new AdressenSucheAction(), "system-users.png"));
      }
      if (Einstellungen.getEinstellung().getKursteilnehmer())
      {
        jverein.addChild(new MyItem(jverein, "Kursteilnehmer",
            new KursteilnehmerSucheAction(), "system-users.png"));
      }
      DBIterator it = Einstellungen.getDBService().createList(
          Beitragsgruppe.class);
      it.addFilter("beitragsart = ?",
          new Object[] { ArtBeitragsart.FAMILIE_ZAHLER.getKey() });
      if (it.size() > 0)
      {
        jverein.addChild(new MyItem(jverein, "Familienbeitrag",
            new FamilienbeitragAction(), "family-icon.png"));
      }

      NavigationItem abrechnung = null;
      abrechnung = new MyItem(abrechnung, "Abrechnung", null);
      abrechnung.addChild(new MyItem(abrechnung, "Abrechnung",
          new AbrechnungSEPAAction(), "accessories-calculator.png"));
      abrechnung.addChild(new MyItem(abrechnung, "Abrechnungslauf",
          new AbrechnunslaufListAction(), "accessories-calculator.png"));
      jverein.addChild(abrechnung);

      jverein.addChild(new MyItem(jverein, "Mitgliedskonten",
          new MitgliedskontoListeAction(), "human_folder_public.png"));
      jverein.addChild(new MyItem(jverein, "Rechnungen",
          new MitgliedskontoRechnungAction(), "invoice.png"));
      jverein.addChild(new MyItem(jverein, "Mahnungen",
          new MitgliedskontoMahnungAction(), "rechnung.png"));
      if (Einstellungen.getEinstellung().getArbeitseinsatz())
      {
        jverein.addChild(new MyItem(jverein, "Arbeitseinsätze prüfen",
            new ArbeitseinsatzUeberpruefungAction(), "shovel.png"));
      }

      if (Einstellungen.getEinstellung().getZusatzbetrag())
      {
        jverein.addChild(new MyItem(jverein, "Zusatzbeträge",
            new ZusatzbetraegeListeAction(), "zusatzbetraege.png"));
        jverein.addChild(new MyItem(jverein, "Zusatzbeträge importieren",
            new ZusatzbetraegeImportAction(), "zusatzbetraege.png"));
      }
      if (Einstellungen.getEinstellung().getWiedervorlage())
      {
        jverein.addChild(new MyItem(jverein, "Wiedervorlage",
            new WiedervorlageListeAction(), "office-calendar.png"));
      }
      if (Einstellungen.getEinstellung().getLehrgaenge())
      {
        jverein.addChild(new MyItem(jverein, "Lehrgänge",
            new LehrgaengeListeAction(), "x-office-presentation.png"));
      }
      jverein.addChild(new MyItem(jverein, "Spendenbescheinigungen",
          new SpendenbescheinigungListeAction(), "rechnung.png"));

      NavigationItem auswertung = null;
      auswertung = new MyItem(auswertung, "Auswertungen", null);
      auswertung.addChild(new MyItem(auswertung, "Mitglieder",
          new AuswertungMitgliedAction(), "list_users.gif"));
      auswertung.addChild(new MyItem(auswertung, "Adressen",
          new AuswertungAdressenAction(), "list_users.gif"));
      auswertung.addChild(new MyItem(auswertung, "Jubiläen",
          new JubilaeenAction(), "document-open.png"));
      if (Einstellungen.getEinstellung().getKursteilnehmer())
      {
        auswertung.addChild(new MyItem(auswertung, "Kursteilnehmer",
            new AuswertungKursteilnehmerAction(), "list_users.gif"));
      }
      auswertung.addChild(new MyItem(auswertung, "Statistik",
          new StatistikMitgliedAction(), "chart16.png"));
      auswertung.addChild(new MyItem(auswertung, "Statistik Jahrgänge",
          new StatistikJahrgaengeAction(), "chart16.png"));

      jverein.addChild(auswertung);

      NavigationItem mail = null;
      mail = new MyItem(mail, "Mail", null);
      mail.addChild(new MyItem(mail, "Mails", new MailListeAction(),
          "email.png"));
      mail.addChild(new MyItem(mail, "Mail-Vorlagen", new MailVorlagenAction(),
          "email.png"));
      jverein.addChild(mail);

      NavigationItem buchfuehrung = null;
      buchfuehrung = new MyItem(buchfuehrung, "Buchführung", null);
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Konten",
          new KontoListAction(), "system-file-manager.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Anfangsbestände",
          new AnfangsbestandListAction(), "tab-new.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Buchungen",
          new BuchungsListeAction(), "preferences-system-windows.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Hibiscus-Buchungen",
          new BuchungsuebernahmeAction(), "hibiscus-icon-64x64.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Buchungsklassen",
          new BuchungsklasseSaldoAction(), "summe.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Jahressaldo",
          new JahressaldoAction(), "summe.png"));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Jahresabschlüsse",
          new JahresabschlussListAction(), "summe.png"));
      jverein.addChild(buchfuehrung);

      NavigationItem administration = null;
      administration = new MyItem(administration, "Administration", null);

      NavigationItem administrationEinstellungen = null;
      administrationEinstellungen = new MyItem(administrationEinstellungen,
          "Einstellungen", null);
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Allgemein",
          new AdministrationEinstellungenAllgemeinAction(), "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Anzeige",
          new AdministrationEinstellungenAnzeigeAction(), "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Mitglieder Spalten",
          new AdministrationEinstellungenMitgliederSpaltenAction(),
          "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Mitglieder Ansicht",
          new AdministrationEinstellungenMitgliedAnsichtAction(),
          "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Abrechnung",
          new AdministrationEinstellungenAbrechnungAction(), "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Dateinamen",
          new AdministrationEinstellungenDateinamenAction(), "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Spendenbescheinigungen",
          new AdministrationEinstellungenSpendenbescheinigungenAction(),
          "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Buchführung",
          new AdministrationEinstellungenBuchfuehrungAction(), "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Rechnungen",
          new AdministrationEinstellungenRechnungenAction(), "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Mail",
          new AdministrationEinstellungenMailAction(), "settings.gif"));
      administrationEinstellungen.addChild(new MyItem(
          administrationEinstellungen, "Statistik",
          new AdministrationEinstellungenStatistikAction(), "settings.gif"));
      administration.addChild(administrationEinstellungen);

      administration.addChild(new MyItem(administration, "Beitragsgruppen",
          new BeitragsgruppeSucheAction(), "breakpoint_view.gif"));

      NavigationItem einstellungenbuchfuehrung = null;
      einstellungenbuchfuehrung = new MyItem(einstellungenbuchfuehrung,
          "Buchführung", null);
      einstellungenbuchfuehrung.addChild(new MyItem(einstellungenbuchfuehrung,
          "Buchungsklassen", new BuchungsklasseListAction(),
          "activity_category.gif"));
      einstellungenbuchfuehrung
          .addChild(new MyItem(einstellungenbuchfuehrung, "Buchungsarten",
              new BuchungsartListAction(), "activity_category.gif"));
      einstellungenbuchfuehrung.addChild(new MyItem(einstellungenbuchfuehrung,
          "Kontenrahmen-Export", new KontenrahmenExportAction(),
          "activity_category.gif"));
      einstellungenbuchfuehrung.addChild(new MyItem(einstellungenbuchfuehrung,
          "Kontenrahmen-Import", new KontenrahmenImportAction(),
          "activity_category.gif"));
      einstellungenbuchfuehrung.addChild(new MyItem(einstellungenbuchfuehrung,
          "QIF Datei-Import", new QIFBuchungsImportViewAction(),
          "import_obj.gif"));
      einstellungenbuchfuehrung.addChild(new MyItem(einstellungenbuchfuehrung,
          "Projekte", new ProjektListAction(), "projects.png"));
      administration.addChild(einstellungenbuchfuehrung);

      administration.addChild(new MyItem(administration,
          "Eigenschaften-Gruppen", new EigenschaftGruppeListeAction(),
          "activity_category.gif"));
      administration.addChild(new MyItem(administration, "Eigenschaften",
          new EigenschaftListeAction(), "activity_category.gif"));
      administration.addChild(new MyItem(administration, "Felddefinitionen",
          new FelddefinitionenAction(), "category_obj.gif"));
      if (Einstellungen.getEinstellung().getUseLesefelder())
      {
        administration.addChild(new MyItem(administration, "Lesefelder",
            new LesefelddefinitionenAction(null), "category_obj.gif"));
      }
      administration.addChild(new MyItem(administration, "Formulare",
          new FormularListeAction(), "layout_co.gif"));
      administration.addChild(new MyItem(administration, "Lehrgangsarten",
          new LehrgangsartListeAction(), "x-office-presentation.png"));
      administration.addChild(new MyItem(administration, "Import",
          new MitgliedImportAction(), "import_obj.gif"));
      if (Einstellungen.getEinstellung().getZusatzadressen())
      {
        administration.addChild(new MyItem(administration, "Adresstypen",
            new AdresstypListAction(), "layout_co.gif"));
      }
      administration.addChild(new MyItem(administration, "SEPA-Konvertierung",
          new SEPAKonvertierungAction(), "sepa.png"));
      NavigationItem einstellungenerweitert = null;
      einstellungenerweitert = new MyItem(einstellungenerweitert, "Erweitert",
          null);
      einstellungenerweitert.addChild(new MyItem(einstellungenerweitert,
          "Diagnose-Backup erstellen", new BackupCreateAction(),
          "thread_obj.gif"));
      einstellungenerweitert.addChild(new MyItem(einstellungenerweitert,
          "Diagnose-Backup importieren", new BackupRestoreAction(),
          "thread2_obj.gif"));
      administration.addChild(einstellungenerweitert);
      jverein.addChild(administration);
      jverein.addChild(new MyItem(jverein, "Dokumentation",
          new DokumentationAction(), "help_view.gif"));
      jverein.addChild(new MyItem(jverein, "Spende für JVerein",
          new SpendenAction(), "emblem-special.png"));
      jverein.addChild(new MyItem(jverein, "Über", new AboutAction(),
          "gtk-info.png"));
    }
    catch (Exception e)
    {
      Logger.error("unable to extend navigation");
    }

  }
}
