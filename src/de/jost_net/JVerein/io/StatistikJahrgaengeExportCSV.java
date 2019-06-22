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

package de.jost_net.JVerein.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.gui.view.StatistikJahrgaengeView;
import de.willuhn.jameica.gui.GUI;

public class StatistikJahrgaengeExportCSV extends StatistikJahrgaengeExport
{

  @Override
  public String getName()
  {
    return "Statistik Jahrgänge CSV-Export";
  }

  @Override
  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != StatistikJahrgaengeView.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {
      @Override
      public String getName()
      {
        return StatistikJahrgaengeExportCSV.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      @Override
      public String[] getFileExtensions()
      {
        return new String[] { "*.csv" };
      }
    };
    return new IOFormat[] { f };
  }

  @Override
  public String getDateiname()
  {
    return "statistikjahrgaenge";
  }

  @Override
  protected void open() throws DocumentException
  {
    try
    {

      ICsvMapWriter writer = new CsvMapWriter(new FileWriter(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

      String[] header = new String[] { "Jahrgang", "m", "w", "o" };
      Map<String, Object> map = new HashMap<>();
      map.put("Jahrgang", 2000);
      map.put("m", 1);
      map.put("w", 1);
      map.put("o", 1);
      CellProcessor[] processors = CellProcessors.createCellProcessors(map);

      writer.writeHeader(header);

      for (String key : statistik.keySet())
      {
        StatistikJahrgang dsbj = statistik.get(key);
        map.put("Jahrgang", key);
        map.put("m", dsbj.getAnzahlmaennlich());
        map.put("w", dsbj.getAnzahlweiblich());
        map.put("o", dsbj.getAnzahlOhne());
        writer.write(map, header, processors);
      }
      GUI.getStatusBar().setSuccessText("Auswertung fertig.");
      writer.close();
      FileViewer.show(file);
    }
    catch (IOException e)
    {
      throw new DocumentException(e);
    }
  }

  @Override
  protected void close() throws IOException, DocumentException
  {
    //
  }
}