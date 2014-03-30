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
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.EinstellungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

public class EinstellungenView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Einstellungen");

    final EinstellungControl control = new EinstellungControl(this);

    ScrolledContainer cont = new ScrolledContainer(getParent());
    TabFolder folder = new TabFolder(cont.getComposite(), SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    TabGroup tabAllgemein = new TabGroup(folder, "Allgemein");
    tabAllgemein.addHeadline("Allgemein");
    tabAllgemein.addLabelPair("Name", control.getName(true));
    tabAllgemein.addLabelPair("Straße", control.getStrasse());
    tabAllgemein.addLabelPair("PLZ", control.getPlz());
    tabAllgemein.addLabelPair("Ort", control.getOrt());
    tabAllgemein.addLabelPair("IBAN", control.getIban());
    tabAllgemein.addLabelPair("BIC", control.getBic());
    tabAllgemein.addPart(control.getButton());
    tabAllgemein.addLabelPair("Gläubiger-ID", control.getGlaeubigerID());
    // tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Bankleitzahl"),
    // control.getBlz());
    // tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Konto"),
    // control.getKonto());

    TabGroup tabAnzeige = new TabGroup(folder, "Anzeige");

    LabelGroup group = new LabelGroup(tabAnzeige.getComposite(), "Anzeige");
    ColumnLayout cols1 = new ColumnLayout(group.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());

    left.addLabelPair("Geburtsdatum Pflichtfeld",
        control.getGeburtsdatumPflicht());
    left.addLabelPair("Eintrittsdatum Pflichtfeld",
        control.getEintrittsdatumPflicht());
    left.addLabelPair("Sterbedatum", control.getSterbedatum());
    left.addLabelPair("Kommunikationsdaten anzeigen",
        control.getKommunikationsdaten());
    left.addLabelPair("Zusatzbeträge anzeigen" + "*", control.getZusatzbetrag());
    left.addLabelPair("Zusatzbeträge auch für Ausgetretene *",
        control.getZusatzbetragAusgetretene());
    left.addLabelPair("Vermerke anzeigen", control.getVermerke());
    left.addLabelPair("Wiedervorlage anzeigen" + "*",
        control.getWiedervorlage());
    left.addLabelPair("Kursteilnehmer anzeigen" + "*",
        control.getKursteilnehmer());
    left.addLabelPair("Lehrgänge anzeigen" + "*", control.getLehrgaenge());
    left.addLabelPair("Juristische Personen erlaubt",
        control.getJuristischePersonen());
    left.addLabelPair("Mitgliedsfoto *", control.getMitgliedfoto());
    SimpleContainer right = new SimpleContainer(cols1.getComposite());
    right.addLabelPair("Lesefelder anzeigen *", control.getUseLesefelder());
    right.addLabelPair("zusätzliche Adressen *", control.getZusatzadressen());
    right.addLabelPair("Auslandsadressen *", control.getAuslandsadressen());
    right.addLabelPair("Arbeitseinsatz *", control.getArbeitseinsatz());
    right.addLabelPair("Dokumentenspeicherung *",
        control.getDokumentenspeicherung());
    right.addLabelPair("individuelle Beiträge *",
        control.getIndividuelleBeitraege());
    right.addLabelPair("externe Mitgliedsnummer",
        control.getExterneMitgliedsnummer());
    right.addLabelPair("Basis für Berechnung des Alters",
        control.getAltersModel());

    right.addHeadline("* " + "Änderung erfordert Neustart");

    TabGroup tabBeitraege = new TabGroup(folder, "Beiträge");
    LabelGroup groupAbu = new LabelGroup(tabBeitraege.getComposite(),
        "Beiträge");
    groupAbu.addLabelPair("Beitragsmodel", control.getBeitragsmodel());
    groupAbu.addInput(control.getZahlungsrhytmus());
    groupAbu.addInput(control.getZahlungsweg());
    groupAbu.addInput(control.getDefaultSEPALand());
    groupAbu.addLabelPair("Arbeitsstunden Model",
        control.getArbeitsstundenmodel());
    groupAbu.addSeparator();
    groupAbu
        .addHeadline("ACHTUNG! Nur ändern, wenn noch keine SEPA-Lastschriften durchgeführt wurden!");
    groupAbu.addLabelPair("Quelle für SEPA-Mandatsreferenz (*)",
        control.getSepamandatidsourcemodel());

    TabGroup tabDateinamen = new TabGroup(folder, "Dateinamen", false, 1);
    LabelGroup groupDatei = new LabelGroup(tabDateinamen.getComposite(),
        "Dateinamenmuster");
    groupDatei.addLabelPair("Auswertung", control.getDateinamenmuster());
    groupDatei.addLabelPair("Spendenbescheinigungen",
        control.getDateinamenmusterSpende());
    LabelGroup groupVorlagenCsvDir = new LabelGroup(
        tabDateinamen.getComposite(), "Verzeichnisse");
    groupVorlagenCsvDir.addLabelPair("CSV Vorlagenverzeichnis",
        control.getVorlagenCsvVerzeichnis());

    TabGroup tabSpendenbescheinigung = new TabGroup(folder,
        "Spendenbescheinigungen");
    LabelGroup groupSpenden = new LabelGroup(
        tabSpendenbescheinigung.getComposite(), "Spendenbescheinigungen");
    groupSpenden.addLabelPair("Finanzamt", control.getFinanzamt());
    groupSpenden.addLabelPair("Steuernummer", control.getSteuernummer());
    groupSpenden.addLabelPair("Bescheiddatum", control.getBescheiddatum());
    groupSpenden.addLabelPair("vorläufiger Bescheid", control.getVorlaeufig());
    groupSpenden.addLabelPair("Vorläufig ab", control.getVorlaeufigab());
    groupSpenden.addLabelPair("Veranlagung von", control.getVeranlagungVon());
    groupSpenden.addLabelPair("Veranlagung bis", control.getVeranlagungBis());
    groupSpenden.addLabelPair("begünstigter Zweck",
        control.getBeguenstigterzweck());
    groupSpenden.addLabelPair("Mitgliedsbeiträge dürfen bescheinigt werden",
        control.getMitgliedsbetraege());
    groupSpenden.addLabelPair("Mindestbetrag",
        control.getSpendenbescheinigungminbetrag());
    groupSpenden.addLabelPair("Verzeichnis",
        control.getSpendenbescheinigungverzeichnis());
    groupSpenden.addLabelPair("Buchungsart drucken",
        control.getSpendenbescheinigungPrintBuchungsart());

    TabGroup tabBuchfuehrung = new TabGroup(folder, "Buchführung");
    LabelGroup groupBuchfuehrung = new LabelGroup(
        tabBuchfuehrung.getComposite(), "Buchführung");
    groupBuchfuehrung.addLabelPair("Beginn Geschäftsjahr (TT.MM.)",
        control.getBeginnGeschaeftsjahr());
    groupBuchfuehrung.addInput(control.getAutoBuchunguebernahme());

    TabGroup tabRechnungen = new TabGroup(folder, "Rechnungen");
    LabelGroup groupRechnungen = new LabelGroup(tabRechnungen.getComposite(),
        "Rechnungen");
    groupRechnungen.addLabelPair("Text Abbuchung",
        control.getRechnungTextAbbuchung());
    groupRechnungen.addLabelPair("Text Überweisung",
        control.getRechnungTextUeberweisung());
    groupRechnungen.addLabelPair("Text Bar", control.getRechnungTextBar());

    TabGroup tabTabellen = new TabGroup(folder, "Tabellen");

    TabFolder folderTabellen = new TabFolder(tabTabellen.getComposite(),
        SWT.NONE);

    TabGroup tabMitglieder = new TabGroup(folderTabellen, "Mitglieder");
    LabelGroup groupMitglieder = new LabelGroup(tabMitglieder.getComposite(),
        "Trefferliste Mitglieder");
    control.getSpaltendefinitionTable(groupMitglieder.getComposite());

    TabGroup tabMail = new TabGroup(folder, "Mail");
    LabelGroup groupMail = new LabelGroup(tabMail.getComposite(), "Mail");
    groupMail.addLabelPair("Server", control.getSmtpServer());
    groupMail.addLabelPair("Port", control.getSmtpPort());
    groupMail.addLabelPair("Benutzer", control.getSmtpAuthUser());
    groupMail.addLabelPair("Passwort", control.getSmtpAuthPwd());
    groupMail.addLabelPair("Absenderadresse", control.getSmtpFromAddress());
    groupMail.addLabelPair("Anzeigename", control.getSmtpFromAnzeigename());
    groupMail.addLabelPair("SSL verwenden", control.getSmtpSsl());
    groupMail.addLabelPair("StartTLS verwenden", control.getSmtpStarttls());
    groupMail.addLabelPair("Immer Cc an Adresse", control.getAlwaysCcTo());
    groupMail.addLabelPair("Immer Bcc an Adresse", control.getAlwaysBccTo());

    groupMail.addSeparator();
    groupMail.addText("IMAP 'Gesendete'-Ordner", false);
    groupMail.addLabelPair("Kopie in 'Gesendete'-Ordner IMAP ablegen",
        control.getCopyToImapFolder());
    groupMail.addLabelPair("IMAP Server", control.getImapHost());
    groupMail.addLabelPair("IMAP Port", control.getImapPort());
    groupMail.addLabelPair("IMAP Benutzer", control.getImapAuthUser());
    groupMail.addLabelPair("IMAP Passwort", control.getImapAuthPwd());
    groupMail.addLabelPair("IMAP SSL verwenden", control.getImap_ssl());
    groupMail.addLabelPair("IMAP StartTLS verwenden",
        control.getImap_starttls());
    groupMail.addLabelPair("IMAP 'Gesendete'-Ordername",
        control.getImapSentFolder());

    TabGroup tabStatistik = new TabGroup(folder, "Statistik");
    LabelGroup grStatistik = new LabelGroup(tabStatistik.getComposite(),
        "Statistik");
    grStatistik.addLabelPair("Altersgruppen", control.getAltersgruppen());
    grStatistik.addLabelPair("Mindestalter f. Mitgliedschaftsjubliäum",
        control.getJubilarStartAlter());
    grStatistik.addLabelPair("Jubiläen", control.getJubilaeen());
    grStatistik.addLabelPair("Altersjubiläen", control.getAltersjubilaeen());

    TabGroup mitgliedAnzeige = new TabGroup(folder, "Mitglied");

    LabelGroup groupSpaltenAnzahl = new LabelGroup(
        mitgliedAnzeige.getComposite(), "Spaltenanzahl");
    ColumnLayout colsMitglied = new ColumnLayout(
        groupSpaltenAnzahl.getComposite(), 2);
    SimpleContainer leftMitglied = new SimpleContainer(
        colsMitglied.getComposite());

    leftMitglied.addLabelPair("Anzahl Spalten Stammdaten",
        control.getAnzahlSpaltenStammdatenInput());

    leftMitglied.addLabelPair("Anzahl Spalten Mitgliedschaft",
        control.getAnzahlSpaltenMitgliedschaftInput());

    leftMitglied.addLabelPair("Anzahl Spalten Zahlung",
        control.getAnzahlSpaltenZahlungInput());

    leftMitglied.addLabelPair("Anzahl Spalten Zusatzfelder",
        control.getAnzahlSpaltenZusatzfelderInput());

    if (Einstellungen.getEinstellung().getUseLesefelder())
      leftMitglied.addLabelPair("Anzahl Spalten Lesefelder",
          control.getAnzahlSpaltenLesefelderInput());

    LabelGroup groupShowInTab = new LabelGroup(mitgliedAnzeige.getComposite(),
        "In Tab anzeigen");
    ColumnLayout colsTab = new ColumnLayout(groupShowInTab.getComposite(), 2);
    SimpleContainer leftTab = new SimpleContainer(colsTab.getComposite());

    leftTab.addLabelPair("Zeige Stammdaten in Tab",
        control.getZeigeStammdatenInTabCheckbox());

    leftTab.addLabelPair("Zeige Mitgliedschaft in Tab",
        control.getZeigeMitgliedschaftInTabCheckbox());

    leftTab.addLabelPair("Zeige Zahlung in Tab",
        control.getZeigeZahlungInTabCheckbox());

    if (Einstellungen.getEinstellung().getZusatzbetrag())
      leftTab.addLabelPair("Zeige Zusatzbeträge in Tab",
          control.getZeigeZusatzbetrageInTabCheckbox());

    leftTab.addLabelPair("Zeige Mitgliedskonto in Tab",
        control.getZeigeMitgliedskontoInTabCheckbox());

    if (Einstellungen.getEinstellung().getVermerke())
      leftTab.addLabelPair("Zeige Vermerke in Tab",
          control.getZeigeVermerkeInTabCheckbox());

    if (Einstellungen.getEinstellung().getWiedervorlage())
      leftTab.addLabelPair("Zeige Wiedervorlage in Tab",
          control.getZeigeWiedervorlageInTabCheckbox());

    leftTab.addLabelPair("Zeige Mails in Tab",
        control.getZeigeMailsInTabCheckbox());

    leftTab.addLabelPair("Zeige Eigenschaften in Tab",
        control.getZeigeEigenschaftenInTabCheckbox());

    leftTab.addLabelPair("Zeige Zusatzfelder in Tab",
        control.getZeigeZusatzfelderInTabCheckbox());

    if (Einstellungen.getEinstellung().getLehrgaenge())
      leftTab.addLabelPair("Zeige Lehrgänge in Tab",
          control.getZeigeLehrgaengeInTabCheckbox());

    if (Einstellungen.getEinstellung().getMitgliedfoto())
      leftTab.addLabelPair("Zeige Foto in Tab",
          control.getZeigeFotoInTabCheckbox());

    if (Einstellungen.getEinstellung().getUseLesefelder())
      leftTab.addLabelPair("Zeige Lesefelder in Tab",
          control.getZeigeLesefelderInTabCheckbox());

    if (Einstellungen.getEinstellung().getArbeitseinsatz())
      leftTab.addLabelPair("Zeige Arbeitseinsatz in Tab",
          control.getZeigeArbeitseinsatzInTabCheckbox());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.EINSTELLUNGEN, false, "help-browser.png");
    buttons.addButton("speichern", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Einstellungen</span></p>"
        + "<p>Anzeige: In diesem Bereich kann gesteuert werden, welche Datenfelder "
        + "angezeigt werden.</p>"
        + "Arbeitsstundenmodell:"
        + "<li>Standard - nur positive Werte können als Arbeitsstunden gebucht werden.</li>"
        + "<li>negative Stunden erlaubt - negative Arbeitsstunden erhöhen die möglichen Zusatzbeträge</li>"
        + "Beitragsmodell:"
        + "<li>jährlich fester Beitrag</li>"
        + "<li>halbjährlich fester Beitrag</li>"
        + "<li>vierteljährlich fester Beitrag</li>"
        + "<li>monatlich fester Beitrag</li>"
        + "<li>Monatlicher Beitrag mit jährlicher, halbjährlicher, vierteljährlicher oder monatlicher Zahlungsweise.</li>"
        + "</form>";
  }
}
