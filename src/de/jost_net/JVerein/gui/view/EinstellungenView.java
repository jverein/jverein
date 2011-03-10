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
 * Revision 1.42  2011-01-30 08:28:18  jost
 * Neu: Zusatzadressen
 *
 * Revision 1.41  2011-01-29 20:32:34  jost
 * Verzögerungszeit für Suchfelder
 *
 * Revision 1.40  2011-01-15 09:46:47  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.39  2011-01-09 14:30:00  jost
 * Stammdaten in die Einstellungen verschoben.
 *
 * Revision 1.38  2011-01-08 15:56:17  jost
 * Einstellungen: Dokumentenspeicherung
 *
 * Revision 1.37  2010-11-17 04:51:11  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 * Revision 1.36  2010-11-13 09:25:58  jost
 * Mit V 1.5 deprecatete Spalten und Tabellen entfernt.
 *
 * Revision 1.35  2010-10-28 19:14:43  jost
 * Neu: Wohnsitzstaat
 *
 * Revision 1.34  2010-10-15 09:58:23  jost
 * Code aufgeräumt
 *
 * Revision 1.33  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.32  2010-08-27 19:07:50  jost
 * neu: Mitgliedsfoto
 *
 * Revision 1.31  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.30  2010-08-10 18:06:49  jost
 * Zahlungswegtexte für den Rechnungsdruck
 *
 * Revision 1.29  2010-08-10 05:40:02  jost
 * Reaktivierung alter Rechnungen
 *
 * Revision 1.28  2010-07-26 08:23:13  jost
 * Manuelle Zahlungen defaultmäßig deaktviert. Reaktvierbar durch Einstellungen.
 *
 * Revision 1.27  2010-07-26 08:04:59  jost
 * 2spaltiges Layout
 *
 * Revision 1.26  2010-07-25 18:42:57  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.25  2010/02/01 21:00:35  jost
 * Neu: Einfache Mailfunktion
 *
 * Revision 1.24  2010/01/01 22:35:57  jost
 * Standardwerte für Zahlungsweg und Zahlungsrhytmus können vorgegeben werden.
 *
 * Revision 1.23  2009/11/19 21:10:51  jost
 * Update-Option entfernt.
 *
 * Revision 1.22  2009/10/17 19:46:44  jost
 * Vorbereitung Mailversand.
 *
 * Revision 1.21  2009/09/13 19:20:17  jost
 * Neu: Prüfung auf Updates
 *
 * Revision 1.20  2009/07/14 07:29:27  jost
 * Neu: Box aktuelle Geburtstage
 *
 * Revision 1.19  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.18  2009/04/25 05:29:21  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.17  2009/04/13 11:40:14  jost
 * Neu: Lehrgänge
 *
 * Revision 1.16  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.15  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.14  2008/12/22 21:17:26  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.13  2008/11/29 13:11:38  jost
 * Neu: Konfiguration der Spalten einer Tabelle
 *
 * Revision 1.12  2008/11/11 20:05:09  jost
 * Anzeige neu gruppiert und mit Scrollbars versehen.
 *
 * Revision 1.11  2008/08/10 12:36:22  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.10  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.9  2008/05/22 06:53:43  jost
 * BuchfÃ¼hrung: Beginn des GeschÃ¤ftsjahres
 *
 * Revision 1.8  2008/03/08 19:29:22  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.7  2008/01/01 19:50:58  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.6  2008/01/01 13:14:15  jost
 * Neu: Dateinamenmuster
 *
 * Revision 1.5  2007/12/02 13:43:16  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.4  2007/12/01 17:46:46  jost
 * Wegfall Standardtab fÃ¼r die Suche
 *
 * Revision 1.3  2007/08/23 19:25:50  jost
 * Header korrigiert.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.EinstellungControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
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
    tabAllgemein.addHeadline("Spendenbescheinigung");
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Finanzamt"),
        control.getFinanzamt());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Steuernummer"),
        control.getSteuernummer());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("Bescheiddatum"),
        control.getBescheiddatum());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n()
        .tr("vorläufiger Bescheid"), control.getVorlaeufig());
    tabAllgemein.addLabelPair(JVereinPlugin.getI18n().tr("begünstigter Zweck"),
        control.getBeguenstigterzweck());
    tabAllgemein.addLabelPair(
        JVereinPlugin.getI18n().tr(
            "Mitgliedsbeiträge dürfen bescheinigt werden"),
        control.getMitgliedsbetraege());

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
    right.addLabelPair(JVereinPlugin.getI18n().tr("zusätzliche Adressen *"),
        control.getZusatzadressen());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Auslandsadressen *"),
        control.getAuslandsadressen());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Arbeitseinsatz *"),
        control.getArbeitseinsatz());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Dokumentenspeicherung *"),
        control.getDokumentenspeicherung());
    right.addLabelPair(JVereinPlugin.getI18n().tr("externe Mitgliedsnummer"),
        control.getExterneMitgliedsnummer());
    right.addLabelPair(
        JVereinPlugin.getI18n().tr("aktuelle Geburtstage - Tage vorher"),
        control.getAktuelleGeburtstageVorher());
    right.addLabelPair(
        JVereinPlugin.getI18n().tr("aktuelle Geburtstage - Tage nachher"),
        control.getAktuelleGeburtstageNachher());
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

    TabGroup tabDateinamen = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Dateinamen"));
    LabelGroup groupDatei = new LabelGroup(tabDateinamen.getComposite(),
        JVereinPlugin.getI18n().tr("Dateinamen"));
    groupDatei.addLabelPair(JVereinPlugin.getI18n().tr("Muster"),
        control.getDateinamenmuster());
    groupDatei.addText("a$ = Aufgabe, d$ = Datum, s$ = Sortierung, z$ = Zeit",
        true);

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

    ButtonArea buttons = new ButtonArea(getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.EINSTELLUNGEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
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
    return "<form><p><span color=\"header\" font=\"header\">Einstellungen</span></p>"
        + "<p>Anzeige: In diesem Bereich kann gesteuert werden, welche Datenfelder "
        + "angezeigt werden.</p>"
        + "Beitragsmodell:"
        + "<li>jährlich fester Beitrag</li>"
        + "<li>halbjährlich fester Beitrag</li>"
        + "<li>vierteljährlich fester Beitrag</li>"
        + "<li>monatlich fester Beitrag</li>"
        + "<li>Monatlicher Beitrag mit jährlicher, halbjährlicher, vierteljährlicher oder monatlicher Zahlungsweise.</li>"
        + "</form>";
  }
}
