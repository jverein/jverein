package de.jost_net.JVerein.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 * ÷sterreichische Bankendatei
 * 
 * @author heiner
 * 
 */
public class ATBankdatei
{
  private ICsvMapReader csvreader = null;

  private String[] header = null;

  private CellProcessor[] processors = null;

  public ATBankdatei(InputStream is) throws IOException
  {
    CsvPreference pref = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;
    csvreader = new CsvMapReader(new InputStreamReader(is), pref);
    header = csvreader.getHeader(true);

    processors = getProcessors();
  }

  public ATBank next() throws IOException
  {
    Map<String, ? super Object> bankMap;
    if ((bankMap = csvreader.read(header, processors)) != null)
    {
      return new ATBank(bankMap);
    }
    return null;
  }

  private static CellProcessor[] getProcessors()
  {

    final CellProcessor[] processors = new CellProcessor[] { new NotNull(), // Kennzeichen;
        new UniqueHashCode(), // Identnummer;
        new NotNull(), // Bankleitzahl;
        new Optional(), // Institutsart;
        new Optional(), // Sektor;
        new Optional(), // Firmenbuchnummer;
        new NotNull(), // Bankenname;
        new Optional(), // Straﬂe;
        new Optional(), // PLZ;
        new Optional(), // Ort;
        new Optional(), // Politischer Bezirk;
        new Optional(), // Postadresse / Straﬂe;
        new Optional(), // Postadresse / PLZ;
        new Optional(), // Postadresse / Ort;
        new Optional(), // Postfach;
        new Optional(), // Bundesland;
        new Optional(), // Telefon;
        new Optional(), // Fax;
        new Optional(), // E-Mail;
        new Optional(), // SWIFT-Code;
        new Optional(), // Homepage;
        new Optional(), // Gruendungsdatum;
        new Optional() // ZweigniederlassungVon
    };
    return processors;
  }
}
