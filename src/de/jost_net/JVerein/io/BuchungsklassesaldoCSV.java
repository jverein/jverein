/**********************************************************************
 * Copyright (c) by Thomas Laubrock
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsklassesaldoCSV {

  private static CellProcessor[] getProcessors() {

    final CellProcessor[] processors = new CellProcessor[] {new NotNull(), // BuchungsArt/Klasse,
                                                                           // Summe
        // new Optional(new FmtNumber(Einstellungen.DECIMALFORMAT)), //
        // Einnahmen
        new ConvertNullTo("", new FmtNumber(Einstellungen.DECIMALFORMAT)), // Einnahmen
        new ConvertNullTo("", new FmtNumber(Einstellungen.DECIMALFORMAT)), // Ausgaben
        new ConvertNullTo("", new FmtNumber(Einstellungen.DECIMALFORMAT)) // Umbuchung
    };

    return processors;
  }

  public BuchungsklassesaldoCSV(ArrayList<BuchungsklasseSaldoZeile> zeile, final File file,
      Date datumvon, Date datumbis) throws ApplicationException {
    ICsvMapWriter writer = null;
    try {
      writer = new CsvMapWriter(new FileWriter(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
      final CellProcessor[] processors = getProcessors();
      Map<String, Object> csvzeile = new HashMap<>();

      String[] header = {"Buchungsart", "Einnahmen", "Ausgaben", "Umbuchung"};
      writer.writeHeader(header);

      String subtitle = new JVDateFormatTTMMJJJJ().format(datumvon) + " - "
          + new JVDateFormatTTMMJJJJ().format(datumbis);
      csvzeile.put(header[0], subtitle);
      writer.write(csvzeile, header, processors);

      for (BuchungsklasseSaldoZeile bkz : zeile) {
        csvzeile = new HashMap<>();
        switch (bkz.getStatus()) {
          case BuchungsklasseSaldoZeile.HEADER: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsklassenbezeichnung"));
            break;
          }
          case BuchungsklasseSaldoZeile.DETAIL: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsartbezeichnung"));
            csvzeile.put(header[1], (Double) bkz.getAttribute("einnahmen"));
            csvzeile.put(header[2], (Double) bkz.getAttribute("ausgaben"));
            csvzeile.put(header[3], (Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.SALDOFOOTER: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsklassenbezeichnung"));
            csvzeile.put(header[1], (Double) bkz.getAttribute("einnahmen"));
            csvzeile.put(header[2], (Double) bkz.getAttribute("ausgaben"));
            csvzeile.put(header[3], (Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.GESAMTSALDOFOOTER: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsklassenbezeichnung"));
            csvzeile.put(header[1], (Double) bkz.getAttribute("einnahmen"));
            csvzeile.put(header[2], (Double) bkz.getAttribute("ausgaben"));
            csvzeile.put(header[3], (Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.GESAMTGEWINNVERLUST:
          case BuchungsklasseSaldoZeile.SALDOGEWINNVERLUST: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsklassenbezeichnung"));
            csvzeile.put(header[1], (Double) bkz.getAttribute("einnahmen"));
            break;
          }
          case BuchungsklasseSaldoZeile.STEUERHEADER: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsklassenbezeichnung") + " - "
                + (String) bkz.getAttribute("buchungsartbezeichnung"));
            csvzeile.put(header[1], (Double) bkz.getAttribute("einnahmen"));
            csvzeile.put(header[2], (Double) bkz.getAttribute("ausgaben"));
            break;
          }
          case BuchungsklasseSaldoZeile.STEUER: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsartbezeichnung"));
            csvzeile.put(header[1], (Double) bkz.getAttribute("einnahmen"));
            csvzeile.put(header[2], (Double) bkz.getAttribute("ausgaben"));
            break;
          }
          case BuchungsklasseSaldoZeile.NICHTZUGEORDNETEBUCHUNGEN: {
            csvzeile.put(header[0], (String) bkz.getAttribute("buchungsklassenbezeichnung"));
            csvzeile.put(header[1], (Integer) bkz.getAttribute("anzahlbuchungen"));
            break;
          }
          default: {
            csvzeile.put(header[0], "leer - warum auch immer");
          }

        }

        writer.write(csvzeile, header, processors);
      }
      GUI.getStatusBar().setSuccessText("Auswertung fertig.");
      writer.close();

      FileViewer.show(file);
    } catch (Exception e) {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception e) {
          Logger.error("error while creating report", e);
          throw new ApplicationException("Fehler", e);
        }
      }
    }

  }

}
