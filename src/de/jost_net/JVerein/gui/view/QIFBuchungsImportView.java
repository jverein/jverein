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

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.QIFBuchungsartAction;
import de.jost_net.JVerein.gui.action.QIFDateiEinlesenAction;
import de.jost_net.JVerein.gui.action.QIFMitgliederAction;
import de.jost_net.JVerein.gui.control.QIFBuchungsImportControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SimpleContainer;

/**
 * @author Rolf Mamat Dieser View unterstützt beim Import von Buchungen aus
 *         anderen Programmen wie z.B. Quicken. Im Ersten Schritt wird die
 *         Exportdatei des externen Programmes in die Datenbank Tabellen
 *         ImportKontoHead und ImportKontoPos eingelesen. Danach kann die
 *         KontoHead einem JVerein Konto zugeordnet werden und die externen
 *         Buchungsklassen den JVerein Buchungsklassen. Sind alle Zuordnungen in
 *         Ordnung kann aus den Import Tabellen in die Buchungen der JVerein
 *         Konten importiert werden.
 */
public class QIFBuchungsImportView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Importiere QIF Buchungen");

    QIFBuchungsImportControl control = new QIFBuchungsImportControl(this);
    LabelGroup group = new LabelGroup(getParent(), "Konto Kopfdaten");
    group.addLabelPair("Externes Konto", control.getAuswahlExternesKonto());

    ColumnLayout colLayout = new ColumnLayout(group.getComposite(), 2);
    SimpleContainer bankLinks = new SimpleContainer(colLayout.getComposite());

    bankLinks.addLabelPair("Beschreibung", control.getInputBeschreibungKonto());
    bankLinks.addLabelPair("Eröffnet", control.getInputEroeffnungsDatum());
    bankLinks.addLabelPair("Eingelesen", control.getInputImportDatum());
    bankLinks.addLabelPair("Gebucht", control.getInputProcessDatum());

    SimpleContainer bankRechts = new SimpleContainer(colLayout.getComposite());
    bankRechts.addLabelPair("Export Datei", control.getInputImportDatei());
    bankRechts.addLabelPair("Eröffnungs Saldo", control.getInputStartSaldo());
    bankRechts.addLabelPair("Gesamt Saldo", control.getInputEndSaldo());
    bankRechts.addLabelPair("Anzahl Buchungen",
        control.getInputAnzahlBuchungen());

    group.addSeparator();
    group.addHeadline("Zugeordnetes Konto in JVerein..");
    group.addLabelPair("JVerein Konto", control.getAuswahlJVereinKonto());

    LabelGroup poslistGroup = new LabelGroup(getParent(),
        "Importierbare Buchungen im gewählten Konto...", true);
    control.getImportKontoPosList(new QIFBuchungsartAction()).paint(
        poslistGroup.getComposite());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.QIFIMPORT, false, "help-browser.png");
    buttons.addButton("Datei einlesen", new QIFDateiEinlesenAction(), null,
        false, "import_obj.gif");
    buttons.addButton("Import löschen",
        control.getAktuellenImportLoeschenAction(), null, false,
        "user-trash.png");
    buttons.addButton("Imports löschen",
        control.getAlleImportsLoeschenAction(), null, false, "list-remove.png");
    buttons.addButton("Buchungsarten zuordnen", new QIFBuchungsartAction(),
        null, false, "zuordnung.png");
    buttons.addButton("Mitglieder zuordnen", new QIFMitgliederAction(), null,
        false, "contact-new.png");
    buttons.addButton("Buchungen übernehmen", control.getPIFPosBuchenAction(),
        null, false, "document-new.png");
    buttons.paint(this.getParent());

  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Import</span></p>"
        + "<p>Buchungen eines externen Programmes wie z.B. Quicken können eingelesen "
        + "bearbeitet und dann importiert werden.</p>"
        + "<li>Zuerst alle Dateien importieren</li>"
        + "<li>Als nächstes Buchungsarten zuordnen</li>"
        + "<li>Eventuell Mitglieder zuordnen</li>"
        + "<li>Dem Externen ein JVereinskonto zuordnen</li>"
        + "<li>Zum Schluss Buchungen übernehmen</li>" + "</form>";
  }
}
