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
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 *
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.SpendenbescheinigungVar;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.io.Reporter;
import de.jost_net.JVerein.keys.HerkunftSpende;
import de.jost_net.JVerein.keys.Spendenart;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Action zur Generierung von Spendenbescheinigungen aus der Datenbank.<br>
 * Diese Klasse kapselt die Generierung des Standard-Formulars und wird auch bei
 * der Generierung eines Dokuments aus der Detailansicht der
 * Spendenbescheinigung heraus verwendet.
 */
public class SpendenbescheinigungPrintAction implements Action
{
  private boolean standardPdf = true;

  private String fileName = null;

  private de.willuhn.jameica.system.Settings settings;

  /**
   * Konstruktor ohne Parameter. Es wird angenommen, dass das Standard-Dokument
   * aufbereitet werden soll.
   */
  public SpendenbescheinigungPrintAction()
  {
    super();
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  /**
   * Konstruktor. Über den Parameter kann festgelegt werden, ob das Standard-
   * oder das individuelle Dokument aufbereitet werden soll.
   * 
   * @param standard
   *          true=Standard-Dokument, false=individuelles Dokument
   */
  public SpendenbescheinigungPrintAction(boolean standard)
  {
    super();
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
    standardPdf = standard;
  }

  /**
   * Konstruktor. Über den Parameter kann festgelegt werden, ob das Standard-
   * oder das individuelle Dokument aufbereitet werden soll.
   * 
   * @param standard
   *          true=Standard-Dokument, false=individuelles Dokument
   * @param fileName
   *          Dateiname als Vorgabe inklusive Pfad
   */
  public SpendenbescheinigungPrintAction(boolean standard, String fileName)
  {
    super();
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
    standardPdf = standard;
    this.fileName = fileName;
  }

  /**
   * Aufbereitung der Spendenbescheinigungen<br>
   * Hinweis: Das bzw. die generierten Formulare werden nicht im Acrobat Reader
   * angezeigt.
   * 
   * @param context
   *          Die Spendenbescheinigung(en)
   */
  public void handleAction(Object context) throws ApplicationException
  {
    Spendenbescheinigung[] spbArr = null;
    // Prüfung des Contexs, vorhanden, eine oder mehrere
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
    }
    if (context == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Spendenbescheinigung ausgewählt"));
    }
    else if (context instanceof Spendenbescheinigung)
    {
      spbArr = new Spendenbescheinigung[] { (Spendenbescheinigung) context };
    }
    else if (context instanceof Spendenbescheinigung[])
    {
      spbArr = (Spendenbescheinigung[]) context;
    }
    else
    {
      return;
    }
    // Aufbereitung
    try
    {
      String path = Einstellungen.getEinstellung()
          .getSpendenbescheinigungverzeichnis();
      if (path == null)
      {
        path = settings.getString("lastdir", System.getProperty("user.home"));
      }
      else if (path.length() == 0)
      {
        path = settings.getString("lastdir", System.getProperty("user.home"));
      }

      // path = path.endsWith("\\") ? path : path + "\\";
      settings.setAttribute("lastdir", path);
      if (!standardPdf)
      {
        // Check ob auch für alle Spendenbescheinigungen ein Forumular
        // ausgewaehlt ist
        for (Spendenbescheinigung spb : spbArr)
        {
          Formular spendeformular = spb.getFormular();
          if (spendeformular == null)
          {
            GUI.getStatusBar()
                .setErrorText(
                    "Nicht alle Spendenbescheinigungen haben ein gültiges Formular!");
            return;
          }
        }
      }
      // Start der Aufbereitung der Dokumente
      for (Spendenbescheinigung spb : spbArr)
      {
        String fileName = null;
        if (spbArr.length > 1 || this.fileName == null)
        {
          // Dokumentennamen aus konfiguriertem Verzeichnis und dem
          // DateinamenmusterSpende
          // zusammensetzen, wenn mehr als eine Spendenbescheinigung
          // aufzubereiten
          // oder keine Vorgabe für einen Dateinamen gemacht wurde.
          if (spb.getMitglied() != null)
          {
            fileName = new Dateiname(spb.getMitglied(),
                spb.getBescheinigungsdatum(), "Spendenbescheinigung",
                Einstellungen.getEinstellung().getDateinamenmusterSpende(),
                "pdf").get();
          }
          else
          {
            fileName = new Dateiname(spb.getZeile1(), spb.getZeile2(),
                spb.getBescheinigungsdatum(), "Spendenbescheinigung",
                Einstellungen.getEinstellung().getDateinamenmusterSpende(),
                "pdf").get();
          }
          fileName = path + fileName;
        }
        else
        {
          fileName = this.fileName;
        }
        final File file = new File(fileName);
        // Aufbereitung des Dokumentes
        if (standardPdf)
        {
          generiereSpendenbescheinigungStandard(spb, fileName);
        }
        else
        {
          Formular fo = (Formular) Einstellungen.getDBService().createObject(
              Formular.class, spb.getFormular().getID());
          Map<String, Object> map = spb.getMap(null);
          map = new AllgemeineMap().getMap(map);
          FormularAufbereitung fa = new FormularAufbereitung(file);
          fa.writeForm(fo, map);
          fa.closeFormular();
        }
      }
    }
    catch (FileNotFoundException ie)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Aufbereiten der Spendenbescheinigung");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, ie);
    }
    catch (IOException ie)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Aufbereiten der Spendenbescheinigung");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, ie);
    }
    catch (DocumentException ie)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Aufbereiten der Spendenbescheinigung");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, ie);
    }
  }

  /**
   * Generierung des Standard-Dokumentes
   * 
   * @param spb
   *          Die Spendenbescheinigung aus der Datenbank
   * @param fileName
   *          Der Dateiname, wohin das Dokument geschrieben werden soll
   * @throws IOException
   * @throws DocumentException
   */
  private void generiereSpendenbescheinigungStandard(Spendenbescheinigung spb,
      String fileName) throws IOException, DocumentException
  {
    final File file = new File(fileName);
    FileOutputStream fos = new FileOutputStream(file);

    Map<String, Object> map = spb.getMap(null);
    map = new AllgemeineMap().getMap(map);

    boolean isSammelbestaetigung = spb.isSammelbestaetigung();

    Reporter rpt = new Reporter(fos, 80, 50, 50, 50);
    rpt.addHeaderColumn(
        "Aussteller (Bezeichnung und Anschrift der steuerbegünstigten Einrichtung)",
        Element.ALIGN_CENTER, 100, Color.LIGHT_GRAY);

    rpt.createHeader();

    rpt.addColumn("\n" + getAussteller() + "\n ", Element.ALIGN_LEFT);
    rpt.closeTable();

    if (isSammelbestaetigung)
    {
      rpt.add(
          "Sammelbestätigung über "
              + map.get(SpendenbescheinigungVar.SPENDEART.getName()), 13);
    }
    else
    {
      rpt.add(
          "Bestätigung über "
              + map.get(SpendenbescheinigungVar.SPENDEART.getName()), 13);
    }

    rpt.add(
        "im Sinne des § 10b des Einkommenssteuergesetzes an eine der in § 5 Abs. 1 Nr. 9 des Körperschaftssteuergesetzes "
            + "bezeichneten Körperschaften, Personenvereinigungen oder Vermögensmassen\n",
        10);

    rpt.addHeaderColumn("Name und Anschrift des Zuwendenden",
        Element.ALIGN_CENTER, 100, Color.LIGHT_GRAY);
    rpt.createHeader();
    rpt.addColumn(
        (String) map.get(SpendenbescheinigungVar.EMPFAENGER.getName()),
        Element.ALIGN_LEFT);
    rpt.closeTable();

    switch (spb.getSpendenart())
    {
      case Spendenart.GELDSPENDE:
        rpt.addHeaderColumn("Betrag der Zuwendung -in Ziffern-",
            Element.ALIGN_CENTER, 100, Color.LIGHT_GRAY);
        break;
      case Spendenart.SACHSPENDE:
        rpt.addHeaderColumn("Wert der Zuwendung -in Ziffern-",
            Element.ALIGN_CENTER, 100, Color.LIGHT_GRAY);
        break;
    }
    rpt.addHeaderColumn("-in Buchstaben-", Element.ALIGN_CENTER, 250,
        Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Tag der Zuwendung", Element.ALIGN_CENTER, 50,
        Color.LIGHT_GRAY);
    rpt.createHeader();
    rpt.addColumn(
        "*"
            + Einstellungen.DECIMALFORMAT.format((Double) map
                .get(SpendenbescheinigungVar.BETRAG.getName())) + "*",
        Element.ALIGN_CENTER);
    rpt.addColumn(
        (String) map.get(SpendenbescheinigungVar.BETRAGINWORTEN.getName()),
        Element.ALIGN_CENTER);
    rpt.addColumn(
        (String) map.get(SpendenbescheinigungVar.SPENDEDATUM.getName()),
        Element.ALIGN_CENTER);
    rpt.closeTable();

    switch (spb.getSpendenart())
    {
      case Spendenart.SACHSPENDE:
        rpt.addHeaderColumn(
            "Genaue Bezeichnung der Sachzuwendung mit Alter, Zustand, Kaufpreis usw.",
            Element.ALIGN_CENTER, 100, Color.LIGHT_GRAY);
        rpt.createHeader();
        rpt.addColumn(spb.getBezeichnungSachzuwendung(), Element.ALIGN_LEFT);
        rpt.closeTable();
        switch (spb.getHerkunftSpende())
        {
          case HerkunftSpende.BETRIEBSVERMOEGEN:
            rpt.add(
                "Die Sachzuwendung stammt nach den Angaben des Zuwendenden aus dem Betriebsvermögen und ist "
                    + "mit dem Entnahmewert (ggf. mit dem niedrigeren gemeinen Wert) bewertet.\n\n",
                9);
            break;
          case HerkunftSpende.PRIVATVERMOEGEN:
            rpt.add(
                "Die Sachzuwendung stammt nach den Angaben des Zuwendenden aus dem Privatvermögen.\n\n",
                9);
            break;
          case HerkunftSpende.KEINEANGABEN:
            rpt.add(
                "Der Zuwendende hat trotz Aufforderung keine Angaben zur Herkunft der Sachzuwendung gemacht.\n\n",
                9);
            break;
        }
        if (spb.getUnterlagenWertermittlung())
        {
          rpt.add(
              "Geeignete Unterlagen, die zur Wertermittlung gedient haben, z. B. Rechnung, Gutachten, liegen vor.\n\n",
              9);
        }
    }

    /*
     * Bei Sammelbestätigungen ist der Verweis auf Verzicht in der Anlage
     * vermerkt
     */
    String verzicht = "nein";
    boolean andruckVerzicht = false;

    if (spb.getAutocreate())
    {
      if (!isSammelbestaetigung)
      {
        if (spb.getBuchungen().get(0).getVerzicht().booleanValue())
        {
          verzicht = "ja";
        }
        andruckVerzicht = true;
      }
    }
    else
    {
      if (spb.getErsatzAufwendungen())
      {
        verzicht = "ja";
      }
      andruckVerzicht = true;
    }

    if (!isSammelbestaetigung)
    {
      if (andruckVerzicht)
      {
        rpt.add("Es handelt sich um den Verzicht von Aufwendungen: " + verzicht
            + "\n\n", 9);
      }
      else
      {
        rpt.add(
            "Es handelt sich nicht um den Verzicht auf Erstattung von Aufwendungen: "
                + "\n\n", 9);
      }
    }
    if (!Einstellungen.getEinstellung().getVorlaeufig())
    {
      // rdc: "Förderung" entfernt, da in "Beguenstigterzweck" enthalten
      String txt = "Wir sind wegen "
          + Einstellungen.getEinstellung().getBeguenstigterzweck()
          + " nach dem letzten uns zugegangenen Freistellungsbescheid bzw. nach der Anlage zum Körperschaftssteuerbescheid des Finanzamtes "
          + Einstellungen.getEinstellung().getFinanzamt()
          + ", StNr. "
          + Einstellungen.getEinstellung().getSteuernummer()
          + ", vom "
          + new JVDateFormatTTMMJJJJ().format(Einstellungen.getEinstellung()
              .getBescheiddatum())
          + " nach § 5 Abs. 1 Nr. 9 des Körperschaftsteuergesetzes von der Körperschaftsteuer und nach § 3 Nr. 6 des Gewerbesteuergesetzes von der Gewerbesteuer befreit.";
      rpt.add(txt, 9);
    }
    else
    {
      // rdc: "Förderung" entfernt, da in "Beguenstigterzweck" enthalten
      String txt = "Wir sind wegen "
          + Einstellungen.getEinstellung().getBeguenstigterzweck()
          + " durch vorläufige Bescheinigung des Finanzamtes "
          + Einstellungen.getEinstellung().getFinanzamt()
          + ", StNr. "
          + Einstellungen.getEinstellung().getSteuernummer()
          + ", vom "
          + new JVDateFormatTTMMJJJJ().format(Einstellungen.getEinstellung()
              .getBescheiddatum())
          + " ab "
          + new JVDateFormatTTMMJJJJ().format(Einstellungen.getEinstellung()
              .getVorlaeufigab())
          + " als steuerbegünstigten Zwecken dienend anerkannt.";
      rpt.add(txt, 9);
    }
    // rdc: "Förderung" entfernt, da in "Beguenstigterzweck" enthalten
    rpt.add("\n\nEs wird bestätigt, dass die Zuwendung nur zur "
        + Einstellungen.getEinstellung().getBeguenstigterzweck()
        + " verwendet wird.\n", 9);
    if (!Einstellungen.getEinstellung().getMitgliedsbetraege()
        && spb.getSpendenart() == Spendenart.GELDSPENDE)
    {
      rpt.add(
          "Es wird bestätigt, dass es sich nicht um einen Mitgliedsbeitrag i.S.v § 10b Abs. 1 Satz 2 Einkommensteuergesetzes handelt.",
          9);
    }

    if (isSammelbestaetigung)
    {
      rpt.add(new Paragraph(" "));
      rpt.add(
          "Es wird bestätigt, dass über die in der Gesamtsumme enthaltenen Zuwendungen keine weiteren Bestätigungen, weder formelle Zuwendungsbestätigungen noch Beitragsquittungen oder ähnliches ausgestellt wurden und werden.",
          9);
    }

    rpt.add("\n\n" + Einstellungen.getEinstellung().getOrt() + ", "
        + new JVDateFormatTTMMJJJJ().format(spb.getBescheinigungsdatum()), 9);

    rpt.add(
        "\n\n\n\n.................................................................................\nUnterschrift des Zuwendungsempfängers",
        9);

    rpt.add("\n\nHinweis:", 9);
    rpt.add(
        "\nWer vorsätzlich oder grob fahrlässig eine unrichtige Zuwendungsbestätigung erstellt oder wer veranlasst, dass "
            + "Zuwendungen nicht zu den in der Zuwendungsbestätigung angegebenen steuerbegünstigten Zwecken verwendet "
            + "werden, haftet für die Steuer, die dem Fiskus durch einen etwaigen Abzug der Zuwendungen beim Zuwendenden "
            + "entgeht (§ 10b Abs. 4 EStG, § 9 Abs. 3 KStG, § 9 Nr. 5 GewStG).\n\n"
            + "Diese Bestätigung wird nicht als Nachweis für die steuerliche Berücksichtigung der Zuwendung anerkannt, wenn das "
            + "Datum des Freistellungsbescheides länger als 5 Jahre bzw. das Datum der vorläufigen Bescheinigung länger als 3 Jahre "
            + "seit Ausstellung der Bestätigung zurückliegt (BMF vom 15.12.1994 - BStBl I S. 884).",
        8);

    /* Es sind mehrere Spenden für diese Spendenbescheinigung vorhanden */
    if (isSammelbestaetigung)
    {
      List<Buchung> buchungen = spb.getBuchungen();

      rpt.newPage();
      rpt.add(getAussteller(), 13);
      rpt.add(new Paragraph(" "));
      rpt.add(
          "Anlage zur Sammelbestätigung vom "
              + (String) map.get(SpendenbescheinigungVar.BESCHEINIGUNGDATUM
                  .getName()), 11);
      rpt.add(
          "für den Zeitraum vom "
              + (String) map.get(SpendenbescheinigungVar.SPENDENZEITRAUM
                  .getName()), 11);

      rpt.add(new Paragraph(" "));

      rpt.addHeaderColumn("Datum", Element.ALIGN_LEFT, 100, Color.LIGHT_GRAY);
      rpt.addHeaderColumn("Betrag in EUR", Element.ALIGN_RIGHT, 100,
          Color.LIGHT_GRAY);
      rpt.addHeaderColumn("Verwendung", Element.ALIGN_LEFT, 500,
          Color.LIGHT_GRAY);
      rpt.createHeader();

      boolean printBuchungsart = Einstellungen.getEinstellung()
          .getSpendenbescheinigungPrintBuchungsart();

      for (Buchung buchung : buchungen)
      {
        rpt.addColumn(buchung.getDatum(), Element.ALIGN_RIGHT);
        rpt.addColumn(Double.valueOf(buchung.getBetrag()));
        String verwendung = "";
        if (printBuchungsart)
        {
          verwendung = buchung.getBuchungsart().getBezeichnung();
          // rpt.addColumn(buchung.getBuchungsart().getBezeichnung(),
          // Element.ALIGN_LEFT);
        }
        else
        {
          verwendung = buchung.getZweck();
          // rpt.addColumn(buchung.getZweck(), Element.ALIGN_LEFT);
        }
        if (buchung.getVerzicht().booleanValue())
        {
          verwendung = verwendung + " (b)";
          // rpt.addColumn("Verzicht auf Erstattung von Aufwendungen",
          // Element.ALIGN_LEFT);
        }
        else
        {
          verwendung = verwendung + " (a)";
          // rpt.addColumn("Kein Verzicht auf Erstattung von Aufwendungen",
          // Element.ALIGN_LEFT);
        }
        rpt.addColumn(verwendung, Element.ALIGN_LEFT);
      }

      /* Summenzeile */
      String sumString = Einstellungen.DECIMALFORMAT.format(spb.getBetrag());
      rpt.addColumn("Summe", Element.ALIGN_LEFT, Color.LIGHT_GRAY);
      rpt.addColumn(sumString, Element.ALIGN_RIGHT, Color.LIGHT_GRAY);
      rpt.addColumn("", Element.ALIGN_LEFT, Color.LIGHT_GRAY);

      rpt.closeTable();
      // Etwas Abstand
      rpt.add(new Paragraph(" "));
      // Nun noch die Legende
      rpt.add("Legende:", 8);
      rpt.add(
          "(a): Es handelt sich nicht um den Verzicht auf Erstattung von Aufwendungen",
          8);
      rpt.add(
          "(b): Es handelt sich um den Verzicht auf Erstattung von Aufwendungen",
          8);
    }

    rpt.close();
    fos.close();
  }

  private String getAussteller() throws RemoteException
  {
    return Einstellungen.getEinstellung().getNameLang() + ", "
        + Einstellungen.getEinstellung().getStrasse() + ", "
        + Einstellungen.getEinstellung().getPlz() + " "
        + Einstellungen.getEinstellung().getOrt();
  }
}
