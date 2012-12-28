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
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Einstellungen"));

    final EinstellungControl control = new EinstellungControl(this);

    ScrolledContainer cont = new ScrolledContainer(getParent());
    TabFolder folder = new TabFolder(cont.getComposite(), SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    TabGroup tabAllgemein = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Allgemein"));
    tabAllgemein.addHeadline("Allgemein");
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Name"),
        control.getName(true));
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Langer Name"),
        control.getNameLang());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Straße"),
        control.getStrasse());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("PLZ"),
        control.getPlz());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Ort"),
        control.getOrt());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Bankleitzahl"),
        control.getBlz());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Konto"),
        control.getKonto());

    TabGroup tabAnzeige = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Anzeige"));

    LabelGroup group = new LabelGroup(tabAnzeige.getComposite(), JVereinPlugin
        .getI18n().tr("Anzeige"));
    ColumnLayout cols1 = new ColumnLayout(group.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());

    left.addLabelPair(JVereinPlugin.getI18n().tr("Geburtsdatum Pflichtfeld"),
        control.getGeburtsdatumPflicht());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Eintrittsdatum Pflichtfeld"),
        control.getEintrittsdatumPflicht());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Sterbedatum"),
        control.getSterbedatum());
    left.addLabelPair(JVereinPlugin.getI18n()
        .tr("Kommunikationsdaten anzeigen"), control.getKommunikationsdaten());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zusatzbeträge anzeigen")
        + "*", control.getZusatzbetrag());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Vermerke anzeigen"),
        control.getVermerke());
    left.addLabelPair(JVereinPlugin.getI18n()
        .tr("Wiedervorlage anzeigen" + "*"), control.getWiedervorlage());
    left.addLabelPair(JVereinPlugin.getI18n().tr(
        "Kursteilnehmer anzeigen" + "*"), control.getKursteilnehmer());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Lehrgänge anzeigen" + "*"),
        control.getLehrgaenge());
    left.addLabelPair(JVereinPlugin.getI18n()
        .tr("Juristische Personen erlaubt"), control.getJuristischePersonen());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Mitgliedskonten *"),
        control.getMitgliedskonto());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Mitgliedsfoto *"),
        control.getMitgliedfoto());
    SimpleContainer right = new SimpleContainer(cols1.getComposite());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Lesefelder anzeigen *"),
        control.getUseLesefelder());
    right.addLabelPair(JVereinPlugin.getI18n().tr("zusätzliche Adressen *"),
        control.getZusatzadressen());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Auslandsadressen *"),
        control.getAuslandsadressen());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Arbeitseinsatz *"),
        control.getArbeitseinsatz());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Dokumentenspeicherung *"),
        control.getDokumentenspeicherung());
    right.addLabelPair(JVereinPlugin.getI18n().tr("individuelle Beiträge *"),
        control.getIndividuelleBeitraege());
    right.addLabelPair(JVereinPlugin.getI18n().tr("externe Mitgliedsnummer"),
        control.getExterneMitgliedsnummer());
    right
        .addLabelPair(
            JVereinPlugin.getI18n().tr(
                "Verzögerungszeit Suche (in Millisekunden)"),
            control.getDelaytime());
    right.addHeadline("* "
        + JVereinPlugin.getI18n().tr("Änderung erfordert Neustart"));

    TabGroup tabBeitraege = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Beiträge"));
    LabelGroup groupAbu = new LabelGroup(tabBeitraege.getComposite(),
        JVereinPlugin.getI18n().tr("Beiträge"));
    groupAbu.addLabelPair(JVereinPlugin.getI18n().tr("Beitragsmodel"),
        control.getBeitragsmodel());
    groupAbu.addInput(control.getZahlungsrhytmus());
    groupAbu.addInput(control.getZahlungsweg());
    groupAbu.addInput(control.getDtausTextschluessel());
    groupAbu.addInput(control.getDefaultSEPALand());

    TabGroup tabDateinamen = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Dateinamen"));
    LabelGroup groupDatei = new LabelGroup(tabDateinamen.getComposite(),
        JVereinPlugin.getI18n().tr("Dateinamenmuster"));
    groupDatei.addLabelPair(JVereinPlugin.getI18n().tr("Auswertung"),
        control.getDateinamenmuster());
    groupDatei.addLabelPair(JVereinPlugin.getI18n()
        .tr("Spendenbescheinigungen"), control.getDateinamenmusterSpende());

    TabGroup tabSpendenbescheinigung = new TabGroup(folder, JVereinPlugin
        .getI18n().tr("Spendenbescheinigungen"));
    LabelGroup groupSpenden = new LabelGroup(
        tabSpendenbescheinigung.getComposite(), JVereinPlugin.getI18n().tr(
            "Spendenbescheinigungen"));
    groupSpenden.addLabelPair(JVereinPlugin.getI18n().tr("Finanzamt"),
        control.getFinanzamt());
    groupSpenden.addLabelPair(JVereinPlugin.getI18n().tr("Steuernummer"),
        control.getSteuernummer());
    groupSpenden.addLabelPair(JVereinPlugin.getI18n().tr("Bescheiddatum"),
        control.getBescheiddatum());
    groupSpenden.addLabelPair(JVereinPlugin.getI18n()
        .tr("vorläufiger Bescheid"), control.getVorlaeufig());
    groupSpenden.addLabelPair(JVereinPlugin.getI18n().tr("Vorläufig ab"),
        control.getVorlaeufigab());
    groupSpenden.addLabelPair(JVereinPlugin.getI18n().tr("begünstigter Zweck"),
        control.getBeguenstigterzweck());
    groupSpenden.addLabelPair(
        JVereinPlugin.getI18n().tr(
            "Mitgliedsbeiträge dürfen bescheinigt werden"),
        control.getMitgliedsbetraege());
    groupSpenden.addLabelPair(JVereinPlugin.getI18n().tr("Mindestbetrag"),
        control.getSpendenbescheinigungminbetrag());
    groupSpenden.addLabelPair(JVereinPlugin.getI18n().tr("Verzeichnis"),
        control.getSpendenbescheinigungverzeichnis());
    groupSpenden.addLabelPair(
        JVereinPlugin.getI18n().tr("Buchungsart drucken"),
        control.getSpendenbescheinigungPrintBuchungsart());

    TabGroup tabBuchfuehrung = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Buchführung"));
    LabelGroup groupBuchfuehrung = new LabelGroup(
        tabBuchfuehrung.getComposite(), JVereinPlugin.getI18n().tr(
            "Buchführung"));
    groupBuchfuehrung.addLabelPair(
        JVereinPlugin.getI18n().tr("Beginn Geschäftsjahr (TT.MM.)"),
        control.getBeginnGeschaeftsjahr());

    TabGroup tabRechnungen = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Rechnungen"));
    LabelGroup groupRechnungen = new LabelGroup(tabRechnungen.getComposite(),
        JVereinPlugin.getI18n().tr("Rechnungen"));
    groupRechnungen.addLabelPair(JVereinPlugin.getI18n().tr("Text Abbuchung"),
        control.getRechnungTextAbbuchung());
    groupRechnungen.addLabelPair(
        JVereinPlugin.getI18n().tr("Text Überweisung"),
        control.getRechnungTextUeberweisung());
    groupRechnungen.addLabelPair(JVereinPlugin.getI18n().tr("Text Bar"),
        control.getRechnungTextBar());

    TabGroup tabTabellen = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Tabellen"));

    TabFolder folderTabellen = new TabFolder(tabTabellen.getComposite(),
        SWT.NONE);

    TabGroup tabMitglieder = new TabGroup(folderTabellen, JVereinPlugin
        .getI18n().tr("Mitglieder"));
    LabelGroup groupMitglieder = new LabelGroup(tabMitglieder.getComposite(),
        JVereinPlugin.getI18n().tr("Trefferliste Mitglieder"));
    control.getSpaltendefinitionTable(groupMitglieder.getComposite());

    TabGroup tabMail = new TabGroup(folder, JVereinPlugin.getI18n().tr("Mail"));
    LabelGroup groupMail = new LabelGroup(tabMail.getComposite(), JVereinPlugin
        .getI18n().tr("Mail"));
    groupMail.addLabelPair("Server", control.getSmtpServer());
    groupMail.addLabelPair("Port", control.getSmtpPort());
    groupMail.addLabelPair("Benutzer", control.getSmtpAuthUser());
    groupMail.addLabelPair("Passwort", control.getSmtpAuthPwd());
    groupMail.addLabelPair("Absenderadresse", control.getSmtpFromAddress());
    groupMail.addLabelPair("SSL verwenden", control.getSmtpSsl());
    groupMail.addLabelPair("Starttls verwenden", control.getSmtpStarttls());

    TabGroup tabStatistik = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Statistik"));
    LabelGroup grStatistik = new LabelGroup(tabStatistik.getComposite(),
        JVereinPlugin.getI18n().tr("Statistik"));
    grStatistik.addLabelPair(JVereinPlugin.getI18n().tr("Altersgruppen"),
        control.getAltersgruppen());
    grStatistik.addLabelPair(JVereinPlugin.getI18n().tr("Jubiläen"),
        control.getJubilaeen());
    grStatistik.addLabelPair(JVereinPlugin.getI18n().tr("Altersjubiläen"),
        control.getAltersjubilaeen());

    TabGroup mitgliedAnzeige = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Mitglied"));

    LabelGroup groupSpaltenAnzahl = new LabelGroup(
        mitgliedAnzeige.getComposite(), JVereinPlugin.getI18n().tr(
            "Spaltenanzahl"));
    ColumnLayout colsMitglied = new ColumnLayout(
        groupSpaltenAnzahl.getComposite(), 2);
    SimpleContainer leftMitglied = new SimpleContainer(
        colsMitglied.getComposite());

    leftMitglied.addLabelPair(
        JVereinPlugin.getI18n().tr("Anzahl Spalten Stammdaten"),
        control.getAnzahlSpaltenStammdatenInput());

    leftMitglied.addLabelPair(
        JVereinPlugin.getI18n().tr("Anzahl Spalten Mitgliedschaft"),
        control.getAnzahlSpaltenMitgliedschaftInput());

    leftMitglied.addLabelPair(
        JVereinPlugin.getI18n().tr("Anzahl Spalten Zahlung"),
        control.getAnzahlSpaltenZahlungInput());

    leftMitglied.addLabelPair(
        JVereinPlugin.getI18n().tr("Anzahl Spalten Zusatzfelder"),
        control.getAnzahlSpaltenZusatzfelderInput());

    if (Einstellungen.getEinstellung().getUseLesefelder())
      leftMitglied.addLabelPair(
          JVereinPlugin.getI18n().tr("Anzahl Spalten Lesefelder"),
          control.getAnzahlSpaltenLesefelderInput());

    LabelGroup groupShowInTab = new LabelGroup(mitgliedAnzeige.getComposite(),
        JVereinPlugin.getI18n().tr("In Tab anzeigen"));
    ColumnLayout colsTab = new ColumnLayout(groupShowInTab.getComposite(), 2);
    SimpleContainer leftTab = new SimpleContainer(colsTab.getComposite());

    leftTab.addLabelPair(JVereinPlugin.getI18n().tr("Zeige Stammdaten in Tab"),
        control.getZeigeStammdatenInTabCheckbox());

    leftTab.addLabelPair(
        JVereinPlugin.getI18n().tr("Zeige Mitgliedschaft in Tab"),
        control.getZeigeMitgliedschaftInTabCheckbox());

    leftTab.addLabelPair(JVereinPlugin.getI18n().tr("Zeige Zahlung in Tab"),
        control.getZeigeZahlungInTabCheckbox());

    if (Einstellungen.getEinstellung().getZusatzbetrag())
      leftTab.addLabelPair(
          JVereinPlugin.getI18n().tr("Zeige Zusatzbeträge in Tab"),
          control.getZeigeZusatzbetrageInTabCheckbox());

    if (Einstellungen.getEinstellung().getMitgliedskonto())
      leftTab.addLabelPair(
          JVereinPlugin.getI18n().tr("Zeige Mitgliedskonto in Tab"),
          control.getZeigeMitgliedskontoInTabCheckbox());

    if (Einstellungen.getEinstellung().getVermerke())
      leftTab.addLabelPair(JVereinPlugin.getI18n().tr("Zeige Vermerke in Tab"),
          control.getZeigeVermerkeInTabCheckbox());

    if (Einstellungen.getEinstellung().getWiedervorlage())
      leftTab.addLabelPair(
          JVereinPlugin.getI18n().tr("Zeige Wiedervorlage in Tab"),
          control.getZeigeWiedervorlageInTabCheckbox());

    leftTab.addLabelPair(JVereinPlugin.getI18n().tr("Zeige Mails in Tab"),
        control.getZeigeMailsInTabCheckbox());

    leftTab.addLabelPair(
        JVereinPlugin.getI18n().tr("Zeige Eigenschaften in Tab"),
        control.getZeigeEigenschaftenInTabCheckbox());

    leftTab.addLabelPair(JVereinPlugin.getI18n()
        .tr("Zeige Zusatzfelder in Tab"), control
        .getZeigeZusatzfelderInTabCheckbox());

    if (Einstellungen.getEinstellung().getLehrgaenge())
      leftTab.addLabelPair(
          JVereinPlugin.getI18n().tr("Zeige Lehrgänge in Tab"),
          control.getZeigeLehrgaengeInTabCheckbox());

    if (Einstellungen.getEinstellung().getMitgliedfoto())
      leftTab.addLabelPair(JVereinPlugin.getI18n().tr("Zeige Foto in Tab"),
          control.getZeigeFotoInTabCheckbox());

    if (Einstellungen.getEinstellung().getUseLesefelder())
      leftTab.addLabelPair(JVereinPlugin.getI18n()
          .tr("Zeige Lesefelder in Tab"), control
          .getZeigeLesefelderInTabCheckbox());

    if (Einstellungen.getEinstellung().getArbeitseinsatz())
      leftTab.addLabelPair(
          JVereinPlugin.getI18n().tr("Zeige Arbeitseinsatz in Tab"),
          control.getZeigeArbeitseinsatzInTabCheckbox());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.EINSTELLUNGEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
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
    return JVereinPlugin
        .getI18n()
        .tr("<form><p><span color=\"header\" font=\"header\">Einstellungen</span></p>"
            + "<p>Anzeige: In diesem Bereich kann gesteuert werden, welche Datenfelder "
            + "angezeigt werden.</p>"
            + "Beitragsmodell:"
            + "<li>jährlich fester Beitrag</li>"
            + "<li>halbjährlich fester Beitrag</li>"
            + "<li>vierteljährlich fester Beitrag</li>"
            + "<li>monatlich fester Beitrag</li>"
            + "<li>Monatlicher Beitrag mit jährlicher, halbjährlicher, vierteljährlicher oder monatlicher Zahlungsweise.</li>"
            + "</form>");
  }
}
