/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.AbbuchungAction;
import de.jost_net.JVerein.gui.action.AboutAction;
import de.jost_net.JVerein.gui.action.AdressenSucheAction;
import de.jost_net.JVerein.gui.action.AdresstypListAction;
import de.jost_net.JVerein.gui.action.AnfangsbestandListAction;
import de.jost_net.JVerein.gui.action.ArbeitseinsatzUeberpruefungAction;
import de.jost_net.JVerein.gui.action.AuswertungKursteilnehmerAction;
import de.jost_net.JVerein.gui.action.AuswertungMitgliedAction;
import de.jost_net.JVerein.gui.action.BLZUpdateAction;
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
import de.jost_net.JVerein.gui.action.FamilienbeitragAction;
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
import de.jost_net.JVerein.gui.action.SpendenAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungListeAction;
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
  public void extend(Extendable extendable)
  {
    try
    {
      NavigationItem jverein = (NavigationItem) extendable;
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
          "Mitglieder"), new MitgliedSucheAction(), "system-users.png"));
      if (Einstellungen.getEinstellung().getZusatzadressen())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Adressen"), new AdressenSucheAction(), "system-users.png"));
      }
      if (Einstellungen.getEinstellung().getKursteilnehmer())
      {
        jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
            "Kursteilnehmer"), new KursteilnehmerSucheAction(),
            "system-users.png"));
      }
      DBIterator it = Einstellungen.getDBService().createList(
          Beitragsgruppe.class);
      it.addFilter("beitragsart = ?",
          new Object[] { ArtBeitragsart.FAMILIE_ZAHLER });
      if (it.size() > 0)
      {
        jverein.addChild(new MyItem(jverein, "Familienbeitrag",
            new FamilienbeitragAction(), "family-icon.png"));
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
      if (Einstellungen.getEinstellung().getZusatzadressen())
      {
        einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin
            .getI18n().tr("Adresstypen"), new AdresstypListAction(),
            "layout_co.gif"));
      }
      einstellungen.addChild(new MyItem(einstellungen, JVereinPlugin.getI18n()
          .tr("BLZ-Update"), new BLZUpdateAction(), "adler.png"));
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
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr(
          "Spende für JVerein"), new SpendenAction(), "emblem-special.png"));
      jverein.addChild(new MyItem(jverein, JVereinPlugin.getI18n().tr("über"),
          new AboutAction(), "info_tsk.gif"));
    }
    catch (Exception e)
    {
      Logger.error("unable to extend navigation");
    }

  }
}
